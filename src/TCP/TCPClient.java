package TCP;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class TCPClient {
    private String serverAddress;
    private int serverPort;
    private static final int MAX_PACKET_SIZE = 1500; //MTU value for ethernet

    public TCPClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }
    public void start() {
//        try (DatagramSocket clientSocket = new DatagramSocket()) {
//            Console console = System.console();
//            if (console == null) {
//                System.err.println("No console available. Make sure to run in a console environment.");
//                return;
//            }
//            System.out.println("Type your message and press Enter to send (type 'exit' to quit):");
//            while (true) {
//                String userInput = console.readLine();
//
//                if ("exit".equalsIgnoreCase(userInput)) {
//                    System.out.println("Exiting client.");
//                    break;
//                }
//                byte[] dataToSend = userInput.getBytes("UTF-8");
//                if (dataToSend.length > MAX_PACKET_SIZE) {
//                    System.out.println("Message too long. It has been truncated to "+MAX_PACKET_SIZE+" bytes.");
//                    dataToSend = new byte[MAX_PACKET_SIZE];
//                    System.arraycopy(userInput.getBytes("UTF-8"), 0, dataToSend, 0, MAX_PACKET_SIZE);
//                }
//                InetAddress serverInetAddress = InetAddress.getByName(serverAddress);
//                DatagramPacket packet = new DatagramPacket(dataToSend, dataToSend.length, serverInetAddress, serverPort);
//                clientSocket.send(packet);
//                System.out.println("Message sent to " + serverAddress + ":" + serverPort);
//            }
//        } catch (IOException e) {
//            System.err.println("Error in UDP Client: " + e.getMessage());
//        }
    }
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java UDPClient <server-address> <server-port>");
            return;
        }
        String serverAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);
        UDP.UDPClient client = new UDP.UDPClient(serverAddress, serverPort);
        client.start();
    }
}
