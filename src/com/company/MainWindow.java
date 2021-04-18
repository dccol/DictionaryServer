package com.company;

import org.json.JSONObject;

import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.font.TextAttribute;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

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
    private JTextArea insertWordMeanings;
    private JButton insertButton;
    private JTextArea insertResult;

    // Update
    private JLabel updateLabel;
    private JLabel updateWordLabel;
    private JLabel updateMeaningLabel;
    private JTextField updateWordField;
    private JTextArea updateMeaningField;
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
        Font font = queryLabel.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        queryLabel.setFont(font.deriveFont(attributes));
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
        queryResult.setBounds(10, 90, 270, 60);
        contentPane.add(queryResult);
        JScrollPane scrollPaneQueryResult = new JScrollPane( queryResult );
        scrollPaneQueryResult.setBounds(10, 90, 270, 60);
        scrollPaneQueryResult.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scrollPaneQueryResult);

        /** INSERT **/
        JLabel insertLabel = new JLabel("Insert");
        Font font2 = insertLabel.getFont();
        Map attributes2 = font2.getAttributes();
        attributes2.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        insertLabel.setFont(font.deriveFont(attributes2));
        insertLabel.setBounds(300, 160, 46, 13);
        contentPane.add(insertLabel);

        insertWordLabel = new JLabel("Word");
        insertWordLabel.setBounds(300, 180, 46, 13);
        contentPane.add(insertWordLabel);
        insertWordField = new JTextField();
        insertWordField.setBounds(300, 195, 96, 19);
        contentPane.add(insertWordField);
        insertWordField.setColumns(10);

        insertMeaningLabel = new JLabel("Meanings");
        insertMeaningLabel.setBounds(300, 215, 70, 13);
        contentPane.add(insertMeaningLabel);
        insertWordMeanings = new JTextArea();
        insertWordMeanings.setBounds(300, 230, 200, 50);
        JScrollPane scrollPane = new JScrollPane( insertWordMeanings );
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(300, 230, 270, 60);
        contentPane.add(scrollPane);
        //contentPane.add(insertWordMeanings);
        insertWordMeanings.setColumns(10);

        insertButton = new JButton("Insert");
        insertButton.setBounds(300, 290, 85, 21);
        contentPane.add(insertButton);

        insertResult = new JTextArea();
        insertResult.setBounds(300, 315, 270, 60);
        contentPane.add(insertResult);
        JScrollPane scrollPaneInsertResult = new JScrollPane( insertResult );
        scrollPaneInsertResult.setBounds(300, 315, 270, 60);
        scrollPaneInsertResult.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scrollPaneInsertResult);

        /** UPDATE **/
        JLabel updateLabel = new JLabel("Update");
        Font font3 = updateLabel.getFont();
        Map attributes3 = font3.getAttributes();
        attributes3.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        updateLabel.setFont(font.deriveFont(attributes3));
        updateLabel.setBounds(10, 160, 46, 13);
        contentPane.add(updateLabel);

        updateWordLabel = new JLabel("Word");
        updateWordLabel.setBounds(10, 180, 46, 13);
        contentPane.add(updateWordLabel);
        updateWordField = new JTextField();
        updateWordField.setBounds(10, 195, 96, 19);
        contentPane.add(updateWordField);
        updateWordField.setColumns(10);

        updateMeaningLabel = new JLabel("Meanings");
        updateMeaningLabel.setBounds(10, 215, 70, 13);
        contentPane.add(updateMeaningLabel);
        updateMeaningField = new JTextArea();
        updateMeaningField.setBounds(10, 230, 200, 50);
        JScrollPane scrollPane2 = new JScrollPane( updateMeaningField );
        scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane2.setBounds(10, 230, 200, 60);
        contentPane.add(scrollPane2);
        //contentPane.add(updateMeaningField);
        updateMeaningField.setColumns(10);

        updateButton = new JButton("Update");
        updateButton.setBounds(10, 290, 85, 21);
        contentPane.add(updateButton);

        updateResult = new JTextArea();
        updateResult.setBounds(10, 315, 270, 60);
        contentPane.add(updateResult);
        JScrollPane scrollPaneUpdateResult = new JScrollPane( updateResult );
        scrollPaneUpdateResult.setBounds(10, 315, 270, 60);
        scrollPaneUpdateResult.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scrollPaneUpdateResult);

        /** DELETE **/
        JLabel deleteLabel = new JLabel("Delete");
        Font font4 = deleteLabel.getFont();
        Map attributes4 = font4.getAttributes();
        attributes4.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        deleteLabel.setFont(font.deriveFont(attributes4));
        deleteLabel.setBounds(300, 10, 46, 13);
        contentPane.add(deleteLabel);

        deleteWordLabel = new JLabel("Word");
        deleteWordLabel.setBounds(300, 30, 46, 13);
        contentPane.add(deleteWordLabel);
        deleteWordField = new JTextField();
        deleteWordField.setBounds(300, 45, 96, 19);
        contentPane.add(deleteWordField);
        deleteWordField.setColumns(10);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(300, 65, 85, 21);
        contentPane.add(deleteButton);

        deleteResult = new JTextArea();
        deleteResult.setBounds(300, 90, 270, 60);
        contentPane.add(deleteResult);
        JScrollPane scrollPaneDeleteResult = new JScrollPane( deleteResult );
        scrollPaneDeleteResult.setBounds(300, 90, 270, 60);
        scrollPaneDeleteResult.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scrollPaneDeleteResult);

        /** ACTIONS
         *      Send Requests to the Server
         *      One request per socket
         *      One thread per request on the server **/
        queryButton.addActionListener(arg0 -> {
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
                queryResult.setText(null);

                // Format result
                String[] meanings = message.replace("[", "").replace("]", "").split(", ");
                for(String meaning : meanings){
                    queryResult.append(meaning + "\n");

                }
            } catch (IOException e) {
                queryResult.setText("Error: The Client has lost connection to the Server");
            }
        });

        insertButton.addActionListener(arg0 -> {
            // Open a socket per request
            try(Socket socket = new Socket(serverIP, serverPort))
            {
                // Output and Input Stream
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                // Send the client request ie. Query, Insert, Delete, Update.
                JSONObject obj = new JSONObject();

                String[] meanings  = insertWordMeanings.getText().split("\n");

                obj.put("Word", insertWordField.getText());
                obj.put("Meanings", meanings);
                obj.put("Method", "Insert");

                System.out.println("Data sent to Server--> " + obj.toString());
                output.writeUTF(obj.toString());
                output.flush();

                // Read result from server
                String message = input.readUTF();
                insertResult.setText(null);
                insertResult.append(message + "\n");

            } catch (IOException e) {
                insertResult.setText("Error: The Client has lost connection to the Server");
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

                String[] meanings  = updateMeaningField.getText().split("\n");

                obj.put("Word", updateWordField.getText());
                obj.put("Meanings", meanings);
                obj.put("Method", "Update");

                System.out.println("Data sent to Server--> " + obj.toString());
                output.writeUTF(obj.toString());
                output.flush();

                // Read result from server
                String message = input.readUTF();
                updateResult.setText(null);
                updateResult.append(message + "\n");

            } catch (IOException e) {
                updateResult.setText("Error: The Client has lost connection to the Server");
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
                deleteResult.setText(null);
                deleteResult.append(message + "\n");

            } catch (IOException e) {
                deleteResult.setText("Error: The Client has lost connection to the Server");
            }
        });
    }

}
