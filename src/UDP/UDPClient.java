package UDP;

import java.io.Console;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    private String serverAddress;
    private int serverPort;
    private static final int MAX_PACKET_SIZE = 1500; //MTU value for ethernet

    public UDPClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }
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
                byte[] data_to_send = userInput.getBytes("UTF-8");
                if (data_to_send.length > MAX_PACKET_SIZE) {
                    System.out.println("Message is too long. It has been truncated to "+MAX_PACKET_SIZE+" bytes.");
                    data_to_send = new byte[MAX_PACKET_SIZE];
                    System.arraycopy(userInput.getBytes("UTF-8"), 0, data_to_send, 0, MAX_PACKET_SIZE);
                }
                InetAddress serverInetAddress = InetAddress.getByName(serverAddress);
                DatagramPacket packet = new DatagramPacket(data_to_send, data_to_send.length, serverInetAddress, serverPort);
                clientSocket.send(packet);
                System.out.println("Message sent to " + serverAddress + ":" + serverPort);
            }
        } catch (IOException e) {
            System.err.println("Error in UDP Client: " + e.getMessage());
        }
    }
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
