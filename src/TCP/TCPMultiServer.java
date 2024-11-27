package TCP;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPMultiServer {
    private String state;
    private int port;
    public static int DEFAULT_PORT = 8080;

    public TCPMultiServer(int serv_listening_port) {
        this.port = serv_listening_port;
        this.state = "Open";
    }
    public TCPMultiServer() {
        this.port = DEFAULT_PORT;
        this.state = "Open";
    }

    public void launch() throws IOException {
        ServerSocket serverSocket = null;
        try {
            this.state = "Running";
            serverSocket = new ServerSocket(port);
            while (true){
                Socket socket = serverSocket.accept();

                InetAddress clientAddress = socket.getInetAddress();
                int clientPort = socket.getPort();

                InputStream from_client = socket.getInputStream();
                OutputStream to_client = socket.getOutputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(from_client));
                PrintWriter out = new PrintWriter(to_client,true);
                while(true){
                    String data_to_print = in.readLine();
                    if (data_to_print == null){
                        socket.close();
                        System.out.println("Client@"+clientAddress+":"+clientPort+" Disconnected");
                        break;
                    }
                    System.out.println("Client@"+clientAddress+":"+clientPort+" - "+data_to_print);
                    out.println(data_to_print);
                }
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
        return "TCPMultiServer{" +
                "state='" + state + '\'' +
                ", port=" + port +
                '}';
    }
    public static void main(String[] args) throws IOException {

        int server_port;
        if (args.length < 1) {
            server_port = TCP.TCPMultiServer.DEFAULT_PORT;
        } else{
            server_port = Integer.parseInt(args[0]);
        }

        TCP.TCPMultiServer TCPMultiServer = new TCP.TCPMultiServer(server_port);
        TCPMultiServer.launch();
    }
}