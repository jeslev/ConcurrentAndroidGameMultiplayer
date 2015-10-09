package com.example.jeslev.concurrentgamenetwork;

import android.util.Log;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by jeslev on 08/10/15.
 */
public class TCPServer {
    private String message;

    public static final int SERVERPORT = 4444;
    private OnMessageReceived messageListener = null;
    private boolean running = false;

    PrintWriter mOut;
    BufferedReader in;

    //ArrayList<OutputStream> oStream;
    //ArrayList<ObjectOutputStream> ooStream;

    //ArrayList<InputStream> istream;
    //ArrayList<ObjectInputStream> oistream;


    ArrayList<ClientThread> threads;

    int id;

    //el constructor pide una interface OnMessageReceived
    public TCPServer(OnMessageReceived messageListener) {
        this.messageListener = messageListener;
        threads = new ArrayList<ClientThread>();
        id = 0;
//        oStream = new ArrayList<OutputStream>();
//        ooStream = new ArrayList<ObjectOutputStream>();
//
//        istream = new ArrayList<InputStream>();
//        oistream = new ArrayList<ObjectInputStream>();
    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public synchronized void sendMessage(Game message){
        try {
            for(ClientThread xclient : threads){
                xclient.sendMessage(message);
            }
        }catch (Exception e) { e.printStackTrace();}
    }

    public synchronized void sendMessage(Game message,int id){
        try {
            for(ClientThread xclient : threads){
                if(xclient.getID()!=id)
                    xclient.sendMessage(message);
            }
        }catch (Exception e) { e.printStackTrace();}
    }

    public void stopClient(){
        running = false;
    }

    public void run() {
        ///////////////////////
        running = true;

        try {
            //here you must put your computer's IP address.
            //InetAddress serverAddr = InetAddress.getByName(SERVERIP);

            ServerSocket serverSocket = new ServerSocket(SERVERPORT);

            while(running) {
                Log.e("TCP Server", "S : Connecting...");

                //create a socket to make the connection with the server
                //Socket socket = new Socket(serverAddr, SERVERPORT);


                //create client socket... the method accept() listens for a connection to be made to this socket and accepts it.
                Socket client = serverSocket.accept();

                //System.out.println("S: Receiving...");
                Log.e("TCP Server", "S: Receiving...");
                id++;
                ClientThread tmpThread = new ClientThread(client, id, messageListener);

                threads.add(tmpThread);
                tmpThread.start();
            }
        } catch (Exception e) {

            Log.e("TCP Server", "C: Error", e);

        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(Game message,int id);
    }
}