package com.mygdx.game.sprite;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.screens.PlayScreen;

public class Player extends Sprite {

    public World world;
    public Body b2body;
    float x;
    float y;

    public Player (World world, PlayScreen screen, float x, float y){

        super(screen.getAtlas().findRegion("c1sprite"));

        this.world = world;
        this.x = x;
        this.y = y;

        definePlayer();

        TextureRegion playerStand = new TextureRegion(getTexture(), 0, 0, 97, 97);
        setBounds(0, 0, 97 * 1.2f, 119*1.2f );
        setRegion(playerStand);
    }

    /**
     * Physic creation of rectangular player
     */
    public void definePlayer(){

        BodyDef bdef = new BodyDef();
        bdef.position.set(x , y);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);
        b2body.setUserData(this);

        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(30,70);

        fixtureDef.shape = polygonShape;
        b2body.createFixture(fixtureDef);


    }
}
