package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.game.misc.WorldMisc;

public class MyStage extends Stage {

    private float accumulator, TIME_STEP;
    private Box2DDebugRenderer renderer;
    private WorldMisc wrl;
    private World world;
    private Body ground,leftWall,rightWall;

    public MyStage(){

        super(new ExtendViewport(16f, 9f, new OrthographicCamera(16f, 9f)));
        accumulator = 0.0F;
        TIME_STEP = 1/300F; // Recomended by libgdx

        setupWorld();
    }

    private void setupWorld() {

        wrl = new WorldMisc();
        world = wrl.createWorld();
        renderer = new Box2DDebugRenderer();
        setupGround();
        setupLeftWall();
        setupRightWall();

    }

    private void setupGround(){
        ground = wrl.createGround(world);
    }

    private void setupLeftWall(){
        leftWall = wrl.createLeftWall(world);
    }

    private void setupRightWall(){
        rightWall = wrl.createRightWall(world);
    }

    @Override
    public void act(float delta) {

        super.act(delta);
        accumulator += delta;

        while (accumulator >= delta) {
            world.step(TIME_STEP, 8, 4);
            accumulator -= TIME_STEP;
        }
    }

    @Override
    public void draw() {

        super.draw();
        renderer.render(world, getViewport().getCamera().combined);

    }
}
