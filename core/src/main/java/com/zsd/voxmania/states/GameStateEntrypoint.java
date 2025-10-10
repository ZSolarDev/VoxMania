package com.zsd.voxmania.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.function.BiConsumer;

public class GameStateEntrypoint extends com.badlogic.gdx.Game {
    BiConsumer<Integer, Integer> onResize;
    Runnable onPause;
    Runnable onResume;
    public static OrthographicCamera camera;
    public static FitViewport viewport;
    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera);
        StateManager.init(this);
    }

    @Override
    public void render() {
        super.render();
        viewport.apply();
        camera.update();
        StateManager.update(Gdx.graphics.getDeltaTime());
    }

    @Override public void resize(int width, int height) {
        viewport.update(width, height, true);
        if (onResize != null)
            onResize.accept(width, height);
    }
    @Override public void pause() {
        if (onPause != null)
            onPause.run();
    }
    @Override public void resume() {
        if (onResume != null)
            onResume.run();
    }
}
