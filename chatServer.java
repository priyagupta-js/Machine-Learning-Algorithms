// Chat Server:
import java.io.*;
import java.net.*;
import java.util.*;

public class chatServer {
    private static final int PORT = 5000;
    private static List<PrintWriter> clients = new ArrayList<>();

    public static void main(String[] args) {
    System.out.println("Chat Server started on port"+ PORT);
    try(ServerSocket serverSocket = new ServerSocket(PORT))
    {
while(true){
Socket clientSocket = serverSocket.accept();
System.out.println("New client connected:" + clientSocket.getInetAddress());

PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
synchronized(clients)
{
clients.add(out);
}

//Start a thread to handle this client

Thread clientThread = new Thread(new ClientHandler(clientSocket,out));
clientThread.start();
}
}
catch(IOException e)
{
System.err.println("Server error:" + e.getMessage());
}
}

//Broadcast message to all clients

private static void broacast(String message)
{
synchronized(clients)
{
for (PrintWriter client: clients)
{
client.println(message);
}
}
}

 // Inner class to handle each client
    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;

        public ClientHandler(Socket socket, PrintWriter out) {
            this.socket = socket;
            this.out = out;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    broadcast(message);
                }
            } catch (IOException e) {
                System.err.println("Client error: " + e.getMessage());
            } finally {
                synchronized (clients) {
                    clients.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error closing socket: " + e.getMessage());
                }
                System.out.println("Client disconnected: " + socket.getInetAddress());
            }
        }
    }
}
Chat client:
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_IP = "127.0.0.1"; // Localhost for testing
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            System.out.println("Connected to server at " + SERVER_IP + ":" + SERVER_PORT);

            // Thread to receive messages from server
            Thread receiver = new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("Received: " + message);
                    }
                } catch (IOException e) {
                    System.err.println("Receiver error: " + e.getMessage());
                }
            });
            receiver.start();

            // Main thread to send messages
            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 Scanner scanner = new Scanner(System.in)) {
                System.out.println("Enter messages (type 'exit' to quit):");
                String userInput;
                while (true) {
                    userInput = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(userInput)) {
                        break;
                    }
                    out.println(userInput);
                }
            }
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
        System.out.println("Disconnected from server.");
    }
}

