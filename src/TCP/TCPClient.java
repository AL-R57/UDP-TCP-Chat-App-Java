package TCP;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPClient {
    private String serverAddress;
    private int serverPort;

    public TCPClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    /**
     * javadoc for start function
     */
    public void start() {
        try (Socket clientSocket = new Socket(serverAddress,serverPort)) {
            Console console = System.console();
            if (console == null) {
                System.err.println("No console available.");
                return;
            }
            System.out.println("What is your message? Press Enter to send ('exit' to quit):");

            InputStream from_server = clientSocket.getInputStream();
            OutputStream to_server = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(from_server));

            PrintWriter out = new PrintWriter(to_server, true);
            while (true){
                String userInput = console.readLine();
                if ("exit".equalsIgnoreCase(userInput) | userInput == null) {
                    System.out.println("Exiting client.");
                    break;
                }
                String data_to_send = new String(userInput.getBytes(StandardCharsets.UTF_8));
                out.println(data_to_send);
                String data_in = in.readLine();
                System.out.println("Message sent to " + serverAddress + ":" + serverPort);
                System.out.println("Client@"+serverAddress+":"+serverPort+" - "+data_in);

            }

        } catch (IOException e) {
            System.err.println("Error in TCP Client: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java TCPClient <server-address> <server-port>");
            return;
        }
        String serverAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);
        TCP.TCPClient client = new TCP.TCPClient(serverAddress, serverPort);
        client.start();
    }
}
