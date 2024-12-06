package UDP;

import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UDPServerTest {

    @Test
    void testDefaultConstructor() {
        UDPServer server = new UDPServer();
        assertEquals(8080, server.getPort(), "Default port should be 8080");
        assertEquals("Open", server.getState(), "Default state should be 'Open'");
    }

    @Test
    void testConstructor() {
        UDPServer server = new UDPServer(10000);
        assertEquals(10000, server.getPort(), "Server port should match the value passed to the constructor");
        assertEquals("Open", server.getState(), "Default state should be 'Open'");
    }

    @Test
    void testLaunchAndStopServer(){
        UDPServer server = new UDPServer(10000);
        Thread serverThread = new Thread(() -> {
            try {
                server.launch();
            } catch (IOException e) {
                fail("Server launch threw an unexpected exception: " + e.getMessage());
            }
        });
        serverThread.start();
        // Wait a moment for the server to start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Thread sleep interrupted");
        }
        assertEquals("Running", server.getState(), "State should be 'Running' after launch");
        // Simulate server shutdown
        serverThread.interrupt();
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            fail("Failed to join server thread");
        }
        assertEquals("Close", server.getState(), "State should be 'Close' after shutdown");
}

    @Test
    void testToString() {
        UDPServer server = new UDPServer(10000);
        String expectedString = "UDPServer{state='Open', port=10000}";
        assertEquals(expectedString, server.toString(), "toString should return correct server details");
    }
}
