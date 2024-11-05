package io.github.testcar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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
    private Texture StorebuttonTexture;
    private ImageButton button2;
    private ImageButton button3;
    private ImageButton button4;
    private ImageButton button5;
    private Texture selectedCarTexture;
    private Texture ExitbuttonTexture;
    private Texture AudiobuttonTexture;
    private boolean isMusicOn = true; // Track if music is on
    private Texture musicOnTexture;
    private Texture musicOffTexture;
    private Music music;
    float screenHeight;
    float screenWidth;
    int width;
    int height;
    int width2;
    int height2;
    int width3;
    int height3;
    int height5;
    int width5;
    private Texture ControllerbuttonTexture;


    public GameMenu(SpriteBatch batch, Texture selectedCarTexture) {
        this.batch = batch;
        initializeViewport(); // Call this to ensure viewport is set up
        create();  // Call create to initialize resources
        setupStage(); // Setup the stage and buttons
        this.selectedCarTexture=selectedCarTexture;
    }

    private void initializeViewport() {
        viewport = new FitViewport(35, 20); // Initialize the viewport
    }

    private void create() {
        backgroundTexture = new Texture("GameBackground.jpg");
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        musicOnTexture = new Texture("MusicOn.png");
        musicOffTexture = new Texture("MusicOff.png");
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        music.setLooping(true);
        music.play();
    }

    private void setupStage() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Play button texture
        playbuttonTexture = new Texture("Startbutton.png");
        height = playbuttonTexture.getHeight();
        width = playbuttonTexture.getWidth();

        TextureRegion buttonRegion = new TextureRegion(playbuttonTexture);
        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(buttonRegion);
        button1 = new ImageButton(buttonDrawable);
        System.out.println(width+" "+height);
        button1.setPosition((screenWidth/2)-((float) width /2), (3*screenHeight/4)-((float) height /2));
        System.out.println(viewport.getWorldHeight());

        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Start Game Button Clicked!");
                Main game = (Main) Gdx.app.getApplicationListener(); // Ensure this class manages screens
                game.setScreen(new GameScreen(batch,selectedCarTexture)); // Switch to GameScreen
            }

        });

        ExitbuttonTexture = new Texture("Quitbutton.png");
        height2 = ExitbuttonTexture.getHeight();
        width2 = ExitbuttonTexture.getWidth();
        TextureRegion buttonRegion2 = new TextureRegion(ExitbuttonTexture);
        TextureRegionDrawable buttonDrawable2 = new TextureRegionDrawable(buttonRegion2);
        button2 = new ImageButton(buttonDrawable2);
        button2.setPosition((screenWidth/2)-((float) width2 /2), (screenHeight/4)-((float) height2 /2));

        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Game Exited!");
                System.exit(-1);
            }

        });

        StorebuttonTexture = new Texture("GarageButton.png");
        height3 = StorebuttonTexture.getHeight();
        width3 = StorebuttonTexture.getWidth();
        TextureRegion buttonRegion3 = new TextureRegion(StorebuttonTexture);
        TextureRegionDrawable buttonDrawable3 = new TextureRegionDrawable(buttonRegion3);
        button3 = new ImageButton(buttonDrawable3);
        button3.setPosition((screenWidth/2)-((float) width3 /2), (screenHeight/2)-((float) height3 /2));

        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Store!");
                Main game = (Main) Gdx.app.getApplicationListener(); // Ensure this class manages screens
                game.setScreen(new StoreScreen(batch)); // Switch to GameScreen
            }

        });

        TextureRegionDrawable initialDrawable = new TextureRegionDrawable(new TextureRegion(musicOnTexture));
        button4 = new ImageButton(initialDrawable);
        button4.setPosition(Gdx.graphics.getWidth()- musicOnTexture.getWidth(), Gdx.graphics.getHeight() - (2*musicOnTexture.getHeight()));
//System.out.println(viewport.getWorldWidth()-musicOnTexture.getWidth());
//        System.out.println("dfhfdhfgh");

        button4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isMusicOn = !isMusicOn; // Toggle music state

                if (isMusicOn) {
                    button4.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(musicOnTexture));
                    music.play(); // Turn music on
                } else {
                    button4.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(musicOffTexture));
                    music.pause(); // Turn music off
                }

                System.out.println("Music Button Clicked! Music is now " + (isMusicOn ? "On" : "Off"));
            }
        });
        ControllerbuttonTexture = new Texture("Controller.png");
        height5 = ControllerbuttonTexture.getHeight();
        width5 = ControllerbuttonTexture.getWidth();
        TextureRegion buttonRegion5 = new TextureRegion(ControllerbuttonTexture);
        TextureRegionDrawable buttonDrawable5 = new TextureRegionDrawable(buttonRegion5);
        button5 = new ImageButton(buttonDrawable5);
        button5.setPosition((Gdx.graphics.getWidth()- musicOnTexture.getWidth()), Gdx.graphics.getHeight() - (6*musicOnTexture.getHeight()));

        button5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Controller Button Pressed!");
                Main game = (Main) Gdx.app.getApplicationListener(); // Ensure this class manages screens
                game.setScreen(new StoreScreen(batch)); // Switch to GameScreen
            }

        });
        stage.addActor(button1);// Add button to the stage
        stage.addActor(button2);// Add button to the stage
        stage.addActor(button3);// Add button to the stage
        stage.addActor(button4);// Add button to the stage
        stage.addActor(button5);// Add button to the stage


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); // Set input processor
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK); // Clear the screen to black
        viewport.apply(); // Apply the viewport settings
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        button1.setPosition((screenWidth / 2) - ((float) width / 2), (3 * screenHeight / 4) - ((float) height / 2));
        button2.setPosition((screenWidth/2)-((float) width2 /2), (screenHeight/4)-((float) height2 /2));
        button3.setPosition((screenWidth/2)-((float) width3 /2), (screenHeight/2)-((float) height3 /2));

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
