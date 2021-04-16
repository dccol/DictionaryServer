package com.company;

import Exceptions.InvalidCommand;
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
                if(input.available() > 0){
                    // Attempt to convert read data to JSON
                    JSONObject jsonObject = new JSONObject(input.readUTF());

//                    org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) parser.parse(input.readUTF());
                    System.out.println("COMMAND RECEIVED:   "+jsonObject.toString());
                    ArrayList<String> result = executeCommand(jsonObject);
                    output.writeUTF("Server: " + result.toString());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidCommand invalidCommand) {
            invalidCommand.printStackTrace();
        }
    }

    private ArrayList<String> executeCommand(org.json.JSONObject jsonObject) throws InvalidCommand {

        ArrayList<String> result = new ArrayList<>();

        try {
            String method = (String) jsonObject.get("Method");
            if (method.equals("Query")) {
                result = dataAccess.QueryDictionary(jsonObject.get("Word").toString());
            } else if (method.equals("Insert")) {
                dataAccess.InsertWord(jsonObject);
            } else if (method.equals("Delete")) {
                result = dataAccess.DeleteWord(jsonObject.get("Word").toString());
            } else if (method.equals("Update")) {
                result = dataAccess.UpdateMeaning(jsonObject);
            } else {
                throw new InvalidCommand("The command does not specify a valid action to perform.");
            }
        }
        catch (WordNotFound | WordAlreadyExists e) {
        String message = e.getMessage();
        System.out.println(message);
        }
        return result;
    }
}
