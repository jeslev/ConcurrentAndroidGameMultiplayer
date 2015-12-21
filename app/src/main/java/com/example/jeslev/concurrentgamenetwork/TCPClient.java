package com.example.jeslev.concurrentgamenetwork;

import android.util.Log;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by jeslev on 08/10/15.
 */
public class TCPClient {

    private String servermsj;

    public  String SERVERIP;// your computer IP address

    public static final int SERVERPORT = 4444;

    private OnMessageReceived mMessageListener = null;
    private  boolean mRun = false;

    OutputStream oStream;
    ObjectOutputStream ooStream;
    //public MySurfaceServerView mySurfaceServerView;
    public MySurfaceClientView mySurfaceClientView;

    InputStream istream;
    ObjectInputStream oistream;

    InetAddress serverAddr;
    Socket socket;

    Container container;

    public TCPClient(String ip){
        SERVERIP = ip;

        try {
            serverAddr = InetAddress.getByName(SERVERIP);
            socket = new Socket(serverAddr, SERVERPORT);
            try{
                oStream = socket.getOutputStream();
                ooStream = new ObjectOutputStream(oStream);
                Log.e("TCP1", "Recibi ID" );
                istream = socket.getInputStream();
                oistream = new ObjectInputStream(istream);
                Log.e("TCP2", "Recibi ID" );
                container = (Container)oistream.readObject();
                Log.e("TCP3", "Recibi ID" );
            }catch(Exception e){
                e.printStackTrace();
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public Container getContainer() { return container;}

    public void setMessageReceived(OnMessageReceived listener){
        mMessageListener = listener;
    }

    public synchronized void sendMessage(Game message){
        try {
            if (ooStream != null) {
                ooStream.reset();
                ooStream.writeObject(message);
                ooStream.flush();
                //Log.e("TCP", "Envio accion de SERVER " + message.getShip().getAngle());
            }
        }catch (Exception e) { e.printStackTrace();}
    }

    public void stopClient(){
        mRun = false;
    }

    //public void setSurface(MySurfaceServerView surface){      this.mySurfaceServerView = surface;    }
    public void setSurface(MySurfaceClientView surface){      this.mySurfaceClientView = surface;    }

    public void run() {

        mRun = true;

        try {
            try {

                //Log.e("TCPClient", "Envio Mensaje");

                while (mRun) {
                    Game game = null;
                    game= (Game) oistream.readObject();
                    //Log.e("String ID Spaceship: ", ""+game.idSpaceship);
                    //Log.e("TCP", ""+game.getShip().getAngle());
                    if (game != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(game);
                    }
                }

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //cerramos el servidor
                socket.close();
            }

        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }

    }

    //Declaramos la interface  OnMessageReived con el metodo messageReceived(String message)
    // utiliazdo en MyActivity en la clase asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(Game message);
    }
}
