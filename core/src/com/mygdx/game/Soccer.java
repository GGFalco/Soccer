package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.screens.WelcomeScreen;

import java.util.ArrayList;
import java.util.List;

public class Soccer extends Game {

    public SpriteBatch batch;
    public BitmapFont bigFont;
    public BitmapFont mediumFont;
    public BitmapFont normalFont;

    public TextButton.TextButtonStyle textButtonStyle;

    public Skin skin;

    public final String stylesPath = "style/lgdxs-ui.json";

    public java.util.List<String> spriteNames = new ArrayList<>();
    public final int spriteFrameCols = 7;

    @Override
    public void create() {

        this.batch = new SpriteBatch();
        this.skin = new Skin(Gdx.files.internal(stylesPath));

        normalFont = new BitmapFont(Gdx.files.internal("fonts/font-export.fnt"));
        normalFont.getData().setScale(1f, 1f);

        mediumFont = new BitmapFont(Gdx.files.internal("fonts/sub-title-font-export.fnt"));
        mediumFont.getData().setScale(3.3f, 3.8f);

        bigFont = new BitmapFont(Gdx.files.internal("fonts/title-font-export.fnt"));


        textButtonStyle = skin.get("default", TextButton.TextButtonStyle.class);
        textButtonStyle.font = this.bigFont;

        spriteNames.add("red");
        spriteNames.add("purple");
        spriteNames.add("white");

        this.setScreen(new WelcomeScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        normalFont.dispose();
        mediumFont.dispose();
        bigFont.dispose();
        skin.dispose();
    }
}
