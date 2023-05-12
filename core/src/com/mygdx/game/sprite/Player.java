package com.mygdx.game.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.screens.PlayScreen;

public class Player extends Sprite {

    public World world;
    public Body b2body;
    private TextureRegion playerStand;

    public Player (World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("c1sprite"));
        this.world = world;
        definePlayer();

        playerStand = new TextureRegion(getTexture(),0, 0, 97, 97 );
        setBounds(0, 0, 96 , 119);
        setRegion(playerStand);
    }

    public void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(100, Gdx.graphics.getHeight() - 875);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        b2body.setUserData(this);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(50);
        fdef.shape = shape;
        b2body.createFixture(fdef);

    }
}
