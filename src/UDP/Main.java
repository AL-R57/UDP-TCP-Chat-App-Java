package UDP;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or

import java.io.IOException;

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {

        int server_port;
        if (args.length < 1) {
            server_port = UDPServer.DEFAULT_PORT;
        } else{
            server_port = Integer.parseInt(args[0]);
        }

        UDPServer udpServer = new UDPServer(server_port);
        udpServer.toString();
        udpServer.lauch();
    }
}