package com.shadyaardvark.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class Box2DDebugSystem extends EntitySystem {
    private Box2DDebugRenderer b2dr;

    private World world;

    private OrthographicCamera camera;

    public Box2DDebugSystem(Box2DDebugRenderer b2dr, World world, OrthographicCamera camera) {
        this.b2dr = b2dr;
        this.camera = camera;
        this.world = world;
    }

    @Override
    public void update(float deltaTime) {
        b2dr.render(world, camera.combined);
    }
}
