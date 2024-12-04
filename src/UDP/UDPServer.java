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

    /**
     * UDPServer constructor
     * @param serv_listening_port
     */
    public UDPServer(int serv_listening_port) {
        this.port = serv_listening_port;
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
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     *
     */
    public void launch() throws IOException {
        DatagramSocket serverSocket = null;
        try {
            this.state = "Running";
            System.out.println(state);
            serverSocket = new DatagramSocket(port);
            byte[] buf = new byte[MAX_PACKET_SIZE];
            while(true) {
                DatagramPacket datagramPacket = new DatagramPacket(buf, MAX_PACKET_SIZE);
                serverSocket.receive(datagramPacket);

                InetAddress clientAddress = datagramPacket.getAddress();
                int clientPort = datagramPacket.getPort();

                String data_received = new String(datagramPacket.getData(), 0, datagramPacket.getLength(), StandardCharsets.UTF_8);
                System.out.println("Received from "+clientAddress+":"+clientPort+" - "+data_received+"\n");
            }
        } finally {
            if(serverSocket != null || !serverSocket.isClosed()){
                serverSocket.close();
                this.state = "Close";
                System.out.println(state);
            }
        }
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
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * @param args
     * @throws IOException
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
