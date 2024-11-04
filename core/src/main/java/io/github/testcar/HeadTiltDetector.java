//package io.github.testcar;
//
//import org.opencv.core.*;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.objdetect.CascadeClassifier;
//import org.opencv.videoio.VideoCapture;
//import org.opencv.highgui.HighGui;
//
//import java.util.LinkedList;
//import java.util.Queue;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.atomic.AtomicReference;
//
//public class HeadTiltDetector implements Runnable {
//    private static final int SMOOTHING_WINDOW = 5; // Size of the smoothing window for tilt angles
//    private static final Queue<Double> angleHistory = new LinkedList<>(); // History of tilt angles
//    private static final int STRAIGHT_ANGLE_THRESHOLD = 5; // Threshold for determining straight head position
//    private static final int REDETECTION_DELAY_FRAMES = 15; // Frames to skip re-detection after locking on
//
//    // Tracking the detected face and eye positions
//    private static Rect trackedFace = null;
//    private static final int lostFramesThreshold = 10; // Number of frames to consider face lost
//    private static int lostFramesCount = 0; // Count of lost frames
//    private int reDetectionCounter = 0; // Counter for re-detection delay
//
//    // Last detected eye centers
//    private static Point lastLeftEyeCenter = null;
//    private static Point lastRightEyeCenter = null;
//
//    // Cascade classifiers for face and eye detection
//    private final CascadeClassifier faceCascade;
//    private final CascadeClassifier eyeCascade;
//
//    // Thread control and data storage
//    private static final AtomicBoolean running = new AtomicBoolean(false); // Control the running state
//    private static final AtomicReference<Double> tiltAngle = new AtomicReference<>(0.0); // Average tilt angle
//    private static final AtomicInteger steeringDirection = new AtomicInteger(0); // Direction for steering
//
//    public HeadTiltDetector() {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Load OpenCV native library
//        faceCascade = new CascadeClassifier("haarcascades/haarcascade_frontalface_default.xml");
//        eyeCascade = new CascadeClassifier("haarcascades/haarcascade_eye.xml");
//
//        // Check if the cascade classifiers are loaded correctly
//        if (faceCascade.empty() || eyeCascade.empty()) {
//            System.out.println("Error: Cascade files not loaded correctly.");
//            return;
//        }
//    }
//
//    @Override
//    public void run() {
//        System.out.println("HeadTiltDetector thread started.");
//        running.set(true);
//        VideoCapture capture = new VideoCapture(0); // Open the default camera
//
//        // Check if the camera opens successfully
//        if (!capture.isOpened()) {
//            System.out.println("Error: Cannot open the camera.");
//            return;
//        }
//
//        Mat frame = new Mat(); // Frame to hold camera input
//
//        // Main detection loop
//        while (running.get() && capture.read(frame)) {
//            if (frame.empty()) {
//                System.out.println("No captured frame. Exiting...");
//                break;
//            }
//
//            Core.flip(frame, frame, 1); // Mirror the frame
//            Mat gray = new Mat(); // Matrix for grayscale conversion
//            Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY); // Convert to grayscale
//            Imgproc.equalizeHist(gray, gray); // Improve contrast
//
//            // Detect the face only if not detected or if lost beyond threshold frames
//            if ((trackedFace == null || lostFramesCount > lostFramesThreshold) && reDetectionCounter == 0) {
//                reDetectionCounter = REDETECTION_DELAY_FRAMES; // Reset re-detection counter
//                MatOfRect faces = new MatOfRect();
//                faceCascade.detectMultiScale(gray, faces, 1.1, 5, 0, new Size(30, 30), new Size());
//
//                if (faces.toArray().length > 0) {
//                    trackedFace = faces.toArray()[0]; // Track the first detected face
//                    lostFramesCount = 0; // Reset lost frames counter
//                } else {
//                    trackedFace = null; // No face detected
//                }
//            } else {
//                reDetectionCounter = Math.max(0, reDetectionCounter - 1); // Countdown for re-detection
//            }
//
//            // Process if a face is being tracked
//            if (trackedFace != null) {
//                Imgproc.rectangle(frame, trackedFace.tl(), trackedFace.br(), new Scalar(0, 255, 0), 2); // Draw rectangle around the face
//
//                Mat faceROI = gray.submat(trackedFace); // Region of Interest for the face
//                MatOfRect eyes = new MatOfRect();
//                eyeCascade.detectMultiScale(faceROI, eyes, 1.1, 5, 0, new Size(20, 20), new Size());
//
//                Rect[] detectedEyes = eyes.toArray(); // Array of detected eyes
//
//                if (detectedEyes.length >= 2) {
//                    // Determine left and right eye based on x-coordinate
//                    Rect leftEye = detectedEyes[0].x < detectedEyes[1].x ? detectedEyes[0] : detectedEyes[1];
//                    Rect rightEye = detectedEyes[0].x < detectedEyes[1].x ? detectedEyes[1] : detectedEyes[0];
//
//                    // Calculate centers of the eyes
//                    lastLeftEyeCenter = new Point(
//                        trackedFace.x + leftEye.x + leftEye.width * 0.5,
//                        trackedFace.y + leftEye.y + leftEye.height * 0.5
//                    );
//                    lastRightEyeCenter = new Point(
//                        trackedFace.x + rightEye.x + rightEye.width * 0.5,
//                        trackedFace.y + rightEye.y + rightEye.height * 0.5
//                    );
//                } else {
//                    lostFramesCount++; // Increment lost frames count if eyes are not detected
//                }
//
//                // If both eyes are detected, calculate the tilt angle
//                if (lastLeftEyeCenter != null && lastRightEyeCenter != null) {
//                    Imgproc.circle(frame, lastLeftEyeCenter, 5, new Scalar(255, 0, 0), 2); // Draw left eye center
//                    Imgproc.circle(frame, lastRightEyeCenter, 5, new Scalar(0, 0, 255), 2); // Draw right eye center
//
//                    double dy = lastRightEyeCenter.y - lastLeftEyeCenter.y;
//                    double dx = lastRightEyeCenter.x - lastLeftEyeCenter.x;
//                    double angle = Math.toDegrees(Math.atan2(dy, dx)); // Calculate angle based on eye positions
//
//                    // Smooth the angle using a history queue
//                    if (angleHistory.size() >= SMOOTHING_WINDOW) {
//                        angleHistory.poll(); // Remove the oldest angle
//                    }
//                    angleHistory.add(angle); // Add new angle to history
//
//                    // Calculate the average angle
//                    double avgAngle = angleHistory.stream().mapToDouble(a -> a).average().orElse(0.0);
//                    tiltAngle.set(avgAngle); // Update the tilt angle atomically
//
//                    // Determine head position and steering direction
//                    String headPosition;
//                    int currentSteeringDirection;
//                    if (avgAngle > STRAIGHT_ANGLE_THRESHOLD) {
//                        headPosition = "Head Tilted Right";
//                        currentSteeringDirection = 1; // Right
//                    } else if (avgAngle < -STRAIGHT_ANGLE_THRESHOLD) {
//                        headPosition = "Head Tilted Left";
//                        currentSteeringDirection = -1; // Left
//                    } else {
//                        headPosition = "Head Straight";
//                        currentSteeringDirection = 0; // Straight
//                    }
//
//                    // Update the steering direction atomically
//                    steeringDirection.set(currentSteeringDirection);
//
//                    // Display head position on the frame
//                    Imgproc.putText(frame, headPosition, new Point(trackedFace.x, trackedFace.y - 10),
//                        Imgproc.FONT_HERSHEY_SIMPLEX, 0.9, new Scalar(0, 255, 0), 2);
//                }
//            } else {
//                lostFramesCount++; // Increment lost frames count if no face is tracked
//            }
//
//            // Show the processed frame in a window
//            HighGui.imshow("Single Face Tracking", frame);
//
//            // Exit the loop if 'q' key is pressed
//            if (HighGui.waitKey(30) == 'q') {
//                break;
//            }
//        }
//
//        // Release resources after exiting the loop
//        capture.release();
//        HighGui.destroyAllWindows();
//    }
//
//    // Stop the detection thread safely
//    public static void stop() {
//        running.set(false);
//    }
//
//    // Get the latest tilt angle atomically
//    public static double getTiltAngle() {
//        return tiltAngle.get();
//    }
//
//    // Get the latest steering direction atomically
//    public static int getSteeringDirection() {
//        return steeringDirection.get();
//    }
//}
