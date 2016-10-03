package com.shadyaardvark.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shadyaardvark.Constants;
import com.shadyaardvark.HalloweenGame2016;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class GameScreen extends ScreenAdapter {
    private TiledMap map;

    private TiledMapRenderer mapRenderer;

    private Viewport viewport;

    private OrthographicCamera camera;

    private Music spookyMusic;

    private World world;

    private RayHandler rayHandler;

    public GameScreen(HalloweenGame2016 game) {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, camera);
        viewport.apply(true);

        map = new TmxMapLoader().load("test.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, Constants.PPM, game.getSpriteBatch());

        spookyMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/spooky song.mp3"));
        spookyMusic.setLooping(true);

        Gdx.input.setInputProcessor(null);

        world = new World(Constants.DEFAULT_GRAVITY, true);
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.2f);

        for (MapObject light : map.getLayers()
                .get("lights")
                .getObjects()) {
            Ellipse shape = ((EllipseMapObject) light).getEllipse();
            new PointLight(rayHandler,
                    32,
                    null,
                    shape.width * Constants.PPM,
                    (shape.x + shape.width / 2) * Constants.PPM,
                    (shape.y + shape.height / 2) * Constants.PPM);
        }
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
        rayHandler.useCustomViewport(viewport.getScreenX(),
                viewport.getScreenY(),
                viewport.getScreenWidth(),
                viewport.getScreenHeight());
    }

    @Override
    public void render(float delta) {
        world.step(delta, 8, 3);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(camera);
        mapRenderer.render();

        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
    }

    @Override
    public void dispose() {
        map.dispose();
        spookyMusic.dispose();
        world.dispose();
        rayHandler.dispose();
    }
}
