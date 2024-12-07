package UDP;

import org.junit.jupiter.api.Test;

import java.io.Console;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class UDPClientTest {

    @Test
    void testConstructor() {
        UDPClient client = new UDPClient("localhost", 8080);
        assertNotNull(client, "The client should be instantiated.");
    }

    // Test the encodeMessage method for short and long messages
    @Test
    void testEncodeMessage() {
        UDPClient client = new UDPClient("localhost", 8080);
        // Short message
        String message = "Hello";
        byte[] encodedMessage = client.encodeMessage(message);
        assertEquals(message.length(), encodedMessage.length, "Short message should encode correctly.");
        // Long message (greater than MAX_PACKET_SIZE)
        String longMessage = "A".repeat(2000);  // Message longer than MAX_PACKET_SIZE
        byte[] longEncodedMessage = client.encodeMessage(longMessage);
        assertEquals(1500, longEncodedMessage.length, "Long message should be truncated to MAX_PACKET_SIZE.");
    }


    @Test
    void testSendPacket() throws Exception {
        // Start a local server to receive the packet
        Thread serverThread = new Thread(() -> {
            try (DatagramSocket serverSocket = new DatagramSocket(8080)) {
                byte[] buffer = new byte[1500];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(packet);
                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                assertEquals("Test Message", receivedMessage);
            } catch (Exception e) {
                fail("Server error: " + e.getMessage());
            }
        });
        serverThread.start();
        // Allow server time to initialize
        Thread.sleep(1000);
        // Client sends a packet
        UDPClient client = new UDPClient("localhost", 8080);
        DatagramSocket clientSocket = new DatagramSocket();
        byte[] message = "Test Message".getBytes();
        client.sendPacket(clientSocket, message);
        clientSocket.close();
    }

    @Test
    void testStartWithMockConsole() throws Exception {
        // Start a local server
        Thread serverThread = new Thread(() -> {
            try (DatagramSocket serverSocket = new DatagramSocket(8080)) {
                byte[] buffer = new byte[1500];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(packet);
                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                assertEquals("Mocked Message", receivedMessage);
            } catch (IOException e) {
                fail("Server error: " + e.getMessage());
            }
        });
        serverThread.start();

        // Allow server time to initialize
        Thread.sleep(1000);

        // Mock Console
        Console mockedConsole = mock(Console.class);
        when(mockedConsole.readLine())
                .thenReturn("Mocked Message", "exit");

        // Inject mocked console into UDPClient
        UDPClient client = new UDPClient("localhost", 8080) {
            @Override
            protected Console getConsole() {
                return mockedConsole;
            }
        };
        client.start();
        serverThread.join();
    }

    @Test
    void testMain() throws Exception {
        // Start a local server to simulate receiving messages
        Thread serverThread = new Thread(() -> {
            try (DatagramSocket serverSocket = new DatagramSocket(8080)) {
                byte[] buffer = new byte[1500];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(packet);
                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                assertEquals("Hello from Main", receivedMessage);
            } catch (IOException e) {
                fail("Server error: " + e.getMessage());
            }
        });
        serverThread.start();
        // Allow server time to initialize
        Thread.sleep(1000);
        // Simulate `main` method execution
        Thread clientThread = new Thread(() -> {
            String[] args = {"localhost", "8080"};
            // Simulate user input for the client
            System.setIn(new java.io.ByteArrayInputStream("Hello from Main\nexit\n".getBytes()));
            assertDoesNotThrow(() -> UDPClient.main(args));
        });
        clientThread.start();

        // Wait for threads to complete
        clientThread.join();
        serverThread.join();
    }
}
