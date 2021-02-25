package DatabaseManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;

public class FileManager {

    public boolean saveFileOnClient(File file, String directory){
        try {
            File saveFile = new File(directory);
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(saveFile);
            fos.write(fis.readAllBytes());
            return true;
        } catch (IOException  e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean saveFileOnClient(String fileBytes, String directory) {
        boolean bool = isFolder(directory);
        if(!bool) {
            try {
                byte[] fileByteArray = castStringToByteArray(fileBytes);
                File saveFile = new File(directory);
                FileOutputStream fos = new FileOutputStream(saveFile);
                fos.write(fileByteArray);

                fos.close();
                return true;

            } catch (IOException e) {
                System.out.println(e.getMessage());
                return false;
            }
        } else {
            File file = new File(directory);
            file.mkdirs();
            return true;
        }
    }

    public void deleteFromDevice(String fileAddress) {
        try {
            Files.deleteIfExists(Paths.get(fileAddress));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public byte[] castStringToByteArray(String fileBytes) {
        return Base64.getDecoder().decode(fileBytes);
    }

    private boolean isFolder(String directory){
        char[] directoryCharArray = directory.toCharArray();

        for(int counter = 0 ; counter < directory.length() ; counter++){
            if(directoryCharArray[counter] == '.') {
                return false;
            }
        }
        return true;
    }
}
