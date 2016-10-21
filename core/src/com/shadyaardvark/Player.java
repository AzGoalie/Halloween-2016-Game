package com.shadyaardvark;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Player {
    private static final float PLAYER_SPEED = 0.5f;

    private static final float PLAYER_JUMP = 3.0f;

    private Vector2 tmp;

    private Body playerBody;

    public Player(World world, Body body) {
        this.playerBody = body;
        this.playerBody.setFixedRotation(true);
        tmp = new Vector2();
    }

    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            tmp.set(-PLAYER_SPEED, 0);
            playerBody.applyLinearImpulse(tmp, playerBody.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            tmp.set(PLAYER_SPEED, 0);
            playerBody.applyLinearImpulse(tmp, playerBody.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && canJump()) {
            tmp.set(0, PLAYER_JUMP);
            playerBody.applyLinearImpulse(tmp, playerBody.getWorldCenter(), true);
        }
    }

    public boolean canJump() {
        return true;
    }
}
