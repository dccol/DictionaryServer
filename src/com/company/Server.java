package com.company;

import javax.net.ServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    // Identifies the user number connected
    private static int counter = 0;
    // Server port number
    private static int port;
    // Dictionary Filename
    private static String fileName;

    // GUI
    private static ServerInterface window;

    public static void main(String[] args) {

        // READ IN COMMAND LINE ARGS
        if(args.length < 2){
            System.out.println("Missing Arguments. Please Try Again");
            System.exit(0);

        }
        else if(args.length > 2){
            System.out.println("Too many Arguments. Please Try Again");
            System.exit(0);

        }
        try {
            port = Integer.parseInt(args[0]);
        }
        catch(NumberFormatException e){
            System.out.println("The port number must be an integer.");
            System.exit(0);
        }
        fileName = args[1];

        // Create Dictionary
        DictionaryDataAccess dictionaryAccess = DictionaryDataAccess.getInstance(fileName);

        window = new ServerInterface(dictionaryAccess, fileName);
        window.run();

        // Initialise ServerSocketFactory and Create Welcome Socket
        ServerSocketFactory factory = ServerSocketFactory.getDefault();

        try(ServerSocket server = factory.createServerSocket(port))
        {
            System.out.println("Waiting for client connection on port " + port);

            // Wait for connections to welcome socket.
            while(true)
            {
                // Create unique socket for client to communicate on
                Socket client = server.accept();
                counter++;
                System.out.println("CLIENT "+counter+": Applying for connection!");

                // Start a new thread for a connection
                MyThread t = new MyThread(counter, client, dictionaryAccess);
                t.start();
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
