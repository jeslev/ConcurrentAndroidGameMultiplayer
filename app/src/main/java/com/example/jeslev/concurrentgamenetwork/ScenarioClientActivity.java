package com.example.jeslev.concurrentgamenetwork;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class ScenarioClientActivity extends AppCompatActivity {

    public MySurfaceClientView SurfaceViewX;
    final MediaPlayer mp = new MediaPlayer();
    ConnectTask connectTask;
    String TAG_SOCKET = "socket";
    String myip = "";

    public TCPClient tcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_scenario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = getBaseContext().getAssets().openFd("music.mp3");
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mp.prepareAsync();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
                @Override
                public void onPrepared(MediaPlayer mediaPlayer){
                    mp.start();
                }
            });
        }catch(Exception e){ e.printStackTrace();}

        Intent intent = getIntent();

        tcpClient = MainClientActivity.tcpClient;

        connectTask  = new ConnectTask();

        SurfaceViewX = new MySurfaceClientView(this, tcpClient); // "this" send context of the current class
        setContentView(SurfaceViewX);
        connectTask.execute("");
    }


    @Override
    protected void onStop(){
        super.onStop();
        tcpClient.stopClient();
        mp.stop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mp.stop();
    }

    public class ConnectTask extends AsyncTask<String,Game,TCPClient> {
        String myip;

        public ConnectTask(){
            //creamos el objeto TCPClient
            tcpClient.setMessageReceived(new TCPClient.OnMessageReceived() {
                @Override
                public void messageReceived(Game message) {
                    publishProgress(message);
                }
            });
        }

        @Override
        protected TCPClient doInBackground(String... message) {
            tcpClient.run();
            return null;
        }

        @Override
        protected synchronized  void onProgressUpdate(Game... values) {
            super.onProgressUpdate(values);
            SurfaceViewX.setGame(values[0]);
        }
    }
}