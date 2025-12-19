/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatfeature;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Daniel
 */
public class ChatRoomGUI extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private EchoClient echoClient;

    public ChatRoomGUI() {
        /**
         * Create UI for the chat room
         */
        setTitle("Chat Room");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText();
                sendMessage(message);
                inputField.setText("");
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(inputPanel, BorderLayout.SOUTH);

        /**
         * Run Server and Client classes
         */
        echoClient = new EchoClient();
        echoClient.startConnection("localhost", 5555); // Change this to your server IP and port
        new Thread(new ServerListener()).start();
    }

    /**
     * Message to be sent by user and displayed on chat room
     * @param message 
     */
    private void sendMessage(String message) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                String response = echoClient.sendMessage(message);
                appendToChatArea("You: " + message);
                if (response != null) {
                    appendToChatArea("Server: " + response);
                }
                return null;
            }
        };
        worker.execute();
    }

    private void appendToChatArea(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                chatArea.append(message + "\n");
            }
        });
    }

    /**
     * Echos the input from user to be displayed on the whole Server
     */
    private class ServerListener implements Runnable {
        public void run() {
            try {
                while (true) {
                    String message = echoClient.receiveMessage();
                    if (message != null) {
                        appendToChatArea("Server: " + message);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Main class in GUI (Server needs to be running first to work)
     * @param args 
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChatRoomGUI().setVisible(true);
            }
        });
    }
}