package ServerManager;

import DatabaseManager.DatabaseOperator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;
    public static final String EXIT_PROGRAM = "exit";
    private DatabaseOperator databaseOperator = new DatabaseOperator();

    public ServerThread (Socket socket){
        this.socket = socket;
    }

    public void run(){
        try {
            BufferedReader getDataFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter setDataToClient = new PrintWriter(socket.getOutputStream(), true);

            String dataFromClient;
            String responseToClient;

            while (true){
                dataFromClient = getDataFromClient.readLine();
System.out.println(dataFromClient);
                responseToClient = databaseOperator.operateDB(dataFromClient);  //get JSON response
System.out.println(responseToClient);
                if (responseToClient.equals(EXIT_PROGRAM)){
                    break;
                }
                setDataToClient.println(responseToClient);
            }

        } catch (IOException e){
            System.out.println("Server transportation error : " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e){
                System.out.println("Socket close error : " + e.getMessage());
            }
        }
    }
}
