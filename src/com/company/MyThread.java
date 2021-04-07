package com.company;

import Exceptions.WordAlreadyExists;
import Exceptions.WordNotFound;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.JSONValue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
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
            // Client will send json object specifying which method to use and the parameters for that method
            String rec = "";
            JSONObject jsonObject = new JSONObject();
            try {
                while (true) {
                    rec += input.readUTF();
                }
            } catch (EOFException e) { }
            finally{
                // Build Query
                Object obj = JSONValue.parse(rec);
                jsonObject = (JSONObject) obj;
            }

            String method = (String) jsonObject.get("Method");
            if(method.equals("Query")){
                dataAccess.QueryDictionary(jsonObject.get("Word").toString());
            }
            else if(method.equals("Insert")){
                dataAccess.InsertWord(jsonObject);
            }
            else if(method.equals("Delete")){
                dataAccess.DeleteWord(jsonObject.get("Word").toString());
            }
            else if(method.equals("Update")){
                dataAccess.UpdateMeaning(jsonObject);
            }
            else{
                //Error
            }

            output.writeUTF("SERVER: Hi Client "+clientNumber+", this is "+threadName+" how may I help?");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (WordAlreadyExists e) {
            // Send message back to the client
            String message = e.getMessage();
            System.out.println(message);
        } catch (WordNotFound e) {
            String message = e.getMessage();
            System.out.println(message);
        }
    }

}
