package com.shadyaardvark.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.shadyaardvark.components.PhysicsComponent;
import com.shadyaardvark.components.PlayerComponent;

public class PlayerSystem extends IteratingSystem {

    private static final float PLAYER_SPEED = 0.1f;

    private Vector2 tmp;

    private ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);

    public PlayerSystem() {
        super(Family.all(PlayerComponent.class, PhysicsComponent.class)
                .get());
        tmp = new Vector2();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Body body = pm.get(entity).body;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            tmp.set(-PLAYER_SPEED, 0);
            body.applyLinearImpulse(tmp, body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            tmp.set(PLAYER_SPEED, 0);
            body.applyLinearImpulse(tmp, body.getWorldCenter(), true);
        }
    }
}
