import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    ServerSocket listenerSocket;
    List<ClientThread> clientThreads=new ArrayList<>();;

    Server(int port){
        //clientThreads = new ArrayList<>();
        try {
            listenerSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void listen(){
        System.out.println("Listening...");
        while(true) {
            Socket clientSocket = null;
            try {
                clientSocket = listenerSocket.accept();
                System.out.println("New client connected");
                ClientThread clientThread = new ClientThread(clientSocket, this);
                clientThreads.add(clientThread);
                clientThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcast(String msg) {
        for (ClientThread client : clientThreads) {
            client.send(msg);
        }
    }
}
