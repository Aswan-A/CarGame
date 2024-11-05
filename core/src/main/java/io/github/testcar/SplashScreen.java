package io.github.testcar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class SplashScreen implements Screen {

    private final SpriteBatch batch;
    private Sprite splashSprite; // Use Sprite instead of Texture
    private long startTime; // Track the start time for the splash duration
    private Texture selectedCarTexture;

    public SplashScreen(SpriteBatch batch) {
        this.batch = batch;
        initialize();
    }

    private void initialize() {
        // Load the splash screen texture
        Texture splashTexture = new Texture("Splash.jpg");

        // Create a Sprite from the texture
        splashSprite = new Sprite(splashTexture);

        // Set the size of the sprite to fit the screen or any desired dimensions
        splashSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Center the sprite (optional if you want it centered on screen)
        splashSprite.setPosition((Gdx.graphics.getWidth() - splashSprite.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - splashSprite.getHeight()) / 2f);

        // Record the start time
        startTime = TimeUtils.nanoTime();
    }

    @Override
    public void show() {
        // This method is called when the splash screen is displayed
        Gdx.input.setInputProcessor(null); // Disable input during splash screen
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the splash sprite
        batch.begin();
        splashSprite.draw(batch); // Draw the sprite instead of texture
        batch.end();

        // Check if enough time has passed (e.g., 3 seconds)
        if (TimeUtils.nanoTime() - startTime > 2_000_000_000L) {
            // Replace with your main screen (e.g., GameMenu)
            Main game = (Main) Gdx.app.getApplicationListener();
            game.setScreen(new GameMenu(batch, selectedCarTexture));
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        // Dispose of the sprite texture to free resources
        splashSprite.getTexture().dispose();
    }
}
