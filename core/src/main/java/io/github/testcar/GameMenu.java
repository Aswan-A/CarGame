package io.github.testcar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Screen;

public class GameMenu implements Screen {
    private final SpriteBatch batch;
    private FitViewport viewport;
    private Texture backgroundTexture;
    private Sprite backgroundSprite;

    private Stage stage; // Declare Stage
    private Texture playbuttonTexture;
    private ImageButton button1;
    private Screen screen;
    private Texture ExitbuttonTexture;
    private ImageButton button2;

    public GameMenu(SpriteBatch batch) {
        this.batch = batch;
        initializeViewport(); // Call this to ensure viewport is set up
        create();  // Call create to initialize resources
        setupStage(); // Setup the stage and buttons
    }

    private void initializeViewport() {
        viewport = new FitViewport(35, 20); // Initialize the viewport
    }

    private void create() {
        backgroundTexture = new Texture("Slide 16_9 - 1.png");
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
    }

    private void setupStage() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Play button texture
        playbuttonTexture = new Texture("play-button.png");
        TextureRegion buttonRegion = new TextureRegion(playbuttonTexture);
        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(buttonRegion);
        button1 = new ImageButton(buttonDrawable);
        button1.setSize(800, 400);
        button1.setPosition(960, 540);

        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Start Game Button Clicked!");
                Main game = (Main) Gdx.app.getApplicationListener(); // Ensure this class manages screens
                game.setScreen(new GameScreen(batch)); // Switch to GameScreen
            }

        });

        ExitbuttonTexture = new Texture("Exitbutton.png");
        TextureRegion buttonRegion2 = new TextureRegion(ExitbuttonTexture);
        TextureRegionDrawable buttonDrawable2 = new TextureRegionDrawable(buttonRegion2);
        button2 = new ImageButton(buttonDrawable2);
        button2.setSize(800, 400);
        button2.setPosition(960, 300);

        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Game Exited!");
                System.exit(-1);
            }

        });

        stage.addActor(button1);// Add button to the stage
        stage.addActor(button2);// Add button to the stage

    }




    @Override
    public void show() {
        // Code to handle when the screen is shown
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK); // Clear the screen to black
        viewport.apply(); // Apply the viewport settings
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        backgroundSprite.draw(batch); // Draw the background sprite
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw(); // Draw the stage (which includes the button)
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // Update the viewport
        stage.getViewport().update(width, height, true); // Update stage viewport
    }

    @Override
    public void pause() {
        // Code to handle when the game is paused
    }

    @Override
    public void resume() {
        // Code to handle when the game is resumed
    }

    public void hide() {
        // Code to handle when the screen is hidden
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose(); // Dispose the background texture
        playbuttonTexture.dispose(); // Dispose the button texture
        stage.dispose(); // Dispose the stage
    }
}
