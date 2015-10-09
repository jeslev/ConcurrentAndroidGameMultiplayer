package com.example.jeslev.concurrentgamenetwork;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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

    OutputStream oStream;
    ObjectOutputStream ooStream;

    //el constructor pide una interface OnMessageReceived
    public TCPServer(OnMessageReceived messageListener) {
        this.messageListener = messageListener;
    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public synchronized void sendMessage(Game message){
        try {
            if (ooStream != null) {
                ooStream.writeObject(message);
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

            Log.e("TCP Server","S : Connecting...");

            //create a socket to make the connection with the server
            //Socket socket = new Socket(serverAddr, SERVERPORT);

            ServerSocket serverSocket = new ServerSocket(SERVERPORT);

            //create client socket... the method accept() listens for a connection to be made to this socket and accepts it.
            Socket client = serverSocket.accept();

            //System.out.println("S: Receiving...");
            Log.e("TCP Server","S: Receiving...");

            try {

                //send the message to the server
                //mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

                oStream = client.getOutputStream();
                ooStream = new ObjectOutputStream(oStream);

                Log.e("TCP Server", "C: Sent.");

                Log.e("TCP Server", "C: Done.");

                //receive the message which the server sends back
                //in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                //in this while the client listens for the messages sent by the server
                while (running) {
//                    message = in.readLine();
//
//                    if (message != null && messageListener != null) {
//                        //call the method messageReceived from MyActivity class
//                        messageListener.messageReceived(message);
//                    }
//                    message = null;

                }

//                Log.e("RESPONSE FROM CLIENT", "S: Received Message: '" + message + "'");

            } catch (Exception e) {

                Log.e("TCP Server", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                client.close();
                serverSocket.close();/////
            }

        } catch (Exception e) {

            Log.e("TCP Server", "C: Error", e);

        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}