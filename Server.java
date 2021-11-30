import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server
 *
 * This program gets the connection and passes it to the serverThread class so it can receive input
 * from the user. It also handles the creation of those threads
 *
 * @author Hudson Reamer, Lucas Mazza, Supriya Dixit, CS 18000
 * @version December 6, 2020
 *
 */

public class Server {
    static final int PORT = 6174; //Kaprekar's routine final number

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {
            Socket socket = serverSocket.accept();

            new ServerThread(socket).start();
        }
    }
}

