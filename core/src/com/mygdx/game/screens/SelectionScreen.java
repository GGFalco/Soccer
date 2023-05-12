package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

    static int index = 0;
    static int index2 = 0;

    public SelectionScreen(final Soccer game) {

        this.game = game;
        this.skin = game.skin;
        this.stage = new Stage();
        this.camera = new OrthographicCamera(1920, 1080);
        camera.setToOrtho(false, 1920, 1080);
        Gdx.input.setInputProcessor(stage);

        backgroundScreen = new Texture(Gdx.files.internal("backgrounds/selectionBackground.jpg"));
        backgroundSprite = new Sprite(backgroundScreen);

        labelStyle = skin.get("c2", Label.LabelStyle.class);
        labelStyle.font = game.bigFont;
        selectLabel = new Label("SELECT YOUR CHARACTER", labelStyle);
        selectLabel.setPosition(775,Gdx.graphics.getHeight() - 200);

        /*
         * Left character animation
         */
        charSheet = new Texture(Gdx.files.internal("sprites/sprites.png"));
        tmp = TextureRegion.split(charSheet, (int) (charSheet.getWidth() / (game.spriteFrameCols * 3.005f)),
                charSheet.getHeight());
        spriteImage = new Image();
        spriteImage.setPosition(300, Gdx.graphics.getHeight() - 650);
        spriteImage.setScale(3);

        changeSprite("red", true);

        /*
         * Right character animation
         */
        charSheet2 = new Texture(Gdx.files.internal("sprites/sprites.png"));
        tmp = TextureRegion.split(charSheet2, (int) (charSheet2.getWidth() / (game.spriteFrameCols * 3.005f)),
                charSheet2.getHeight());
        spriteImage2 = new Image();
        spriteImage2.setPosition(Gdx.graphics.getWidth() - 550, Gdx.graphics.getHeight() - 650);
        spriteImage2.setScale(3);

        changeSprite("red", false);

        configureArrowButtons();
        handleButtonClick();

        stage.addActor(selectLabel);
        stage.addActor(spriteImage);
        stage.addActor(spriteImage2);
        stage.addActor(rightArrowL);
        stage.addActor(rightArrowR);
        stage.addActor(leftArrowL);
        stage.addActor(leftArrowR);
    }

    /**
     * Button configuration
     */
    private void configureArrowButtons(){
        arrowStyle = skin.get("oval2", TextButton.TextButtonStyle.class);
        arrowStyle.font = game.bigFont;

        rightArrowL = new TextButton(">", arrowStyle);
        rightArrowL.setPosition(500, Gdx.graphics.getHeight() - 660);

        leftArrowL  = new TextButton("<", arrowStyle);
        leftArrowL.setPosition(250, Gdx.graphics.getHeight() - 660);

        rightArrowR = new TextButton(">", arrowStyle);
        rightArrowR.setPosition(Gdx.graphics.getWidth() - 325, Gdx.graphics.getHeight() - 660);

        leftArrowR  = new TextButton("<", arrowStyle);
        leftArrowR.setPosition(Gdx.graphics.getWidth() - 575, Gdx.graphics.getHeight() - 660);


    }

    /**
     * Handle the left and right button click
     */
    private void handleButtonClick(){

        rightArrowL.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                index++;

                if(index >= game.spriteNames.size()){
                    index = 0;
                }

                changeSprite(game.spriteNames.get(index), true);
            }
        });

        leftArrowL.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                index--;
                if(index < 0){
                    index = game.spriteNames.size() - 1;
                }

                changeSprite(game.spriteNames.get(index), true);
            }
        });

        rightArrowR.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                index2++;

                if(index2 >= game.spriteNames.size()){
                    index2 = 0;
                }

                changeSprite(game.spriteNames.get(index2), false);
            }
        });

        leftArrowR.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                index2--;
                if(index2 < 0){
                    index2 = game.spriteNames.size() - 1;
                }

                changeSprite(game.spriteNames.get(index2), false);
            }
        });

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,1);
        camera.update();

        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = charAnimation.getKeyFrame(stateTime, true);
        spriteImage.setDrawable(new TextureRegionDrawable(currentFrame));
        spriteImage.setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight());

        TextureRegion currentFrame2 = charAnimation2.getKeyFrame(stateTime, true);
        spriteImage2.setDrawable(new TextureRegionDrawable(currentFrame2));
        spriteImage2.setSize(currentFrame2.getRegionWidth(), currentFrame2.getRegionHeight());

        game.batch.begin();
        backgroundSprite.draw(game.batch, .25f);
        game.batch.end();
        stage.draw();
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
    }

    /**
     * Handle the animation of the sprites on the screen
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
        if(left){
            charAnimation = new Animation<>(0.200f, walkFrames);
        } else {
            charAnimation2 = new Animation<>(0.200f, walkFrames);
        }
        stateTime = 0f;
    }
}