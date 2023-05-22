package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Soccer;

public class WelcomeScreen implements Screen {

    final Soccer game;

    Stage stage;
    OrthographicCamera camera;
    Skin skin;

    TextButton playButton;
    Texture backgroundScreen;
    Sprite backgroundSprite;


    public WelcomeScreen(final Soccer game) {

        this.game = game;
        this.skin = game.skin;
        this.stage = new Stage();
        this.camera = new OrthographicCamera(1920, 1080);
        camera.setToOrtho(false, 1920, 1080);
        Gdx.input.setInputProcessor(stage);

        backgroundScreen = new Texture(Gdx.files.internal("backgrounds/MainBackground.png"));
        backgroundSprite = new Sprite(backgroundScreen);

        playButton = new TextButton("PLAY", game.textButtonStyle);
        playButton.setPosition((float) (Gdx.graphics.getWidth() / 2 * .95), (float) (Gdx.graphics.getHeight() / 2));

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new SelectionScreen(game));
            }
        });


        stage.addActor(playButton);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0,0,0,1);
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.begin();

        backgroundSprite.draw(game.batch, .35f);
        handleClickEvents();

        game.batch.end();
        stage.draw();
    }

    /**
     * Handle the click events on the screen
     */
    private void handleClickEvents(){
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            game.setScreen(new SelectionScreen(game));
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
        stage.dispose();
        skin.dispose();
        backgroundScreen.dispose();
    }
}
