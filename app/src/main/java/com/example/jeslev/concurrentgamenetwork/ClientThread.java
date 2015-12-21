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

    MySurfaceServerView mySurfaceServerView;
//    MySurfaceClientView mySurfaceClientView;
    int id;
    OutputStream oStream;
    ObjectOutputStream ooStream;

    InputStream istream;
    ObjectInputStream oistream;

    boolean running=false;

    public ClientThread(Socket socket,int id, TCPServer.OnMessageReceived messageListener, MySurfaceServerView mySurfaceView){
        this.socket = socket;
        this.id = id;
        this.messageListener = messageListener;
        this.mySurfaceServerView = mySurfaceView;
       // this.mySurfaceClientView = null;
    }
/*
    public ClientThread(Socket socket,int id, TCPServer.OnMessageReceived messageListener, MySurfaceClientView mySurfaceView){
        this.socket = socket;
        this.id = id;
        this.messageListener = messageListener;
        this.mySurfaceClientView = mySurfaceView;
        this.mySurfaceServerView = null;
    }
*/
    public int getID(){ return id;}

    @Override
    public void run(){

        running=true;

        try{
            oStream = socket.getOutputStream();
            ooStream = new ObjectOutputStream(oStream);

            //Log.e("TCP Server", "C"+id+": Sent.");
            //Log.e("TCP Server", "C"+id+": Done.");

            istream = socket.getInputStream();
            oistream = new ObjectInputStream(istream);

            //Send to client ID and game state to start playing
            ooStream.reset();
            Game gameTemp = mySurfaceServerView.getGame();
            //else if(mySurfaceClientView==null)
            //    gameTemp = mySurfaceClientView.getGame();
            gameTemp.addSpaceship(id);
            gameTemp.getShip(id).setLiveVisible(false);
            Log.e("GAME ID","num "+id);
            ooStream.writeObject(new Container(gameTemp,id));
            ooStream.flush();

            //Log.e("ClientThread", "envio mensaje");

            //in this while the client listens for the messages sent by the server
            while (running) {

                Game game = null;
                game= (Game) oistream.readObject();

                //Log.e("TCP", ""+id); /*noamlr*/
                if (game != null && messageListener != null) {
                    messageListener.messageReceived(game,id);
                }

            }

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
                //Log.e("TCP", "Envio accion de SERVER " + message.getShip(id).getAngle()); /*noamlr*/
            }
        }catch (Exception e) { e.printStackTrace();}
    }

}

