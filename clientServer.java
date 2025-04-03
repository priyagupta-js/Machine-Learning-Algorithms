// Chat client:
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class clientServer {
    private static final String SERVER_IP = "172.16.10.39"; // Localhost for testing
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

