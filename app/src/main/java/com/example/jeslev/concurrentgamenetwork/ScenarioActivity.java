    package com.example.jeslev.concurrentgamenetwork;

    import android.content.Intent;
    import android.media.MediaPlayer;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.os.StrictMode;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;

public class ScenarioActivity extends AppCompatActivity {

    public MySurfaceView SurfaceViewX;
    final MediaPlayer mp = new MediaPlayer();
    ConnectTask connectTask;
    String TAG_IP = "ip";
    String myip = "";

    public TCPClient tcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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


        tcpClient = new TCPClient(myip);


        connectTask  = new ConnectTask();


        SurfaceViewX = new MySurfaceView(this, tcpClient); // "this" send context of the current class

        setContentView(SurfaceViewX);
        connectTask.execute("");


    }


    public class ConnectTask extends AsyncTask<String,Game,TCPClient> {
        String myip;



        public ConnectTask(){


            //creamos el objeto TCPClient
//            tcpClient = new TCPClient(myip,new TCPClient.OnMessageReceived() {
//                @Override
//                //Utilizamos el metodo de messageReceived(String message) de la interface OnMessageReived
//                public void messageReceived(Game message) {
//                    //llama a onProgressUpdate
//                    publishProgress(message);
//                }
//            });
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
            //ClienteReciveCoordenadas(values[0]);/////////////////
            //SurfaceViewA.doDraw(holderA,null, 50, 50,0);
            SurfaceViewX.setGame(values[0]);
            //Log.e("TCP", "cliente recibe accion! "+ values[0].getShip().getAngle() );



        }
    }



}
