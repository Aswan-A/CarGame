package io.github.testcar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PauseScreen implements Screen {

    private final SpriteBatch batch;
    private final GameScreen gameScreen;
    private FitViewport viewport;
    private Stage stage;
    private Texture backgroundTexture;
    private Sprite backgroundSprite;

    PauseScreen(SpriteBatch batch, GameScreen gameScreen) {
        this.batch = batch;
        this.gameScreen = gameScreen; // Store the reference
        initializeViewport();
        create();
        setupStage();
        draw();
    }

    public void create() {
        backgroundTexture = new Texture("WhatsApp Image 2024-10-15 at 19.49.58_41e090b8.jpg");
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
    }

    private void setupStage() {
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Play button texture
        Texture resumebuttonTexture = new Texture("Resumebutton.png");
        TextureRegion buttonRegion = new TextureRegion(resumebuttonTexture);
        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(buttonRegion);
        ImageButton button1 = new ImageButton(buttonDrawable);
        button1.setSize(10, 7);
        button1.setPosition(15, 5);

        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Resume Button Clicked!");
                gameScreen.resumeGame(); // Call resumeGame on the existing GameScreen instance
                Main game = (Main) Gdx.app.getApplicationListener();
                dispose(); // Dispose of the PauseScreen instance
                game.setScreen(gameScreen); // Switch back to GameScreen without creating a new instance
//                System.out.println("qwerty");
            }
        });
        stage.addActor(button1); // Add button to the stage
    }

    private void initializeViewport() {
        // Initialize the viewport here to ensure it is ready before resize calls
        viewport = new FitViewport(35, 20);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        backgroundSprite.draw(batch);
        batch.end();
        stage.draw();

    }

    @Override
    public void show() {

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
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Dispose of all the resources used in the pause screen
        if (backgroundTexture != null) {
            backgroundTexture.dispose(); // Dispose of the background texture
        }
        if (stage != null) {
            stage.dispose(); // Dispose of the stage to free resources
        }
        Gdx.input.setInputProcessor(null);
    }


}
