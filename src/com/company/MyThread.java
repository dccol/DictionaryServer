package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MyThread extends Thread{

    private final String threadName;
    private final Socket socket;
    private final int clientNumber;

    private DictionaryDataAccess dataAccess;

    MyThread(int counter, Socket socket, DictionaryDataAccess dataAccess) {
        threadName = "Thread "+counter;
        this.socket = socket;
        this.clientNumber = counter;
        this.dataAccess = dataAccess;
    }

    /**
     * Thread.start() execution
     */
    public void run()
    {
        serveClient(socket);
        System.out.println(threadName + " exiting.");
    }

    private void serveClient(Socket client)
    {
        try(Socket clientSocket = client)
        {
            // Input stream
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            // Output Stream
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            // Read the client request and perform action, ie. Query, Insert, Delete, Update
            System.out.println("CLIENT: "+input.readUTF());

            output.writeUTF("SERVER: Hi Client "+clientNumber+", this is "+threadName+" how may I help?");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
