package com.mygdx.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Soccer;

public class Wall extends Sprite {

    public World world;
    public Body b2body;
    float x;
    float y;

    public Wall(World world, float x, float y){

        this.world = world;
        this.x = x;
        this.y = y;

        defineWall();
    }

    /**
     * Wall definition
     */
    public void defineWall(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        b2body = world.createBody(bodyDef);
        b2body.setUserData(this);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = Soccer.BIT_WALL;
        fixtureDef.filter.maskBits = Soccer.BIT_BALL |  Soccer.BIT_PLAYER;

        PolygonShape ground = new PolygonShape();
        ground.setAsBox(28 / Soccer.PPM, Gdx.graphics.getHeight() / Soccer.PPM);

        fixtureDef.shape = ground;

        b2body.createFixture(fixtureDef);
    }
}
