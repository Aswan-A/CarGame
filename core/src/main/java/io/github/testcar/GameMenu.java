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
    private Stage stage;

    private Texture playbuttonTexture;
    private ImageButton button1;
    private Texture StorebuttonTexture;
    private ImageButton button2;
    private ImageButton button3;
    private ImageButton button4;
    private ImageButton button5;

    private Texture selectedCarTexture;
    private Texture ExitbuttonTexture;
    private Texture AudiobuttonTexture;
    private Texture musicOnTexture;
    private Texture musicOffTexture;
    private Music music;
    private Texture normalTexture;
    private Texture selectedTexture;

    public static boolean isSelected=false;
    public static boolean isMusicOn = true;

    float screenHeight;
    float screenWidth;
    int width, height, width2, height2, width3, height3, height5, width5;

    public GameMenu(SpriteBatch batch, Texture selectedCarTexture) {
        this.batch = batch;
        this.selectedCarTexture = selectedCarTexture;

        initializeViewport();
        create();
        setupStage();
    }

    private void initializeViewport() {
        viewport = new FitViewport(35, 20);
    }

    private void create() {
        // Load assets
        normalTexture = new Texture("Head.png");
        selectedTexture = new Texture("WASD.png");
        backgroundTexture = new Texture("GameBackground.jpg");
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());

        musicOnTexture = new Texture("MusicOn.png");
        musicOffTexture = new Texture("MusicOff.png");
        music = Gdx.audio.newMusic(Gdx.files.internal("music1.mp3"));

        if (isMusicOn) {
            music.play();
            music.setLooping(true);
        }

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
    }

    private void setupStage() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Initialize button textures and create button styles
        playbuttonTexture = new Texture("Startbutton.png");
        width = playbuttonTexture.getWidth();
        height = playbuttonTexture.getHeight();

        TextureRegionDrawable playButtonDrawable = new TextureRegionDrawable(new TextureRegion(playbuttonTexture));
        button1 = new ImageButton(playButtonDrawable);
        button1.setPosition((screenWidth / 2) - (width / 2f), (3 * screenHeight / 4) - (height / 2f));

        // Play button listener
        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Start Game Button Clicked!");
                Main game = (Main) Gdx.app.getApplicationListener();
                music.stop();
                game.setScreen(new GameScreen(batch, selectedCarTexture));
            }
        });

        ExitbuttonTexture = new Texture("Quitbutton.png");
        width2 = ExitbuttonTexture.getWidth();
        height2 = ExitbuttonTexture.getHeight();

        TextureRegionDrawable exitButtonDrawable = new TextureRegionDrawable(new TextureRegion(ExitbuttonTexture));

        button2 = new ImageButton(exitButtonDrawable);
        button2.setPosition((screenWidth / 2) - (width2 / 2f), (screenHeight / 4) - (height2 / 2f));

        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Game Exited!");
                music.stop();
                dispose();
                System.exit(-1);
            }
        });

        StorebuttonTexture = new Texture("GarageButton.png");
        width3 = StorebuttonTexture.getWidth();
        height3 = StorebuttonTexture.getHeight();

        TextureRegionDrawable storeButtonDrawable = new TextureRegionDrawable(new TextureRegion(StorebuttonTexture));
        button3 = new ImageButton(storeButtonDrawable);
        button3.setPosition((screenWidth / 2) - (width3 / 2f), (screenHeight / 2) - (height3 / 2f));

        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Store!");
                Main game = (Main) Gdx.app.getApplicationListener();
                music.stop();
                game.setScreen(new StoreScreen(batch));
            }
        });

        // Music toggle button
        TextureRegionDrawable initialDrawable = isMusicOn ? new TextureRegionDrawable(new TextureRegion(musicOnTexture))
            : new TextureRegionDrawable(new TextureRegion(musicOffTexture));
        button4 = new ImageButton(initialDrawable);
        button4.setPosition(screenWidth - musicOnTexture.getWidth(), screenHeight - (2 * musicOnTexture.getHeight()));

        button4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isMusicOn = !isMusicOn;
                if (isMusicOn) {
                    button4.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(musicOnTexture));
                    music.play();
                } else {
                    button4.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(musicOffTexture));
                    music.stop();
                }
                System.out.println("Music Button Clicked! Music is now " + (isMusicOn ? "On" : "Off"));
            }
        });

        // Initialize button styles
        ImageButton.ImageButtonStyle normalStyle = new ImageButton.ImageButtonStyle();
        normalStyle.imageUp = new TextureRegionDrawable(new TextureRegion(normalTexture));

        ImageButton.ImageButtonStyle selectedStyle = new ImageButton.ImageButtonStyle();
        selectedStyle.imageUp = new TextureRegionDrawable(new TextureRegion(selectedTexture));

// Initialize button with normal style
        if (isSelected){button5 = new ImageButton(selectedStyle);}
        else{
            button5 = new ImageButton(normalStyle);}
        button5.setPosition(screenWidth - normalTexture.getWidth(), screenHeight - (4 * normalTexture.getHeight()));
        button5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Controller Button Pressed!");

                // Toggle the selection state
                isSelected = !isSelected;

                // Update button style based on the new state
                button5.setStyle(isSelected ? selectedStyle : normalStyle);

                // Switch to StoreScreen only if selected
                if (isSelected) {

                }
            }
        });

        // Add buttons to the stage
        stage.addActor(button1);
        stage.addActor(button2);
        stage.addActor(button3);
        stage.addActor(button4);
        stage.addActor(button5);
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
