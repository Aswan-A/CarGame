package io.github.testcar;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Preferences;


public class Main implements ApplicationListener {
    Texture backgroundTexture;
    Texture PlayerCarTexture;
    Texture dropTexture;
    Texture GameoverTexture;
    Sound dropSound;
    Music music;
    SpriteBatch spriteBatch;
    static FitViewport viewport;
    static Sprite PlayerCarSprite;
    Sprite backgroundSprite;
    Sprite backgroundSprite2;
    Vector2 touchPos;
    static Array<Sprite> dropSprites;
    float dropTimer;
    Rectangle PlayerCarRectangle;
    Rectangle dropRectangle;
    private static int score;
    private String Score;
    BitmapFont yourfont;
    static boolean[] gameOver = {false};
    private static Skin skin;
    enum GameState {
        RUNNING, PAUSED, GAMEOVER,MAINMENU
    }
    private static GameState gameState = GameState.MAINMENU;
    private Preferences preferences;
    private static final String HIGH_SCORE_KEY = "high_score";
    private static int highScore=0;
    int flag=0;

    @Override
    public void create() {
        backgroundTexture = new Texture("background.png");
        PlayerCarTexture = new Texture("PlayerCar.png");
        dropTexture = new Texture("drop.png");
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(15, 10);
        backgroundSprite=new Sprite(backgroundTexture);
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        backgroundSprite2=new Sprite(backgroundTexture);
        backgroundSprite2.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        backgroundSprite2.setY(viewport.getWorldHeight());
        PlayerCarSprite = new Sprite(PlayerCarTexture);
        PlayerCarSprite.setSize(1, 2);
        touchPos = new Vector2();
        dropSprites = new Array<>();
        PlayerCarRectangle = new Rectangle();
        dropRectangle = new Rectangle();
        music.setLooping(true);
        music.setVolume(2f);
        music.play();
        score = 0;
        Score=" 0";
        yourfont=new BitmapFont();
        yourfont.getData().setScale(0.1f, 0.1f);
        preferences = Gdx.app.getPreferences("MyGamePreferences");
        highScore = preferences.getInteger(HIGH_SCORE_KEY, 0);

        HeadTiltDetector headTiltDetector = new HeadTiltDetector();
        Thread headTiltThread = new Thread(headTiltDetector);
        headTiltThread.start();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        input();
        ScreenUtils.clear(Color.BLACK);
        checkForPauseInput();
        switch (gameState) {
            case MAINMENU:
                if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                    gameState = GameState.RUNNING;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    Gdx.app.exit();  // Exit the game
                }
                mainmenu();
                break;
            case RUNNING:
                input();  // Handle player input
                logic();  // Game logic
                draw();   // Render the game
                break;

            case PAUSED:
                drawPauseMenu();  // Display the pause menu
                handlePauseInput(); // Handle pause menu input
                break;

            case GAMEOVER:
                drawGameOverMenu();  // Display the game over screen
                break;
        }
    }

    private void mainmenu() {
        ScreenUtils.clear(Color.WHITE);

        Skin skin = new Skin(Gdx.files.internal("ma/clean-crispy-ui.json"));
        Stage stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Create the UI layout
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // Create labels and text fields for input
        Label nameLabel = new Label("P FOR PLAY ", skin);
        nameLabel.setFontScale(8.0f);
        root.add(nameLabel).padBottom(20);  // Add some padding
        root.row();  // Move to the next row in the table
        Label nameLabel2 = new Label("PRESS E TO EXIT", skin);
        nameLabel2.setFontScale(8.0f);
        root.add(nameLabel2).padBottom(20);
        root.row();
        // Render the stage
        stage.act();  // Update the stage
        stage.draw();
    }
    public void checkAndUpdateHighScore(int score) {
        if (score > highScore) {
            highScore = score; // Update high score
            preferences.putInteger(HIGH_SCORE_KEY, highScore); // Save the new high score
            preferences.flush(); // Write the changes to storage
        }
    }
    public static int getHighScore() {
        return highScore;
    }



    private void handlePauseInput() {
        // If the player presses ESC again, resume the game
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            gameState = GameState.RUNNING;
        }

        // If the player presses Q, quit the game
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            Gdx.app.exit();  // Exit the game
        }

        // Add more options here if needed (like restarting from the pause menu)
    }


    private void drawPauseMenu() {
        // Clear the screen with a semi-transparent black background to indicate the pause
        ScreenUtils.clear(Color.WHITE);

        Skin skin = new Skin(Gdx.files.internal("ma/clean-crispy-ui.json"));
        Stage stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Create the UI layout
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // Create labels and text fields for input
        Label nameLabel = new Label("PAUSED", skin);
        nameLabel.setFontScale(8.0f);
        Label addressLabel = new Label("PRESS T TO UNPAUSE", skin);
        addressLabel.setFontScale(8.0f);
        Label addressLabel2 = new Label("PRESS E TO EXIT", skin);
        addressLabel2.setFontScale(8.0f);
        root.add(nameLabel).padBottom(20);  // Add some padding
        root.row();  // Move to the next row in the table
        root.add(addressLabel).padBottom(20);
        root.row();
        root.add(addressLabel2);

        // Render the stage
        stage.act();  // Update the stage
        stage.draw();
    }

    private void checkForPauseInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gameState = GameState.PAUSED;  // Switch to PAUSED state
        }
    }

    private void input() {
        if (gameOver[0]) return;
        float speed = 5f;
        float delta = Gdx.graphics.getDeltaTime();
        double tiltAngle = HeadTiltDetector.getTiltAngle();
        if (tiltAngle > 30) {
            PlayerCarSprite.translateX(-speed * delta);  // Move right
        } else if (tiltAngle < -30) {
            PlayerCarSprite.translateX(speed * delta);  // Move left
        }
        else{
            PlayerCarSprite.setCenterX(viewport.getWorldWidth()/2);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            PlayerCarSprite.translateX(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            PlayerCarSprite.translateX(-speed * delta);
        }


        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);
            PlayerCarSprite.setCenterX(touchPos.x);
        }
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


        backgroundSprite.translateY(-6f * delta);
        backgroundSprite2.translateY(-6f * delta);


        for (int i = dropSprites.size - 1; i >= 0; i--) {
            Sprite dropSprite = dropSprites.get(i);
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            // Move the drop down
            dropSprite.translateY(-8f * delta);

            // Update the drop's rectangle for collision detection
            dropRectangle.set(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);

            // Remove the drop if it's off the screen

            // Check for collision between the drop and the PlayerCar
            if (flag!=1 && dropSprite.getY() < -dropHeight && gameState != GameState.GAMEOVER && !PlayerCarRectangle.overlaps(dropRectangle)) {
                dropSprites.removeIndex(i);  // Safely remove the drop
                score++;
                Score= "" +score;
            }

        }


        dropTimer += delta;
        if (dropTimer > 0.5f) {
            dropTimer = 0;
            createDroplet();
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        yourfont.setColor(Color.WHITE);
        yourfont.draw(spriteBatch, Score, 12, viewport.getWorldHeight()-.5f);

        // Draw background
        backgroundSprite.draw(spriteBatch);
        backgroundSprite2.draw(spriteBatch);

        // Draw player car
        PlayerCarSprite.draw(spriteBatch);

        // Draw the raindrops
        for (Sprite dropSprite : dropSprites) {
            dropSprite.draw(spriteBatch);
        }
        if (gameOver[0]) {
            // Draw the Game Over texture at the center of the viewport
            spriteBatch.draw(GameoverTexture, viewport.getWorldWidth() / 2 - (float) GameoverTexture.getWidth() / 2,
                viewport.getWorldHeight() / 2 - (float) GameoverTexture.getHeight() / 2);
        }

        // Set the color and draw the score at the top-left corner, dynamically adjusting for viewport size
        yourfont.setColor(Color.WHITE);
        yourfont.draw(spriteBatch, Score, viewport.getWorldWidth()/2, viewport.getWorldHeight()-.5f);

        spriteBatch.end();
    }


    private void createDroplet() {
        float dropWidth = 1;
        float dropHeight = 2f;
        float worldHeight = viewport.getWorldHeight();

        // Define lane positions (adjust based on your game width)
        float[] lanePositions = {3f,4f, 6f,7f, 9f,11f,12f};  // Example: 3 lanes at these x-positions

        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropWidth, dropHeight);

        // Randomly select a lane for the drop to appear in
        int laneIndex = MathUtils.random(0, lanePositions.length - 1);
        float laneX = lanePositions[laneIndex];

        // Set the drop to appear at the selected lane's x-position
        dropSprite.setX(laneX - dropWidth / 2);  // Centering the drop in the lane
        dropSprite.setY(worldHeight);  // Start the drop at the top of the screen
        dropSprites.add(dropSprite);
    }

    static void  drawGameOverMenu () {
        // Check for user input to restart the game or exit
        if (Gdx.input.isKeyJustPressed(Input.Keys.R) || Gdx.input.isTouched()) {
            restartGame(score, dropSprites, PlayerCarSprite, viewport);  // Restart the game
            gameState = GameState.RUNNING;
        }


        if (Gdx.input.isTouched()) {
            restartGame(score, dropSprites, PlayerCarSprite, viewport);  // Restart on screen tap
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            Gdx.app.exit();
            System.exit(-1);
        }

        // Initialize the skin and stage only once to avoid recreating them repeatedly
        Skin skin = new Skin(Gdx.files.internal("ma/clean-crispy-ui.json"));
        Stage stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Create the UI layout
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // Create labels and text fields for input
        Label nameLabel = new Label("GAME OVER", skin);
        nameLabel.setFontScale(8.0f);
        Label addressLabel = new Label("PRESS R TO RESTART", skin);
        addressLabel.setFontScale(8.0f);
        Label addressLabel2 = new Label("PRESS E TO EXIT", skin);
        addressLabel2.setFontScale(8.0f);
        Label highScoreLabel = new Label("High Score: " + getHighScore(), skin);
        highScoreLabel.setFontScale(8.0f); // Scale the font for visibility

        // Add elements to the table
        Table table = new Table();
        table.add(highScoreLabel);
        table.row();
        table.add(nameLabel);
        table.row();
        table.add(addressLabel);
        table.row();
        table.add(addressLabel2);
        root.add(table).center(); // Center the table in the menu

        // Draw the stage
        stage.act();
        stage.draw();
    }


    public static void restartGame(int score, Array<Sprite> dropSprites, Sprite PlayerCarSprite, FitViewport viewport) {
        score = 0;  // Reset the score
        dropSprites.clear();  // Clear all drops

        // Reset player position
        PlayerCarSprite.setPosition(viewport.getWorldWidth() / 2 - PlayerCarSprite.getWidth() / 2, 1);
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        PlayerCarTexture.dispose();
        dropTexture.dispose();
        backgroundTexture.dispose();
        dropSound.dispose();
        music.dispose();
        spriteBatch.dispose();
        HeadTiltDetector.stop();
    }


}
