package com.company;

import javax.net.ServerSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    // Identifies the user number connected
    private static int counter = 0;

    public static int main(String[] args) {

        // READ IN COMMAND LINE ARGS
        if(args.length < 3){
            System.out.println("Missing Arguments. Please Try Again");
             return 0;
        }
        else if(args.length > 3){
            System.out.println("Too many Arguments. Please Try Again");
            return 0;
        }
        int port = Integer.parseInt(args[1]);
        String dictionaryFilename = args[2];

        // Initialise ServerSocketFactory and Create Welcome Socket
        ServerSocketFactory factory = ServerSocketFactory.getDefault();

        int count = 0;
        try(ServerSocket server = factory.createServerSocket(port))
        {
            System.out.println("Waiting for client connection-");

            // Wait for connections to welcome socket.
            while(true)
            {
                Socket client = server.accept();
                counter++;
                System.out.println("Client "+counter+": Applying for connection!");

                // Start a new thread for a connection
                //Thread t = new Thread(() -> serveClient(client));
                //t.start();
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return 1;
    }
}
