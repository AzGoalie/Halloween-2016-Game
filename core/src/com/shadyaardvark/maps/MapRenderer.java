package com.shadyaardvark.maps;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shadyaardvark.Constants;

public class MapRenderer implements Disposable {
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private RayHandler rayHandler;

    private World world;
    private Box2DDebugRenderer b2dr;

    public MapRenderer(Map map, Viewport viewport) {
        camera = (OrthographicCamera) viewport.getCamera();
        renderer = new OrthogonalTiledMapRenderer(map.getTiledMap(), Constants.PPM);
        rayHandler = map.getRayHandler();

        world = map.getWorld();
        b2dr = new Box2DDebugRenderer();
    }

    public void updateViewport(Viewport viewport) {
        rayHandler.useCustomViewport(viewport.getScreenX(),
                viewport.getScreenY(),
                viewport.getScreenWidth(),
                viewport.getScreenHeight());
    }

    public void render(float dt) {
        renderer.setView(camera);
        renderer.render();

        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();

        b2dr.render(world, camera.combined);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        rayHandler.dispose();
    }
}
