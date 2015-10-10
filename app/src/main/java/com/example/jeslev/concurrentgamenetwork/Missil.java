package com.example.jeslev.concurrentgamenetwork;

import java.io.Serializable;

/**
 * Created by yarvis on 08/10/15.
 */
public class Missil implements Serializable {

    float anglePosition;
    float speed[] = {0.0f, 0.0f};
    float acceleration[] = {0.0f, 0.0f};
    float position[] = {0.0f, 0.0f};
    boolean active;
    int time,id;

    public Missil(Spaceship ship,int id){
        this.id = id;
        speed[0] = ship.getSpeed()[0] + 33*ship.getAcceleration()[0];
        speed[1] = ship.getSpeed()[1] + 33*ship.getAcceleration()[1];

        acceleration[0] = ship.getAcceleration()[0];
        acceleration[1] = ship.getAcceleration()[1];

        position[0] = ship.getPosX()+acceleration[0]*45;
        position[1] = ship.getPosY()+acceleration[1]*45;

        anglePosition = ship.getAngle();
        active=true;
        time=0;
    }

    void update(int wx,int wy){
        position[0]+=speed[0];
        position[1]+=speed[1];

        if(time<80) time++;
        else active=false;

        if(position[0]>wx || position[0]<0) active=false;
        if(position[1]>wy || position[1]<0) active=false;

    }

    public float getAnglePosition() {return  anglePosition;}

    public float getPosX() {return  position[0];}
    public float getPosY() {return  position[1];}

    public boolean getActive() {return active;}

    public void setActive(boolean b) {active = b;}
}
