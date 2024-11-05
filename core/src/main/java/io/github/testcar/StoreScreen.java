package io.github.testcar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static io.github.testcar.GameMenu.isMusicOn;

public class StoreScreen implements Screen {

    private final SpriteBatch batch;
    private Texture backgroundTexture;
    private Stage stage;
    private FitViewport viewport;
    private Texture car1Texture, car2Texture, selectedCarTexture;
    private Skin skin;
    private String selectedCar = "Car 1"; // Default selected car
    private Table carTable;
    private Sprite backgroundSprite;
    private Texture car3Texture;
    private Texture car4Texture;
    private Texture car5Texture;
    private Texture car6Texture;
    private Texture car7Texture;
    private Texture car8Texture;
    private ImageButton selectButton;
    private ImageButton selectedCarButton; // Track the currently selected button
    Music music;
    // Play button texture
    Texture selectButtonTexture = new Texture("Selectbutton.png");

    public StoreScreen(SpriteBatch batch) {
        this.batch = batch;
        initialize();
        create();
    }

    private void create() {
        backgroundTexture = new Texture("popo.png");
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight() );
        carTable.row();

        // Arrange the layout
        carTable.add(createCarButton(car1Texture, "Car 1A")).pad(200,10,20,10);
        carTable.add(createCarButton(car2Texture, "Car 2A")).pad(200,10,20,10);
        carTable.add(createCarButton(car3Texture, "Car 3A")).pad(200,10,20,10);
        carTable.add(createCarButton(car4Texture, "Car 4A")).pad(200,10,20,10);

        carTable.row();

        carTable.add(createCarButton(car5Texture, "Car 5A")).pad(10);
        carTable.add(createCarButton(car6Texture, "Car 6A")).pad(10);
        carTable.add(createCarButton(car7Texture, "Car 7A")).pad(10);
        carTable.add(createCarButton(car8Texture, "Car 8A")).pad(10);

        carTable.row();
        stage.addActor(createSelectButton()); // Add button to the stage
        stage.addActor(carTable); // Add the table to the stage
        music = Gdx.audio.newMusic(Gdx.files.internal("music1.mp3"));
        if (isMusicOn){
            music.setLooping(true);
            music.setVolume(1f);
            music.play();}
    }

    private void initialize() {
        viewport = new FitViewport(800, 480);
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        // Load textures for car selection
        car1Texture = new Texture("car1A.png");
        car2Texture = new Texture("car2A.png");
        car3Texture = new Texture("car3A.png");
        car4Texture = new Texture("car4A.png");
        car5Texture = new Texture("car5A.png");
        car6Texture = new Texture("car6A.png");
        car7Texture = new Texture("car7A.png");
        car8Texture = new Texture("car8A.png");
        selectedCarTexture = car1Texture; // Default selected car

        // Load the skin for UI elements
        skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));

        // Create car selection table
        carTable = new Table();
        carTable.setFillParent(true);
    }

    private ImageButton createCarButton(Texture carTexture, String carName) {
        ImageButton carButton = new ImageButton(new TextureRegionDrawable(carTexture));
        carButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedCar = carName; // Update selected car
                selectedCarTexture = carTexture; // Set selected texture

                // Reset the color of the previously selected button, if any
                if (selectedCarButton != null) {
                    selectedCarButton.getImage().setColor(1f, 1f, 1f, 1f); // Reset to original color
                }

                // Apply red tint to the currently selected button
                carButton.getImage().setColor(0f, 1f, 0f, 0.75f); // Change to red
                selectedCarButton = carButton; // Update reference to the selected button

                System.out.println("Selected: " + selectedCar); // Debug log
            }
        });
        return carButton;
    }



    private ImageButton createSelectButton() {
        TextureRegion buttonRegion = new TextureRegion(selectButtonTexture);
        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(buttonRegion);
        selectButton = new ImageButton(buttonDrawable); // Use the class variable
        selectButton.setPosition(350, 403);
        selectButton.setSize(100,87);
        selectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Selected car confirmed: " + selectedCar); // Confirm selected car
                Main game = (Main) Gdx.app.getApplicationListener();
                music.stop();
                game.setScreen(new GameMenu(batch, selectedCarTexture)); // Navigate to GameMenu
            }
        });
        return selectButton;
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0.5f); // Clear the screen
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT); // Clear the buffer

        batch.begin();
        backgroundSprite.draw(batch); // Draw the background
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
        car1Texture.dispose();
        car2Texture.dispose();
        car3Texture.dispose();
        car4Texture.dispose();
        car5Texture.dispose();
        car6Texture.dispose();
        car7Texture.dispose();
        car8Texture.dispose();
        skin.dispose();
        backgroundTexture.dispose(); // Dispose the background texture as well
    }
}
