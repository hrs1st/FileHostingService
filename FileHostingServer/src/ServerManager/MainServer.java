package ServerManager;

import DatabaseManager.Database;

import java.io.IOException;
import java.net.ServerSocket;

public class MainServer {
    public static void main (String[] args){
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            Database database = new Database();
            database.createUsersTable();

            while (true){
                ServerThread serverThread = new ServerThread(serverSocket.accept());
                serverThread.start();
            }

        } catch (IOException e){
            System.out.println("Server error on Connecting : " + e.getMessage());
        }
    }
}
