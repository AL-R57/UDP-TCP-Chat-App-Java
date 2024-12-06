package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * A UDP server which retrieves messages from the UDP client.
 * The server retrieves the UDP packets sent by the UDP client and echo them to the client.
 * @see UDPClient
 */
public class UDPServer {
    private String state;
    private final int port;
    private static final int DEFAULT_PORT = 8080;
    private static final int MAX_PACKET_SIZE = 1500; //MTU value for ethernet
    private static final int MAX_TIME_SERVER = 60000; //1 minute of inaction to close UDP server

    /**
     * UDPServer constructor
     * @param servListeningPort port available to listen to UDP Client
     */
    public UDPServer(int servListeningPort) {
        this.port = servListeningPort;
        this.state = "Open";
    }

    /**
     * UDPServer default constructor
     */
    public UDPServer() {
        this.port = DEFAULT_PORT;
        this.state = "Open";
    }

    /**
     * Launches the UDP server and begins listening for incoming messages.
     * Initializes the socket and delegates message processing to helper methods.
     */
    public void launch() throws IOException {
        DatagramSocket serverSocket = null;
        try {
            startServer();
            serverSocket = new DatagramSocket(port);
            listenForPackets(serverSocket);
        } finally {
            assert serverSocket != null;
            stopServer(serverSocket);
        }
    }

    /**
     * Updates the state of the server in Running
     */
    private void startServer() {
        this.state = "Running";
        System.out.println(state);
    }

    /**
     * Listens to server port and process the received datagram.
     */
    private void listenForPackets(DatagramSocket serverSocket) throws IOException{
        long lastActiveTime = System.currentTimeMillis(); //the time when the server receive a packet
        byte[] buf = new byte[MAX_PACKET_SIZE];
        while(true) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - lastActiveTime;
            if (elapsedTime >= MAX_TIME_SERVER) {
                System.out.println("Server timed out after " + MAX_TIME_SERVER/1000 + " seconds of inactivity. Shutting down...");
                break;
            }
            // Set the socket timeout to allow periodic checks for inactivity
            serverSocket.setSoTimeout((int) (MAX_TIME_SERVER - elapsedTime)); // Remaining time until timeout
            try {
                DatagramPacket datagramPacket = new DatagramPacket(buf, MAX_PACKET_SIZE);
                serverSocket.receive(datagramPacket); // Blocking call, waits for a packet
                processReceivedPacket(datagramPacket);
                lastActiveTime = System.currentTimeMillis(); // Reset timer on activity
            } catch (java.net.SocketTimeoutException e) {
                System.err.println("No packet received within the socket timeout"); //loop back to check inactivity
            }
        }
    }

    /**
     * Processes the received packet
     */
    private void processReceivedPacket(DatagramPacket datagramPacket){
        InetAddress clientAddress = datagramPacket.getAddress();
        int clientPort = datagramPacket.getPort();
        String dataReceived = new String(datagramPacket.getData(), 0, datagramPacket.getLength(), StandardCharsets.UTF_8);
        System.out.println("Received from "+clientAddress+":"+clientPort+" - "+dataReceived+"\n");
    }

    /**
     * Stop the server and close the socket
     */
    private void stopServer(DatagramSocket  serverSocket){
        serverSocket.close();
        this.state = "Close";
        System.out.println(state);
    }

    /**
     * @return the state of the UDPServer and its port
     */
    @Override
    public String toString() {
        return "UDPServer{" +
                "state='" + state + '\'' +
                ", port=" + port +
                '}';
    }

    /**
     * Creates and launches a UDPServer with the specified port or a default port.
     */
    public static void main(String[] args) throws IOException {
        int server_port;
        if (args.length < 1) { //create a default UDP server if the user didn't precise the port
            server_port = UDPServer.DEFAULT_PORT;
        } else{
            server_port = Integer.parseInt(args[0]);
        }
        UDPServer udpServer = new UDPServer(server_port);
        System.out.println(udpServer); //we don't need to put udpServer.toString() to call the method toString of the UDPServer
        udpServer.launch();
        System.out.println(udpServer);
    }
}
