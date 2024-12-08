package TCP;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A TCP server that accepts client connections, receives messages, and echoes them back.
 * Messages are exchanged in lines of text. Each received message is echoed with the client's IP address.
 */
public class TCPServer {
    private String state;
    private final int port;
    public static int DEFAULT_PORT = 8080;

    /**
     * TCPServer constructor
     * @param servListeningPort port available to listen to UDP Client
     */
    public TCPServer(int servListeningPort) {
        this.port = servListeningPort;
        this.state = "Open";
    }

    /**
     * TCPServer default constructor
     */
    public TCPServer() {
        this.port = DEFAULT_PORT;
        this.state = "Open";
    }

    /**
     * Launches the TCP server, accepts client connections, and processes messages.
     */
    public void launch() throws IOException {
        ServerSocket serverSocket = null;
        try {
            this.state = "Running";
            serverSocket = new ServerSocket(port);
            System.out.println("Server running on port: " + port);
            while (true) {
                Socket clientSocket = acceptClient(serverSocket);
                processClientConnection(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Error in TCP Server: " + e.getMessage());
        } finally {
            closeServer(serverSocket);
        }
    }

    /**
     * Wait for Client connection
     */
    private Socket acceptClient(ServerSocket serverSocket) throws IOException {
        Socket socket = serverSocket.accept();
        InetAddress clientAddress = socket.getInetAddress();
        int clientPort = socket.getPort();
        System.out.println("Client connected: " + clientAddress + ":" + clientPort);
        return socket;
    }

    /**
     * Process the client connection
     */
    private void processClientConnection(Socket socket) throws IOException {
        InetAddress clientAddress = socket.getInetAddress();
        int clientPort = socket.getPort();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            System.out.println("Connected to Client@" + clientAddress + ":" + clientPort);

            // Read and echo messages
            String message;
            while ((message = in.readLine()) != null) { //Wait for complete lines
                System.out.println("Client@" + clientAddress + ":" + clientPort + " - " + message);
                out.println("Echo: " + message); //Echo the message back to the client
            }
        } catch (IOException e) {
            System.err.println("Error handling client@" + clientAddress + ":" + clientPort + ": " + e.getMessage());
        } finally {
            socket.close();
        }
    }

    /**
     * Close the server
     */
    private void closeServer(ServerSocket serverSocket) {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                this.state = "Close";
                System.out.println("Server closed.");
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "TCPServer{" +
                "state='" + state + '\'' +
                ", port=" + port +
                '}';
    }

    /**
     * Main method to start the TCP server. If no port is provided via command-line arguments, the server will use the default port (8080).
     */
    public static void main(String[] args) throws IOException {
            int server_port;
            if (args.length < 1) {
                server_port = TCP.TCPServer.DEFAULT_PORT;
            } else{
                server_port = Integer.parseInt(args[0]);
            }
            TCP.TCPServer tcpServer = new TCP.TCPServer(server_port);
            tcpServer.launch();
    }
}