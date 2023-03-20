import java.io.*;
import java.net.*;

public class TCPClient {
    private static final int SERVER_PORT = 6789;
    private static final String SERVER_HOST = "localhost";

    public static void main(String argv[]) throws Exception {
        String sentence;
        String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket(SERVER_HOST, SERVER_PORT); //create a new client socket and connects to the server

        try {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println("The TCP client is on. Please enter your input:");

            while ((sentence = inFromUser.readLine()) != null) { //reads user input from the keyboard
                outToServer.writeBytes(sentence + '\n'); //send the input message to the server
                modifiedSentence = inFromServer.readLine(); //read the reply message from the server
                System.out.println("FROM SERVER: " + modifiedSentence); //print the reply message received from the server

                if (sentence.equals("exit")) { //if user input is exit then close the client socket after 1 second
                    Thread.sleep(1000);
                    break;
                }
            }

            clientSocket.close(); //close the client socket
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
