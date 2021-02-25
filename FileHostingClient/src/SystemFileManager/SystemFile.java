package SystemFileManager;

import java.io.*;
import java.util.Scanner;

public class SystemFile {

    public void writeToFile(String output, String fileDirectory) {
        try {
            File file = new File(fileDirectory);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(output.getBytes());

            fos.close();

        } catch(IOException exception){
//            System.out.println(exception.getMessage());
        }
    }

    public String readFromFile(String fileDirectory) {
        try {
            File file = new File(fileDirectory);
            Scanner scanner = new Scanner(file);
            String input = scanner.nextLine();

            scanner.close();
            return input;

        } catch (IOException e) {
            return "";
        }
    }
}
