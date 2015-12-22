package com.example.jeslev.concurrentgamenetwork;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by yarvis on 08/10/15.
 */
public class Asteroid implements Serializable {

    float anglePosition;
    float speed[] = {0.0f, 0.0f};
    float acceleration[] = {0.0f, 0.0f};
    float position[] = {0.0f, 0.0f};
    boolean active;
    int time,id;

    int level;

    public Asteroid(int wx, int wy, int level){
        this.level = level;
        Random rand = new Random();
        speed[0] = rand.nextInt(10)*level;
        speed[1] = rand.nextInt(10)*level;

        acceleration[0] = 2 + 10*level;
        acceleration[1] = 2 + 10*level;

        position[0] = rand.nextInt(wx-200) + 100;
        position[1] = rand.nextInt(wy-200) + 100;

        anglePosition = rand.nextInt(360);
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
