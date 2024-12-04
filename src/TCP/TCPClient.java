package TCP;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * TCPClient
 * @see TCPServer
 * @see TCPMultiServer
 * 
 */
public class TCPClient {
    private String serverAddress;
    private int serverPort;
    private static final int MAX_PACKET_SIZE = 1500; //MTU value for ethernet

    public TCPClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    /**
     * javadoc for start function
     * <p>
     *     ksjfglsdkjf
     * </p>
     * @see TCPServer
     */
    public void start() {
        try (Socket clientSocket = new Socket(serverAddress,serverPort)) {
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
                byte[] data_to_send = userInput.getBytes(StandardCharsets.UTF_8);
                if (data_to_send.length > MAX_PACKET_SIZE) {
                    System.out.println("Message too long. It has been truncated to "+MAX_PACKET_SIZE+" bytes.");
                    data_to_send = new byte[MAX_PACKET_SIZE];
                    System.arraycopy(userInput.getBytes(StandardCharsets.UTF_8), 0, data_to_send, 0, MAX_PACKET_SIZE);
                }

                InputStream data_client = clientSocket.getInputStream();
                OutputStream data_server = clientSocket.getOutputStream();
                BufferedInputStream in = new BufferedInputStream(data_client);
                PrintWriter out = new PrintWriter(data_server);
                System.out.println("Message sent to " + serverAddress + ":" + serverPort);
                //while connection établi lecture écriture
                System.out.println("Received from "+serverAddress+":"+serverPort+" - "+in+"\n");
                out.println(Arrays.toString(data_to_send));

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
