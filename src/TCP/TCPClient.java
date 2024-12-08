package TCP;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * TCPClient connects to a TCPServer and exchanges messages.
 * @see TCPServer
 */
public class TCPClient {
    private String serverAddress;
    private int serverPort;
    private static final int MAX_PACKET_SIZE = 1500; //MTU value for ethernet

    public TCPClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    /**
     * Starts the client, allowing the user to send messages to the server and receive echoes.
     */
    public void start() {
        try (Socket clientSocket = new Socket(serverAddress, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            Console console = System.console();
            if (console == null) {
                System.err.println("No console available.");
                return;
            }
            System.out.println("What is your message? Press Enter to send ('exit' to quit):");
            while (true) {
                String userInput = console.readLine();
                if ("exit".equalsIgnoreCase(userInput)) {
                    System.out.println("Exiting client.");
                    break;
                }
                byte[] dataToSend = userInput.getBytes(StandardCharsets.UTF_8);
                if (dataToSend.length > MAX_PACKET_SIZE) {
                    System.out.println("Message too long. It has been truncated to " + MAX_PACKET_SIZE + " bytes.");
                    userInput = new String(dataToSend, 0, MAX_PACKET_SIZE, StandardCharsets.UTF_8);
                }

                // Send message to the server
                out.println(userInput);
                System.out.println("Message sent to " + serverAddress + ":" + serverPort);

                // Receive response from the server
                String response = in.readLine();
                if (response != null) {
                    System.out.println("Received from " + serverAddress + ":" + serverPort + " - " + response);
                }
            }
        } catch (IOException e) {
            System.err.println("Error in TCP Client: " + e.getMessage());
        }
    }

    /**
     * Creates and launch a TCPClient with the given address linked to the given port
     * @param args <p>args[0] server_address</p><p>args[1] server_port</p>
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java TCPClient <server-address> <server-port>");
            return;
        }
        String serverAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);
        TCP.TCPClient client = new TCP.TCPClient(serverAddress, serverPort);
        client.start();
    }
}
