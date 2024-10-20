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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.*;


public class Main implements ApplicationListener {
    Texture backgroundTexture;
    Texture PlayerCarTexture;
    Texture dropTexture;
    Texture GameoverTexture;
    Texture shadowTexture;
    Texture shadowTexture2;
    Sound dropSound;
    Music music;
    SpriteBatch spriteBatch;
    static FitViewport viewport;
    static Sprite PlayerCarSprite;
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
    private static Skin skin;
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
    enum GameState {
        RUNNING, PAUSED, GAMEOVER,MAINMENU
    }
    private static GameState gameState = GameState.MAINMENU;
    private Preferences preferences;
    private static final String HIGH_SCORE_KEY = "high_score";
    private static int highScore=0;
    int flag=0;
    float treeY;
    float treeX;
    float treeX2;
    float treeSpeed;
    float scale2;

    @Override
    public void create() {
        backgroundTexture = new Texture("pixil-frame-0 (2).png");
        PlayerCarTexture = new Texture("pop.png");
        buildingTexture=new Texture ("Building.png");
        TreeTexture=new Texture("Tree.png");
        TreeTexture2=new Texture("Tree.png");
        dropTexture = new Texture("drop.png");
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        shadowTexture=new Texture("resized_black_white_alternate_lines_shadow.png");
        shadowTexture2=new Texture("resized_black_white_alternate_lines_shadow.png");
        spriteBatch = new SpriteBatch();
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
        PlayerCarSprite.setSize(3, 3);
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
        shadowSprite.setPosition(0, 0);
        shadowSprite2.setPosition(25, 25); // Ensure both shadows start in view
        shadowY = 0;   // Initial Y position
        shadowY2=0;
        shadowSpeed = 4f;  // Speed of the shadow movement (in pixels per second)
        u=1;
        buildingSprite = new Sprite(buildingTexture);
        treeY = viewport.getWorldHeight()*15/32;
        treeX = viewport.getWorldWidth()/2;
        treeX2 = viewport.getWorldWidth()/2;
        treeSpeed = 15f;  // Adjust the speed as necessary
        scale2 = 0.0f;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
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
        float speed = 12f;
        float delta = Gdx.graphics.getDeltaTime();

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
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        // Draw the background first


        float backgroundHeight = backgroundSprite.getHeight();
        if (backgroundSprite.getY() <= 0 && fl==1) {
            backgroundSprite.setY(backgroundSprite2.getY() + backgroundHeight);
            fl=0;
        }
        backgroundSprite.draw(spriteBatch);
        backgroundSprite2.draw(spriteBatch);
        float delta = Gdx.graphics.getDeltaTime();

        // Draw shadows with transparency
        spriteBatch.setColor(1f, 1f, 1f, 0.1f); // Set opacity to 0.5 (50% transparent)

        // Update the positions of both shadows
        shadowY += -shadowSpeed * delta;
        shadowY2 += -shadowSpeed * delta;


        float shadowHeight = viewport.getWorldHeight() / 2;  // Height of each shadow texture

// Draw the shadow textures
        spriteBatch.draw(shadowTexture, 0, shadowY, viewport.getWorldWidth(), shadowHeight);
        spriteBatch.draw(shadowTexture2, 0, shadowY2, viewport.getWorldWidth(), shadowHeight);
        if(shadowY<0 && u==1){
            shadowY2=shadowY+shadowHeight;
            u=0;
        }
// Check if the shadow textures have gone off the screen, and reset their positions
        if (shadowY <= -shadowHeight) {
            shadowY = shadowY2 + shadowHeight;  // Place it right above shadowTexture2
        }
        if (shadowY2 <= -shadowHeight) {
            shadowY2 = shadowY + shadowHeight;  // Place it right above shadowTexture
        }


        // Reset color to white for other drawing
        spriteBatch.setColor(1f, 1f, 1f, 1f);

        // Update the tree's position (moving it downwards)
        treeY -= treeSpeed * delta;
        treeX -= treeSpeed *delta;
        treeX2 += treeSpeed*delta;
        float scale = 0.01f;  // Scale factor


// Check if the tree is outside the bottom of the screen
        if (treeY + (TreeTexture.getHeight())*scale< 0) {
            // Reset the tree's position to the top
            treeY = viewport.getWorldHeight()*15/32;
            treeX= viewport.getWorldWidth()/2;
            treeX2= viewport.getWorldWidth()/2;
            scale2=0;

        }
// Draw the tree texture at its current position
        scale2=scale2+0.0005f;
        float treeWidth = TreeTexture.getWidth() * (scale2);
        float treeHeight = TreeTexture.getHeight() * scale2;

        spriteBatch.draw(TreeTexture, treeX, treeY, treeWidth, treeHeight);
        spriteBatch.draw(TreeTexture2, treeX2, treeY, treeWidth, treeHeight);

        // Draw player car

        PlayerCarSprite.draw(spriteBatch);
        float buildingWidth = viewport.getWorldWidth();  // Fit to the full width of the viewport
        float buildingHeight = buildingWidth * ((float) buildingTexture.getHeight() / buildingTexture.getWidth()); // Maintain aspect ratio
        // Draw Game Over texture if the game is over
        if (gameOver[0]) {
            spriteBatch.draw(GameoverTexture, viewport.getWorldWidth() / 2 - (float) GameoverTexture.getWidth() / 2,
                viewport.getWorldHeight() / 2 - (float) GameoverTexture.getHeight() / 2);
        }
        float xPosition = 0;  // Starting from the left edge
        float yPosition = viewport.getWorldHeight() - buildingHeight;  // Position it at the top

        // Draw the score at the top-left corner
        yourfont.setColor(Color.WHITE);
        yourfont.draw(spriteBatch, Score, 12, viewport.getWorldHeight() - 0.5f); // Adjusted position

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
    }


}
