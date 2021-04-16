package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

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
            JSONObject obj = new JSONObject();

            // Query test
//            obj.put("Word", "Orange");
//            obj.put("Method", "Query");

            // Insert Test
//            ArrayList<String> meanings = new ArrayList<>();
//            meanings.add("World");
//            meanings.add("Hi");
//            obj.put("Word", "Hello");
//            obj.put("Meanings", meanings.toArray());
//            obj.put("Method", "Insert");

            // Update
            ArrayList<String> meanings = new ArrayList<>();
            meanings.add("An orange is a fruit");
            meanings.add("An orange is good looking little fella");
            obj.put("Word", "Orange");
            obj.put("Meanings", meanings.toArray());
            obj.put("Method", "Update");

            // Delete
//            obj.put("Word", "Apple");
//            obj.put("Method", "Delete");


            System.out.println("Data sent to Server--> " + obj.toString());
            output.writeUTF(obj.toString());

            output.flush();


            // Read result from server
            String message = input.readUTF();
            System.out.println(message);


        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            System.out.println("Connection Refused. Server not available");
        }


    }
}
