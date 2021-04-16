package com.company;

import org.json.JSONObject;

import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainWindow {

    private JFrame frame;
    private JPanel contentPane;

    // Query
    private JLabel queryLabel;
    private JLabel queryWordLabel;
    private JTextField queryWordField;
    private JButton queryButton;
    private JTextArea queryResult;
    // Insert
    private JLabel insertLabel;
    private JLabel insertWordLabel;
    private JLabel insertMeaningLabel;
    private JTextField insertWordField;
    private JTextField insertWordMeanings;
    private JButton insertButton;
    private JTextArea insertResult;

    // Update
    private JLabel updateLabel;
    private JLabel updateWordLabel;
    private JLabel updateMeaningLabel;
    private JTextField updateWordField;
    private JTextField updateMeaningField;
    private JButton updateButton;
    private JTextArea updateResult;

    // Delete
    private JLabel deleteLabel;
    private JLabel deleteWordLabel;
    private JTextField deleteWordField;
    private JButton deleteButton;
    private JTextArea deleteResult;


    private String serverIP;
    private int serverPort;

    /**
     * Launch the window.
     */
    public void run() {
        try {
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the application.
     */
    public MainWindow(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 450);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Dictionary Server");
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.white);
        GridLayout layout = new GridLayout(2,2, 10, 10);

        contentPane = new JPanel();
        frame.setContentPane(contentPane);
        contentPane.setLayout(null);

        /** QUERY **/
        queryLabel = new JLabel("Query");
        queryLabel.setBounds(10, 10, 46, 13);
        contentPane.add(queryLabel);

        queryWordLabel = new JLabel("Word");
        queryWordLabel.setBounds(10, 30, 46, 13);
        contentPane.add(queryWordLabel);
        queryWordField = new JTextField();
        queryWordField.setBounds(10, 45, 96, 19);
        contentPane.add(queryWordField);
        queryWordField.setColumns(10);

        queryButton = new JButton("Query");
        queryButton.setBounds(10, 65, 85, 21);
        contentPane.add(queryButton);

        queryResult = new JTextArea();
        queryResult.setBounds(10, 90, 200, 21);
        contentPane.add(queryResult);

        /** INSERT **/
        JLabel insertLabel = new JLabel("Insert");
        insertLabel.setBounds(300, 10, 46, 13);
        contentPane.add(insertLabel);

        insertWordLabel = new JLabel("Word");
        insertWordLabel.setBounds(300, 30, 46, 13);
        contentPane.add(insertWordLabel);
        insertWordField = new JTextField();
        insertWordField.setBounds(300, 45, 96, 19);
        contentPane.add(insertWordField);
        insertWordField.setColumns(10);

        insertMeaningLabel = new JLabel("Meanings");
        insertMeaningLabel.setBounds(300, 70, 70, 13);
        contentPane.add(insertMeaningLabel);
        insertWordMeanings = new JTextField();
        insertWordMeanings.setBounds(300, 85, 200, 50);
        contentPane.add(insertWordMeanings);
        insertWordMeanings.setColumns(10);

        insertButton = new JButton("Insert");
        insertButton.setBounds(300, 140, 85, 21);
        contentPane.add(insertButton);

        insertResult = new JTextArea();
        insertResult.setBounds(300, 170, 200, 21);
        contentPane.add(insertResult);

        /** UPDATE **/
        JLabel updateLabel = new JLabel("Update");
        updateLabel.setBounds(10, 200, 46, 13);
        contentPane.add(updateLabel);

        updateWordLabel = new JLabel("Word");
        updateWordLabel.setBounds(10, 220, 46, 13);
        contentPane.add(updateWordLabel);
        updateWordField = new JTextField();
        updateWordField.setBounds(10, 235, 96, 19);
        contentPane.add(updateWordField);
        updateWordField.setColumns(10);

        updateMeaningLabel = new JLabel("Meanings");
        updateMeaningLabel.setBounds(10, 255, 70, 13);
        contentPane.add(updateMeaningLabel);
        updateMeaningField = new JTextField();
        updateMeaningField.setBounds(10, 270, 200, 50);
        contentPane.add(updateMeaningField);
        updateMeaningField.setColumns(10);

        updateButton = new JButton("Update");
        updateButton.setBounds(10, 325, 85, 21);
        contentPane.add(updateButton);

        updateResult = new JTextArea();
        updateResult.setBounds(10, 355, 200, 21);
        contentPane.add(updateResult);

        /** DELETE **/
        JLabel deleteLabel = new JLabel("Delete");
        deleteLabel.setBounds(300, 200, 46, 13);
        contentPane.add(deleteLabel);

        deleteWordLabel = new JLabel("Word");
        deleteWordLabel.setBounds(300, 220, 46, 13);
        contentPane.add(deleteWordLabel);
        deleteWordField = new JTextField();
        deleteWordField.setBounds(300, 235, 96, 19);
        contentPane.add(deleteWordField);
        deleteWordField.setColumns(10);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(300, 255, 85, 21);
        contentPane.add(deleteButton);

        deleteResult = new JTextArea();
        deleteResult.setBounds(300, 285, 200, 21);
        contentPane.add(deleteResult);

        queryButton.addActionListener(arg0 -> {
            System.out.println("Button clicked!" + arg0.toString());
            // Open a socket per request
            try(Socket socket = new Socket(serverIP, serverPort))
            {
                // Output and Input Stream
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                // Send the client request ie. Query, Insert, Delete, Update.
                JSONObject obj = new JSONObject();

                // Query test
                obj.put("Word", queryWordField.getText());
                obj.put("Method", "Query");

                System.out.println("Data sent to Server--> " + obj.toString());
                output.writeUTF(obj.toString());
                output.flush();

                // Read result from server
                String message = input.readUTF();
                queryResult.append(message + "\n");

            } catch (IOException e) {
                System.out.println("Exception sending data");
            }
        });

        insertButton.addActionListener(arg0 -> {
            System.out.println("Button clicked!" + arg0.toString());
            // Open a socket per request
            try(Socket socket = new Socket(serverIP, serverPort))
            {
                // Output and Input Stream
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                // Send the client request ie. Query, Insert, Delete, Update.
                JSONObject obj = new JSONObject();

                // Insert test
                obj.put("Word", insertWordField.getText());

                // Parse Meanings first
                obj.put("Meanings", insertWordMeanings.getText());
                obj.put("Method", "Insert");

                System.out.println("Data sent to Server--> " + obj.toString());
                output.writeUTF(obj.toString());
                output.flush();

                // Read result from server
                String message = input.readUTF();
                queryResult.append(message + "\n");

            } catch (IOException e) {
                System.out.println("Exception sending data");
            }
        });

        updateButton.addActionListener(arg0 -> {
            // Open a socket per request
            try(Socket socket = new Socket(serverIP, serverPort))
            {
                // Output and Input Stream
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                // Send the client request ie. Query, Insert, Delete, Update.
                JSONObject obj = new JSONObject();

                // Query test
                obj.put("Word", updateWordField.getText());

                // Parse meanings first
                obj.put("Meanings", updateMeaningField.getText());
                obj.put("Method", "Update");

                System.out.println("Data sent to Server--> " + obj.toString());
                output.writeUTF(obj.toString());
                output.flush();

                // Read result from server
                String message = input.readUTF();
                queryResult.append(message + "\n");

            } catch (IOException e) {
                System.out.println("Exception sending data");
            }
        });

        deleteButton.addActionListener(arg0 -> {
            // Open a socket per request
            try(Socket socket = new Socket(serverIP, serverPort))
            {
                // Output and Input Stream
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                // Send the client request ie. Query, Insert, Delete, Update.
                JSONObject obj = new JSONObject();

                // Query test
                obj.put("Word", deleteWordField.getText());
                obj.put("Method", "Delete");

                System.out.println("Data sent to Server--> " + obj.toString());
                output.writeUTF(obj.toString());
                output.flush();

                // Read result from server
                String message = input.readUTF();
                queryResult.append(message + "\n");

            } catch (IOException e) {
                System.out.println("Exception sending data");
            }
        });
    }

}
