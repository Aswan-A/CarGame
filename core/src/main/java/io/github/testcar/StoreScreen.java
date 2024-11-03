package io.github.testcar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

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

    public StoreScreen(SpriteBatch batch) {
        this.batch = batch;
        initialize();
        create();
    }

    private void create() {
        backgroundTexture = new Texture("StoreScreenBackground.jpg");
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight() );

        // Arrange the layout
        carTable.add(createCarButton(car1Texture, "Car 1A")).pad(10);
        carTable.add(createCarButton(car2Texture, "Car 2A")).pad(10);
        carTable.row();
        carTable.add(createSelectButton()).colspan(2).center().pad(10);

        stage.addActor(carTable); // Add the table to the stage
    }

    private void initialize() {
        viewport = new FitViewport(800, 480);
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        // Load textures for car selection
        car1Texture = new Texture("car1A.png");
        car2Texture = new Texture("car2A.png");
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
                System.out.println("Selected: " + selectedCar); // Debug log
            }
        });
        return carButton;
    }

    private ImageButton createSelectButton() {
        ImageButton selectButton = new ImageButton(skin);
        selectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Selected car confirmed: " + selectedCar); // Confirm selected car
                Main game = (Main) Gdx.app.getApplicationListener();
                game.setScreen(new GameMenu(batch, selectedCarTexture)); // Navigate to GameMenu
            }
        });
        return selectButton;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Clear the screen
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
        skin.dispose();
        backgroundTexture.dispose(); // Dispose the background texture as well
    }
}
