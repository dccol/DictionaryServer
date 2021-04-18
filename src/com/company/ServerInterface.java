package com.company;

import java.awt.*;
import javax.swing.*;

public class ServerInterface {

    private JFrame frame;
    private JPanel contentPane;

    private JButton closeButton;

    private DictionaryDataAccess dataAccess;
    private String filename;
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
    public ServerInterface(DictionaryDataAccess dataAccess, String filename) {
        this.dataAccess = dataAccess;
        this.filename = filename;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 350, 80);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Dictionary Server Interface");
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.BLUE);

        contentPane = new JPanel();
        frame.setContentPane(contentPane);
        contentPane.setLayout(null);

        closeButton = new JButton("Shut Down");
        closeButton.setBounds(10, 10, 120, 21);
        contentPane.add(closeButton);

        /** ACTIONS **/
        closeButton.addActionListener(arg0 -> {

            // Save state of dictionary and exit
            dataAccess.saveDictionaryToFile(filename);

            System.out.println("Shutting Down");
            System.exit(0);
        });
    }

}
