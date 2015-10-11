package com.example.jeslev.concurrentgamenetwork;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {

    Button startButton,connecButton;
    EditText ipText;
    TextView name,state;
    ImageView image;

    String TAG_SOCKET = "socket";
    int idClient;
    public static TCPClient tcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ipText = (EditText) findViewById(R.id.ipText);

        name = (TextView) findViewById(R.id.labelPlayerName2);
        state = (TextView) findViewById(R.id.labelState);

        startButton = (Button) findViewById(R.id.startButton);

        connecButton = (Button) findViewById(R.id.connectButton);

        image = (ImageView) findViewById(R.id.imageView);

        connecButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String iptxt = ipText.getText().toString();

                ConnectServer task = new ConnectServer();
                task.execute(iptxt);

            }
        });

        startButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {

                Intent scenarioIntent = new Intent(getApplicationContext(),ScenarioActivity.class);
                startActivity(scenarioIntent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class ConnectServer extends AsyncTask<String,String,String> {

        private ProgressDialog dialog;


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Espere por favor ...");
            dialog.show();
        }


        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(dialog.isShowing()) dialog.dismiss();
            idClient = tcpClient.getContainer().getID();
            name.setText("Jugador " + idClient);
            state.setText("Conectado");
            if(idClient==1) image.setImageResource(R.drawable.spaceship);
            if(idClient==2) image.setImageResource(R.drawable.spaceship);
        }

        @Override
        protected String doInBackground(String... params) {
            tcpClient = new TCPClient(params[0]);
            return null;
        }


    }
}
