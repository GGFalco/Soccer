package com.mygdx.game.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Soccer;

public class Ball extends Sprite {

    World world;
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
        setBounds(0, 0, 50 / Soccer.PPM, 50 / Soccer.PPM);
        setRegion(soccerBall);
    }

    public void defineBall(){

        BodyDef bdef = new BodyDef();
        bdef.position.set(x , y);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);
        b2body.setUserData(this);

        FixtureDef fixtureDef = new FixtureDef();

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(20 / Soccer.PPM);


        fixtureDef.shape = circleShape;
        b2body.createFixture(fixtureDef);

        circleShape.dispose();
    }
}
