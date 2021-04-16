package com.company;

import Exceptions.InvalidCommand;
import Exceptions.MissingMeaning;
import Exceptions.WordAlreadyExists;
import Exceptions.WordNotFound;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

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
            // Client will send json object specifying which method to use and the parameters for that method
            while(true){
                if(input.available() > 0) {
                    // Attempt to convert read data to JSON
                    JSONObject jsonObject = new JSONObject(input.readUTF());

                    System.out.println("COMMAND RECEIVED:   " + jsonObject.toString());
                    String result = executeCommand(jsonObject);
                    output.writeUTF("Server: " + result.toString());
                    break;
                }
            }
        }
        catch (InvalidCommand | IOException | MissingMeaning e) {
            // Send back to client
        }
        System.out.println("Client served, exiting");
    }

    private String executeCommand(org.json.JSONObject jsonObject) throws InvalidCommand, MissingMeaning {

        String result;

        try {
            String method = (String) jsonObject.get("Method");
            switch (method) {
                case "Query":
                    result = dataAccess.QueryDictionary(jsonObject.get("Word").toString());
                    break;
                case "Insert":
                    result = dataAccess.InsertWord(jsonObject);
                    break;
                case "Delete":
                    result = dataAccess.DeleteWord(jsonObject.get("Word").toString());
                    break;
                case "Update":
                    result = dataAccess.UpdateMeaning(jsonObject);
                    break;
                default:
                    throw new InvalidCommand("The command does not specify a valid action to perform.");
            }
        }
        catch (MissingMeaning e) {
            throw e;
        }
        return result;
    }
}
