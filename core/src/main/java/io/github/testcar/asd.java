package io.github.testcar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;

public class asd implements Screen {

    private final Skin skin;
    private final Stage stage;

    public asd() {
        // Initialize the Skin and Stage
        this.skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));
        this.stage = new Stage();
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        ScreenUtils.clear(0, 0, 0, 1);

        // Set up a Table for layout
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // Generate the math problem and create a label
        RandomMathProblemGenerator generator = new RandomMathProblemGenerator(1, 3);
        String mathProblem = generator.generateProblem();
        Label mathLabel = new Label(mathProblem, skin);
        mathLabel.setFontScale(2.0f); // Scale up for visibility
        root.add(mathLabel).padBottom(20).row();

        // Draw the stage with the label
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); // Set input processor to the stage
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        // Dispose of resources
        stage.dispose();
    }
}
