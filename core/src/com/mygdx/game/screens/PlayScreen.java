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
import com.badlogic.gdx.utils.Array;
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

    boolean timeExpired = false;
    private float timeSeconds = 0f;
    float period = 1f;
    private int minutes = 1;
    private int seconds = 0;

    private float goalTimeSeconds = 0f;
    private int goalTimer = 4;

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
    Label minuteLabel;
    Label secondsLabel;

    TypingLabel typingLabel;
    String atlasLeftPlayer;
    String atlasRightPlayer;

    static int leftGoal = 0;
    static int rightGoal = 0;
    static boolean pause;
    static boolean goal;

    public PlayScreen(final Soccer game, String atlasLeftPlayer, String atlasRightPlayer) {

        super(new ScreenViewport(new OrthographicCamera(1920, 1080)));

        this.game = game;
        this.skin = game.skin;
        this.stage = new Stage();
        this.camera = new OrthographicCamera(Soccer.V_WIDTH, Soccer.V_HEIGHT);
        this.atlasLeftPlayer = atlasLeftPlayer;
        this.atlasRightPlayer = atlasRightPlayer;
        pause = false;
        goal = false;
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
        leftScoreLabel.setPosition((Soccer.SCREEN_WIDTH / 2f) - 150, Soccer.SCREEN_HEIGHT - 150);
        rightScoreLabel = new Label("0", labelStyle);
        rightScoreLabel.setPosition((Soccer.SCREEN_WIDTH / 2f) + 150, Soccer.SCREEN_HEIGHT - 150);

        labelStyle.font = game.littleArcadeFont;
        minuteLabel = new Label(String.format("%02d:", minutes), labelStyle);
        minuteLabel.setPosition((Soccer.SCREEN_WIDTH / 2f) - 40, Soccer.SCREEN_HEIGHT - 150);
        minuteLabel.setColor(Color.SKY);
        secondsLabel = new Label(String.format("%02d", seconds), labelStyle);
        secondsLabel.setPosition((Soccer.SCREEN_WIDTH / 2f) + 40, Soccer.SCREEN_HEIGHT - 150);
        secondsLabel.setColor(Color.SKY);

        labelStyle.font = game.arcadeFont;
        typingLabel = new TypingLabel("", labelStyle);
        typingLabel.setFontScale(2f, 2f);
        typingLabel.setPosition((Soccer.SCREEN_WIDTH / 2f) - 175, (Soccer.SCREEN_HEIGHT / 2f) + 175);

        atlas = new TextureAtlas(atlasLeftPlayer + ".atlas");
        player = new Player(atlas, world, this, 200 / Soccer.PPM, (Gdx.graphics.getHeight() - 850) / Soccer.PPM, false, atlasLeftPlayer);
        atlas = new TextureAtlas(atlasRightPlayer + ".atlas");
        rightPlayer = new Player(atlas, world, this, (Soccer.SCREEN_WIDTH - 200) / Soccer.PPM, (Gdx.graphics.getHeight() - 850) / Soccer.PPM, true, atlasRightPlayer);
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
        stage.addActor(minuteLabel);
        stage.addActor(secondsLabel);
        stage.addActor(typingLabel);
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

        switch (state) {
            case RUN:

                update(delta);

                handleCollision();
                handleLeftPlayerMovements();
                handleRightPlayerMovements();
                handleTimer();

                game.batch.setProjectionMatrix(camera.combined);
                game.batch.begin();

                leftScoreLabel.draw(game.batch, 1);
                rightScoreLabel.draw(game.batch, 1);
                backgroundSprite.draw(game.batch, 1f);
                player.draw(game.batch, 1);
                rightPlayer.draw(game.batch, 1);
                ball.draw(game.batch, 1);
                typingLabel.act(delta);
                handleGoalEvent();

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
                System.out.println("in pausa");

                game.batch.end();

                stage.draw();
                break;
        }

        handleClickEvents();
    }

    public void handleGoalEvent() {

        if (goal) {

            goalTimeSeconds += Gdx.graphics.getDeltaTime();

            if (goalTimeSeconds > period) {

                goalTimeSeconds -= period;
                goalTimer--;
                System.out.println(goalTimer);

                if (goalTimer == 0) {

                    typingLabel.setText("");
                    goalTimer = 4;
                    goal = false;
                }
            }
        }
    }

    public void handleTimer() {

        if (!timeExpired && !pause) {
            timeSeconds += Gdx.graphics.getDeltaTime();
            if (timeSeconds > period) {
                timeSeconds -= period;
                seconds--;

                if (seconds <= 0) {
                    if (minutes > 0) {
                        seconds = 59;
                        minutes--;
                    }
                }
                secondsLabel.setText(String.format("%02d", seconds));
                minuteLabel.setText(String.format("%02d: ", minutes));

                if (minutes == 0 && seconds <= 0) {
                    timeExpired = true;
                    //pause = true;
                    endSession();
                }
            }
        }
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
                            atlas = new TextureAtlas(atlasLeftPlayer + ".atlas");
                            player = new Player(atlas, world, PlayScreen.this, 200 / Soccer.PPM, (Gdx.graphics.getHeight() - 850) / Soccer.PPM, false, atlasLeftPlayer);
                            atlas = new TextureAtlas(atlasRightPlayer + ".atlas");
                            rightPlayer = new Player(atlas, world, PlayScreen.this, (Soccer.SCREEN_WIDTH - 200) / Soccer.PPM, (Gdx.graphics.getHeight() - 850) / Soccer.PPM, true, atlasRightPlayer);
                        }
                    });

                    rightGoal++;
                    rightScoreLabel.setText(rightGoal);
                    typingLabel.setText("{SICK}G   O   A   L");
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
                            atlas = new TextureAtlas(atlasLeftPlayer + ".atlas");
                            player = new Player(atlas, world, PlayScreen.this, 200 / Soccer.PPM, (Gdx.graphics.getHeight() - 850) / Soccer.PPM, false, atlasLeftPlayer);
                            atlas = new TextureAtlas(atlasRightPlayer + ".atlas");
                            rightPlayer = new Player(atlas, world, PlayScreen.this, (Soccer.SCREEN_WIDTH - 200) / Soccer.PPM, (Gdx.graphics.getHeight() - 850) / Soccer.PPM, true, atlasRightPlayer);
                        }
                    });

                    leftGoal++;
                    leftScoreLabel.setText(leftGoal);
                    typingLabel.setText("{SICK}G   O   A   L");
                    goal = true;
                }

                if (fA.getUserData() == "body" && fB.getUserData() == "ball") {
                    //stopPlayer(fA);
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

                    //stopPlayer(fA);
                }
                /*
                Headshot
                 */
                if (fA.getUserData() == "head" && fB.getUserData() == "ball") {

                    if (fA.getBody().getLinearVelocity().x >= 0) {

                        if (fA.getBody().getLinearVelocity().y >= 0) {
                            fB.getBody().applyLinearImpulse(new Vector2(12f, 8f), fB.getBody().getWorldCenter(), true);
                        } else {
                            fB.getBody().applyLinearImpulse(new Vector2(6f, 8f), fB.getBody().getWorldCenter(), true);
                        }
                    } else {

                        if (fA.getBody().getLinearVelocity().y >= 0) {
                            fB.getBody().applyLinearImpulse(new Vector2(-12f, 8f), fB.getBody().getWorldCenter(), true);
                        } else {
                            fB.getBody().applyLinearImpulse(new Vector2(-6f, 8f), fB.getBody().getWorldCenter(), true);
                        }
                    }

                    //stopPlayer(fA);
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fA = contact.getFixtureA();
                Fixture fB = contact.getFixtureB();

                if(fA.getUserData() == "body" && fB.getUserData() == "ball"){
                    fA.getBody().setLinearVelocity(new Vector2(0,0));
                }

                if(fA.getUserData() == "foot" && fB.getUserData() == "ball"){
                    fA.getBody().setLinearVelocity(new Vector2(0,0));
                }

                if(fA.getUserData() == "head" && fB.getUserData() == "ball"){
                    fA.getBody().setLinearVelocity(new Vector2(0,0));
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }

    private void stopPlayer(Fixture fixture) {
        Array<Fixture> fixtureList = fixture.getBody().getFixtureList();
        for (Fixture fixtures : fixtureList) {
            fixtures.getBody().setLinearVelocity(new Vector2(0, 0));
        }
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

    public void endSession() {
        game.setScreen(new WelcomeScreen(game));
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
