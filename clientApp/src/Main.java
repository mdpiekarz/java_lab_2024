import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        ClientThread senderThread = new ClientThread("localhost", 8080);
        senderThread.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            String msg = reader.readLine();
            senderThread.broadcast(msg);
        }
        //senderThread.send("Hellow World!");
    }
}