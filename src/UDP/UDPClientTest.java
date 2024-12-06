package UDP;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static org.junit.jupiter.api.Assertions.*;

class UDPClientTest {

    @Test
    void start() throws Exception {
        // Simulate a server to receive messages
        int serverPort = 9090;
        try (DatagramSocket serverSocket = new DatagramSocket(serverPort)) {
            // Run the client in a separate thread
            Thread clientThread = new Thread(() -> {
                try {
                    // Redirect System.in to simulate user input
                    String simulatedInput = "Hello, Server\nexit\n";
                    InputStream input = new ByteArrayInputStream(simulatedInput.getBytes());
                    System.setIn(input);
                    UDPClient client = new UDPClient("localhost", serverPort);
                    client.start();
                } catch (Exception e) {
                    fail("Client threw an exception: " + e.getMessage());
                }
            });
            clientThread.start();
            // Server-side: Wait for packets
            byte[] buffer = new byte[1500];
            DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(receivedPacket);
            String receivedMessage = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
            assertEquals("Hello, Server", receivedMessage);
            clientThread.join(); // Ensure the client thread finishes
        }
    }

    @Test
    void main() {
        // Test the main method to ensure it starts the client correctly
        String[] args = {"localhost", "9090"};
        assertDoesNotThrow(() -> UDPClient.main(args));
    }
}
