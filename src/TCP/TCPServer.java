package TCP;

import java.io.*;
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
        try {
            this.state = "Running";
            serverSocket = new ServerSocket(port);
            while(true) {
                Socket socket = serverSocket.accept();

                InetAddress clientAddress = socket.getInetAddress();
                int clientPort = socket.getPort();

                InputStream data_server = socket.getInputStream();
                OutputStream data_client = socket.getOutputStream();
                BufferedInputStream in = new BufferedInputStream(data_server);
                PrintWriter out = new PrintWriter(data_client);
                //while connection établi lecture écriture
                System.out.println("Received from "+clientAddress+":"+clientPort+" - "+in+"\n");
                out.println("echo"+in);
                //
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