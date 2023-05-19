package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.screens.WelcomeScreen;

import java.util.ArrayList;

public class Soccer extends Game {

    public static final float PPM = 100f;
    public static final int SCREEN_WIDTH = 1920;
    public static final float V_WIDTH = SCREEN_WIDTH / PPM;
    public static final int SCREEN_HEIGHT = 1080;
    public static final float V_HEIGHT = SCREEN_HEIGHT / PPM;

    public static final short BIT_BALL = 2;
    public static final short BIT_NET = 4;
    public static final short BIT_PLAYER = 8;
    public static final short BIT_FOOT = 16;
    public static final short BIT_HEAD = 32;
    public static final short BIT_GROUND = 64;
    public static final short BIT_WALL = 128;


    public SpriteBatch batch;
    public BitmapFont bigFont;
    public BitmapFont mediumFont;
    public BitmapFont normalFont;
    public BitmapFont arcadeFont;
    public BitmapFont littleArcadeFont;

    public TextButton.TextButtonStyle textButtonStyle;

    public Skin skin;
    public Skin arcadeSkin;

    public final String stylesPath = "style/lgdxs-ui.json";
    public final String arcadeStylePath = "style/arcade/arcade-ui.json";

    public java.util.List<String> spriteNames = new ArrayList<>();
    public java.util.List<String> spriteAtlas = new ArrayList<>();
    public final int spriteFrameCols = 7;

    @Override
    public void create() {

        this.batch = new SpriteBatch();
        this.skin = new Skin(Gdx.files.internal(stylesPath));
        this.arcadeSkin = new Skin(Gdx.files.internal(arcadeStylePath));

        arcadeFont = new BitmapFont(Gdx.files.internal("style/arcade/font-export.fnt"));
        arcadeFont.getData().setScale(3f,3f);

        littleArcadeFont = new BitmapFont(Gdx.files.internal("style/arcade/font-export.fnt"));
        littleArcadeFont.getData().setScale(1.8f, 1.8f);


        normalFont = new BitmapFont(Gdx.files.internal("fonts/font-export.fnt"));
        normalFont.getData().setScale(1f, 1f);

        mediumFont = new BitmapFont(Gdx.files.internal("fonts/sub-title-font-export.fnt"));
        mediumFont.getData().setScale(3.3f, 3.8f);

        bigFont = new BitmapFont(Gdx.files.internal("fonts/title-font-export.fnt"));


        textButtonStyle = skin.get("default", TextButton.TextButtonStyle.class);
        textButtonStyle.font = this.bigFont;

        spriteNames.add("red");
        spriteNames.add("mario");
        spriteNames.add("luigi");
        spriteAtlas.add("char1-walk");
        spriteAtlas.add("char2-walk");
        spriteAtlas.add("char3-walk");


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
        arcadeSkin.dispose();
        arcadeFont.dispose();
        littleArcadeFont.dispose();
    }

}
