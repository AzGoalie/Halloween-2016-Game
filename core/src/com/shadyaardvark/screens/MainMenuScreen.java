package com.shadyaardvark.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shadyaardvark.Constants;
import com.shadyaardvark.HalloweenGame2016;

public class MainMenuScreen extends ScreenAdapter {
    private Stage stage;

    private Viewport viewport;

    private TextureAtlas uiAtlas;

    private BitmapFont font;

    public MainMenuScreen(final HalloweenGame2016 game) {
        viewport = new FitViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        stage = new Stage(viewport, game.getSpriteBatch());
        Gdx.input.setInputProcessor(stage);

        uiAtlas = new TextureAtlas("ui/ui.atlas");
        font = new BitmapFont();

        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
        tbs.font = font;
        tbs.up = new TextureRegionDrawable(uiAtlas.findRegion("red_button01"));
        tbs.down = new TextureRegionDrawable(uiAtlas.findRegion("red_button00"));
        tbs.over = new TextureRegionDrawable(uiAtlas.findRegion("red_button02"));

        TextButton playButton = new TextButton("Play", tbs);
        TextButton settingsButton = new TextButton("Settings", tbs);
        TextButton exitButton = new TextButton("Exit", tbs);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getSpriteBatch()
                        .setColor(Color.WHITE);
                game.setScreen(new GameScreen(game));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        Image title = new Image(new TextureRegionDrawable(uiAtlas.findRegion("title")));

        Table table = new Table();
        table.debug();

        table.row();
        table.add(title)
                .padTop(10f)
                .colspan(2);
        table.row();
        table.add(playButton)
                .padTop(10f)
                .colspan(2);
        table.row();
        table.add(settingsButton)
                .padTop(10f)
                .colspan(2);
        table.row();
        table.add(exitButton)
                .padTop(10f)
                .colspan(2);

        table.setFillParent(true);
        table.pack();

        table.getColor().a = 0f;
        table.addAction(fadeIn(2f));

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
        uiAtlas.dispose();
        font.dispose();
    }
}
