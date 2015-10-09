package com.example.jeslev.concurrentgamenetwork;

/**
 * Created by yarvis on 09/10/15.
 */
public class Container {

    public Game game;
    int id;

    public Container(Game game,int id){
        this.game = game;
        this.id = id;
    }

    public Game getGame(){ return game;}

    public int getID() {return id;}

}
