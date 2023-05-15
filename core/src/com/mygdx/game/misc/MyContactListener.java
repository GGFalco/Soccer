package com.mygdx.game.misc;

import com.badlogic.gdx.physics.box2d.*;


public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {


        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        if(fA.getUserData() == "leftNet" && fB.getUserData() == "ball"){
            System.out.println("GOAL LEFT");
        }
        if(fA.getUserData() == "rightNet" && fB.getUserData() == "ball"){
            System.out.println("GOAL right");
        }


    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
