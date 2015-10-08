package com.example.jeslev.concurrentgamenetwork;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * Created by jeslev on 06/10/15.
 */
public class Game {

    Spaceship ship;
    int wx, wy;
    Canvas canvas;

    public Game(){
        ship = new Spaceship();
    }

    public void update(int wx,int wy) {
        this.wx = wx;
        this.wy = wy;

        ship.update(wx, wy);
    }

    public void draw(Bitmap bitmap, Canvas canvas, int wx, int wy){
        this.canvas = canvas;

        Matrix matrix = new Matrix();
        //matrix.postRotate((float)ship.getAngle());
        matrix.postRotate((float)ship.getAngle(), 14, 19);
        matrix.postTranslate(ship.getPosX(), ship.getPosY());
        canvas.drawBitmap(bitmap, matrix, null);
        System.err.println(ship.getPosX()+" "+ship.getPosY()+" "+ship.getAngle());
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
}
