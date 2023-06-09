package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Soccer;

public class SelectionScreen implements Screen {

    public final Soccer game;

    Stage stage;
    OrthographicCamera camera;
    Skin skin;

    Texture backgroundScreen;
    Sprite backgroundSprite;

    Label selectLabel;
    Label.LabelStyle labelStyle;

    Label leftPlayerName;
    Label rightPlayerName;

    Image spriteImage;
    Animation<TextureRegion> charAnimation;
    TextureRegion[][] tmp;
    Texture charSheet;
    float stateTime;

    Image spriteImage2;
    Animation<TextureRegion> charAnimation2;
    Texture charSheet2;

    TextButton.TextButtonStyle arrowStyle;

    TextButton rightArrowL;
    TextButton leftArrowL;
    TextButton rightArrowR;
    TextButton leftArrowR;
    TextButton playButton;

    TextButton rightArrowMap;
    TextButton leftArrowMap;

    TextureAtlas mapAtlas;
    TextureRegion mapScreen;
    Sprite mapSprite;

    static int index;
    static int index2;
    static int mapIndex;

    public SelectionScreen(final Soccer game) {

        this.game = game;
        this.skin = game.skin;
        this.stage = new Stage();
        this.camera = new OrthographicCamera();
        index = 0;
        index2 = 0;
        mapIndex = 0;
        camera.setToOrtho(false, 1920, 1080);
        Gdx.input.setInputProcessor(stage);

        mapAtlas = new TextureAtlas("mappe.atlas");
        changeMapImage(0);

        backgroundScreen = new Texture(Gdx.files.internal("backgrounds/selectionBackground.jpg"));
        backgroundSprite = new Sprite(backgroundScreen);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        labelStyle = skin.get("c2", Label.LabelStyle.class);
        labelStyle.font = game.bigFont;
        selectLabel = new Label("SELECT YOUR CHARACTER", labelStyle);
        selectLabel.setPosition(795, Gdx.graphics.getHeight() - 200);

        labelStyle = game.arcadeSkin.get("default", Label.LabelStyle.class);
        labelStyle.font = game.arcadeFont;
        labelStyle.font.getData().setScale(1.5f, 1.5f);
        leftPlayerName = new Label("", labelStyle);
        leftPlayerName.setPosition(345, Gdx.graphics.getHeight() - 740);
        rightPlayerName = new Label("", labelStyle);
        rightPlayerName.setPosition(Gdx.graphics.getWidth() - 480, Gdx.graphics.getHeight() - 740);

        /*
         * Left character animation
         */
        charSheet = new Texture(Gdx.files.internal("sprites/sprites2.png"));
        tmp = TextureRegion.split(charSheet, charSheet.getWidth() / 21, charSheet.getHeight());
        spriteImage = new Image();
        spriteImage.setPosition(300, Gdx.graphics.getHeight() - 650);
        spriteImage.setScale(3);

        changeSprite("Giustino", true);

        /*
         * Right character animation
         */
        charSheet2 = new Texture(Gdx.files.internal("sprites/sprites2.png"));
        tmp = TextureRegion.split(charSheet2, charSheet2.getWidth() / 21, charSheet2.getHeight());
        spriteImage2 = new Image();
        spriteImage2.setPosition(Gdx.graphics.getWidth() - 550, Gdx.graphics.getHeight() - 650);
        spriteImage2.setScale(3);

        changeSprite("Giustino", false);

        configureButtons();
        handleButtonClick();




        /*
         * Add actors
         */
        stage.addActor(leftPlayerName);
        stage.addActor(rightPlayerName);
        stage.addActor(selectLabel);
        stage.addActor(spriteImage);
        stage.addActor(spriteImage2);
        stage.addActor(rightArrowL);
        stage.addActor(rightArrowR);
        stage.addActor(leftArrowL);
        stage.addActor(leftArrowR);
        stage.addActor(playButton);
        stage.addActor(leftArrowMap);
        stage.addActor(rightArrowMap);
    }

    /**
     * Button configuration
     */
    private void configureButtons() {

        arrowStyle = skin.get("oval1", TextButton.TextButtonStyle.class);
        arrowStyle.font = game.normalFont;

        leftArrowMap = new TextButton("<", arrowStyle);
        leftArrowMap.setPosition((Soccer.SCREEN_WIDTH / 2f) - 250, (Soccer.SCREEN_HEIGHT / 2f) + 75);
        leftArrowMap.setBounds(leftArrowMap.getX(), leftArrowMap.getY(), 60, 35);

        rightArrowMap = new TextButton(">", arrowStyle);
        rightArrowMap.setPosition((Soccer.SCREEN_WIDTH / 2f) + 250, (Soccer.SCREEN_HEIGHT / 2f) + 75);
        rightArrowMap.setBounds(rightArrowMap.getX(), rightArrowMap.getY(), 60, 35);

        arrowStyle = skin.get("oval2", TextButton.TextButtonStyle.class);
        arrowStyle.font = game.bigFont;

        playButton = new TextButton("PLAY", game.textButtonStyle);
        playButton.setPosition((float) Gdx.graphics.getWidth() * .97f / 2, (float) Gdx.graphics.getHeight() - 760);

        rightArrowL = new TextButton(">", arrowStyle);
        rightArrowL.setPosition(500, Gdx.graphics.getHeight() - 660);

        leftArrowL = new TextButton("<", arrowStyle);
        leftArrowL.setPosition(250, Gdx.graphics.getHeight() - 660);

        rightArrowR = new TextButton(">", arrowStyle);
        rightArrowR.setPosition(Gdx.graphics.getWidth() - 325, Gdx.graphics.getHeight() - 660);

        leftArrowR = new TextButton("<", arrowStyle);
        leftArrowR.setPosition(Gdx.graphics.getWidth() - 575, Gdx.graphics.getHeight() - 660);
    }

    /**
     * Handle the left and right button click
     */
    private void handleButtonClick() {

        leftArrowMap.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (mapIndex <= 0) {
                    mapIndex = game.mapNames.size() - 1;
                } else {
                    mapIndex--;
                }
                changeMapImage(mapIndex);
            }
        });

        rightArrowMap.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (mapIndex >= game.mapNames.size() - 1) {
                    mapIndex = 0;
                } else {
                    mapIndex++;
                }
                changeMapImage(mapIndex);
            }
        });

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                game.setScreen(new PlayScreen(game, game.spriteAtlas.get(index), game.spriteAtlas.get(index2), game.mapNames.get(mapIndex)));
            }
        });

        rightArrowL.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                index++;

                if (index >= game.spriteNames.size()) {
                    index = 0;
                }

                changeSprite(game.spriteNames.get(index), true);
            }
        });

        leftArrowL.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                index--;
                if (index < 0) {
                    index = game.spriteNames.size() - 1;
                }

                changeSprite(game.spriteNames.get(index), true);
            }
        });

        rightArrowR.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                index2++;

                if (index2 >= game.spriteNames.size()) {
                    index2 = 0;
                }

                changeSprite(game.spriteNames.get(index2), false);
            }
        });

        leftArrowR.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                index2--;
                if (index2 < 0) {
                    index2 = game.spriteNames.size() - 1;
                }

                changeSprite(game.spriteNames.get(index2), false);
            }
        });
    }

    /**
     * Changes the map image on the screen depending on user choice
     *
     * @param i index in the array that contains the names of atlas region
     */
    public void changeMapImage(int i) {
        mapScreen = new TextureRegion(mapAtlas.findRegion(game.mapNames.get(i)));
        mapSprite = new Sprite(mapScreen);
        mapSprite.setPosition((Soccer.SCREEN_WIDTH - 1000) / 2f, (Soccer.SCREEN_HEIGHT - 400) / 2f);
        mapSprite.setScale(.35f, .35f);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = charAnimation.getKeyFrame(stateTime, true);
        spriteImage.setDrawable(new TextureRegionDrawable(currentFrame));
        spriteImage.setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight());

        TextureRegion currentFrame2 = charAnimation2.getKeyFrame(stateTime, true);
        spriteImage2.setDrawable(new TextureRegionDrawable(currentFrame2));
        spriteImage2.setSize(currentFrame2.getRegionWidth(), currentFrame2.getRegionHeight());

        updateNames();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        backgroundSprite.draw(game.batch, .25f);
        mapSprite.draw(game.batch, 1);
        handleClickEvent();

        game.batch.end();
        stage.draw();
    }

    /**
     * Update the sprite name label on player selection
     */
    public void updateNames() {

        leftPlayerName.setText(game.spriteNames.get(index));
        rightPlayerName.setText(game.spriteNames.get(index2));
    }

    /**
     * Handle the click events on the screen
     */
    private void handleClickEvent() {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            game.setScreen(new PlayScreen(game, game.spriteAtlas.get(index), game.spriteAtlas.get(index2), game.mapNames.get(mapIndex)));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
        backgroundScreen.dispose();
        charSheet.dispose();
        charSheet2.dispose();
    }

    /**
     * Handle the animation of the sprites on the screen
     *
     * @param spriteName the name of the sprite character
     * @param left refer to left player
     */
    public void changeSprite(String spriteName, boolean left) {

        int listPosition = game.spriteNames.indexOf(spriteName) * game.spriteFrameCols;
        TextureRegion[] walkFrames = new TextureRegion[game.spriteFrameCols];
        int index = 0;
        for (int i = listPosition; i < game.spriteFrameCols + listPosition; i++) {
            walkFrames[index++] = tmp[0][i];
        }
        if (left) {
            charAnimation = new Animation<>(0.200f, walkFrames);
        } else {
            charAnimation2 = new Animation<>(0.200f, walkFrames);
        }
        stateTime = 0f;
    }
}
