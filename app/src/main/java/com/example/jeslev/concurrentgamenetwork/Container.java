package com.example.jeslev.concurrentgamenetwork;

import java.io.Serializable;

/**
 * Created by jeslev on 10/10/15.
 */
public class Container implements Serializable {

    public Game game;
    int id;

    public Container(Game game,int id){
        this.game = game;
        this.id = id;
    }

    public Game getGame(){ return game;}

    public int getID() {return id;}

}
