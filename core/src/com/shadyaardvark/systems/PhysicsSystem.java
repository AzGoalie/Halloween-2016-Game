package com.shadyaardvark.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.shadyaardvark.components.PhysicsComponent;
import com.shadyaardvark.components.TransformComponent;

public class PhysicsSystem extends IteratingSystem {
    private static final float STEP_TIME = 1 / 300f;

    private float accumulator = 0f;

    private double currentTime = 0f;

    private World world;

    private Array<Entity> bodiesQueue;

    private ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);

    private ComponentMapper<TransformComponent> tm =
            ComponentMapper.getFor(TransformComponent.class);

    public PhysicsSystem(World world) {
        super(Family.all(PhysicsComponent.class, TransformComponent.class)
                .get());
        this.world = world;
        bodiesQueue = new Array<>();
    }

    private void interpolate(float alpha) {
        for (Entity entity : bodiesQueue) {
            Body body = pm.get(entity).body;
            TransformComponent transformComponent = tm.get(entity);

            Transform transform = body.getTransform();
            Vector2 bodyPosition = transform.getPosition();
            float bodyAngle = transform.getRotation();

            if (body.isActive()) {
                transformComponent.position.x =
                        bodyPosition.x * alpha + transformComponent.position.x * (1.0f - alpha);
                transformComponent.position.y =
                        bodyPosition.y * alpha + transformComponent.position.y * (1.0f - alpha);
                transformComponent.rotation =
                        bodyAngle * alpha + transformComponent.rotation * (1.0f - alpha);
            } else {
                transformComponent.position.x = bodyPosition.x;
                transformComponent.position.y = bodyPosition.y;
                transformComponent.rotation = bodyAngle;
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        double newTime = TimeUtils.millis() / 1000.0;
        double frameTime = Math.min(newTime - currentTime, 0.25);

        currentTime = newTime;
        accumulator += frameTime;

        while (accumulator >= STEP_TIME) {
            world.step(STEP_TIME, 6, 2);
            accumulator -= STEP_TIME;
            interpolate(accumulator / STEP_TIME);
        }

        bodiesQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }
}
