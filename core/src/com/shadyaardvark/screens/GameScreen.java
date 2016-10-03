package com.shadyaardvark.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.shadyaardvark.Constants;
import com.shadyaardvark.HalloweenGame2016;

public class GameScreen extends ScreenAdapter {
    private TiledMap map;

    private TiledMapRenderer mapRenderer;

    private OrthographicCamera camera;

    private Music spookyMusic;

    public GameScreen(HalloweenGame2016 game) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        map = new TmxMapLoader().load("test.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 128f, game.getSpriteBatch());

        spookyMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/spooky song.mp3"));
        spookyMusic.setLooping(true);
    }

    @Override
    public void show() {
        spookyMusic.play();
    }

    @Override
    public void hide() {
        spookyMusic.stop();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    @Override
    public void dispose() {
        map.dispose();
        spookyMusic.dispose();
    }
}
