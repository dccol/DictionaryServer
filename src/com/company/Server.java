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
        port = Integer.parseInt(args[0]);
        fileName = args[1];

        // Create Dictionary
        //ConcurrentHashMap<String, String> dictionary = CreateDictionary(fileName);
        DictionaryDataAccess dictionaryAccess = DictionaryDataAccess.getInstance(fileName);

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
