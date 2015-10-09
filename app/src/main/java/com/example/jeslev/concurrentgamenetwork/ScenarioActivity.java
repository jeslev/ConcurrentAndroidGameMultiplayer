    package com.example.jeslev.concurrentgamenetwork;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class ScenarioActivity extends AppCompatActivity {

    public MySurfaceView SurfaceViewX;
    final MediaPlayer mp = new MediaPlayer();

    public TCPServer tcpServer;
    public ConnectTask connectTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = getBaseContext().getAssets().openFd("music.mp3");
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mp.prepare();
            mp.start();
        }catch(Exception e){ e.printStackTrace();}
        */


        connectTask = new ConnectTask();


        SurfaceViewX = new MySurfaceView(this,tcpServer); // "this" send context of the current class
        setContentView(SurfaceViewX);

        connectTask.execute("");


    }



    public class ConnectTask extends AsyncTask<String, Game, TCPServer> {

        public ConnectTask(){
            //we create a TCPClient object and
            tcpServer = new TCPServer(new TCPServer.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(Game message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
        }


        @Override
        protected TCPServer doInBackground(String... message) {

            tcpServer.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(Game... values) {
            super.onProgressUpdate(values);

            ///////agregar(values[0]);//////////////////////////////////////////
            //in the arrayList we add the messaged received from server
            /////arrayList.add(values[0]);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            /////mAdapter.notifyDataSetChanged();
            SurfaceViewX.setGame(values[0]);
            //ServidorReciveCoordenadas(values[0]);
            Log.e("TCP", "Servidor recibe coordenadas");

        }

    }


}
