package com.example.jeslev.concurrentgamenetwork;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jeslev on 06/10/15.
 */
public class Game implements Serializable {

    ArrayList<Spaceship> ships;  /*noamlr*/
    ArrayList<Missil> missils;
    ArrayList<Asteroid> asteroids;
    int wx, wy;
    int idSpaceship;

    boolean players[];

    public Game(){
        ships = new ArrayList<Spaceship>();
        missils = new ArrayList<Missil>();
        asteroids = new ArrayList<Asteroid>();
        players = new boolean[3];
        for(int i=0;i<3;i++) players[i]=false;
    }

    public void updateIdSpaceship(int id){
        this.idSpaceship = id;
    }

    public void update(int wx,int wy,float dpx,float mdpx) {
        this.wx= wx;
        this.wy= wy;

        //Log.e("Spaceship:", "");

        for(Spaceship tmpship : ships) tmpship.update(wx, wy);

        for(Missil tmpMissil : missils) tmpMissil.update(wx,wy);

        for(Asteroid tmpAsteroid : asteroids) tmpAsteroid.update(wx,wy);

        //check colissions
        for(Spaceship tmpShip : ships){
            if(!tmpShip.getActive()) continue;

            float sx = tmpShip.getPosX();
            float sy = tmpShip.getPosY();

            /*Ship vs missil*/
            for(Missil tmpMissil : missils){
                float mx = tmpMissil.getPosX();
                float my = tmpMissil.getPosY();
                if(tmpShip.getLive() && tmpMissil.getActive() && calculateDistance(sx, sy, mx, my)<mdpx*2){
                    tmpShip.setLive(false);
                    tmpShip.setLiveVisible(false);
                    tmpMissil.setActive(false);
                    //Log.e("Colision TM: ", ""+sx+" "+sy+" "+mx+" "+my);
                }
            }

            /*Ship vs asteroid*/
            for(Asteroid tmpAsteroid : asteroids){
                float ax = tmpAsteroid.getPosX();
                float ay = tmpAsteroid.getPosY();
                if(tmpShip.getLive() && tmpAsteroid.getActive() && calculateDistance(sx, sy, ax, ay)<dpx){
                    tmpShip.setLive(false);
                    tmpShip.setLiveVisible(false);
                    tmpAsteroid.setActive(false);
                    //Log.e("Colision TM: ", ""+sx+" "+sy+" "+mx+" "+my);
                }
            }

            /*Ship vs ship*/
            for(Spaceship tmpShip2 : ships){
                if(!tmpShip2.getActive()) continue;

                float s2x = tmpShip2.getPosX();
                float s2y = tmpShip2.getPosY();
                //if(tmpShip!=tmpShip2)Log.e("Distancia: ", ""+calculateDistance(sx, sy, s2x, s2y));
                if(tmpShip.getLive() && tmpShip2.getLive() && (tmpShip != tmpShip2) && calculateDistance(sx, sy, s2x, s2y)<dpx  ){
                    tmpShip.setLive(false);
                    tmpShip2.setLive(false);
                    tmpShip.setLiveVisible(false);
                    tmpShip2.setLiveVisibfalse);
                    //Log.e("Colision TM: ", "" + sx + " " + sy + " " + s2x + " " + s2y);
                }
            }

        }
    }

    public void turbo(int id){
        ships.get(id).turbo();
    }


    public void rotateLeft(int id){
        ships.get(id).rotateLeft();
    }

    public void rotateRight(int id){
        ships.get(id).rotateRight();
    }

    public void noRotate(int id){
        ships.get(id).noRotate();
    }

    public boolean getTurbo(int id) {
        return ships.get(id).getTurbo();
    }

    public void shot(int id) {
        missils.add(new Missil(ships.get(id), id));
        ships.get(id).setActive(true);
    }

    public void shotAsteroid(int level) {
        asteroids.add(new Asteroid(this.wx, this.wy, level));
    }

    public void addSpaceship(int id){
        ships.add(id, new Spaceship(wx, wy));
        players[id]=true;
    }

    public Spaceship getShip(int id) { return ships.get(id);}

    public ArrayList<Missil> getMissil() { return missils;}
    public ArrayList<Asteroid> getAsteroids() { return asteroids;}

    public ArrayList<Spaceship> getShips() {return ships; }

    public float calculateDistance(float x1, float y1, float x2, float y2){
        return (float)Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }

    public void setWXY(int wx, int wy){
        this.wx=wx;
        this.wy=wy;
    }


}
