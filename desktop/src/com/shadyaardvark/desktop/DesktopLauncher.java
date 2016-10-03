package com.shadyaardvark.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.shadyaardvark.Constants;
import com.shadyaardvark.HalloweenGame2016;

public class DesktopLauncher {
    public static void main(String[] arg) {
//        TexturePacker.Settings settings = new TexturePacker.Settings();
//        settings.maxWidth = 512;
//        settings.maxHeight = 512;
//        TexturePacker.process(settings, "./ui", "../assets", "ui");

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Constants.SCREEN_WIDTH;
        config.height = Constants.SCREEN_HEIGHT;
        new LwjglApplication(new HalloweenGame2016(), config);
    }
}
