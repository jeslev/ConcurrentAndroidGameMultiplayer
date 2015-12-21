package com.example.jeslev.concurrentgamenetwork;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by yarvis on 07/10/15.
 */
public class MainThread extends Thread {

    private int FPS = 60;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private MySurfaceServerView surfaceServerView;
    private MySurfaceClientView surfaceClientView;
    private boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, MySurfaceServerView surfaceView){
        super();
        this.surfaceHolder = surfaceHolder;
        this.surfaceServerView = surfaceView;
        this.surfaceClientView = null;
    }

    public MainThread(SurfaceHolder surfaceHolder, MySurfaceClientView surfaceView){
        super();
        this.surfaceHolder = surfaceHolder;
        this.surfaceServerView = null;
        this.surfaceClientView = surfaceView;
    }

    @Override
    public void run(){
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime=0;
        int frameCount = 0;
        long targetTime = 1000/FPS;

        while(running){
            startTime = System.nanoTime();
            canvas = null;

            //try lock the canvas for pixel
            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                if(this.surfaceClientView!=null) {
                    this.surfaceClientView.update();     /*noamlr*/
                    this.surfaceClientView.doDraw(canvas); /*noamlr*/
                }
                if(this.surfaceServerView!=null) {
                    this.surfaceServerView.update();     /*noamlr*/
                    this.surfaceServerView.doDraw(canvas); /*noamlr*/
                }

                }

            }catch(Exception e){}
            finally {
                if(canvas!=null){
                    try{ surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch(Exception e){ e.printStackTrace();}
                }

            }


            timeMillis = (System.nanoTime()-startTime)/ 1000000;
            waitTime = targetTime - timeMillis;

            //wait to fill 30 ms
            try{
                this.sleep(waitTime);
            }catch (Exception e){}

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            if(frameCount==FPS){
                averageFPS = 1000/ ((totalTime/frameCount)/1000000);
                frameCount= 0;
                totalTime = 0;
                //System.out.println(averageFPS);
            }
        }
    }

    public void setRunning(boolean b){ running =b; }
}
