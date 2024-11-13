package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    private String state;
    private int port;
    public static int DEFAULT_PORT = 4038; //verify with "sudo netstat -tuln"
    public static int MAX_PACKET_SIZE = 1500; //MTU value for ethernet

    public UDPClient(int serv_listening_port) {
        this.port = serv_listening_port;
        this.state = "Open";
    }
    public UDPClient() {
        this.port = DEFAULT_PORT;
        this.state = "Open";
    }

    public void lauch() throws IOException {
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

                String data_received = new String(datagramPacket.getData(), 0, datagramPacket.getLength(), "UTF-8");
                System.out.println("Received from "+clientAddress+":"+clientPort+" - "+data_received);
            }
        } finally {
            if(serverSocket != null && !serverSocket.isClosed()){
                serverSocket.close();
                this.state = "Close";
            }
        }
    }

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
            server_port = UDPClient.DEFAULT_PORT;
        } else{
            server_port = Integer.parseInt(args[0]);
        }

        UDPClient udpServer = new UDPClient(server_port);
        udpServer.toString();
        udpServer.lauch();
    }
}


//use "netstat -latupn"



