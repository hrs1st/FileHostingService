package ClientManager;

import DatabaseManager.*;
import SystemFileManager.SystemFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;

public class ClientOperator {
    private FileManager fileManager = new FileManager();
    private TableManager tableManager = new TableManager();
    private UploadToDB uploadToDB = new UploadToDB();
    private EditDB editDB = new EditDB();
    private SystemFile systemFile = new SystemFile();

    public static final String FILE_ADDRESS = "fileAddress";
    public static final String FILE_NAME = "fileName";
    public static final String FILE_PATH = "filePath";
    public static final String FILE_ID = "fileId";
    public static final String FILE_SIZE = "fileSize";
    public static final String FILE_COMMENT = "fileComment";
    public static final String IS_DOWNLOAD = "isDownload";
    public static final String FILE_BYTES = "fileBytes";

    public boolean register(HashMap<String, String> command) {
        String mainFolderDirectory = command.get("folderDirectory");
        String stringBuilder = System.getProperty("user.dir") +
                "\\" + "MainFolderPath.dat";
        systemFile.writeToFile(mainFolderDirectory, stringBuilder);

        String username = command.get("username");
        String inAppName = command.get("inAppName");
        String profilePicture = command.get("profilePicture");

        boolean uploadUserInfo = uploadToDB.uploadUserInfo(username, inAppName, profilePicture);
        return uploadUserInfo;
    } //done

    public boolean editMainFolderAddress(String newFolderAddress) {
        String directory = generateMainFolderDirectory();
        fileManager.deleteFromDevice(directory);

        systemFile.writeToFile(newFolderAddress,directory);
        return true;
    } //done

    public String logIn() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.getProperty("user.dir"));
        stringBuilder.append("\\").append("MainFolderPath.dat");

        return systemFile.readFromFile(stringBuilder.toString());
    } //done

    public boolean deleteAccount() {
        editDB.deleteClientDB();

        String systemFilePath = System.getProperty("user.dir") + "\\" + "MainFolderPath.dat";
        fileManager.deleteFromDevice(systemFilePath);
        return true;
    } //done

    public boolean editUserInfo(String username, String newInAppName, File newProfilePicture){
        return editDB.updateUserInfo(username, newInAppName, castFileToString(newProfilePicture));
    }

    public boolean deleteFile(String fileId) {
        HashMap<String, String> fileInfo = tableManager.fileFinder("allFiles", fileId);
        String fileAddress = fileInfo.get(FILE_PATH) + "\\" + fileInfo.get(FILE_NAME);

        String tableName = tableManager.chooseFormat(fileAddress);
        boolean deleteByFormatResponse = editDB.deleteFromTable(tableName, fileId);

        boolean deleteFromAllFiles = editDB.deleteFromTable("allFiles",fileId);

        return deleteByFormatResponse && deleteFromAllFiles;
    } //done

    public boolean deleteShareFile(String fileId){
        return editDB.deleteFromTable("receivedFiles", fileId);
    } //done

    public boolean deleteFileFromDevice(String fileId){
        HashMap<String, String> fileInfo = tableManager.fileFinder("allFiles", fileId);
        String fileAddress = fileInfo.get(FILE_PATH) + "\\" + fileInfo.get(FILE_NAME);

        String tableName = tableManager.chooseFormat(fileAddress);
        boolean deleteByFormatResponse = editDB.deleteFromTable(tableName, fileId);
        boolean updateDownloadStatusResponse = editDB.updateDownloadStatus("allFiles", fileId, "0");

        fileManager.deleteFromDevice(fileAddress);

        return  deleteByFormatResponse && updateDownloadStatusResponse ;
    } //done

    public boolean editShareFile(String newFileName, String newComment, String fileId) {
        boolean editFileName = editDB.updateFileName("receivedFiles", fileId, newFileName);
        boolean editFileComment = editDB.updateFileComment("receivedFiles", fileId, newComment);

        return editFileComment && editFileName;
    } //done

    public boolean editFile(String newFilePath, String newFileName, String newComment, String fileId, String isDownload){
        HashMap<String,String> oldFileInfo = tableManager.fileFinder("allFiles",fileId);
        String oldFileAddress = oldFileInfo.get(FILE_PATH) + "\\" + oldFileInfo.get(FILE_NAME);
        String newFileAddress = newFilePath + "\\" + newFileName;
        String tableNameByFormat = tableManager.chooseFormat(oldFileInfo.get(FILE_NAME));

        if (oldFileInfo.get(FILE_NAME).equals(newFileName)){
            boolean updateCommentInAllFiles = editDB.updateFileComment("allFiles", newComment, fileId);
            if(isDownload.equals("1")){
                boolean updateCommentByFormat = editDB.updateFileComment(tableNameByFormat, newComment, fileId);
                return updateCommentInAllFiles && updateCommentByFormat;
            } else {
                return updateCommentInAllFiles;
            }
        } else {
            boolean updateCommentInAllFiles = editDB.updateFileComment("allFiles", newComment, fileId);
            if (isDownload.equals("1")) {
                handleRenameFromDevice(oldFileAddress, newFileAddress, fileId, "allFiles");
                return updateCommentInAllFiles;
            } else {
                boolean bool = editDB.updateFileName("allFiles", fileId, newFileName);
                return updateCommentInAllFiles && bool;
            }
        }
    } //done

    public boolean downloadFile(JSONObject fileInfo){
        String fileAddress = (String)fileInfo.get(FILE_ADDRESS);
        String fileName = tableManager.fileNameAndPathGenerator(fileAddress).get(FILE_NAME);
        String filePath = tableManager.fileNameAndPathGenerator(fileAddress).get(FILE_PATH);
        String fileId = (String)fileInfo.get(FILE_ID);
        String fileSize = (String)fileInfo.get(FILE_SIZE);
        String fileBytes = (String)fileInfo.get(FILE_BYTES);
        String fileComment = (String)fileInfo.get(FILE_COMMENT);

        HashMap<String, String> fileInfoForInsert = new HashMap<>();
        fileInfoForInsert.put(FILE_ID, fileId);
        fileInfoForInsert.put(FILE_NAME, fileName);
        fileInfoForInsert.put(FILE_PATH, filePath);
        fileInfoForInsert.put(FILE_SIZE, fileSize);
        fileInfoForInsert.put(FILE_COMMENT, fileComment);

        String tableNameByFormat = tableManager.chooseFormat(fileName);

        boolean downloadFromAllFiles = editDB.updateDownloadStatus("allFiles", fileId, "1");
        boolean uploadToTableByFormat = uploadToDB.uploadByFormat(tableNameByFormat, fileInfoForInsert);
        boolean saveOnDevice = fileManager.saveFileOnClient(fileBytes,fileAddress);

        return downloadFromAllFiles && uploadToTableByFormat;
    } //done

    public boolean acceptFile(String fileId){
        return editDB.deleteFromTable("receivedFiles", fileId);
    } //done

    public void fileCheck(JSONObject jsonObject){
        JSONArray serverMainFiles = (JSONArray) jsonObject.get("mainFiles");
        JSONArray serverShareFiles = (JSONArray) jsonObject.get("shareFiles");

        Iterator itMainFiles = serverMainFiles.iterator();
        Iterator itShareFiles = serverShareFiles.iterator();

        ArrayList<HashMap<String, String>> clientMainFiles = tableManager.generateFilesInfo("allFiles");
        ArrayList<HashMap<String, String>> clientShareFiles = tableManager.generateFilesInfo( "receivedFiles");

        ArrayList<HashMap<String, String>> newMainFiles = compareLists(itMainFiles, clientMainFiles, "allFiles");
        ArrayList<HashMap<String, String>> newShareFiles = compareLists(itShareFiles, clientShareFiles, "receivedFiles");

        addToTable(newMainFiles, "allFiles");
        addToTable(newShareFiles, "receivedFiles");
    } //done

    private ArrayList<HashMap<String, String>> compareLists(Iterator serverFiles, ArrayList<HashMap<String, String>> clientFiles, String tableName){
        ArrayList<HashMap<String, String>> newFiles = new ArrayList<>();
        HashMap<String, String> fileInfo = new HashMap<>();

        while (serverFiles.hasNext()){
            JSONObject jsonObject = (JSONObject) serverFiles.next();
            String fileId = (String) jsonObject.get(FILE_ID);
            String fileSize = (String) jsonObject.get(FILE_SIZE);
            String fileAddress = (String) jsonObject.get(FILE_ADDRESS);
            String fileComment = (String) jsonObject.get(FILE_COMMENT);

            HashMap<String, String> newFileInfo = new HashMap<>();
            newFileInfo.put(FILE_ID, fileId);
            newFileInfo.put(FILE_ADDRESS, fileAddress);
            newFileInfo.put(FILE_SIZE, fileSize);
            newFileInfo.put(FILE_COMMENT, fileComment);

            if(clientFiles.size() == 0){
                newFiles.add(newFileInfo);
            }

            for(int counter = 0; counter < clientFiles.size(); counter++){
                fileInfo = clientFiles.get(counter);
                if (fileId.equals(fileInfo.get(FILE_ID))){
                    if (!fileSize.equals(fileInfo.get(FILE_SIZE))) {
                        newFiles.add(newFileInfo);
                    } else {
                        if (!fileAddress.equals(fileInfo.get(FILE_ADDRESS))){
                            handleRenameFromDevice(fileAddress, fileInfo.get(FILE_ADDRESS), fileId, tableName);
                        }
                    }
                    break;
                }
                if (counter == clientFiles.size()-1){
                    newFiles.add(newFileInfo);
                }
            }
        }
        return newFiles;
    } //done

    private void addToTable(ArrayList<HashMap<String, String>> newFiles, String tableName){
        HashMap<String, String> fileInfo;

        for(HashMap<String, String> newFileInfo : newFiles){
            fileInfo = new HashMap<>();
            String fileName = tableManager.fileNameAndPathGenerator(newFileInfo.get(FILE_ADDRESS)).get(FILE_NAME);
            String filePath = tableManager.fileNameAndPathGenerator(newFileInfo.get(FILE_ADDRESS)).get(FILE_PATH);

            fileInfo.put(FILE_ID, newFileInfo.get(FILE_ID));
            fileInfo.put(FILE_NAME, fileName);
            fileInfo.put(FILE_PATH, filePath);
            fileInfo.put(FILE_SIZE, newFileInfo.get(FILE_SIZE));
            fileInfo.put(FILE_COMMENT, newFileInfo.get(FILE_COMMENT));
            fileInfo.put(IS_DOWNLOAD, "0");

            uploadToDB.uploadFile(tableName, fileInfo);
        }
    } //done

    private void handleRenameFromDevice(String oldFileAddress, String newFileAddress, String fileId, String tableName){
        boolean bool = editDB.updateFileName(tableName, fileId, tableManager.fileNameAndPathGenerator(newFileAddress).get(FILE_NAME));
        if(bool){
            try {
                File oldFile = new File(oldFileAddress);
                FileInputStream fis = new FileInputStream(oldFile);
                File newFile = new File(newFileAddress);
                FileOutputStream fos = new FileOutputStream(newFile);

                fos.write(fis.readAllBytes());

                fis.close();
                fos.close();

                fileManager.deleteFromDevice(oldFileAddress);

            } catch (IOException e){
                System.out.println(e.getMessage());
            }

            HashMap<String, String> fileInfo = tableManager.fileFinder(tableName, fileId);
            if(fileInfo.get(IS_DOWNLOAD).equals("1")){
                String tableNameByFormat = tableManager.chooseFormat(fileInfo.get(FILE_NAME));
                boolean booleans = editDB.updateFileName(tableNameByFormat, fileId, tableManager.fileNameAndPathGenerator(newFileAddress).get(FILE_NAME));
            }
        }
    } //done

    private String generateMainFolderDirectory(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.getProperty("user.dir"));
        stringBuilder.append("\\").append("MainFolderPath.dat");
        return stringBuilder.toString();
    } //done


    private String castFileToString (File file){
        try {
            FileInputStream fis = new FileInputStream(file);
            return Base64.getEncoder().encodeToString(fis.readAllBytes());

        } catch (IOException e){
            System.out.println(e.getMessage());
            return "";
        }
    } //done
}
