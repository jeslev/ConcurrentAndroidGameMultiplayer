package com.example.jeslev.concurrentgamenetwork;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jeslev on 06/10/15.
 */
public class Game implements Serializable{

    Spaceship ship;
    ArrayList<Missil> missils;
    int wx, wy;

    public Game(){
        ship = new Spaceship();
        missils = new ArrayList<Missil>();
    }

    public void update(int wx,int wy) {
        this.wx = wx;
        this.wy = wy;

        ship.update(wx, wy);
        for(int i=0;i<missils.size();i++) missils.get(i).update(wx,wy);
    }

    public void turbo(int id){
        ship.turbo();
    }


    public void rotateLeft(int id){
        ship.rotateLeft();
    }

    public void rotateRight(int id){
        ship.rotateRight();
    }

    public void noRotate(int id){
        ship.noRotate();
    }

    public boolean getTurbo() {return ship.getTurbo();}

    public void shot(int id) {
        missils.add(new Missil(ship, 0));
    }

    public Spaceship getShip() { return ship;}

    public ArrayList<Missil> getMissil() { return missils;}
}
