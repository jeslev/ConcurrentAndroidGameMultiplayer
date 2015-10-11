package com.example.jeslev.concurrentgamenetwork;


import java.io.Serializable;
import java.util.Random;

/**
 * Created by jeslev on 06/10/15.
 */
public class Spaceship implements Serializable {

    private float tempSpeed[] = {0.0f,0.0f};
    private float tempAcceleration[] = {0.0f,0.0f};
    private float position[] = {0,0};
    private double speed;
    private double acceleration;
    private float anglePosition;
    private float angleSpeed;
    private int id;
    private boolean turnon;
    private int boundarieX, boundarieY;

    private boolean live,active, liveVisible;

    public Spaceship(int wx, int wy){
        anglePosition = 270;
        angleSpeed = 0.0f;
        turnon = false;
        live = true;
        liveVisible = true;
        active=false;
        Random rand = new Random();
        position[0] = rand.nextInt(wx-200) + 100;
        position[1] = rand.nextInt(wy-200) + 100;
    }

    public void update(int wx,int wy){

        boundarieX = wx;
        boundarieY = wy;

        anglePosition += angleSpeed;
        position[0]+=tempSpeed[0];
        position[1]+=tempSpeed[1];

        convertAngleVector();

        if(turnon){
            tempSpeed[0] += tempAcceleration[0];
            tempSpeed[1] += tempAcceleration[1];
            /*friction stop*/
            tempSpeed[0]*= 0.98;
            tempSpeed[1]*= 0.98;
        }else{
            /*More friction to stop */
            tempSpeed[0]*= 0.8;
            tempSpeed[1]*= 0.8;
        }

        /*Check boundaries*/
        if(position[0]<0 ) position[0]=0;
        if(position[0]>boundarieX ) position[0]=boundarieX;
        if(position[1]<0 ) position[1]=0;
        if(position[1]>boundarieY ) position[1]=boundarieY;
    }

    private void convertAngleVector(){
        tempAcceleration[0] = (float)Math.cos(anglePosition*Math.PI/180.0);
        tempAcceleration[1] = (float)Math.sin(anglePosition*Math.PI/180.0);
    }

    public void turbo(){
        turnon = !turnon;
        //Log.e("turbo: ", ""+turnon);
    }

    public void rotateLeft(){
        angleSpeed = 12.0f;
        active = true;
    }

    public void rotateRight(){
        angleSpeed = -12.0f;
        active = true;
    }

    public void noRotate(){
        angleSpeed = 0.0f;
        active = true;
    }

    public float getPosX(){ return position[0];}
    public float getPosY(){ return position[1];}
    public float getAngle(){ return anglePosition; }

    public boolean getTurbo() { return turnon;}

    public float[] getSpeed() { return tempSpeed;}

    public float[] getAcceleration() { return tempAcceleration;}

    public void setLive(boolean b) {live = b;}
    public void setLiveVisible(boolean b) {liveVisible = b;}

    public boolean getLive() {return live;}
    public boolean getLiveVisible() {return liveVisible;}

    //Vars to identify if player is playing already
    public void setActive(boolean b) {active=b;}
    public boolean getActive() { return active;}
}
