package io.github.testcar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static io.github.testcar.GameScreen.getHighScore;

public class GameoverMenu implements Screen {

    private final SpriteBatch batch;
    private final Stage stage;
    private final FitViewport viewport;
    private final Skin skin;
    private final Sprite backgroundSprite;
    private Texture selectedCarTexture;

    public GameoverMenu(SpriteBatch batch) {
        this.batch = batch;

        // Initialize viewport and stage
        viewport = new FitViewport(800, 480);
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        // Load the background texture and set up the sprite
        Texture backgroundTexture = new Texture("GameoverScreenBackground.jpg"); // Replace with your background image
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());

        // Load the skin for UI elements
        skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));

        // Create and arrange UI elements
        setupStage();
    }

    private void setupStage() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // High score label
        Label highScoreLabel = new Label("High Score: " + getHighScore(), skin);
        highScoreLabel.setFontScale(2.0f); // Scale up for visibility
        root.add(highScoreLabel).padBottom(20).row();

        // Restart button
        Texture resumebuttonTexture = new Texture("Restartbutton.png");
        ImageButton button1 = new ImageButton(new TextureRegionDrawable(resumebuttonTexture));
        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Restart Button Clicked!");
                Main game = (Main) Gdx.app.getApplicationListener();
                game.setScreen(new GameScreen(batch, selectedCarTexture));
            }
        });
        root.add(button1).padBottom(10).row();

        // Exit button
        Texture exitButtonTexture = new Texture("Exitbutton.png");
        ImageButton button2 = new ImageButton(new TextureRegionDrawable(exitButtonTexture));
        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Quit Button Clicked!");
                dispose();

                Main game = (Main) Gdx.app.getApplicationListener();
                game.setScreen(new GameMenu(batch, selectedCarTexture));
            }
        });
        root.add(button2).padBottom(10).row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        backgroundSprite.draw(batch);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
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
        backgroundSprite.getTexture().dispose();
        skin.dispose();
    }
}
