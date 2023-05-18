package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Soccer;
import com.mygdx.game.sprite.Ball;
import com.mygdx.game.sprite.Player;
import com.mygdx.game.world.Ground;
import com.mygdx.game.world.Net;
import com.mygdx.game.world.Wall;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

public class PlayScreen extends Stage implements Screen {

    final Soccer game;

    public enum State {PAUSE, RUN}


    public static State state = State.RUN;

    Stage stage;
    OrthographicCamera camera;
    Skin skin;

    Texture backgroundScreen;
    Sprite backgroundSprite;

    TextureAtlas atlas;
    private World world;
    private Box2DDebugRenderer b2dr;

    static Player player;
    static Player rightPlayer;
    static Ball ball;
    Ground ground;
    Ground sky;
    Wall leftWall;
    Wall rightWall;
    Ground leftDoor;
    Ground rightDoor;
    Net leftSoccerGoal;
    Net rightSoccerGoal;

    Label.LabelStyle labelStyle;
    Label leftScoreLabel;
    Label rightScoreLabel;

    TypingLabel typingLabel;


    static int leftGoal = 0;
    static int rightGoal = 0;
    static boolean pause = false;
    static float deltaTime;
    static boolean goal = false;

    public PlayScreen(final Soccer game) {

        super(new ScreenViewport(new OrthographicCamera(1920, 1080)));
        String atl = "char1-walk.atlas";
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


        labelStyle = game.arcadeSkin.get("default", Label.LabelStyle.class);
        labelStyle.font = game.arcadeFont;

        leftScoreLabel = new Label("0", labelStyle);
        leftScoreLabel.setPosition((Soccer.SCREEN_WIDTH / 2f) - 150, Soccer.SCREEN_HEIGHT - 100);
        rightScoreLabel = new Label("0", labelStyle);
        rightScoreLabel.setPosition((Soccer.SCREEN_WIDTH / 2f) + 150, Soccer.SCREEN_HEIGHT - 100);

        typingLabel = new TypingLabel("{COLOR=RED} GOAL", labelStyle);
        typingLabel.setPosition(200, 300);
        typingLabel.debug();


        player = new Player(world, this, 200 / Soccer.PPM, (Gdx.graphics.getHeight() - 850) / Soccer.PPM, false);
        rightPlayer = new Player(world, this, (Soccer.SCREEN_WIDTH - 200) / Soccer.PPM, (Gdx.graphics.getHeight() - 850) / Soccer.PPM, true);
        ball = new Ball(world, Soccer.V_WIDTH / 2, Soccer.V_HEIGHT / 2);

        ground = new Ground(world, 0, (Soccer.V_HEIGHT / 8) + (20 / Soccer.PPM), 1920, 10);
        sky = new Ground(world, 0, Soccer.V_HEIGHT, 1920, 10);

        leftDoor = new Ground(world, 0, (Soccer.SCREEN_HEIGHT - 625) / Soccer.PPM, 102, 18);
        rightDoor = new Ground(world, Soccer.SCREEN_WIDTH / Soccer.PPM, (Soccer.SCREEN_HEIGHT - 625) / Soccer.PPM, 102, 18);

        leftSoccerGoal = new Net(world, 20 / Soccer.PPM, (Soccer.SCREEN_HEIGHT - 790) / Soccer.PPM, 20, 170, "leftNet");
        rightSoccerGoal = new Net(world, (Soccer.SCREEN_WIDTH - 20) / Soccer.PPM, (Soccer.SCREEN_HEIGHT - 790) / Soccer.PPM, 20, 170, "rightNet");

        leftWall = new Wall(world, 0, 0);
        rightWall = new Wall(world, Soccer.V_WIDTH, 0);

        stage.addActor(leftScoreLabel);
        stage.addActor(rightScoreLabel);
        stage.addActor(typingLabel);
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
        rightPlayer.update(dt);

        ball.setCenter(ball.b2body.getPosition().x, ball.b2body.getPosition().y);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        deltaTime = delta;

        switch (state) {
            case RUN:
                update(delta);

                handleCollision();
                handleLeftPlayerMovements();
                handleRightPlayerMovements();

                game.batch.setProjectionMatrix(camera.combined);
                game.batch.begin();

                leftScoreLabel.draw(game.batch, 1);
                rightScoreLabel.draw(game.batch, 1);
                backgroundSprite.draw(game.batch, 1f);
                player.draw(game.batch, 1);
                rightPlayer.draw(game.batch, 1);
                ball.draw(game.batch, 1);

                game.batch.end();

                stage.draw();
                break;
            case PAUSE:

                game.batch.setProjectionMatrix(camera.combined);
                game.batch.begin();

                leftScoreLabel.draw(game.batch, 1);
                rightScoreLabel.draw(game.batch, 1);
                backgroundSprite.draw(game.batch, 1f);
                player.draw(game.batch, 1);
                rightPlayer.draw(game.batch, 1);
                ball.draw(game.batch, 1);



                game.batch.end();

                stage.draw();
                break;
        }
        handleClickEvents();
    }




    public void handleClickEvents() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {

            if (!pause) {
                state = State.PAUSE;
                pause = true;

            } else {
                state = State.RUN;
                pause = false;

            }
        }
    }

    public void handleCollision() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fA = contact.getFixtureA();
                Fixture fB = contact.getFixtureB();

                /*
                Right player goal
                 */
                if (fA.getUserData() == "leftNet" && fB.getUserData() == "ball") {

                    final Body toRemove = fA.getBody().getType() == BodyDef.BodyType.DynamicBody ? fA.getBody() : fB.getBody();
                    final Body toRemoveLeft = player.b2body.getFixtureList().first().getBody();
                    final Body toRemoveRight = rightPlayer.b2body.getFixtureList().first().getBody();

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            world.destroyBody(toRemove);
                            world.destroyBody(toRemoveLeft);
                            world.destroyBody(toRemoveRight);

                            ball = new Ball(world, Soccer.V_WIDTH / 2, Soccer.V_HEIGHT / 2);
                            player = new Player(world, PlayScreen.this, 200 / Soccer.PPM, (Gdx.graphics.getHeight() - 850) / Soccer.PPM, false);
                            rightPlayer = new Player(world, PlayScreen.this, (Soccer.SCREEN_WIDTH - 200) / Soccer.PPM, (Gdx.graphics.getHeight() - 850) / Soccer.PPM, true);
                        }
                    });

                    rightGoal++;
                    rightScoreLabel.setText(rightGoal);
                    System.out.println("goal");
                    goal = true;
                }
                /*
                Left player goal
                 */

                if (fA.getUserData() == "rightNet" && fB.getUserData() == "ball") {

                    final Body toRemove = fA.getBody().getType() == BodyDef.BodyType.DynamicBody ? fA.getBody() : fB.getBody();
                    final Body toRemoveLeft = player.b2body.getFixtureList().first().getBody();
                    final Body toRemoveRight = rightPlayer.b2body.getFixtureList().first().getBody();

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            world.destroyBody(toRemove);
                            world.destroyBody(toRemoveLeft);
                            world.destroyBody(toRemoveRight);

                            ball = new Ball(world, Soccer.V_WIDTH / 2, Soccer.V_HEIGHT / 2);
                            player = new Player(world, PlayScreen.this, 200 / Soccer.PPM, (Gdx.graphics.getHeight() - 850) / Soccer.PPM, false);
                            rightPlayer = new Player(world, PlayScreen.this, (Soccer.SCREEN_WIDTH - 200) / Soccer.PPM, (Gdx.graphics.getHeight() - 850) / Soccer.PPM, true);
                        }
                    });

                    leftGoal++;
                    leftScoreLabel.setText(leftGoal);
                    goal = true;

                }
                /*
                Kick shot
                 */

                if (fA.getUserData() == "foot" && fB.getUserData() == "ball") {

                    if (fA.getBody().getLinearVelocity().x >= 0) {

                        if (fA.getBody().getLinearVelocity().x >= 3.4f) {
                            fB.getBody().applyLinearImpulse(new Vector2(10f, 4f), fB.getBody().getWorldCenter(), true);
                        } else {
                            fB.getBody().applyLinearImpulse(new Vector2(3f, 12f), fB.getBody().getWorldCenter(), true);
                        }
                    } else {

                        if (fA.getBody().getLinearVelocity().x <= -3.4f) {
                            fB.getBody().applyLinearImpulse(new Vector2(-10f, 4f), fB.getBody().getWorldCenter(), true);
                        } else {
                            fB.getBody().applyLinearImpulse(new Vector2(-3f, 12f), fB.getBody().getWorldCenter(), true);
                        }
                    }

                    ball.rotate(360f);
                }
                /*
                Headshot
                 */
                if (fA.getUserData() == "head" && fB.getUserData() == "ball") {

                    if (fA.getBody().getLinearVelocity().x > 0) {

                        if (fA.getBody().getLinearVelocity().y > 0) {
                            System.out.println("colpo potente");
                            fB.getBody().applyLinearImpulse(new Vector2(12f, 1f), fB.getBody().getWorldCenter(), true);
                        } else {
                            fB.getBody().applyLinearImpulse(new Vector2(6f, 2f), fB.getBody().getWorldCenter(), true);
                        }
                    } else {

                        if (fA.getBody().getLinearVelocity().y > 0) {
                            System.out.println("in volo");
                            fB.getBody().applyLinearImpulse(new Vector2(-12f, 1f), fB.getBody().getWorldCenter(), true);
                        } else {
                            fB.getBody().applyLinearImpulse(new Vector2(-6f, 2f), fB.getBody().getWorldCenter(), true);
                        }
                    }

                    ball.rotate(360f);
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

    public void handleRightPlayerMovements() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && rightPlayer.b2body.getLinearVelocity().y <= 4f && rightPlayer.b2body.getPosition().y <= 2.37f) {
            rightPlayer.jump();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && rightPlayer.b2body.getLinearVelocity().x <= 3f) {
            rightPlayer.b2body.applyLinearImpulse(new Vector2(.8f, 0), rightPlayer.b2body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && rightPlayer.b2body.getLinearVelocity().x >= -3f) {
            rightPlayer.b2body.applyLinearImpulse(new Vector2(-.8f, 0), rightPlayer.b2body.getWorldCenter(), true);
        }
    }

    /**
     * Handle the sprite movement
     */
    public void handleLeftPlayerMovements() {

        if (Gdx.input.isKeyPressed(Input.Keys.W) && player.b2body.getLinearVelocity().y <= 4f && player.b2body.getPosition().y <= 2.37f) {

            player.jump();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x <= 3f) {

            player.b2body.applyLinearImpulse(new Vector2(.8f, 0), player.b2body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x >= -3f) {

            player.b2body.applyLinearImpulse(new Vector2(-.8f, 0), player.b2body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            Fixture footFixture = player.b2body.getFixtureList().get(1);
            System.out.println(footFixture.getUserData());
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
