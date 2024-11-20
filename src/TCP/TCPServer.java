package TCP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TCPServer {
    private String state;
    private int port;
    public static int DEFAULT_PORT = 8080;
    public static int MAX_PACKET_SIZE = 1500; //MTU value for ethernet

    public TCPServer(int serv_listening_port) {
        this.port = serv_listening_port;
        this.state = "Open";
    }
    public TCPServer() {
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

                String data_received = new String(datagramPacket.getData(), 0, datagramPacket.getLength(), "UTF-8");
                System.out.println("Received from "+clientAddress+":"+clientPort+" - "+data_received+"\n");
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
        return "TCPServer{" +
                "state='" + state + '\'' +
                ", port=" + port +
                '}';
    }
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