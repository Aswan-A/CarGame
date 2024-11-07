package io.github.testcar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

    public class asd implements Screen {

        SpriteBatch batch;
        BitmapFont font;


        public asd(SpriteBatch batch) {
            // Initialize the SpriteBatch and load the custom BitmapFont
            this.batch=batch;
            font = new BitmapFont(Gdx.files.internal("press.fnt"), Gdx.files.internal("press.png"), false);
            font.getData().setScale(1.5f);
        }
        @Override
        public void render(float delta) {
            // Clear the screen
            ScreenUtils.clear(0, 0, 0, 1);  // Black background

            // Begin drawing
            batch.begin();

            // Set the color (optional)
            font.setColor(1, 1, 1, 1); // White color

            // Draw text
            RandomMathProblemGenerator generator = new RandomMathProblemGenerator(1,3);
            font.draw(batch, generator.generateProblem(), 800, 1000);



            // End drawing
            batch.end();
        }
        @Override
        public void show() {

        }



        @Override
        public void resize(int width, int height) {

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

        public void dispose() {
            // Dispose resources
            batch.dispose();
            font.dispose();
        }
    }

