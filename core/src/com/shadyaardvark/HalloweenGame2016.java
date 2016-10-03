package com.shadyaardvark;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shadyaardvark.screens.MainMenuScreen;

public class HalloweenGame2016 extends Game {
    private SpriteBatch spriteBatch;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        setScreen(new MainMenuScreen(this));
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
}
