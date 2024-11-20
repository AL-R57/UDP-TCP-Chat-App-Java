package UDP;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage:");
            System.err.println("To start the server: java Main server <port>");
            System.err.println("To start the client: java Main client <server-address> <port>");
            return;
        }

        String role = args[0].toLowerCase();

        try {
            switch (role) {
                case "server":
                    // Start the UDPServer
                    int serverPort = (args.length > 1) ? Integer.parseInt(args[1]) : UDPServer.DEFAULT_PORT;
                    UDPServer server = new UDPServer(serverPort);
                    System.out.println("Starting UDPServer on port: " + serverPort);
                    server.launch();
                    break;

                case "client":
                    // Start the UDPClient
                    if (args.length < 3) {
                        System.err.println("Usage for client: java Main client <server-address> <port>");
                        return;
                    }
                    String serverAddress = args[1];
                    int clientPort = Integer.parseInt(args[2]);
                    UDPClient client = new UDPClient(serverAddress, clientPort);
                    System.out.println("Starting UDPClient connecting to " + serverAddress + " on port: " + clientPort);
                    client.launch();
                    break;

                default:
                    System.err.println("Unknown role: " + role);
                    System.err.println("Use 'server' to start a server or 'client' to start a client.");
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}