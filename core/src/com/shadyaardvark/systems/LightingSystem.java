package com.shadyaardvark.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

import box2dLight.RayHandler;

public class LightingSystem extends EntitySystem {
    private RayHandler rayHandler;

    private Viewport viewport;

    public LightingSystem(RayHandler rayHandler, Viewport viewport) {
        this.rayHandler = rayHandler;
        this.viewport = viewport;

        resize();
    }

    public void resize() {
        rayHandler.useCustomViewport(viewport.getScreenX(),
                viewport.getScreenY(),
                viewport.getScreenWidth(),
                viewport.getScreenHeight());
    }

    @Override
    public void update(float deltaTime) {
        rayHandler.setCombinedMatrix((OrthographicCamera) viewport.getCamera());
        rayHandler.updateAndRender();
    }
}
