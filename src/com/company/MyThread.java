/**
 * Author: Daniel Coleman, 994887
 * Date: 18/04/2021
 * */

package com.company;

import Exceptions.InvalidCommand;
import org.json.JSONObject;

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
        System.out.println("Client served, " + threadName +  " exiting");
    }

    /**
     * Read client command and execute action on dictionary
     * Return status of operation or result of query
     * @param client
     */
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
            while (true) {
                if (input.available() > 0) {
                    // Attempt to convert read data to JSON
                    try {
                        JSONObject jsonObject = new JSONObject(input.readUTF());

                        System.out.println("COMMAND RECEIVED:   " + jsonObject.toString());
                        String result = executeCommand(jsonObject);
                        output.writeUTF(result);
                        //break;
                    }catch(InvalidCommand e){
                        // Send error back to client
                        output.writeUTF(e.getMessage());
                    }
                }
            }
        }
        catch (IOException e) {
            // Socket error
            System.out.println(threadName + " socket closed, unable to send client data");
        }
    }

    private String executeCommand(org.json.JSONObject jsonObject) throws InvalidCommand {

        String result;

        String method = (String) jsonObject.get("Method");
        switch (method) {
            case "Query":
                result = dataAccess.queryDictionary(jsonObject.get("Word").toString());
                break;
            case "Insert":
                result = dataAccess.insertWord(jsonObject);
                break;
            case "Delete":
                result = dataAccess.deleteWord(jsonObject.get("Word").toString());
                break;
            case "Update":
                result = dataAccess.updateMeaning(jsonObject);
                break;
            default:
                throw new InvalidCommand("The command does not specify a valid action to perform.");
        }
        return result;
    }
}
