package UDP;

import java.io.Console;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * A UDP client which sends messages to a UDP server.
 * @see UDPServer
 * The client reads messages from the console and sends them as UDP packets.
 */
public class UDPClient {
    private final String serverAddress;
    private final int serverPort;
    private static final int MAX_PACKET_SIZE = 1500; //MTU value for ethernet

    /**
     * UDPClient constructor
     * @param serverAddress
     * @param serverPort
     */
    public UDPClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    /**
     * Starts the UDP client and allows the user to send messages via the console.
     * Prompts the user for input and sends each message as a UDP packet.
     * The client exits when the user types "exit".
     */
    public void start() {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
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
                byte[] dataToSend = encodeMessage(userInput);
                sendPacket(clientSocket,dataToSend);
            }
        } catch (IOException e) {
            System.err.println("Error in UDP Client: " + e.getMessage());
        }
    }

    /**
     * Encodes the message to UTF-8 and truncates it if necessary
     * @param message the message to encode
     * @return the encoded message
     */
    private byte[] encodeMessage(String message) {
        byte[] dataToSend = message.getBytes(StandardCharsets.UTF_8);
        if (dataToSend.length > MAX_PACKET_SIZE) {
            System.out.println("Message is too long. It has been truncated to "+MAX_PACKET_SIZE+" bytes.");
            byte[] trunc_data_to_send = new byte[MAX_PACKET_SIZE];
            System.arraycopy(dataToSend, 0, dataToSend, 0, MAX_PACKET_SIZE);
            return trunc_data_to_send;
        }
        return dataToSend;
    }

    /**
     * Sends a packet to the server.
     * @param clientSocket the socket to use for sending the packet
     * @param dataToSend the data to send
     * @throws IOException if an I/O error occurs
     */
    private void sendPacket(DatagramSocket clientSocket, byte[] dataToSend) throws IOException {
        InetAddress serverInetAddress = InetAddress.getByName(serverAddress);
        DatagramPacket packet = new DatagramPacket(dataToSend, dataToSend.length, serverInetAddress, serverPort);
        clientSocket.send(packet);
        System.out.println("Message sent to " + serverAddress + ":" + serverPort);
    }

    /**
     * Creates and launch a UDPClient with the given address linked to the given port
     * @param args <p>args[0] server_address</p><p>args[1] server_port</p>
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java UDPClient <server-address> <server-port>");
            return;
        }
        String serverAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);
        UDPClient client = new UDPClient(serverAddress, serverPort);
        client.start();
    }
}
