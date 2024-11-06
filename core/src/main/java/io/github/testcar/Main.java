package io.github.testcar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {

    public SpriteBatch batch;
    private Texture selectedCarTexture;

    @Override
    public void create() {
        batch = new SpriteBatch();
      setScreen(new SplashScreen(new SpriteBatch()));
   //     setScreen(new GameoverMenu(new SpriteBatch()));
//              setScreen(new asd(new SpriteBatch()));

    }

    // No need to override render() - Game's render() method will handle it

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (batch != null) {
            batch.dispose();
        }
    }
}
