# Create a UDP/TCP Chat App with Java sockets

# 1- Creating a UDP Client-Server

# 1.1 UDP Server

No argument
![image](https://github.com/user-attachments/assets/7bbcf6a8-6728-4b3c-baf2-e7917316cbda)

Port 10000 in argument
![image](https://github.com/user-attachments/assets/3bd547bc-5230-4a73-ab22-0c31857bd92c)


UDP server test
![image](https://github.com/user-attachments/assets/4bfcefde-9760-47ca-a30c-dc3b36499766)


![image](https://github.com/user-attachments/assets/607fd557-8dd3-46a4-8203-6a1eb4791210)

tests:
![image](https://github.com/user-attachments/assets/b21a319e-7ba2-4b4b-8473-ac9199dd307c)


![image](https://github.com/user-attachments/assets/611e2191-08bd-4ef2-bbac-65f9891150d5)


UDP def:
 UDPServer class representing a server that expects to receive datagrams from its clients containing strings encoded in ”UTF-8”. This server simply
 displays on the standard output of the received string prefixed with the client’s address. It
 is assumed that the server must accept strings whose size, once encoded; the server truncates the data received beyond this size.

 UDPClient class that reads the text lines entered by the user from the standard input and sends them, encoded in ”utf-8”, in a UDP datagram to
 the server specified in the argument on the command line.

TCP def:


modèle MVC



todo:

-test codes
-read javadoc
-derniere question
-MTU


# Create a UDP/TCP Chat App with Java sockets

---

## Project Overview

This project supports both **UDP** and **TCP** communication protocols and is a **Java-based Client-Server Chat Application**. The goal is to show how to build a basic chat application that can manage client requests and server responses using Java sockets. In order to manage several TCP connections at once, the project also incorporates multi-threading.

---

## Application Components

### 1. UDP Communication

#### 1.1 UDP Server (`UDPServer.java`)
After processing the data it receives and listening for incoming datagrams, the UDP server shows the messages that are preceded by the client's IP address.

- **Key Methods**:
  - `UDPServer(int port)`: Constructor to initialize the server with a specified port.
  - `UDPServer()`: Default constructor with a predefined port.
  - `launch()`: Starts the server to listen for client messages.
  - `main(String[] args)`: Allows launching with the command:  
    ```sh
    $ java UDPServer 8080
    ```

- **Usage**:  
  To test, use the `netcat` command in a separate terminal:
  ```sh
  $ nc -u localhost 8080

#### 1.2 UDP Client (`UDPClient.java`)

User input is transmitted over UDP from the UDP client to the server.

- **Key Methods**:
  - Reads user input from the console.
  - Sends the input as a UTF-8 encoded datagram to the server.

- **Usage**:
  - After starting the UDP server, run the UDP client using the following command:
    ```sh
    $ java UDPClient <hostname> <port>
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

---

### 2. TCP Communication

#### 2.1 TCP Server (`TCPServer.java`)

After connecting to a client, the TCP server receives messages and relays them back to the client, prefixing them with the IP address of the client.

- **Key Methods**:
  - `launch()`: 
    - Creates a `ServerSocket` to listen for incoming connections.
    - Accepts a client connection using the `accept()` method.
    - Reads data from the client and sends an echo response.

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

After connecting to the TCP server, the TCP client transmits messages and shows the server's echoed answers.

- **Key Methods**:
  - Reads user input from the console.
  - Sends the input as a UTF-8 encoded message to the server.
  - Reads the echoed response from the server and prints it to the console.

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
