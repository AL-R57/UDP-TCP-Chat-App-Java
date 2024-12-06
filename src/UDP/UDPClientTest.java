package UDP;

import org.junit.jupiter.api.Test;

import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static jdk.internal.vm.compiler.word.LocationIdentity.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Console;
import java.io.IOException;
import java.net.DatagramSocket;

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


    // Test the sendPacket method (mock DatagramSocket)
    @Test
    void testSendPacket() throws IOException {
        UDPClient client = new UDPClient("localhost", 8080);
        DatagramSocket mockSocket = mock(DatagramSocket.class);

        byte[] dataToSend = "Test Message".getBytes();
        client.sendPacket(mockSocket, dataToSend);

        // Verify that the send() method was called on the mock socket
        verify(mockSocket).send(any());
    }

    // Test for the main method - No need to test deeply, just check execution
    @Test
    void testMain() {
        String[] args = {"localhost", "8080"};
        // We won't assert anything but just ensure that it runs without exception
        assertDoesNotThrow(() -> UDPClient.main(args));
    }
}
