package com.mygdx.game.misc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class WorldMisc {

    public World createWorld() {

        return new World(new Vector2(0, -10), true);

    }

    public Body createGround(World world) {

        BodyDef ground = new BodyDef();
        ground.type = BodyDef.BodyType.StaticBody;
        ground.position.set(new Vector2(0.0F, 0.0F));
        Body body = world.createBody(ground);
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(16.01F, 2.0F);
        body.createFixture(groundShape, 0.5F);
        body.setUserData(new GroundUserData(2*(16.01F), 4.0F));
        groundShape.dispose();
        return body;

    }

    public Body createLeftWall(World world) {

        BodyDef leftWall = new BodyDef();
        leftWall.type = BodyDef.BodyType.StaticBody;
        leftWall.position.set(new Vector2(-1.0F, 12.0F));
        Body body = world.createBody(leftWall);
        PolygonShape leftWallShape = new PolygonShape();
        leftWallShape.setAsBox(1.0F, 11.0F);
        body.createFixture(leftWallShape, 0.5F);
        body.setUserData(new LeftWallUserData(2.0F, 22.0F));
        leftWallShape.dispose();
        return body;

    }

    public Body createRightWall(World world) {

        BodyDef rightWall = new BodyDef();
        rightWall.type = BodyDef.BodyType.StaticBody;
        rightWall.position.set(new Vector2(17F, 12.0F));
        Body body = world.createBody(rightWall);
        PolygonShape rightWallShape = new PolygonShape();
        rightWallShape.setAsBox(0.999F, 11.0F);
        body.createFixture(rightWallShape, 0.5F);
        body.setUserData(new RightWallUserData(1.998F, 22.0F));
        rightWallShape.dispose();
        return body;

    }
}
