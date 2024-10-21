package io.github.testcar;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.highgui.HighGui;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import java.util.concurrent.atomic.AtomicReference;

public class HeadTiltDetector implements Runnable {
    private static final int STRAIGHT_ANGLE_THRESHOLD = 20; // Increased dead zone threshold
    private static final int HYSTERESIS_MARGIN = 10; // Increased hysteresis margin
    private static final int SMOOTHING_WINDOW = 5; // Window size for smoothing

    private static final AtomicReference<Double> tiltAngle = new AtomicReference<>(0.0); // Shared tilt angle
    private static final AtomicBoolean running = new AtomicBoolean(true); // Control thread execution

    private final CascadeClassifier faceCascade;
    private final CascadeClassifier eyeCascade;
    private final VideoCapture capture;

    // Store the last detected orientation and a queue for angle smoothing
    private String lastOrientation = "Straight";
    private final Queue<Double> angleHistory = new LinkedList<>();

    public HeadTiltDetector() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Load OpenCV library
        faceCascade = new CascadeClassifier("haarcascades/haarcascade_frontalface_default.xml");
        eyeCascade = new CascadeClassifier("haarcascades/haarcascade_eye_tree_eyeglasses.xml");
        capture = new VideoCapture(0); // Open the default camera
        if (!capture.isOpened()) {
            throw new RuntimeException("Error: Cannot open the camera.");
        }
    }

    public static double getTiltAngle() {
        return tiltAngle.get();
    }

    @Override
    public void run() {
        Mat frame = new Mat();
        Mat gray = new Mat();
        MatOfRect faces = new MatOfRect();
        MatOfRect eyes = new MatOfRect();

        try {
            while (running.get() && capture.read(frame)) {
                if (frame.empty()) {
                    System.out.println("No captured frame. Exiting...");
                    break;
                }

                Core.flip(frame, frame, 1); // Mirror the video feed
                Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
                Imgproc.equalizeHist(gray, gray);

                // Detect faces
                faceCascade.detectMultiScale(gray, faces, 1.1, 8, 0, new Size(30, 30), new Size());

                if (!faces.empty()) {
                    Rect face = faces.toArray()[0]; // Take the first detected face
                    Mat faceROI = gray.submat(face);

                    // Draw rectangle around the detected face
                    Imgproc.rectangle(frame, face.tl(), face.br(), new Scalar(0, 255, 0), 2);

                    // Detect eyes within the detected face
                    eyeCascade.detectMultiScale(faceROI, eyes, 1.1, 5);
                    if (eyes.toArray().length >= 2) {
                        Point leftEye = getEyeCenter(face, eyes.toArray()[0]);
                        Point rightEye = getEyeCenter(face, eyes.toArray()[1]);

                        // Draw circles around the detected eyes
                        Imgproc.circle(frame, leftEye, 5, new Scalar(255, 0, 0), -1);
                        Imgproc.circle(frame, rightEye, 5, new Scalar(255, 0, 0), -1);

                        // Calculate and smooth the tilt angle
                        double angle = calculateTiltAngle(leftEye, rightEye);
                        smoothTiltAngle(angle);

                        // Determine and display the head orientation
                        String orientation = getHeadOrientation(tiltAngle.get());
                        Imgproc.putText(
                            frame, "Tilt: " + orientation,
                            new Point(face.x, face.y - 10), // Display above the face rectangle
                            Imgproc.FONT_HERSHEY_SIMPLEX, 0.8, new Scalar(0, 255, 255), 2
                        );
                    }
                }

                // Display the frame in the GUI
                HighGui.imshow("Head Tilt Detection", frame);
                if (HighGui.waitKey(1) == 'q') break; // Exit on 'q' key press
            }
        } finally {
            releaseResources();
        }
    }

    private Point getEyeCenter(Rect face, Rect eye) {
        return new Point(
            face.x + eye.x + eye.width * 0.5,
            face.y + eye.y + eye.height * 0.5
        );
    }

    private double calculateTiltAngle(Point leftEye, Point rightEye) {
        double dy = rightEye.y - leftEye.y;
        double dx = rightEye.x - leftEye.x;
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    private void smoothTiltAngle(double newAngle) {
        if (angleHistory.size() >= SMOOTHING_WINDOW) {
            angleHistory.poll(); // Remove the oldest angle
        }
        angleHistory.add(newAngle); // Add the new angle

        // Calculate the average tilt angle
        double averageAngle = angleHistory.stream().mapToDouble(a -> a).average().orElse(0.0);
        tiltAngle.set(averageAngle);
    }

    private String getHeadOrientation(double angle) {
        // Apply hysteresis logic to prevent rapid switching
        switch (lastOrientation) {
            case "Straight":
                if (angle > STRAIGHT_ANGLE_THRESHOLD + HYSTERESIS_MARGIN) {
                    lastOrientation = "Right";
                } else if (angle < -STRAIGHT_ANGLE_THRESHOLD - HYSTERESIS_MARGIN) {
                    lastOrientation = "Left";
                }
                break;
            case "Right":
                if (angle < STRAIGHT_ANGLE_THRESHOLD) {
                    lastOrientation = "Straight";
                }
                break;
            case "Left":
                if (angle > -STRAIGHT_ANGLE_THRESHOLD) {
                    lastOrientation = "Straight";
                }
                break;
        }
        return lastOrientation;
    }

    private void releaseResources() {
        capture.release();
        HighGui.destroyAllWindows();
    }

    public static void stop() {
        running.set(false); // Stop the detection thread
    }
}
