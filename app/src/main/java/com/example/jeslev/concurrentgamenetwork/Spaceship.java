package com.example.jeslev.concurrentgamenetwork;


/**
 * Created by jeslev on 06/10/15.
 */
public class Spaceship {

    private double tempSpeed[] = {0.0,0.0};
    private double tempAcceleration[] = {0.0,0.0};
    private int position[] = {50,100};
    private double speed;
    private double acceleration;
    private double anglePosition;
    private double angleSpeed;
    private int id;
    private boolean turnon;
    private int boundarieX, boundarieY;

    public Spaceship(){
        anglePosition = 270;
        angleSpeed = 0.0;
        turnon = false;
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
            tempSpeed[0]*= 0.88;
            tempSpeed[1]*= 0.88;
        }else{
            /*More friction to stop */
            tempSpeed[0]*= 0.98;
            tempSpeed[1]*= 0.98;
        }

        /*Check boundaries*/
        if(position[0]<0 ) position[0]=0;
        if(position[0]>boundarieX ) position[0]=boundarieX;
        if(position[1]<0 ) position[1]=0;
        if(position[1]>boundarieY ) position[1]=boundarieY;
    }

    private void convertAngleVector(){
        tempAcceleration[0] = Math.cos(anglePosition);
        tempAcceleration[1] = Math.sin(anglePosition);
    }

    public void turbo(){
        turnon = !turnon;
    }

    public void rotateLeft(){
        angleSpeed = 5.0;
    }

    public void rotateRight(){
        angleSpeed = -5.0;
    }

    public void noRotate(){
        angleSpeed = 0.0;
    }

    public int getPosX(){ return position[0];}
    public int getPosY(){ return position[1];}
    public double getAngle(){ return anglePosition; }
}
