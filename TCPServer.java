import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TCPServer {
    static AtomicInteger messageCount = new AtomicInteger(0); //counter for the msg count

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(6789); //create a new server socket that listens on port 6789
        System.out.println("The TCP server is on.");

        while (true) {
            Socket clientSocket = serverSocket.accept(); //wait for a new cleint to connect to the server
            new Thread(new ClientHandler(clientSocket)).start(); //create a new thread to handle the client request
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            String clientSentence;
            try {
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

                while ((clientSentence = inFromClient.readLine()) != null) {
                    if (clientSentence.equals("exit")) {
                        break;
                    }

                    synchronized (messageCount) { //synchronize updating the message count
                        messageCount.incrementAndGet();
                        System.out.println("Message " + messageCount.get() + " received from client " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " - " + clientSentence);
                    }

                    String replyMessage = "Total messages received: " + messageCount.get() + "\n";
                    outToClient.writeBytes(replyMessage); //send the reply message to the client
                }

                clientSocket.close(); // close the client socket
                System.out.println("Connection closed by client " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
