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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Soccer;
import com.mygdx.game.sprite.Ball;
import com.mygdx.game.sprite.Player;
import com.mygdx.game.world.Ground;
import com.mygdx.game.world.Wall;

public class PlayScreen extends Stage implements Screen {

    final Soccer game;


    Stage stage;
    OrthographicCamera camera;
    Skin skin;

    Texture backgroundScreen;
    Sprite backgroundSprite;

    TextureAtlas atlas;
    private  World world;
    private  Box2DDebugRenderer b2dr;


    Player player;
    Ball ball;
    Ground ground;
    Ground sky;
    Wall leftWall;
    Wall rightWall;

    public PlayScreen(final Soccer game) {

        super(new FitViewport(48f,28f, new OrthographicCamera(48f,28f)));

        atlas = new TextureAtlas("sprite_head.atlas");

        environmentConfiguration();

        this.game = game;
        this.skin = game.skin;
        this.stage = new Stage();
        this.camera = new OrthographicCamera(1920f, 1080f);
        camera.setToOrtho(false, 1920f, 1080f);
        Gdx.input.setInputProcessor(this);


        backgroundScreen = new Texture(Gdx.files.internal("backgrounds/sfondo_partita.png"));
        backgroundSprite = new Sprite(backgroundScreen);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    /**
     * Environment creation in terms of assets and sprites
     */
    public void environmentConfiguration(){

        world = new World(new Vector2(0, -1000), true);
        b2dr = new Box2DDebugRenderer();

        player = new Player(world, this, 200, Gdx.graphics.getHeight() - 850);
        ball = new Ball(world, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);


        ground = new Ground(world, 0, Gdx.graphics.getHeight() /8);
        sky = new Ground(world, 0, Gdx.graphics.getHeight());


        leftWall = new Wall(world, 0, 0);
        rightWall = new Wall(world, Gdx.graphics.getWidth(), 0);

    }



    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public void update(){

        world.step(1/60f, 6, 2);
        b2dr.render(world, camera.combined);

        camera.viewportWidth = Gdx.graphics.getWidth();
        camera.viewportHeight = Gdx.graphics.getHeight();

        camera.update();

        player.setCenter(player.b2body.getPosition().x, player.b2body.getPosition().y + 30);
        ball.setCenter(ball.b2body.getPosition().x, ball.b2body.getPosition().y);
    }

    @Override
    public void render(float delta) {

        //ScreenUtils.clear(0,0,0,1);
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update();
        handleMovements();



        game.batch.begin();

        //backgroundSprite.draw(game.batch, 1);
        player.draw(game.batch, 1);
        ball.draw(game.batch, 1);


        game.batch.end();


        //myStage.draw();
        stage.draw();
    }

    public void handleMovements(){
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            System.out.println("UP");
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) ){
            player.b2body.applyLinearImpulse(new Vector2(4f, 0), player.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            player.b2body.applyLinearImpulse(new Vector2(-4f, 0), player.b2body.getWorldCenter(), true);
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
        stage.dispose();
        //myStage.dispose();
        atlas.dispose();
    }
}
