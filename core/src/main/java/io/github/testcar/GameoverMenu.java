package io.github.testcar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameoverMenu implements Screen {

    private final SpriteBatch batch;
    private Stage stage;
    private final FitViewport viewport;
    private final Skin skin;
    private final Sprite backgroundSprite; // Use Sprite for background
    private Texture selectedCarTexture;

    public GameoverMenu(SpriteBatch batch) {
        this.batch = batch;

        // Initialize viewport and stage
        viewport = new FitViewport(800, 480);
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        // Load the background texture and set up the sprite
        Texture backgroundTexture = new Texture("Adish.png"); // Replace with your background image
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Load the skin for UI elements
        skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));

        // Create buttons
        setupStage();
    }

    private void setupStage() {
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Play button texture
        Texture resumebuttonTexture = new Texture("Restartbutton.png");
        TextureRegion buttonRegion = new TextureRegion(resumebuttonTexture);
        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(buttonRegion);
        ImageButton button1 = new ImageButton(buttonDrawable);
        button1.setPosition(12, 200);

        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Restart Button Clicked!");
                Main game = (Main) Gdx.app.getApplicationListener(); // Ensure this class manages screens
                game.setScreen(new GameScreen(batch, selectedCarTexture)); // Switch to GameScreen
            }
        });

        Texture ExitbuttonTexture = new Texture("Exitbutton.png");
        TextureRegion buttonRegion2 = new TextureRegion(ExitbuttonTexture);
        TextureRegionDrawable buttonDrawable2 = new TextureRegionDrawable(buttonRegion2);
        ImageButton button2 = new ImageButton(buttonDrawable2);
        button2.setPosition(12, 12);

        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Quit Button Clicked!");
                dispose(); // Dispose of the PauseScreen instance
                Main game = (Main) Gdx.app.getApplicationListener();
                game.setScreen(new GameMenu(batch, selectedCarTexture)); // Navigate to GameMenu
            }
        });

        stage.addActor(button1); // Add button to the stage
        stage.addActor(button2); // Add button to the stage
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the buffer

        batch.begin();
        backgroundSprite.draw(batch); // Draw the sprite as background
        batch.end();

        stage.act(delta);
        stage.draw(); // Draw the stage UI elements
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); // Set the stage as input processor when the screen is shown
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Use screen dimensions
    }


    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        backgroundSprite.getTexture().dispose(); // Dispose the background texture
        skin.dispose(); // Dispose the skin
    }
}
