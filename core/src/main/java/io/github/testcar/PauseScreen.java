package io.github.testcar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static io.github.testcar.GameMenu.isMusicOn;


public class PauseScreen implements Screen {

    private final SpriteBatch batch;
    private final GameScreen gameScreen;
    private FitViewport viewport;
    private Stage stage;
    private Texture backgroundTexture;
    private Sprite backgroundSprite;
    private Texture blurredBackgroundTexture;
    private ShaderProgram blurShader;
    private Texture selectedCarTexture;
    private Music music;
    public PauseScreen(SpriteBatch batch, GameScreen gameScreen, Texture selectedCarTexture, Music music) {
        this.batch = batch;
        this.gameScreen = gameScreen;
        this.music=music;
        this.selectedCarTexture=selectedCarTexture;
        initializeViewport();
        create();
        setupStage();

        // Capture and blur the game screen
        blurredBackgroundTexture = gameScreen.captureScreen();
        initializeBlurShader();
    }

    public void create() {
        backgroundTexture = new Texture("Pausescreenbackground.png");
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(viewport.getWorldWidth()/2, 3*viewport.getWorldHeight()/4);
        backgroundSprite.setX(viewport.getWorldWidth()/4);
        backgroundSprite.setY(viewport.getWorldHeight()/8);
        blurShader = new ShaderProgram(Gdx.files.internal("blur_vertex.glsl"), Gdx.files.internal("blur_fragment.glsl"));

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
        button1.setPosition(12, 9);

        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Resume Button Clicked!");

                gameScreen.resumeGame(); // Call resumeGame on the existing GameScreen instance
                if (isMusicOn){
                gameScreen.playmusic();}
                Main game = (Main) Gdx.app.getApplicationListener();
                dispose(); // Dispose of the PauseScreen instance
                game.setScreen(gameScreen); // Switch back to GameScreen without creating a new instance
//                System.out.println("qwerty");
            }
        });

        Texture ExitbuttonTexture = new Texture("Exitbutton.png");
        TextureRegion buttonRegion2 = new TextureRegion(ExitbuttonTexture);
        TextureRegionDrawable buttonDrawable2 = new TextureRegionDrawable(buttonRegion2);
        ImageButton button2 = new ImageButton(buttonDrawable2);
        button2.setSize(10, 2);
        button2.setPosition(12, 5);

        // Inside a method, likely setupStage()
        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Quit Button Clicked!");
                dispose(); // Dispose of the PauseScreen instance
                Main game = (Main) Gdx.app.getApplicationListener();
                game.setScreen(new GameMenu(batch, selectedCarTexture)); // Navigate to GameMenu
            }
        });

// Initialize button3 and add it to the stage
        Texture restartbuttonTexture = new Texture("Restartbutton.png");
        TextureRegion buttonRegion3 = new TextureRegion(restartbuttonTexture);
        TextureRegionDrawable buttonDrawable3 = new TextureRegionDrawable(buttonRegion3);
        ImageButton button3 = new ImageButton(buttonDrawable3);
        button3.setPosition(12, 6);
        button3.setSize(10, 7); // Set size for button3

        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Restart Button Clicked!");
                Main game = (Main) Gdx.app.getApplicationListener(); // Ensure this class manages screens
                game.setScreen(new GameScreen(batch, selectedCarTexture)); // Switch to GameScreen
            }
        });

        stage.addActor(button1); // Add button1 to the stage
        stage.addActor(button2); // Add button2 to the stage
        stage.addActor(button3); // Add button3 to the stage

// Close the method here if necessary, i.e., add a single closing brace here
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


    // Initialize the blur shader
    private void initializeBlurShader() {
        blurShader = new ShaderProgram(Gdx.files.internal("blur_vertex.glsl"), Gdx.files.internal("blur_fragment.glsl"));
        if (!blurShader.isCompiled()) {
            System.err.println("Shader compilation failed: " + blurShader.getLog());
        }
    }

    // Render method in PauseScreen to draw the blurred background
    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK); // Clear the screen to black
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        batch.setShader(blurShader); // Apply the blur shader
        blurShader.setUniformf("blurSize", 1f / Gdx.graphics.getWidth()); // Adjust blur size
        batch.draw(blurredBackgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.setShader(null); // Reset shader after drawing background
        backgroundSprite.draw(batch);
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true); // Update the viewp
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
