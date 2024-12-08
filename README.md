# Create a UDP/TCP Chat App with Java sockets

## Project Overview

This project supports both **UDP** and **TCP** communication protocols and is a **Java-based Client-Server Chat Application**. The goal is to show how to build a basic chat application that can manage client requests and server responses using Java sockets. In order to manage several TCP connections at once, the project also incorporates multi-threading.

---

## Application Components

### 1. UDP Communication

#### 1.1 UDP Server (`UDPServer.java`)

- **Definition**: 
  A server that receives datagrams from clients is represented by the `UDPServer` class. The server anticipates that strings encoded in **UTF-8** will be present in the datagrams. The client's address appears before each received string on the server's standard output. Each message can only be up to 1500 bytes in size. Any data that is larger than this is truncated.

- **Key Methods**:
  - `UDPServer(int servListeningPort)`: Constructor to initialize the server with a specified port.
  - `UDPServer()`: Default constructor with a default port (`8080`).
  - `launch()`: Starts the server, listens for client messages, and handles inactivity timeouts.
  - `startServer()`: Sets the server state to "Running" and logs it to the console.
  - `processReceivedPacket(DatagramPacket datagramPacket)`: Processes incoming packets and displays the client’s address and message.
  - `stopServer(DatagramSocket serverSocket)`: Stops the server, closes the socket, and sets the state to "Close."
  - `main(String[] args)`: Allows launching with the command:  
    ```sh
    $ java UDPServer 8080
    
- **Usage**:  
  Use the `netcat` command in a separate terminal to test:
  ```sh
  $ nc -u localhost 8080

- **UDPServerTest**:
  - All tests passed

#### 1.2 UDP Client (`UDPClient.java`)

- **Definition**:
  The `UDPClient` class reads user-inputted text lines from the standard input and transmits them to the server as **UTF-8** encoded datagrams. When launching the client, the address and port of the server are entered as command-line parameters.

- **Key Methods**:
  - `UDPClient(String hostname, int port)`: Constructor to initialize the client with the server’s hostname and port.
  - `encodeMessage(String message)`: Encodes the message into a byte array, truncating if necessary.
  - `sendPacket(DatagramSocket socket, byte[] message)`: Sends the encoded message as a UDP datagram to the server.
  - `start()`: Reads user input and sends messages to the server until "exit" is typed.
  - `main(String[] args)`: Allows launching with the command:  
    ```sh
    $ java UDPClient localhost 8080

- **Usage**:
  - After starting the UDP server, run the UDP client using the following command:
    ```sh
    $ java UDPClient <serverAddress> <serverPort>
    ```
    Example:
    ```sh
    $ java UDPClient localhost 8080
    ```

- **How It Works**:
  1. The client reads lines of text entered by the user.
  2. Each line is sent as a datagram to the server at the specified hostname and port.
  3. The server responds by printing the received message along with the client's IP.

- **Exiting**:
  - Type `exit` to terminate the client.

- **UDPClientTest**:
  - Doesn't work yet because of console interations
  - Need to be fixed to test the methods `start()` and `main(String[] args)`

---

### 2. TCP Communication

#### 2.1 TCP Server (`TCPServer.java`)

- **Definition**:
  A **TCP** connection is made to a client via the `TCPServer` class. After connecting, it sends back an echo response after receiving messages from the client and displaying them on the server console. The answer provides the original message prefixed with the client’s IP address. One client at a time is served by the server.

After connecting to a client, the TCP server receives messages and relays them back to the client, prefixing them with the IP address of the client.

- **Key Methods**:
  - `TCPServer(int servListeningPort)`: Constructor to initialize the server with a specified port.
  - `TCPServer()`: Default constructor with a default port (`8080`).
  - `launch()`: Starts the server, accepts client connections, and handles message exchange.
  - `acceptClient(ServerSocket serverSocket)`: Accepts a client connection and logs the client’s address and port.
  - `processClientConnection(Socket socket)`: Handles communication with a connected client, echoes received messages, and disconnects when the client stops sending data.
  - `closeServer(ServerSocket serverSocket)`: Closes the server socket and updates the server state to "Close."
  - `main(String[] args)`: Allows launching with the command:  
    ```sh
    $ java TCPServer 8080

- **Command to run**:
    ```sh
    $ java TCPServer <port>
    ```
    Example:
    ```sh
    $ java TCPServer 8080
    ```

- **How It Works**:
  1. The server waits for a client to connect.
  2. Once connected, it reads messages from the client.
  3. The server sends back an echoed response with the client’s IP address.
  4. The connection continues until the client disconnects.

#### 2.2 TCP Client (`TCPClient.java`)

- **Definition**:
  Using **TCP**, the `TCPClient` class establishes a connection with the server and transmits user-inputted text lines. It shows the echoed response from the server on the console after waiting for it. Until the user closes the input stream (using `\CTRL> + D`), the client keeps sending messages.

After connecting to the TCP server, the TCP client transmits messages and shows the server's echoed answers.

- **Key Methods**:
  - `start()`: synchronizes data transmission, response, input reading, and exit condition checks. assigns work to assistant techniques.
  - `createSocket()`: connects to the server via a socket connection.
  - `createInputStream()`: To read data from the server, a `BufferedReader` is created.
  - `createOutputStream()`: To communicate data to the server, a `PrintWriter` is created.
  - `getConsole()`: retrieves the `Console` so that user input may be read.
  - `readUserInput()`: takes in a line of user input.
  - `sendMessageToServer()`: transmits the message from the user to the server.
  - `receiveMessageFromServer()`: reads the response from the server.
  - `printServerResponse()`: prints the response from the server to the terminal.

- **Command to run**:
    ```sh
    $ java TCPClient <hostname> <port>
    ```
    Example:
    ```sh
    $ java TCPClient localhost 8080
    ```

- **How It Works**:
  1. The client connects to the server using the provided hostname and port.
  2. It reads user input line by line and sends each line to the server.
  3. The client prints the server's echoed response.
  4. The session ends when the user inputs `<CTRL> + D` or closes the standard input.

- **Exiting**:
  - Use `<CTRL> + D` to end the input and close the client connection.
