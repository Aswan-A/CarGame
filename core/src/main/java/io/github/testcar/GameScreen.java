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
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Preferences;


import static io.github.testcar.GameMenu.isMusicOn;
import static io.github.testcar.GameMenu.isSelected;
import static io.github.testcar.asd.generator;

public class GameScreen implements Screen {
    private  BitmapFont font;
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

    static Sprite OptionSprite1;
    static Sprite OptionSprite2;
    static Sprite OptionSprite3;
    int flag=1;
    Sprite backgroundSprite;
    Sprite BlockSprite;
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

    Texture OptionTexture1;
    Texture OptionTexture2;
    Texture OptionTexture3;

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
    private Texture BlockTexture;
    private ImageButton button1;
    private Stage stage;
    float delta = Gdx.graphics.getDeltaTime();
    private boolean isPaused;
    private FrameBuffer frameBuffer;
    Texture selectedCarTexture;
    private float opt1XY;
    private float opt2XY;
    private float opt3XY;
    private String eq="";
    private int sol;
    private Preferences preferences;
    private Sprite BlockSprite2;
    private int angle2;
    private float velocityX2;
    private float velocityY2;
    private long spawnInterval2;
    private long lastSpawnTime2;
    int steer=0;
    private Texture CloudTexture;
    private Sprite Cloudsprite;
    private Sprite Cloudsprite2;
    private Sprite Cloudsprite3;

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
        font = new BitmapFont(Gdx.files.internal("press.fnt"), Gdx.files.internal("press.png"), false);
        font.getData().setScale(0.07f);
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
    private static final String HIGH_SCORE_KEY = "high_score";
    private static int highScore=0;
    float treeY;
    float treeX;
    float treeXY;
    float treeX2;
    float treeX2Y;
    float treeSpeed;
    float scale2;
    float scale3;
    float angle;
    float Opt1Y;
    float Opt1X;
    float speed;
    float Opt2Y;
    float Opt2X;
    float velocityX;
    float velocityY;
    float Opt3Y;
    float Opt3X;
    float startTime;
    Array<Sprite> blockSprites;
    Array<Sprite> blockSprites2;
    float spawnInterval;
    long lastSpawnTime;
    private float speed2;
    private float leftLimit;
    private float rightLimit;
    private int direction; // 1 means right, -1 means left
    RandomMathProblemGenerator generator = new RandomMathProblemGenerator(1, 3);

    public void create() {
        font = new BitmapFont(Gdx.files.internal("press.fnt"), Gdx.files.internal("press.png"), false);
        font.getData().setScale(1.5f);
        backgroundTexture = new Texture("Adish.jpg");
        BlockTexture = new Texture("Block2.png");
        if(selectedCarTexture!=null){
            PlayerCarTexture =selectedCarTexture;
        }else{
            PlayerCarTexture=new Texture ("pop.png");
        }
        buildingTexture=new Texture ("Building.png");
        BuildTexture=new Texture("Build3.png");
        TreeTexture=new Texture("Tree.png");
        CloudTexture=new Texture ("Cloud.png");

        OptionTexture1 =new Texture("opt1.png");
        OptionTexture2 =new Texture("opt2.png");
        OptionTexture3 =new Texture("opt3.png");

        TreeTexture2=new Texture("Tree.png");
        dropTexture = new Texture("drop.png");
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        shadowTexture=new Texture("resized_black_white_alternate_lines_shadow.png");
        shadowTexture2=new Texture("resized_black_white_alternate_lines_shadow.png");
        viewport = new FitViewport(35, 20);
        backgroundSprite=new Sprite(backgroundTexture);
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        startTime=0;
        angle = 70; // Adjust this to your desired angle
        speed = 10; // Speed in pixels per second
        angle2=73;
        velocityX = speed * MathUtils.cosDeg(angle);
        velocityY = speed * MathUtils.sinDeg(angle);
        velocityX2 = speed * MathUtils.cosDeg(angle2);
        velocityY2 = speed * MathUtils.sinDeg(angle2);
        BlockSprite=new Sprite(BlockTexture);
        BlockSprite.setSize(0.5f, 0.4f);
        BlockSprite.setPosition(17.5f,13.31f);
        BlockSprite2=new Sprite(BlockTexture);
        BlockSprite2.setSize(0.5f, 0.4f);
        BlockSprite2.setPosition(17.5f,13.31f);


        blockSprites = new Array<>();
        blockSprites2 = new Array<>();
        spawnInterval = 0_700_000_000L; // 1 second in nanoseconds
        lastSpawnTime = 0;
        spawnInterval2 = 0_700_000_000L; // 1 second in nanoseconds
        lastSpawnTime2 = 0;

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
        Cloudsprite=new Sprite(CloudTexture);
        Cloudsprite2=new Sprite(CloudTexture);
        Cloudsprite3=new Sprite(CloudTexture);

        treeSprite.setOrigin(treeSprite.getWidth()/2,0);
        treeSprite2 = new Sprite(TreeTexture);
        treeSprite2.setOrigin(treeSprite    .getWidth()/2,0);

        OptionSprite1=new Sprite(OptionTexture1);
        OptionSprite2=new Sprite(OptionTexture2);
        OptionSprite3 = new Sprite(OptionTexture3);
        OptionSprite1.setSize(0,0);
        OptionSprite2.setSize(0,0);
        OptionSprite3.setSize(0,0);
        OptionSprite1.setPosition(2,5);
        OptionSprite2.setPosition(2,5);

        OptionSprite3.setPosition(2,5);
        speed2 = 0.01f;

        leftLimit = 5f;
        rightLimit = 25f;
        direction = 1; // 1 means right, -1 means left
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
        yourfont.getData().setScale(0.1f, 0.1f);
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
        Opt1Y =13.31f;
        Opt2Y=13.31f;
        Opt3Y=13.31f;

        touchPos = new Vector2();
        dropSprites = new Array<>();
        PlayerCarRectangle = new Rectangle();
        dropRectangle = new Rectangle();
        if (isMusicOn){
            music.setLooping(true);
            music.setVolume(1f);
            music.play();}
        score = 0;
        Score=" 0";
        yourfont=new BitmapFont();
        yourfont.getData().setScale(0.1f, 0.1f);
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
        Opt1X =17.5f;
        Opt2X=17.499995f;
        Opt3X=17.500005f;

        Cloudsprite.setPosition(15, 15);
        Cloudsprite.setSize(3, 3);
        Cloudsprite2.setPosition(5, 14);
        Cloudsprite2.setSize(5, 5);
        Cloudsprite3.setPosition(32, 16);
        Cloudsprite3.setSize(4, 3);


        treeXY=treeX;
        treeX3Y=treeX;
        treePositions2 = new float[]{20f,24f,28f,32.5f};
        treeIndex2 = MathUtils.random(0, treePositions2.length - 1);
        treeX2 = treePositions2[treeIndex2];
        treeX2Y=treeX2;
        treeSpeed = 1f;  // Adjust the speed as necessary
        scale2 = 1f;
        scale3=1f;
            if (!isSelected){
                HeadTiltDetector headTiltDetector = new HeadTiltDetector();
                Thread headTiltThread = new Thread(headTiltDetector);
                headTiltThread.start();}
        preferences = Gdx.app.getPreferences("MyGamePreferences");
        highScore = preferences.getInteger(HIGH_SCORE_KEY, 0);
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

    public void update(float deltaTime) {
        // Spawn a new BlockSprite every second for blockSprites
        if (TimeUtils.nanoTime() - lastSpawnTime > spawnInterval) {
            Sprite newBlockSprite = new Sprite(new Texture("block.png")); // Replace with your texture
            newBlockSprite.setPosition(17.5f, 12.7f); // Set initial spawn position
            newBlockSprite.setSize(0.5f,0.5f); // Set initial spawn position

            blockSprites.add(newBlockSprite);
            lastSpawnTime = TimeUtils.nanoTime();
        }

        // Update and draw each BlockSprite in blockSprites
        for (Sprite blockSprite : blockSprites) {
            // Calculate movement based on angle
            float velocityX = speed * MathUtils.cosDeg(angle);
            float velocityY = speed * MathUtils.sinDeg(angle);

            // Move the sprite
            blockSprite.setPosition(
                blockSprite.getX() + velocityX * deltaTime,
                blockSprite.getY() - velocityY * deltaTime
            );

            // Draw the sprite
            blockSprite.draw(batch);
        }
    }

    // Update method for blockSprites2
    public void update2(float deltaTime) {
        // Spawn a new BlockSprite every second for blockSprites2
        if (TimeUtils.nanoTime() - lastSpawnTime2 > spawnInterval2) {
            Sprite newBlockSprite2 = new Sprite(new Texture("block.png")); // Replace with your texture
            newBlockSprite2.setPosition(17.3f, 12.700000000f); // Set initial spawn position
            newBlockSprite2.setSize(0.5f,0.5f); // Set initial spawn position


            blockSprites2.add(newBlockSprite2);
            lastSpawnTime2 = TimeUtils.nanoTime();
        }

        // Update and draw each BlockSprite in blockSprites2
        for (Sprite blockSprite2 : blockSprites2) {
            // Calculate movement based on angle
            float velocityX2 = speed * MathUtils.cosDeg(angle2);
            float velocityY2 = speed * MathUtils.sinDeg(angle2);

            // Move the sprite
            blockSprite2.setPosition(
                blockSprite2.getX() - velocityX2 * deltaTime,
                blockSprite2.getY() - velocityY2 * deltaTime
            );

            // Draw the sprite
            blockSprite2.draw(batch);
        }
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

            float newPosition = Cloudsprite.getX() + speed2 * direction;

            // Check if we've reached the movement boundaries
            if (newPosition <= leftLimit || newPosition >= rightLimit) {
                direction *= -1; // Reverse direction
            }

            // Apply new positions to each cloud sprite
            Cloudsprite.setPosition(newPosition, Cloudsprite.getY());
            Cloudsprite2.setPosition(newPosition - 5, Cloudsprite2.getY()); // Offset for each sprite
            Cloudsprite3.setPosition(newPosition + 5, Cloudsprite3.getY());

//          System.out.println("56");
            checkForPauseInput();
//            moveBlockSprite(delta);
            switch (gameState) {
                case RUNNING:
                    input();  // Handle player input
                    logic();  // Game logic
                    draw();   // Render the game
                    break;
            }
        }
    }

    public static int getHighScore() {
        return highScore;
    }

    public void checkAndUpdateHighScore(int score) {
        if (score > highScore) {
            highScore = score; // Update high score
            preferences.putInteger(HIGH_SCORE_KEY, highScore); // Save the new high score
            preferences.flush(); // Write the changes to storage
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
if(!isSelected){
        steer =HeadTiltDetector.getSteeringDirection();}
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
    private void createonetree(){
        treeSprite = createtree();  // Create a new sprite when the tree goes off screen
        treeSprite2 = createtree();  // Create a new sprite when the tree goes off screen
        treeY =13.3f;
        treePositions = new float[]{4f,8f,12f,14f};
        treeIndex = MathUtils.random(0, treePositions.length - 1);
        treeX = treePositions[treeIndex];
        treeXY=treeX;
        treePositions2 = new float[]{21f,24f,28f,32.5f};
        treeIndex2 = MathUtils.random(0, treePositions2.length - 1);
        treeX2= treePositions2[treeIndex2];
        treeX2Y=treeX2;
        treeX3= viewport.getWorldWidth()/5;
        scale2=0;
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
        if (PlayerCarRectangle.overlaps(OptionSprite1.getBoundingRectangle()) && sol!=1) {
            checkAndUpdateHighScore(score);
            Main game = (Main) Gdx.app.getApplicationListener();
            game.setScreen(new GameoverMenu(new SpriteBatch()));
        }
        if (PlayerCarRectangle.overlaps(OptionSprite2.getBoundingRectangle()) && sol!=2) {
            checkAndUpdateHighScore(score);
            Main game = (Main) Gdx.app.getApplicationListener();
            game.setScreen(new GameoverMenu(new SpriteBatch()));            }
        if (PlayerCarRectangle.overlaps(OptionSprite3.getBoundingRectangle()) && sol!=3) {
            checkAndUpdateHighScore(score);
            Main game = (Main) Gdx.app.getApplicationListener();
            game.setScreen(new GameoverMenu(new SpriteBatch()));            }
        if(PlayerCarRectangle.overlaps(OptionSprite1.getBoundingRectangle()) || PlayerCarRectangle.overlaps(OptionSprite2.getBoundingRectangle())||PlayerCarRectangle.overlaps(OptionSprite3.getBoundingRectangle())){
            eq=generator.generateProblem();
            System.out.println(eq);
            sol=generator.Isol;
            float optionHeight = OptionTexture1.getHeight()*scale3;
            float optionWidth = OptionTexture1.getWidth()*scale3;
            float optionHeight2 = OptionTexture2.getHeight()*scale3;
            float optionWidth2 = OptionTexture2.getWidth()*scale3;
            float optionHeight3 = OptionTexture3.getHeight()*scale3;
            float optionWidth3 = OptionTexture3.getWidth()*scale3;
            OptionSprite1= new Sprite(OptionTexture1);
            OptionSprite1.setSize(optionWidth, optionHeight);
            OptionSprite1.setOrigin(optionWidth / 2, optionHeight / 2);
//            System.out.println("Origin X: " + OptionSprite1.getOriginX());
//            System.out.println("Origin Y: " + OptionSprite1.getOriginY());
//            System.out.println("Position X: " + OptionSprite1.getX());
//            System.out.println("Position Y: " + OptionSprite1.getY());

            OptionSprite2= new Sprite(OptionTexture2);
            OptionSprite2.setSize(optionWidth2, optionHeight2);
            OptionSprite2.setOrigin(optionWidth / 2, optionHeight / 2);

            OptionSprite3= new Sprite(OptionTexture3);
            OptionSprite3.setSize(optionWidth3, optionHeight3);
            OptionSprite3.setOrigin(optionWidth / 2, optionHeight / 2);

            Opt1Y =13.3f;
            Opt1X =17.5f;
            opt1XY = Opt1X;

            Opt2X =17.499995f;
            opt2XY = Opt2X;

            Opt3X =17.500005f;
            opt3XY = Opt3X;
            scale3=0;
//            System.out.println("sff");
            flag=0;
            score =  score + 5;
        }
    }
    public void moveBlockSprite(float deltaTime) {
        // Update BlockSprite position
        BlockSprite.setPosition(
            BlockSprite.getX() + velocityX * deltaTime,
            BlockSprite.getY() - velocityY * deltaTime
        );
        BlockSprite2.setPosition(
            BlockSprite2.getX() - velocityX2 * deltaTime,
            BlockSprite2.getY() - velocityY2 * deltaTime
        );
    }
    private void draw() {
        ScreenUtils.clear(Color.BLACK);
//        System.out.println(OptionSprite1.getHeight());

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
        update(delta);
        update2(delta);
        Cloudsprite.draw(batch);
        Cloudsprite2.draw(batch);
        Cloudsprite3.draw(batch);
//        Cloudsprite.setPosition(15, 15);
//        Cloudsprite.setSize(5, 5);
//        Cloudsprite.draw(batch);
//        Cloudsprite2.setPosition(10, 15);
//        Cloudsprite2.setSize(5, 5);
//        Cloudsprite2.draw(batch);
//        Cloudsprite3.setPosition(20, 15);
//        Cloudsprite3.setSize(5, 5);
//        Cloudsprite3.draw(batch);
//        BlockSprite.draw(batch);
//        BlockSprite2.draw(batch);
//        if (TimeUtils.nanoTime() - startTime > 1_000_000_000L) {
//
//        }
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
            treePositions = new float[]{4f,8f,12f};
            treeIndex = MathUtils.random(0, treePositions.length - 1);
            treeX = treePositions[treeIndex];
            treeXY=treeX;
            treePositions2 = new float[]{21f,24f,28f,32.5f};
            treeIndex2 = MathUtils.random(0, treePositions2.length - 1);
            treeX2= treePositions2[treeIndex2];
            treeX2Y=treeX2;
            treeX3= viewport.getWorldWidth()/5;
            scale2=0;
            
//            System.out.println(treeX);

        }
        if (Opt1Y +5<0){
            // Reset the tree's position to the top

            eq=generator.generateProblem();
//            System.out.println(eq);
            sol=generator.Isol;

            float optionHeight = OptionTexture1.getHeight()*scale3;
            float optionWidth = OptionTexture1.getWidth()*scale3;
            float optionHeight2 = OptionTexture2.getHeight()*scale3;
            float optionWidth2 = OptionTexture2.getWidth()*scale3;
            float optionHeight3 = OptionTexture3.getHeight()*scale3;
            float optionWidth3 = OptionTexture3.getWidth()*scale3;
            OptionSprite1= new Sprite(OptionTexture1);
            OptionSprite1.setSize(optionWidth, optionHeight);
            OptionSprite1.setOrigin(optionWidth / 2, optionHeight / 2);
//            System.out.println("Origin X: " + OptionSprite1.getOriginX());
//            System.out.println("Origin Y: " + OptionSprite1.getOriginY());
//            System.out.println("Position X: " + OptionSprite1.getX());
//            System.out.println("Position Y: " + OptionSprite1.getY());

            OptionSprite2= new Sprite(OptionTexture2);
            OptionSprite2.setSize(optionWidth2, optionHeight2);
            OptionSprite2.setOrigin(optionWidth / 2, optionHeight / 2);

            OptionSprite3= new Sprite(OptionTexture3);
            OptionSprite3.setSize(optionWidth3, optionHeight3);
            OptionSprite3.setOrigin(optionWidth / 2, optionHeight / 2);

            Opt1Y =13.3f;
            Opt1X =17.5f;
            opt1XY = Opt1X;

            Opt2X =17.499995f;
            opt2XY = Opt2X;

            Opt3X =17.500005f;
            opt3XY = Opt3X;
            scale3=0;
//            System.out.println("sff");
            flag=0;
//            System.out.println(Opt2X);
                /*treePositions = new float[]{4f,8f,12f,14f};
                treeIndex = MathUtils.random(0, treePositions.length - 1);
                treeX = treePositions[treeIndex];
                treeXY=treeX;
                treePositions2 = new float[]{21f,24f,28f,32.5f};
                treeIndex2 = MathUtils.random(0, treePositions2.length - 1);
                treeX2= 17.5f;
                treeX2Y=treeX2;*/
            //treeX3= viewport.getWorldWidth()/5;
            //scale2=0;
        }

//        System.out.println(treeX+"    hi     ");
//        System.out.println(OptionSprite1.getHeight());


// Draw the tree texture at its current position
        scale2 += 0.00003f;
        scale3 += 0.00003f;

        float treeWidth = TreeTexture.getWidth() * scale2;
        float treeHeight = TreeTexture.getHeight() * scale2;

        float optionHeight = OptionTexture1.getHeight()*scale3;
        float optionWidth = OptionTexture1.getWidth()*scale3;
        float optionHeight2 = OptionTexture2.getHeight()*scale3;
        float optionWidth2 = OptionTexture2.getWidth()*scale3;
        float optionHeight3 = OptionTexture3.getHeight()*scale3;
        float optionWidth3 = OptionTexture3.getWidth()*scale3;


        treeY -= treeSpeed * delta;
        Opt1Y -=treeSpeed* delta;
        Opt2Y -=treeSpeed* delta;
        Opt3Y -=treeSpeed* delta;
        float width = viewport.getWorldWidth()/2;

//        System.out.println(OptionTexture1.getWidth()*scale3);
        treeX =(((treeY-((viewport.getWorldHeight()/2)+9))*-((width)-(treeXY))/((-5)))+(width));
        Opt1X= (float) (((Opt1Y-((viewport.getWorldHeight()/2)+9))*-((width)-(opt1XY))/((-0.000015)))+(width));
        Opt2X= (float) (((Opt1Y-((viewport.getWorldHeight()/2)+9))*-((width)-(opt2XY))/((-0.000015)))+(width));

//            System.out.println(viewport.getWorldWidth()/4);
        //   System.out.println(treeX2);

        treeX2 =((treeY-((viewport.getWorldHeight()/2)+9))*((treeX2Y)-(width))/((-5)))+(width);
        Opt3X= (float) (((Opt1Y-((viewport.getWorldHeight()/2)+9))*((opt3XY)-(width))/((-0.000015)))+(width));


        treeX3=treeX-treeWidth;
//                  System.out.println(treeY);
        //       System.out.println(viewport.getWorldHeight());

        if (treeSprite != null) {

            treeSprite.setPosition(treeX, treeY);
            treeSprite.setSize(treeWidth, treeHeight);
            treeSprite.draw(batch);
        }
        if(flag==0) {
            OptionSprite1.setPosition(Opt1X, Opt1Y);
            OptionSprite1.setSize(optionWidth, optionHeight);
            OptionSprite1.draw(batch);

            OptionSprite2.setPosition(Opt2X, Opt1Y);
            OptionSprite2.setSize(optionWidth2, optionHeight2);
            OptionSprite2.draw(batch);

            OptionSprite3.setPosition(Opt3X, Opt1Y);
            OptionSprite3.setSize(optionWidth3, optionHeight3);
            OptionSprite3.draw(batch);
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
        Score=""+score;
        yourfont.draw(batch, Score, 12, viewport.getWorldHeight() - 0.5f); // Adjusted position
        font.draw(batch, eq, 14, 17);

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
        // newSprite =new Sprite(OptionTexture1);
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

}
