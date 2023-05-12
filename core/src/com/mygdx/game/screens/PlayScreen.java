package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MyStage;
import com.mygdx.game.Soccer;
import com.mygdx.game.sprite.Player;

public class PlayScreen implements Screen {

    final Soccer game;

    MyStage myStage;

    Stage stage;
    OrthographicCamera camera;
    Skin skin;

    Texture backgroundScreen;
    Sprite backgroundSprite;

    TextureAtlas atlas;
    private World world;
    private Box2DDebugRenderer b2dr;
    Player player;

    public PlayScreen(final Soccer game) {

        atlas = new TextureAtlas("sprite_head.atlas");

        configurePlayer();

        this.game = game;
        this.skin = game.skin;
        //this.stage = new Stage();
        this.myStage = new MyStage();
        this.camera = new OrthographicCamera(1920, 1080);
        camera.setToOrtho(false, 1920, 1080);
        Gdx.input.setInputProcessor(myStage);


        backgroundScreen = new Texture(Gdx.files.internal("backgrounds/sfondo_partita.png"));
        backgroundSprite = new Sprite(backgroundScreen);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    private void configurePlayer(){
        world = new World(new Vector2(0, -9.81f), true);
        b2dr = new Box2DDebugRenderer();

        player = new Player(world, this);
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public void update(){
        world.step(1/60f, 6, 2);
        camera.update();
    }

    @Override
    public void render(float delta) {

        //ScreenUtils.clear(0,0,0,1);
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update();
        handleMovements();

        b2dr.render(world, camera.combined);

        game.batch.begin();
        backgroundSprite.draw(game.batch, 1);
        player.draw(game.batch, 1);


        game.batch.end();
        myStage.draw();
        //stage.draw();
    }

    public void handleMovements(){
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            System.out.println("UP");
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2){
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2){
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.b2body.getLinearVelocity().x >= -2){
            player.b2body.applyLinearImpulse(new Vector2(0, -4f), player.b2body.getWorldCenter(), true);
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
        //stage.dispose();
        myStage.dispose();
        atlas.dispose();
    }
}
