/**
 * Author: Daniel Coleman, 994887
 * Date: 18/04/2021
 * */

package com.company;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {

    // IP and port
    private static String serverIp;
    private static int serverPort;

    // GUI
    private static MainWindow window;

    public static void main(String[] args)
    {
        // Validate command line input
        if(args.length < 2){
            System.out.println("Missing Arguments. Please Try Again");
            System.exit(0);
        }
        else if(args.length > 2){
            System.out.println("Too many Arguments. Please Try Again");
            System.exit(0);
        }

        serverIp = args[0];
        try {
            serverPort = Integer.parseInt(args[1]);
        }
        catch(NumberFormatException e){
            System.out.println("The port number must be an integer.");
            System.exit(0);
        }

        try{
            Socket socket = new Socket(serverIp, serverPort);
            // Open GUI
            window = new MainWindow(socket);
            window.run();
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Connection Refused.");
            System.exit(0);
        }
    }
}
