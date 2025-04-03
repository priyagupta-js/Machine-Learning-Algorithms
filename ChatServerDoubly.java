import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServerDoubly {
    private static final int PORT = 5000;
    private static List<PrintWriter> clients = new ArrayList<>();
    private static TransactionNode head = null;
    private static TransactionNode tail = null;
    private static int msgCounter =1;

    static class TransactionNode{
        String transactionID;
        String description;
        int amount;
        TransactionNode prev , next;

        TransactionNode(String id, String desc,int amt)
        {
            this.transactionID = id;
            this.description = desc;
            this.amount = amt;
        }
    }

    public static void main(String[] args) throws IOException
    {
        InetAddress ip = InetAddress.getLocalHost();
        System.out.println("Server IP :" + ip.getHostAddress());
        System.out.println("Chat server started on port" + PORT);

    

      try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                synchronized (clients) {
                    clients.add(out);
                }
                Thread clientThread = new Thread(new ClientHandler(clientSocket, out));
                clientThread.start();
            }
        }
    }
    

    static void addTransaction(String desc) {
        String id = "MSG" + String.format("%03d", msgCounter++);
        TransactionNode newNode = new TransactionNode(id, desc, desc.length());
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
    }

    static void broadcast(String message) {
        synchronized (clients) {
            for (PrintWriter client : clients) {
                client.println(message);
            }
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;

        ClientHandler(Socket socket, PrintWriter out) {
            this.socket = socket;
            this.out = out;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String message;
                while ((message = in.readLine()) != null) {
                    addTransaction(message);
                    broadcast(message);
                    processFraudulentTransactions();
                }
            } catch (IOException e) {
                System.err.println("Client error: " + e.getMessage());
            } finally {
                synchronized (clients) {
                    clients.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }
// }
// Part b :Detect and handle fraudulent message:
static void processFraudulentTransactions() {
    int THRESHOLD = 50;
    String[] suspiciousKeywords = {"fraud", "scam", "unauthorized"};
    TransactionNode current = head;
    while (current != null) {
        boolean isFraud = current.amount > THRESHOLD || 
                          Arrays.stream(suspiciousKeywords).anyMatch(current.description::contains);
        if (isFraud) {
            System.out.println("Fraudulent transaction: " + current.transactionID);
            List<String> deletedIDs = new ArrayList<>();
            TransactionNode temp = current.next;
            while (temp != null) {
                deletedIDs.add(temp.transactionID);
                temp = temp.next;
            }
            System.out.println("Deleted transactions: " + String.join(", ", deletedIDs));
            current.next = null;
            tail = current;
            if (current == head) head = null; // Edge case: fraud at start
            break;
        }
        current = current.next;
    }
}

// Report Remaining Transactions
// Add this method to ChatServer:
static void displayRemainingCount() {
    int count = 0;
    TransactionNode current = head;
    while (current != null) {
        count++;
        current = current.next;
    }
    System.out.println("Remaining transactions: " + count);
}
}
}
// // Call this after processFraudulentTransactions() in ClientHandler

