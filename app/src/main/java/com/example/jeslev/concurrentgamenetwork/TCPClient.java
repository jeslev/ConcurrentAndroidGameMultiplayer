package com.example.jeslev.concurrentgamenetwork;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by jeslev on 08/10/15.
 */
public class TCPClient {

    private String servermsj;

    public  String SERVERIP;// = "192.168.2.9"; //your computer IP address
    //public  String SERVERIP = "192.168.2.9"; //your computer IP address

    //public int SERVERPORT = 4444;

    //public static final String SERVERIP = "192.168.2.9"; //your computer IP address

    public static final int SERVERPORT = 4444;


    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    PrintWriter out;
    BufferedReader in;

    Game game;

    InputStream istream;
    ObjectInputStream oistream;

    public TCPClient(String ip,OnMessageReceived listener) {
        SERVERIP = ip;
        mMessageListener = listener;
    }

//    public void sendMessage(String message){
//        if (out != null && !out.checkError()) {
//            out.println(message);
//            out.flush();
//        }
//    }

    public void stopClient(){
        mRun = false;
    }

    public void run() {

        mRun = true;

        try {

            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
            //Log.e("TCP Client", "C: Conectando...");

            Socket socket = new Socket(serverAddr, SERVERPORT);

            try {

                //envio de mensaje al servidor
                //out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                //Log.e("TCP Client", "C: Sent.");
                //Log.e("TCP Client", "C: Done.");

                //recibe el mensaje del servidor
                //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                istream = socket.getInputStream();
                oistream = new ObjectInputStream(istream);

                //mientras escucha o envia mensajes
                while (mRun) {
                    //servermsj = in.readLine();
                    game = (Game) oistream.readObject();

                    if (game != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(game);
                    }
                    game = null;

                }

                //Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + servermsj + "'");

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //cerramos el servidor
                socket.close();
            }

        } catch (Exception e) {
            //Log.e("TCP", "C: Error", e);
        }

    }

    //Declaramos la interface  OnMessageReived con el metodo messageReceived(String message)
    // utiliazdo en MyActivity en la clase asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(Game message);
    }
}
