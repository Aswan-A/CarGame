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

public class Main implements ApplicationListener {
    Texture backgroundTexture;
    Texture PlayerCarTexture;
    Texture dropTexture;
    Texture GameoverTexture;
    Sound dropSound;
    Music music;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Sprite PlayerCarSprite;
    Sprite backgroundSprite;
    Sprite backgroundSprite2;
    Vector2 touchPos;
    Array<Sprite> dropSprites;
    float dropTimer;
    Rectangle PlayerCarRectangle;
    Rectangle dropRectangle;
    private int[] score;
    private String Score;
    BitmapFont yourfont;
    boolean[] gameOver = {false};
    private static Skin skin;

    @Override
    public void create() {
        backgroundTexture = new Texture("background.png");
        Texture GameoverTexture = new Texture("Picture1.png");
        PlayerCarTexture = new Texture("PlayerCar.png");
        dropTexture = new Texture("drop.png");
        dropSound = Gdx.audio.newSound(Gdx.files.internal("crash-7075.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("acc.mp3"));
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
        score = new int[]{0};
        Score=" 0";
        yourfont=new BitmapFont();
        yourfont.getData().setScale(0.1f, 0.1f);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        if (gameOver[0]) {
            ScreenUtils.clear(Color.WHITE);
            restartMenu(score, dropSprites, gameOver, PlayerCarSprite, viewport);  // Call the restart menu

        } else {
            input();
            logic();
            draw();
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
            if (dropSprite.getY() < -dropHeight) {
                dropSprites.removeIndex(i);  // Safely remove the drop
                score[0]++;
                Score= "" +score[0];
            }
            // Check for collision between the drop and the PlayerCar
            if (PlayerCarRectangle.overlaps(dropRectangle)) {
                dropSprites.removeIndex(i);  // Remove the drop upon collision
                dropSound.play();            // Play the sound
                score [0]=0;                   // Reset the score (or perform other logic)

                float delay = 0.4f; // Delay in seconds before ending the game

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        // End game logic after the delay
                        gameOver[0] = true;// You can call a method that handles ending the game
                        restartMenu(score,dropSprites,gameOver,PlayerCarSprite,viewport);
                    }
                }, delay);  // Schedule the task to run after 'delay' seconds
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
            spriteBatch.draw(GameoverTexture, viewport.getWorldWidth() / 2 - GameoverTexture.getWidth() / 2,
                viewport.getWorldHeight() / 2 - GameoverTexture.getHeight() / 2);
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

    static void restartMenu(int[] score, Array<Sprite> dropSprites, boolean[] gameOver, Sprite PlayerCarSprite, FitViewport viewport) {
        // Check for user input to restart the game or exit
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            restartGame(score, dropSprites, gameOver, PlayerCarSprite, viewport);  // Restart the game
        }

        if (Gdx.input.isTouched()) {
            restartGame(score, dropSprites, gameOver, PlayerCarSprite, viewport);  // Restart on screen tap
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



        // Add elements to the table
        Table table = new Table();
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


    public static void restartGame(int[] score, Array<Sprite> dropSprites, boolean[] gameOver, Sprite PlayerCarSprite, FitViewport viewport) {
        score[0] = 0;  // Reset the score
        dropSprites.clear();  // Clear all drops
        gameOver[0] = false;  // Set gameOver to false

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
