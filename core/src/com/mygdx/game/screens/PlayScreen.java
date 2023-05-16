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
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Soccer;
import com.mygdx.game.sprite.Ball;
import com.mygdx.game.sprite.Player;
import com.mygdx.game.world.Ground;
import com.mygdx.game.world.Net;
import com.mygdx.game.world.Wall;

public class PlayScreen extends Stage implements Screen {

    final Soccer game;

    Stage stage;
    OrthographicCamera camera;
    Skin skin;

    Texture backgroundScreen;
    Sprite backgroundSprite;

    TextureAtlas atlas;
    private World world;
    private Box2DDebugRenderer b2dr;

    static Player player;
    static Ball ball;
    Ground ground;
    Ground sky;
    Wall leftWall;
    Wall rightWall;
    Ground leftDoor;
    Ground rightDoor;
    Net leftSoccerGoal;
    Net rightSoccerGoal;

    public PlayScreen(final Soccer game) {

        super(new ScreenViewport(new OrthographicCamera(1920, 1080)));
        String atl = "char1.atlas";
        atlas = new TextureAtlas(atl);

        this.game = game;
        this.skin = game.skin;
        this.stage = new Stage();
        this.camera = new OrthographicCamera(Soccer.V_WIDTH, Soccer.V_HEIGHT);
        camera.setToOrtho(false, Soccer.V_WIDTH, Soccer.V_HEIGHT);
        Gdx.input.setInputProcessor(this);

        environmentConfiguration();

        backgroundScreen = new Texture(Gdx.files.internal("backgrounds/sfondo_partita.png"));
        backgroundSprite = new Sprite(backgroundScreen);
        backgroundSprite.setSize(Gdx.graphics.getWidth() / Soccer.PPM, Gdx.graphics.getHeight() / Soccer.PPM);
    }

    /**
     * Environment creation in terms of assets and sprites
     */
    public void environmentConfiguration() {

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        player = new Player(world, this, 200 / Soccer.PPM, (Gdx.graphics.getHeight() - 850) / Soccer.PPM);
        ball = new Ball(world, Soccer.V_WIDTH / 2, Soccer.V_HEIGHT / 2);

        ground = new Ground(world, 0, (Soccer.V_HEIGHT / 8) + (20 / Soccer.PPM), 1920, 10);
        sky = new Ground(world, 0, Soccer.V_HEIGHT, 1920, 10);

        leftDoor = new Ground(world, 0, (Soccer.SCREEN_HEIGHT - 625) / Soccer.PPM, 120, 18);
        rightDoor = new Ground(world, Soccer.SCREEN_WIDTH / Soccer.PPM, (Soccer.SCREEN_HEIGHT - 625) / Soccer.PPM, 120, 18);

        leftSoccerGoal = new Net(world, 20 / Soccer.PPM, (Soccer.SCREEN_HEIGHT - 790) / Soccer.PPM, 20, 170, "leftNet");
        rightSoccerGoal = new Net(world, (Soccer.SCREEN_WIDTH - 20) / Soccer.PPM, (Soccer.SCREEN_HEIGHT - 790) / Soccer.PPM, 20, 170, "rightNet");

        leftWall = new Wall(world, 0, 0);
        rightWall = new Wall(world, Soccer.V_WIDTH, 0);
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public void update(float dt) {

        world.step(1 / 60f, 6, 2);
        b2dr.render(world, camera.combined);

        camera.viewportWidth = Soccer.V_WIDTH;
        camera.viewportHeight = Soccer.V_HEIGHT;

        camera.update();
        player.update(dt);

        ball.setCenter(ball.b2body.getPosition().x, ball.b2body.getPosition().y);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        handleCollision();
        handleMovements();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        backgroundSprite.draw(game.batch, .1f);
        player.draw(game.batch, 1);
        ball.draw(game.batch, 1);

        game.batch.end();

        stage.draw();
    }

    public void handleCollision() {

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fA = contact.getFixtureA();
                Fixture fB = contact.getFixtureB();

                if (fA.getUserData() == "leftNet" && fB.getUserData() == "ball") {
                    System.out.println("GOAL LEFT");

                    final Body toRemove = fA.getBody().getType() == BodyDef.BodyType.DynamicBody ? fA.getBody() : fB.getBody();

                    final Body toRemoveLeft = player.b2body.getFixtureList().first().getBody();

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            world.destroyBody(toRemove);
                            world.destroyBody(toRemoveLeft);

                            ball = new Ball(world, Soccer.V_WIDTH / 2, Soccer.V_HEIGHT / 2);
                            player = new Player(world, PlayScreen.this, 200 / Soccer.PPM, (Gdx.graphics.getHeight() - 850) / Soccer.PPM);
                        }
                    });
                }

                if (fA.getUserData() == "rightNet" && fB.getUserData() == "ball") {

                    System.out.println("GOAL right");

                    final Body toRemove = fA.getBody().getType() == BodyDef.BodyType.DynamicBody ? fA.getBody() : fB.getBody();
                    final Body toRemoveLeft = player.b2body.getFixtureList().first().getBody();

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            world.destroyBody(toRemove);
                            world.destroyBody(toRemoveLeft);

                            ball = new Ball(world, Soccer.V_WIDTH / 2, Soccer.V_HEIGHT / 2);
                            player = new Player(world, PlayScreen.this, 200 / Soccer.PPM, (Gdx.graphics.getHeight() - 850) / Soccer.PPM);
                        }
                    });
                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }

    /**
     * Handle the sprite movement
     */
    public void handleMovements() {

        //System.out.println(player.b2body.getPosition());
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().y <= 4f && player.b2body.getPosition().y <= 2.365f) {

            //player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            player.jump();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 3f) {

            player.b2body.applyLinearImpulse(new Vector2(.8f, 0), player.b2body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -3f) {

            player.b2body.applyLinearImpulse(new Vector2(-.8f, 0), player.b2body.getWorldCenter(), true);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            System.out.println("Kick");
            player.kick();
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
        atlas.dispose();
        b2dr.dispose();
        world.dispose();
    }
}
