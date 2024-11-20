package TCP;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private String state;
    private int port;
    public static int DEFAULT_PORT = 8080;

    public TCPServer(int serv_listening_port) {
        this.port = serv_listening_port;
        this.state = "Open";
    }
    public TCPServer() {
        this.port = DEFAULT_PORT;
        this.state = "Open";
    }

    public void launch() throws IOException {
        ServerSocket serverSocket = null;

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

        try {

            this.state = "Running";
            serverSocket = new ServerSocket(port);
            while(true) {
                Socket socket = serverSocket.accept();
                InetAddress clientAddress = socket.getInetAddress();
                int clientPort = socket.getPort();

                InputStream data_received = socket.getInputStream();
                System.out.println("Received from "+clientAddress+":"+clientPort+" - "+data_received+"\n");


            }
        } catch (IOException e) {
            System.err.println("Error in TCP Client: " + e.getMessage());
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