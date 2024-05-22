import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientThread extends Thread {
    Socket socket;
    Server server;
    PrintWriter output;

    ClientThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(),true); //parametr true powoduje automatyczne opróznianie bufora po każdym wywołaniu println;
            String msg;
            while((msg = input.readLine()) != null) {
                System.out.println("Received: " + msg);
                server.broadcast(msg);
            }
            System.out.println("client closed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void send(String msg) {
        //this.output.write(msg);
        this.output.println(msg);
    }
}
