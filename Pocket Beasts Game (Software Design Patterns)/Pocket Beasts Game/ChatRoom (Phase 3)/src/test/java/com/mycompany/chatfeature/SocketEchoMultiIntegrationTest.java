/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatfeature;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

/**
 *
 * @author Daniel
 */
public class SocketEchoMultiIntegrationTest {

    private static int port;

    @BeforeAll
    public static void start() throws InterruptedException, IOException {

        // Take an available port
        ServerSocket s = new ServerSocket(0);
        port = s.getLocalPort();
        s.close();

        Executors.newSingleThreadExecutor().submit(() -> new EchoMultiServer().start(port));
        Thread.sleep(2000);
    }
        
    /**
     * Testing with a string and terminate with "."
     */
    @Test
    public void givenClient1_whenServerResponds_thenCorrect() {
        EchoClient client = new EchoClient();
        client.startConnection("127.0.0.1", port);
        String msg1 = client.sendMessage("First Test");
        String msg2 = client.sendMessage("I'm Daniel");
        String terminate = client.sendMessage(".");

        assertEquals(msg1, "First Test");
        assertEquals(msg2, "I'm Daniel");
        assertEquals(terminate, "End chat");
        client.stopConnection();
    }

    /**
     * Testing empty string and terminate with "."
     */
    @Test
    public void givenClient2_whenServerResponds_thenCorrect() {
        EchoClient client = new EchoClient();
        client.startConnection("127.0.0.1", port);
        String msg1 = client.sendMessage("Third Test");
        String msg2 = client.sendMessage("");
        String terminate = client.sendMessage(".");
        assertEquals(msg1, "Third Test");
        assertEquals(msg2, "");
        assertEquals(terminate, "End chat");
        client.stopConnection();
    }
}
