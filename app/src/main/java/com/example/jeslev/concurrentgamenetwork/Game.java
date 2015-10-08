package com.example.jeslev.concurrentgamenetwork;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;

/**
 * Created by jeslev on 06/10/15.
 */
public class Game {

    Spaceship ship;
    ArrayList<Missil> missils;
    int wx, wy;
    Canvas canvas;

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

    public void draw(Bitmap bitmap, Bitmap bitmapMissil,Canvas canvas, int wx, int wy){
        this.canvas = canvas;

        Matrix matrix = new Matrix();
        //matrix.postRotate((float)ship.getAngle());
        matrix.postRotate(ship.getAngle(), 14, 19);
        matrix.postTranslate(ship.getPosX(), ship.getPosY());
        canvas.drawBitmap(bitmap, matrix, null);
        //System.err.println(ship.getPosX() + " " + ship.getPosY() + " " + ship.getAngle());

        Missil tmpmissil;
        for(int i=0;i<missils.size();i++){
            tmpmissil = missils.get(i);
            matrix = new Matrix();
            matrix.postRotate(tmpmissil.getAnglePosition(),11,3);
            matrix.postTranslate(tmpmissil.getPosX(), tmpmissil.getPosY());
            canvas.drawBitmap(bitmapMissil,matrix,null);
        }
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
}
