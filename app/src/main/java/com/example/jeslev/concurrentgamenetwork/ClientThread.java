package com.example.jeslev.concurrentgamenetwork;

import android.util.Log;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by yarvis on 09/10/15.
 */
public class ClientThread extends Thread {

    protected Socket socket;

    private TCPServer.OnMessageReceived messageListener = null;

    MySurfaceView mySurfaceView;
    int id;
    OutputStream oStream;
    ObjectOutputStream ooStream;

    InputStream istream;
    ObjectInputStream oistream;

    boolean running=false;

    public ClientThread(Socket socket,int id, TCPServer.OnMessageReceived messageListener, MySurfaceView mySurfaceView){
        this.socket = socket;
        this.id = id;
        this.messageListener = messageListener;
        this.mySurfaceView = mySurfaceView;
    }

    public int getID(){ return id;}

    @Override
    public void run(){

        running=true;

        try{
            oStream = socket.getOutputStream();
            ooStream = new ObjectOutputStream(oStream);

            Log.e("TCP Server", "C"+id+": Sent.");
            Log.e("TCP Server", "C"+id+": Done.");

            //receive the message which the server sends back
            //in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            istream = socket.getInputStream();
            oistream = new ObjectInputStream(istream);

            ooStream.reset();
            Game gameTemp = mySurfaceView.getGame();
            gameTemp.addSpaceship(id);
            gameTemp.getShip(id).setLiveVisible(false);
            ooStream.writeObject(new Container(gameTemp,id));
            ooStream.flush();

            Log.e("ClientThread", "envio mensaje");

            //in this while the client listens for the messages sent by the server
            while (running) {
    //          message = in.readLine();
                Game game = null;
                game= (Game) oistream.readObject();
                //Log.e("TCP", ""+game.getShip().getAngle());
                Log.e("TCP", ""+id); /*noamlr*/
                if (game != null && messageListener != null) {
                    messageListener.messageReceived(game,id);
                }
    //          if (message != null && messageListener != null) {
    //             //call the method messageReceived from MyActivity class
    //             messageListener.messageReceived(message);
    //          }
    //          message = null;
            }
    //                Log.e("RESPONSE FROM CLIENT", "S: Received Message: '" + message + "'");
        } catch (Exception e) {
            Log.e("TCP Server", "S: Error", e);
        } finally {
            //the socket must be closed. It is not possible to reconnect to this socket
            // after it is closed, which means a new socket instance has to be created.
            try{
                socket.close();
            }catch (Exception e) { e.printStackTrace();}
        }

    }

    public synchronized void sendMessage(Game message){
        try {
            if (ooStream != null) {
                ooStream.reset();
                ooStream.writeObject(message);
                ooStream.flush();
                //Log.e("TCP", "Envio accion de SERVER " + message.getShip().getAngle());
                Log.e("TCP", "Envio accion de SERVER " + message.getShip(id).getAngle()); /*noamlr*/
            }
        }catch (Exception e) { e.printStackTrace();}
    }

}

