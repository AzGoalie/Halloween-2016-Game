package com.shadyaardvark;

import com.badlogic.gdx.math.Vector2;

public class Constants {

    public static final int VIEWPORT_WIDTH = 15;

    public static final int VIEWPORT_HEIGHT = 25;

    public static final int SCREEN_WIDTH = 480;

    public static final int SCREEN_HEIGHT = 800;

    // Box2D constants
    public static final Vector2 DEFAULT_GRAVITY = new Vector2(0, -10);

    public static final float TIME_STEP = 1 / 60f;

    public static final float PPM = 1 / 128f;

    public static final int NUM_RAYS = 128;

    public static final float PLAYER_WIDTH = .5f;

    public static final float PLAYER_HEIGHT = 1.5f;

    private Constants() {
        // Used for constants!
    }
}
