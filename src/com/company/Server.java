package com.company;

import javax.net.ServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
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
        }
        else if(args.length > 2){
            System.out.println("Too many Arguments. Please Try Again");
        }
        port = Integer.parseInt(args[0]);
        fileName = args[1];

        // Create Dictionary
        ConcurrentHashMap<String, String> dictionary = CreateDictionary(fileName);

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
                System.out.println("Client "+counter+": Applying for connection!");

                // Start a new thread for a connection
                MyThread t = new MyThread("Thread " + counter);
                t.start();
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private static ConcurrentHashMap<String, String> CreateDictionary(String fileName)
    {
        ConcurrentHashMap<String, String> dictionary = new ConcurrentHashMap<>();
        String txt = ReadDictionaryTextFile(fileName);
        String[] lines = txt.split(System.getProperty("line.separator"));
        for(String line : lines){
            String[] split = line.split("\\t");
            dictionary.putIfAbsent(split[0], split[1]);
            //System.out.println(dictionary.get(split[0]));
        }
        return dictionary;
    }
    private static String ReadDictionaryTextFile(String fileName)
    {
        // Open Text File
        String txt = "";
        try {
            File dictionaryFile = new File(fileName);
            Scanner dictionaryReader = new Scanner(dictionaryFile);
            while (dictionaryReader.hasNextLine()) {
                txt += dictionaryReader.nextLine();
                txt += "\r\n";
            }
            dictionaryReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.println(txt);
        return txt;
    }
}
