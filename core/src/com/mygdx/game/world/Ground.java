package com.mygdx.game.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Soccer;

public class Ground extends Sprite {

    public World world;
    public Body b2body;
    float x;
    float y;
    public float hX;
    public float hY;

    public Ground(World world, float x, float y, float hX, float hY){

        this.world = world;
        this.x = x;
        this.y = y;
        this.hX = hX;
        this.hY = hY;

        defineGround();
    }

    public void defineGround(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        b2body = world.createBody(bodyDef);
        b2body.setUserData(this);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = Soccer.BIT_GROUND;
        fixtureDef.filter.maskBits = Soccer.BIT_BALL | Soccer.BIT_PLAYER;


        PolygonShape ground = new PolygonShape();
        ground.setAsBox(hX / Soccer.PPM, hY / Soccer.PPM);

        fixtureDef.shape = ground;

        b2body.createFixture(fixtureDef);
    }

}
