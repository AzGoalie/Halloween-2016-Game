package com.shadyaardvark.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;

public class MapRenderSystem extends EntitySystem {

    private TiledMapRenderer renderer;

    private OrthographicCamera camera;

    public MapRenderSystem(TiledMapRenderer renderer, OrthographicCamera camera) {
        this.renderer = renderer;
        this.camera = camera;
    }

    @Override
    public void update(float deltaTime) {
        renderer.setView(camera);
        renderer.render();
    }
}
