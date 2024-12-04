package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

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

    public void launch() throws IOException {
        DatagramSocket serverSocket = null;
        try {
            this.state = "Running";
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


    public static void main(String[] args) throws IOException {
        int server_port;
        if (args.length < 1) {
            server_port = UDPServer.DEFAULT_PORT;
        } else{
            server_port = Integer.parseInt(args[0]);
        }
        UDPServer udpServer = new UDPServer(server_port);
        System.out.println(udpServer.toString());
        udpServer.launch();
        System.out.println(udpServer.toString());
    }
}
