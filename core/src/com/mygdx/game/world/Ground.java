package com.mygdx.game.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Ground extends Sprite {

    public World world;
    public Body b2body;
    float x;
    float y;

    public Ground(World world, float x, float y){

        this.world = world;
        this.x = x;
        this.y = y;

        defineGround();
    }

    public void defineGround(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        b2body = world.createBody(bodyDef);
        b2body.setUserData(this);

        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape ground = new PolygonShape();
        ground.setAsBox(1920, 10);

        fixtureDef.shape = ground;

        b2body.createFixture(fixtureDef);
    }

}
