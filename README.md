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


# README for UDP and TCP Chat Application with Multi-threading Support

---

## Project Overview

This project is a **Java-based Client-Server Chat Application** supporting both **UDP** and **TCP** communication protocols. The objective is to demonstrate the use of Java sockets to create a simple chat application that can handle client requests and server responses. The project also includes multi-threading for handling multiple TCP connections simultaneously.

---

## Application Components

### 1. UDP Communication

#### 1.1 UDP Server (`UDPServer.java`)
The UDP server listens for incoming datagrams, processes the received data, and displays the messages prefixed with the client’s IP address.

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
