package com.mygdx.game.sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Soccer;
import com.mygdx.game.screens.PlayScreen;
import com.sun.tools.javac.util.List;

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

    public Player(World world, PlayScreen screen, float x, float y) {

        super(screen.getAtlas().findRegion("char1-walk"));

        this.world = world;
        this.x = x;
        this.y = y;
        this.currentState = State.STANDING;
        this.previousState = State.STANDING;
        this.stateTimer = 0;
        this.runningRight = true;

        Array<TextureRegion> frames = new Array<>();

        for (int i = 0; i < 7; i++) {
            frames.add(new TextureRegion(getTexture(), i * 77, 0, 77, 107));
        }
        playerRun = new Animation<>(0.3f, frames);

        definePlayer();

        // Standing player
        playerStand = new TextureRegion(getTexture(), 0, 0, 77, 107);
        setBounds(0, 0, 77 * 1.7f / Soccer.PPM, 107 * 1.7f / Soccer.PPM);
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

    public void kick(){
        
    }

    public void jump() {
        if (currentState != State.JUMPING) {
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
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

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = Soccer.BIT_PLAYER;

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(20 / Soccer.PPM, 70 / Soccer.PPM);

        FixtureDef footFixture = new FixtureDef();
        footFixture.filter.categoryBits = Soccer.BIT_FOOT;
        footFixture.filter.maskBits = Soccer.BIT_BALL;

        PolygonShape footShape = new PolygonShape();
        footShape.setAsBox(10 / Soccer.PPM, 30 / Soccer.PPM, new Vector2(10 / Soccer.PPM, -(70 / Soccer.PPM)) , 0);

        footFixture.shape = footShape;
        fixtureDef.shape = polygonShape;

        b2body.createFixture(fixtureDef).setUserData("body");
        b2body.createFixture(footFixture).setUserData("foot");

        footShape.dispose();
        polygonShape.dispose();
    }
}
