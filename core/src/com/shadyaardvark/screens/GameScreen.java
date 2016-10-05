package com.shadyaardvark.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shadyaardvark.Constants;
import com.shadyaardvark.HalloweenGame2016;
import com.shadyaardvark.maps.Map;
import com.shadyaardvark.maps.MapRenderer;

public class GameScreen extends ScreenAdapter {
    private Viewport viewport;

    private OrthographicCamera camera;

    private Map map;

    private MapRenderer mapRenderer;

    private Music spookyMusic;

    public GameScreen(HalloweenGame2016 game) {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, camera);
        viewport.apply(true);
        spookyMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/spooky song.mp3"));
        spookyMusic.setLooping(true);

        map = new Map("test.tmx");
        mapRenderer = new MapRenderer(map, viewport);

        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void show() {
        spookyMusic.play();
    }

    @Override
    public void resume() {
        spookyMusic.play();
    }

    @Override
    public void pause() {
        spookyMusic.pause();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        mapRenderer.updateViewport(viewport);
    }

    @Override
    public void render(float delta) {
        map.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.render(delta);
    }

    @Override
    public void dispose() {
        spookyMusic.dispose();
        map.dispose();
        mapRenderer.dispose();
    }
}
