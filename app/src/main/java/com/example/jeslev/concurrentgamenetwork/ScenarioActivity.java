    package com.example.jeslev.concurrentgamenetwork;

    import android.content.Intent;
    import android.media.MediaPlayer;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.util.Log;
    import android.view.SurfaceView;

public class ScenarioActivity extends AppCompatActivity {

    public SurfaceView SurfaceViewX;
    final MediaPlayer mp = new MediaPlayer();
    ConnectTask connectTask;
    String TAG_IP = "ip";
    String myip = "";

    public TCPClient tcpClient;

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

        Intent intent = getIntent();
        myip = intent.getStringExtra(TAG_IP);


        connectTask  = new ConnectTask(myip);
        connectTask.execute("");

        SurfaceViewX = new MySurfaceView(this, tcpClient); // "this" send context of the current class
        setContentView(SurfaceViewX);



    }


    public class ConnectTask extends AsyncTask<String,String,TCPClient> {
        String myip;



        public ConnectTask(String ip){
            myip=ip;
        }

        @Override
        protected TCPClient doInBackground(String... message) {
            //creamos el objeto TCPClient
            tcpClient = new TCPClient(myip,new TCPClient.OnMessageReceived() {
                @Override
                //Utilizamos el metodo de messageReceived(String message) de la interface OnMessageReived
                public void messageReceived(String message) {
                    //llama a onProgressUpdate
                    publishProgress(message);
                }
            });
            tcpClient.run();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //ClienteReciveCoordenadas(values[0]);/////////////////
            //SurfaceViewA.doDraw(holderA,null, 50, 50,0);
            Log.e("TCP", "cliente recibe accion!");



        }
    }



}
