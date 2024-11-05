package io.github.testcar;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.awt.image.BufferedImage;

public class GameScreen implements Screen {
    Texture backgroundTexture;
        Texture PlayerCarTexture;
        Texture dropTexture;
        Texture GameoverTexture;
        Texture shadowTexture;
        Texture shadowTexture2;
        Sound dropSound;
        Music music;
        SpriteBatch batch;
        static FitViewport viewport;
        static Sprite PlayerCarSprite;
        static Sprite treeSprite;
        static Sprite treeSprite2;

        Sprite backgroundSprite;
        Sprite backgroundSprite2;
        Sprite shadowSprite;
        Sprite shadowSprite2;
        Vector2 touchPos;
        static Array<Sprite> dropSprites;
        float dropTimer;
        Rectangle PlayerCarRectangle;
        Rectangle dropRectangle;
        private static int score;
        private String Score;
        BitmapFont yourfont;
        static boolean[] gameOver = {false};
        private int fl=1;
        float shadowY;
        float shadowY2;
        float shadowSpeed;
        int u;
        Texture buildingTexture;
        Texture TreeTexture;
        Texture TreeTexture2;
        Sprite buildingSprite;
        Sprite TreeSprite;
        float treeX3;
        float treeX3Y;
        Texture BuildTexture;
        private Sprite BuildSprite;
        private float[] treePositions;
        private int treeIndex;
        private float[] treePositions2;
        private int treeIndex2;
    private Screen screen; // Change the type to Screen
    private Texture PausebuttonTexture;
    private ImageButton button1;
    private Stage stage;
    float delta = Gdx.graphics.getDeltaTime();
    private boolean isPaused;
    private FrameBuffer frameBuffer;
    Texture selectedCarTexture;
    GameScreen(SpriteBatch batch, Texture selectedCarTexture) {
        this.batch = batch;
        this.selectedCarTexture=selectedCarTexture;
        create();  // Call create to initialize resources
        initializeViewport(); // Call this to ensure viewport is set up
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            gameState = io.github.testcar.GameScreen.GameState.RUNNING;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            Gdx.app.exit();  // Exit the game
        }
        float delta = 0f;
//        System.out.println("56");
        isPaused = false; // Game starts unpaused
        setupStage();
        render(delta);
    }
    private void setupStage() {
//        System.out.println("NOAH");
        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);
//        System.out.println("hI");

        // Play button texture
        PausebuttonTexture = new Texture("Pausebutton.png");
        TextureRegion buttonRegion = new TextureRegion(PausebuttonTexture);
        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(buttonRegion);
        button1 = new ImageButton(buttonDrawable);
        button1.setSize(2, 2);
        button1.setPosition(32, 17);

        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                System.out.println("Pause Button Clicked!");
                HeadTiltDetector.stop();
                pauseGame();
                music.pause();
                Main game = (Main) Gdx.app.getApplicationListener();
                game.setScreen(new PauseScreen(batch, GameScreen.this,selectedCarTexture,music)); // Pass the current GameScreen instance
                isPaused=true;
//                System.out.println("asd");
            }
        });
        stage.addActor(button1);// Add button to the stage

    }
    private void initializeViewport() {
        // Initialize the viewport here to ensure it is ready before resize calls
        viewport = new FitViewport(35, 20);
    }

    public Texture captureScreen() {
        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }


    enum GameState {
            RUNNING
        }
        private static io.github.testcar.GameScreen.GameState gameState = GameState.RUNNING;
        private Preferences preferences;
        private static final String HIGH_SCORE_KEY = "high_score";
        private static int highScore=0;
        int flag=0;
        float treeY;
        float treeX;
        float treeXY;
        float treeX2;
        float treeX2Y;
        float treeSpeed;
        float scale2;

        public void create() {



            backgroundTexture = new Texture("Adish.png");
            if(selectedCarTexture!=null){
                PlayerCarTexture =selectedCarTexture;
            }else{
                PlayerCarTexture=new Texture ("pop.png");
            }
            buildingTexture=new Texture ("Building.png");
            BuildTexture=new Texture("Build3.png");
            TreeTexture=new Texture("Tree.png");

            TreeTexture2=new Texture("Tree.png");
            dropTexture = new Texture("drop.png");
            dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
            music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
            shadowTexture=new Texture("resized_black_white_alternate_lines_shadow.png");
            shadowTexture2=new Texture("resized_black_white_alternate_lines_shadow.png");
            viewport = new FitViewport(35, 20);
            backgroundSprite=new Sprite(backgroundTexture);
            backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());

            backgroundSprite2=new Sprite(backgroundTexture);
            backgroundSprite2.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
            backgroundSprite2.setY(0);
            shadowSprite=new Sprite(shadowTexture);
            shadowSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight()/2);
            shadowSprite.setY(0);
            shadowSprite2=new Sprite(backgroundTexture);
            shadowSprite2.setSize(viewport.getWorldWidth(), viewport.getWorldHeight()/2);
            shadowSprite2.setY(0);
            PlayerCarSprite = new Sprite(PlayerCarTexture);
            PlayerCarSprite.setSize(5, 5);
            BuildSprite=new Sprite(BuildTexture);
            treeSprite = new Sprite(TreeTexture);
            treeSprite.setOrigin(treeSprite.getWidth()/2,0);
            treeSprite2 = new Sprite(TreeTexture);
            treeSprite2.setOrigin(treeSprite    .getWidth()/2,0);

            touchPos = new Vector2();
            dropSprites = new Array<>();
            PlayerCarRectangle = new Rectangle();
            dropRectangle = new Rectangle();
            music.setLooping(true);
            music.setVolume(0f);
            music.play();
            score = 0;
            Score=" 0";
            yourfont=new BitmapFont();
            yourfont.getData().setScale(0.01f, 0.01f);
            preferences = Gdx.app.getPreferences("MyGamePreferences");
            highScore = preferences.getInteger(HIGH_SCORE_KEY, 0);
            shadowSprite.setPosition(0, 0);
            shadowSprite2.setPosition(25, 25); // Ensure both shadows start in view
            shadowY = 0;   // Initial Y position
            shadowY2=0;
            shadowSpeed = 4f;  // Speed of the shadow movement (in pixels per second)
            u=1;
            buildingSprite = new Sprite(buildingTexture);
            treeY = 13.31f;
//                    System.out.println(viewport.getWorldWidth());
            treePositions = new float[]{4f,8f,12f,14.5f};
            treeIndex = MathUtils.random(0, treePositions.length - 1);
            treeX = treePositions[treeIndex];
            treeXY=treeX;
            treeX3Y=treeX;
            treePositions2 = new float[]{20f,24f,28f,32.5f};
            treeIndex2 = MathUtils.random(0, treePositions2.length - 1);
            treeX2 = 17.5f;
            treeX2Y=treeX2;
            treeSpeed = 1f;  // Adjust the speed as necessary
            scale2 = 1f;

            HeadTiltDetector headTiltDetector = new HeadTiltDetector();
            Thread headTiltThread = new Thread(headTiltDetector);
            headTiltThread.start();
        }

    @Override
    public void show() {

    }

    public void resumeGame() {
        isPaused = false; // Set the pause flag to false
    }
    public void playmusic(){
             music.play();
    }
    public void pauseGame() {
        isPaused = true; // Set the pause flag to true
    }
    @Override
    public void render(float v) {
        if (!isPaused) {
            ScreenUtils.clear(Color.BLACK);
            if(Gdx.input.getInputProcessor() == null) {
                Gdx.input.setInputProcessor(stage);
            }
            treeSprite2.setOrigin(treeSprite2.getWidth()/2,0);
            treeSprite.setOrigin(treeSprite.getWidth()/2,0);

//          System.out.println("56");
            checkForPauseInput();
            switch (gameState) {
                case RUNNING:
                    input();  // Handle player input
                    logic();  // Game logic
                    draw();   // Render the game
                    break;
            }
        }
    }

    @Override

    public void resize(int width, int height) {
        if (viewport != null) { // Check if viewport is initialized
            viewport.update(width, height, true);
        }
    }
        private void handlePauseInput() {
            // If the player presses ESC again, resume the game
            if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
                gameState = io.github.testcar.GameScreen.GameState.RUNNING;
            }

            // If the player presses Q, quit the game
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                Gdx.app.exit();  // Exit the game
            }

            // Add more options here if needed (like restarting from the pause menu)
        }

        private void checkForPauseInput() {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                gameState = GameState.RUNNING;  // Switch to PAUSED state
            }
        }

        private void input() {
            if (gameOver[0]) return;
            float speed = 12f;
            float delta = Gdx.graphics.getDeltaTime();

            int steer =HeadTiltDetector.getSteeringDirection();
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)||steer==1) {
                PlayerCarSprite.translateX(speed * delta);
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)||steer==-1) {
                PlayerCarSprite.translateX(-speed * delta);
            }

//            if (Gdx.input.isTouched()) {
//                touchPos.set(Gdx.input.getX(), Gdx.input.getY());
//                viewport.unproject(touchPos);
//                PlayerCarSprite.setCenterX(touchPos.x);
//            }
        }

        private void logic() {
            if (gameOver[0]) return;
            float worldWidth = viewport.getWorldWidth();
            float PlayerCarWidth = PlayerCarSprite.getWidth();
            float PlayerCarHeight = PlayerCarSprite.getHeight();

            PlayerCarSprite.setX(MathUtils.clamp(PlayerCarSprite.getX(), 1.8f, worldWidth - PlayerCarWidth-1.8f));

            float delta = Gdx.graphics.getDeltaTime();
            PlayerCarRectangle.set(PlayerCarSprite.getX(), PlayerCarSprite.getY(), PlayerCarWidth, PlayerCarHeight);

            // Move the background down
            float backgroundHeight = backgroundSprite.getHeight();

            // Check if the background sprite has gone off the screen and reposition
            if (backgroundSprite.getY() <= -backgroundHeight) {
                backgroundSprite.setY(backgroundSprite2.getY() + backgroundHeight);
            }
            if (backgroundSprite2.getY() <= -backgroundHeight) {
                backgroundSprite2.setY(backgroundSprite.getY() + backgroundHeight);
            }
        }

        private void draw() {
            ScreenUtils.clear(Color.BLACK);
            viewport.apply();
            stage.act(delta); // Update stage
            batch.setProjectionMatrix(viewport.getCamera().combined);
            batch.begin();

            // Draw the background first


            float backgroundHeight = backgroundSprite.getHeight();
//            if (backgroundSprite.getY() <= 0 && fl==1) {
//                backgroundSprite.setY(backgroundSprite2.getY() + backgroundHeight);
//                fl=0;
//            }
//            backgroundSprite.draw(batch);
            backgroundSprite2.draw(batch);
            float delta = Gdx.graphics.getDeltaTime();
            // Reset color to white for other drawing
            batch.setColor(1f, 1f, 1f, 1f);
            // Update the tree's position (moving it downwards)

//System.out.println((treeY + 3));

// Check if the tree is outside the bottom of the screen
            if (((treeY + 5)< 0) || ((treeX2 > 38 && treeX3+14 < 0))){
                // Reset the tree's position to the top
                treeSprite = createtree();  // Create a new sprite when the tree goes off screen
                treeSprite2 = createtree();  // Create a new sprite when the tree goes off screen
                treeY =13.3f;
                treePositions = new float[]{4f,8f,12f,14f};
                treeIndex = MathUtils.random(0, treePositions.length - 1);
                treeX = treePositions[treeIndex];
                treeXY=treeX;
                treePositions2 = new float[]{21f,24f,28f,32.5f};
                treeIndex2 = MathUtils.random(0, treePositions2.length - 1);
                treeX2= 17.5f;
                treeX2Y=treeX2;
                treeX3= viewport.getWorldWidth()/5;
                scale2=0;
            }
//        System.out.println(treeX+"    hi     ");


// Draw the tree texture at its current position
            scale2 += 0.00003f;

            float treeWidth = TreeTexture.getWidth() * scale2;
            float treeHeight = TreeTexture.getHeight() * scale2;
            treeY -= treeSpeed * delta;
            float width = viewport.getWorldWidth()/2;

//System.out.println(treeX2);
            treeX =(((treeY-((viewport.getWorldHeight()/2)+9))*-((width)-(treeXY))/((-5)))+(width));
//            System.out.println(viewport.getWorldWidth()/4);
                //   System.out.println(treeX2);

            treeX2 =((treeY-((viewport.getWorldHeight()/2)+9))*((treeX2Y)-(width))/((-5)))+(width);

            treeX3=treeX-treeWidth;
//                  System.out.println(treeY);
            //       System.out.println(viewport.getWorldHeight());

            if (treeSprite != null) {
                treeSprite.setPosition(treeX3, treeY);
                treeSprite.setSize(treeWidth, treeHeight);
                treeSprite.draw(batch);
            }



// Only draw treeSprite2 if it's not null
            if (treeSprite2 != null) {
                treeSprite2.setPosition(treeX2, treeY);
                treeSprite2.setSize(treeWidth, treeHeight);
                treeSprite2.draw(batch);
            }
            BuildSprite.setPosition(treeX3, treeY);
            BuildSprite.setSize(treeWidth, treeHeight);
            // Draw player car

            PlayerCarSprite.draw(batch);
            float buildingWidth = viewport.getWorldWidth();  // Fit to the full width of the viewport
            float buildingHeight = buildingWidth * ((float) buildingTexture.getHeight() / buildingTexture.getWidth()); // Maintain aspect ratio
            // Draw Game Over texture if the game is over
            if (gameOver[0]) {
                batch.draw(GameoverTexture, viewport.getWorldWidth() / 2 - (float) GameoverTexture.getWidth() / 2,
                    viewport.getWorldHeight() / 2 - (float) GameoverTexture.getHeight() / 2);
            }
            float xPosition = 0;  // Starting from the left edge
            float yPosition = viewport.getWorldHeight() - buildingHeight;  // Position it at the top

            // Draw the score at the top-left corner
            yourfont.setColor(Color.WHITE);
            yourfont.draw(batch, Score, 12, viewport.getWorldHeight() - 0.5f); // Adjusted position

            batch.end();
            stage.draw();

        }


        private Sprite createtree() {
            // Adjust size to match tree sprite size
            float treeWidth = TreeTexture.getWidth();
            float treeHeight = TreeTexture.getHeight();

            // Randomly decide whether to create a tree or a building sprite
            int randomChoice = MathUtils.random(1);  // 0 for tree, 1 for building

            Sprite newSprite = null;
            if (randomChoice == 0) {
                newSprite = new Sprite(TreeTexture);  // Create a tree sprite
            } else if(randomChoice==1) {
                newSprite = new Sprite(BuildTexture);  // Create a building sprite
            }

            if(newSprite != null)
                newSprite.setSize(treeWidth, treeHeight);  // Set its size
            return newSprite;
        }


        public void pause() {

        }

        public void resume() {

        }

    @Override
    public void hide() {

    }

    public void dispose() {
            backgroundTexture.dispose();
            PlayerCarTexture.dispose();
            dropTexture.dispose();
            if (GameoverTexture != null) {
                GameoverTexture.dispose();
            }
            shadowTexture.dispose();
            shadowTexture2.dispose();
            dropSound.dispose();
            music.dispose();
            batch.dispose();
            buildingTexture.dispose();
            TreeTexture.dispose();
            TreeTexture2.dispose();
            BuildTexture.dispose();
            yourfont.dispose();
            stage.dispose(); // Dispose the stage

    }

    public static Texture convertBufferedImageToTexture(BufferedImage bufferedImage) {
        // Convert BufferedImage to Pixmap
        Pixmap pixmap = new Pixmap(bufferedImage.getWidth(), bufferedImage.getHeight(), Pixmap.Format.RGBA8888);
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int a = (rgb >> 24) & 0xFF;
                pixmap.drawPixel(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        // Create Texture from Pixmap
        Texture texture = new Texture(pixmap);
        pixmap.dispose(); // Clean up the Pixmap
        return texture;
    }


    }

