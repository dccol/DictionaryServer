package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    // IP and port
    private static String serverIp;
    private static int serverPort;

    public static void main(String[] args)
    {
        // READ IN COMMAND LINE ARGS
        if(args.length < 2){
            System.out.println("Missing Arguments. Please Try Again");
            System.exit(0);
        }
        else if(args.length > 2){
            System.out.println("Too many Arguments. Please Try Again");
            System.exit(0);
        }
        serverIp = args[0];
        serverPort = Integer.parseInt(args[1]);

        try(Socket socket = new Socket(serverIp, serverPort);)
        {
            // Output and Input Stream
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            // Send the client request ie. Query, Insert, Delete, Update.
            String sendData ="I want to connect";
            output.writeUTF(sendData);
            System.out.println("Data sent to Server--> " + sendData);
            output.flush();

            while(true && input.available() > 0)
            {
                String message = input.readUTF();
                System.out.println(message);
            }

        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
