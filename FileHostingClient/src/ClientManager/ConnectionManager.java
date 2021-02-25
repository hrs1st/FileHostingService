package ClientManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionManager {

    private String command = "";
    private String response = "";
    private Socket socket;

    public ConnectionManager() {
        try {
            this.socket = new Socket("localhost",5000);
        } catch (IOException e) {e.printStackTrace();}
    }

    public String start() {
        try {
            BufferedReader serverResponse = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter clientCommand = new PrintWriter(socket.getOutputStream(),true);

            clientCommand.println(command);
            response = serverResponse.readLine();
            return response;
        } catch (IOException e) {e.printStackTrace();
        return null;}
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
