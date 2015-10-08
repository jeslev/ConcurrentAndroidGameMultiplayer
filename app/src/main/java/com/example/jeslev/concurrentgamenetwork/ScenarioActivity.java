    package com.example.jeslev.concurrentgamenetwork;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceView;

public class ScenarioActivity extends AppCompatActivity {

    public SurfaceView SurfaceViewX;
    final MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = getBaseContext().getAssets().openFd("music.mp3");
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mp.prepare();
            mp.start();
        }catch(Exception e){ e.printStackTrace();}
        
        SurfaceViewX = new MySurfaceView(this); // "this" send context of the current class
        setContentView(SurfaceViewX);



    }


    //    public class XYPosition {
//
//        private int x;
//        private int y;
//        XYPosition(){
//            this(0,0);
//        }
//        XYPosition(int x,int y){
//            this.x = x;
//            this.y = y;
//        }
//
//        public int getX() {
//            return x;
//
//        }
//
//        public void setX(int x) {
//            this.x = x;
//
//        }
//        public int getY() {
//            return y;
//        }
//
//        public void setY(int y) {
//            this.y = y;
//        }
//
//    }
}
