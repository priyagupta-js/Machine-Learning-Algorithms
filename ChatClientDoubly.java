
// Part (d): Chat Client with Configurable IP

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClientDoubly {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter server IP address: ");
        String SERVER_IP = scanner.nextLine();
        final int SERVER_PORT = 5000;

        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            System.out.println("Connected to server at " + SERVER_IP + ":" + SERVER_PORT);
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

            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                System.out.println("Enter messages (type 'exit' to quit):");
                String userInput;
                while (true) {
                    userInput = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(userInput)) break;
                    out.println(userInput);
                }
            }
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
        System.out.println("Disconnected from server.");
    }
}