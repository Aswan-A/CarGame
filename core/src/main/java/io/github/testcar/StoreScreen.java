package io.github.testcar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
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

public class StoreScreen implements Screen {

    private final SpriteBatch batch;
    private Stage stage;
    private FitViewport viewport;
    private Texture car1Texture, car2Texture, selectedCarTexture;
    private Skin skin;
    private Label carLabel;
    private String selectedCar = "Car 1";
    private Table carTable;

    public StoreScreen(SpriteBatch batch) {
        this.batch = batch;
        initialize();
    }

    private void initialize() {
        viewport = new FitViewport(800, 480);
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        // Load textures for car selection
        car1Texture = new Texture("car1.png");
        car2Texture = new Texture("car2.png");
        selectedCarTexture = car1Texture; // Default selected car

        // Load the skin for UI elements
        skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));

        // Create car selection table
        carTable = new Table();
        carTable.setFillParent(true);

        // Car 1 Button
        ImageButton car1Button = new ImageButton(new TextureRegionDrawable(car1Texture));
        car1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedCar = "Car 1"; // Update selected car
                selectedCarTexture = car1Texture; // Set selected texture
                System.out.println("Selected: " + selectedCar); // Debug log
            }
        });

        // Car 2 Button
        ImageButton car2Button = new ImageButton(new TextureRegionDrawable(car2Texture));
        car2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedCar = "Car 2"; // Update selected car
                selectedCarTexture = car2Texture; // Set selected texture
                System.out.println("Selected: " + selectedCar); // Debug log
            }
        });

        // Select Button
        ImageButton selectButton = new ImageButton(skin);
        selectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Selected car confirmed: " + selectedCar); // Confirm selected car
                Main game = (Main) Gdx.app.getApplicationListener();
                game.setScreen(new GameMenu(batch, selectedCarTexture)); // Navigate to GameMenu
            }
        });

        // Arrange the layout
        carTable.add(car1Button).pad(10);
        carTable.add(car2Button).pad(10);
        carTable.row();
        carTable.add(selectButton).colspan(2).center().pad(10);

        stage.addActor(carTable); // Add the table to the stage
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); // Set the stage as input processor when the screen is shown
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
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
    }
}
