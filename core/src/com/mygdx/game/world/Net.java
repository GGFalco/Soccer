package com.mygdx.game.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Soccer;

public class Net extends Sprite {

    public World world;
    public Body b2body;
    float x;
    float y;
    public float hX;
    public float hY;
    public String userData;


    public Net(World world, float x, float y, float hX, float hY, String userData){

        this.world = world;
        this.x = x;
        this.y = y;
        this.hX = hX;
        this.hY = hY;
        this.userData = userData;

        defineNet();
    }

    public void defineNet(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        b2body = world.createBody(bodyDef);
        b2body.setUserData(userData);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = Soccer.BIT_NET;

        PolygonShape net = new PolygonShape();
        net.setAsBox(hX / Soccer.PPM, hY / Soccer.PPM);

        fixtureDef.shape = net;

        b2body.createFixture(fixtureDef).setUserData(userData);

        net.dispose();
    }
}
