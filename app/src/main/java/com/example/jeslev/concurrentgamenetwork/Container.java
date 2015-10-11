package com.example.jeslev.concurrentgamenetwork;

import java.io.Serializable;

/**
 * Created by yarvis on 09/10/15.
 */
public class Container implements Serializable {

    public Game game;
    int id;

    //Wrapping class, for sending game and ID to client
    public Container(Game game,int id){
        this.game = game;
        this.id = id;
    }

    public Game getGame(){ return game;}

    public int getID() {return id;}

}
