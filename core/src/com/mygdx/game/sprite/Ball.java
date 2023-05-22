package com.mygdx.game.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Soccer;

public class Ball extends Sprite {

    public World world;
    public Body b2body;
    float x;
    float y;

    public Ball(World world, float x, float y){

        this.world = world;
        this.x = x;
        this.y = y;

        defineBall();

        Texture soccerTexture = new Texture(Gdx.files.internal("sprites/ball.png"));
        TextureRegion soccerBall = new TextureRegion(soccerTexture);
        setBounds(0, 0, 55 / Soccer.PPM, 55 / Soccer.PPM);
        setRegion(soccerBall);

    }

    /**
     * Ball definition
     */
    public void defineBall(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x , y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bodyDef);
        b2body.setUserData("ball");

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = .7f;
        fixtureDef.filter.categoryBits = Soccer.BIT_BALL;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(28 / Soccer.PPM);


        fixtureDef.shape = circleShape;
        b2body.createFixture(fixtureDef).setUserData("ball");

        circleShape.dispose();
    }
}
