    package com.example.jeslev.concurrentgamenetwork;

    import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class ScenarioServerActivity extends AppCompatActivity {

    public MySurfaceServerView SurfaceViewX;
    final MediaPlayer mp = new MediaPlayer();

    public TCPServer tcpServer;
    public ConnectTask connectTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_scenario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = getBaseContext().getAssets().openFd("music.mp3");
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

            mp.prepareAsync();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
                @Override
                public void onPrepared(MediaPlayer mediaPlayer){
                    mp.start();
                }
            });
        }catch(Exception e){ e.printStackTrace();}

        connectTask = new ConnectTask();


        SurfaceViewX = new MySurfaceServerView(this,tcpServer); // "this" send context of the current class
        setContentView(SurfaceViewX);


        connectTask.execute("");


    }

    @Override
    protected void onStop(){
        super.onStop();
        mp.stop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mp.stop();
    }


    public class ConnectTask extends AsyncTask<String, Container, TCPServer> {

        public ConnectTask(){
            //we create a TCPClient object and
            tcpServer = new TCPServer(new TCPServer.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public synchronized void messageReceived(Game message, int id) {
                    //this method calls the onProgressUpdate
                    publishProgress(new Container(message,id));
                }
            });
        }


        @Override
        protected TCPServer doInBackground(String... message) {

            tcpServer.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(Container... values) {
            super.onProgressUpdate(values);

            //Log.e("ID: ", "sdfsdf "+ values[0].getID());
            SurfaceViewX.setGame(values[0].getGame(), values[0].getID());
            //Log.e("TCP", "Servidor recibe coordenadas");

        }

    }


}
