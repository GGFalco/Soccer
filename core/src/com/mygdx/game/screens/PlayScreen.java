package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Soccer;

public class PlayScreen implements Screen {

    final Soccer game;

    Stage stage;
    OrthographicCamera camera;
    Skin skin;



    public PlayScreen(final Soccer game) {

        this.game = game;
        this.skin = game.skin;
        this.stage = new Stage();
        this.camera = new OrthographicCamera(1920, 1080);
        camera.setToOrtho(false, 1920, 1080);
        Gdx.input.setInputProcessor(stage);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0,0,0,1);
        camera.update();

        game.batch.begin();

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
}
