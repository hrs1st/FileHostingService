package ClientManager;

import java.io.File;
import java.util.Objects;
import java.util.TimerTask;

public class SignFolder extends Thread {
    String path = "";
    ClientRunnable clientRunnable = new ClientRunnable();
    public void setUsername(String username){
        clientRunnable.setUsername(username);
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void run() {
        while (true) {
            timerTask.run();
            try {
                sleep(30000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            File files = new File(path);
            for(File file : Objects.requireNonNull(files.listFiles())){
                if(!file.isFile()){
                    String path1 = file.getAbsolutePath();
                    SignFolder signFolder = new SignFolder();
                    signFolder.setPath(path1);
                    signFolder.setUsername(clientRunnable.getUsername());
                    Thread thread = new Thread(signFolder);
                    thread.start();
                } else{
                    clientRunnable.uploadProcess(file, file.getParent(), "1");
                    clientRunnable.filesCheckProcess();
                }
            }
        }
    };
}