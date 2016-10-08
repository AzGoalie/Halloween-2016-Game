package com.shadyaardvark;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends InputAdapter {
    private static float MAX_SPEED = 5f;

    private static float ACCELERATION = 0.1f;

    private Body body;

    private Vector2 tmp;

    private boolean leftPressed;

    private boolean rightPressed;

    public Player(World world, Vector2 pos) {
        definePlayer(world, pos);
        tmp = new Vector2();
    }

    public void update(float dt) {
        if (leftPressed && body.getLinearVelocity().x >= -MAX_SPEED) {
            tmp.set(-ACCELERATION, 0);
            body.applyLinearImpulse(tmp, body.getWorldCenter(), true);
        }

        if (rightPressed && body.getLinearVelocity().x <= MAX_SPEED) {
            tmp.set(ACCELERATION, 0);
            body.applyLinearImpulse(tmp, body.getWorldCenter(), true);
        }
    }

    private void definePlayer(World world, Vector2 pos) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(pos.add(Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 2));
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.PLAYER_WIDTH * 0.5f, Constants.PLAYER_HEIGHT * 0.5f);
        body.createFixture(shape, 1);

        shape.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
        case Input.Keys.LEFT:
            leftPressed = true;
            return true;
        case Input.Keys.RIGHT:
            rightPressed = true;
            return true;
        case Input.Keys.UP:
            break;
        case Input.Keys.DOWN:
            break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
        case Input.Keys.LEFT:
            leftPressed = false;
            return true;
        case Input.Keys.RIGHT:
            rightPressed = false;
            return true;
        case Input.Keys.UP:
            break;
        case Input.Keys.DOWN:
            break;
        }

        return false;
    }
}
