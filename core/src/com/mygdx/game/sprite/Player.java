package com.mygdx.game.sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Soccer;
import com.mygdx.game.screens.PlayScreen;

public class Player extends Sprite {

    public enum State {RUNNING, JUMPING, STANDING}

    public State currentState;
    public State previousState;

    public Animation<TextureRegion> playerRun;
    private float stateTimer;
    private boolean runningRight;
    TextureRegion playerStand;

    public World world;
    public Body b2body;
    float x;
    float y;
    boolean right;

    public Player(TextureAtlas atlas, World world, PlayScreen screen, float x, float y, boolean right, String region) {

        super(atlas.findRegion(region));

        this.world = world;
        this.x = x;
        this.y = y;
        this.currentState = State.STANDING;
        this.previousState = State.STANDING;
        this.stateTimer = 0;
        this.runningRight = true;
        this.right = right;

        Array<TextureRegion> frames = new Array<>();

        for (int i = 0; i < 7; i++) {
            frames.add(new TextureRegion(getTexture(), i * 83, 0, 83, 170));
        }
        playerRun = new Animation<>(0.3f, frames);

        definePlayer();

        // Standing player
        playerStand = new TextureRegion(getTexture(), 0, 0, 83, 170);
        setBounds(0, 0, 83 * 1.7f / Soccer.PPM, 170 * 1.7f / Soccer.PPM);
        setRegion(playerStand);
    }

    public void update(float dt) {

        setRegion(getFrame(dt));
        setCenter(b2body.getPosition().x, b2body.getPosition().y + (20 / Soccer.PPM));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;

        switch (currentState) {
            case RUNNING:
                region = playerRun.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
            default:
                region = playerStand;
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

    public State getState() {

        if (b2body.getLinearVelocity().y > 0 || b2body.getLinearVelocity().y < 0) {
            return State.JUMPING;
        } else if (b2body.getLinearVelocity().x > 0 || b2body.getLinearVelocity().x < 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    public void jump() {
        if (currentState != State.JUMPING) {
            b2body.applyLinearImpulse(new Vector2(0, 4.3f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    /**
     * Physic creation of rectangular player
     */
    public void definePlayer() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bodyDef);
        b2body.setUserData(this);

        FixtureDef bodyFixture = new FixtureDef();
        bodyFixture.filter.categoryBits = Soccer.BIT_PLAYER;
        bodyFixture.filter.maskBits = Soccer.BIT_GROUND | Soccer.BIT_WALL | Soccer.BIT_PLAYER | Soccer.BIT_BALL;

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(20 / Soccer.PPM, 70 / Soccer.PPM);

        FixtureDef footFixture = new FixtureDef();
        footFixture.filter.categoryBits = Soccer.BIT_FOOT;
        footFixture.filter.maskBits = Soccer.BIT_BALL | Soccer.BIT_FOOT | Soccer.BIT_PLAYER;


        PolygonShape footShape = new PolygonShape();
        if (right) {
            footShape.setAsBox(10 / Soccer.PPM, 33 / Soccer.PPM, new Vector2(-(13 / Soccer.PPM), -(37 / Soccer.PPM)),
                    0);
        } else {
            footShape.setAsBox(10 / Soccer.PPM, 33 / Soccer.PPM, new Vector2(13 / Soccer.PPM, -(37 / Soccer.PPM)), 0);
        }


        FixtureDef headFixture = new FixtureDef();
        headFixture.filter.categoryBits = Soccer.BIT_HEAD;
        headFixture.filter.maskBits = Soccer.BIT_BALL | Soccer.BIT_PLAYER | Soccer.BIT_HEAD;

        CircleShape headShape = new CircleShape();
        headShape.setRadius(43 / Soccer.PPM);
        headShape.setPosition(new Vector2(0, 37 / Soccer.PPM));

        footFixture.shape = footShape;
        bodyFixture.shape = polygonShape;
        headFixture.shape = headShape;

        b2body.createFixture(bodyFixture).setUserData("body");
        b2body.createFixture(footFixture).setUserData("foot");
        b2body.createFixture(headFixture).setUserData("head");

        headShape.dispose();
        footShape.dispose();
        polygonShape.dispose();
    }
}
