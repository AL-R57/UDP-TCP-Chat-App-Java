package TCP;

import java.io.*;
import java.net.Socket;

/**
 * TCPClient connects to a TCPServer and exchanges messages.
 * @see TCPServer
 */
public class TCPClient {
    private String serverAddress;
    private int serverPort;

    /**
     * TCP constructor
     */
    public TCPClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    /**
     * Starts the client, allowing the user to send messages to the server and receive echoes.
     */
    public void start() {
        try (Socket clientSocket = createSocket();
             BufferedReader in = createInputStream(clientSocket);
             PrintWriter out = createOutputStream(clientSocket)) {

            Console console = getConsole();
            if (console == null) return;

            while (true) {
                String userInput = readUserInput(console);
                if ("exit".equalsIgnoreCase(userInput) || userInput == null) {
                    System.out.println("Exiting client.");
                    break;
                }
                sendMessageToServer(out, userInput);
                String response = receiveMessageFromServer(in);
                printServerResponse(response);
            }
        } catch (IOException e) {
            System.err.println("Error in TCP Client: " + e.getMessage());
        }
    }

    /**
     * Initializes a socket connection to the server.
     */
    private Socket createSocket() throws IOException {
        System.out.println("Connecting to server at " + serverAddress + ":" + serverPort);
        return new Socket(serverAddress, serverPort);
    }

    /**
     * Returns the console object for reading user input.
     */
    private Console getConsole() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available.");
        }
        return console;
    }

    private BufferedReader createInputStream(Socket clientSocket) throws IOException {
        return new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    private PrintWriter createOutputStream(Socket clientSocket) throws IOException {
        return new PrintWriter(clientSocket.getOutputStream(), true);
    }

    /**
     * Reads user input from the console.
     */
    private String readUserInput(Console console) {
        return console.readLine();
    }

    private void sendMessageToServer(PrintWriter out, String message) {
        out.println(message);
        System.out.println("Message sent to " + serverAddress + ":" + serverPort);
    }

    private String receiveMessageFromServer(BufferedReader in) {
        try {
            return in.readLine();
        } catch (IOException e) {
            System.err.println("Error receiving message from server: " + e.getMessage());
            return null;
        }
    }

    /**
     * Prints the server's response in the client terminal.
     */
    private void printServerResponse(String response) {
        if (response != null) {
            System.out.println("Received from " + serverAddress + ":" + serverPort + " - " + response);
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
