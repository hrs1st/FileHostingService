package ClientManager;

import DatabaseManager.Database;
import DatabaseManager.TableManager;
import SystemFileManager.SystemFile;
import JSON.JSONMaker;
import JSON.Parser;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONObject;
import java.io.*;
import java.net.Socket;

public class ClientRunnable {

    public static final String USERNAME = "username";
    public static final String IN_APP_NAME = "inAppName";
    public static final String PROFILE_PICTURE = "profilePicture";

    public static final String FILE_ADDRESS = "fileAddress";
    public static final String FILE_NAME = "fileName";
    public static final String FILE_PATH = "filePath";
    public static final String FILE_SIZE = "fileSize";
    public static final String FILE_COMMENT = "fileComment";
    public static final String FILE_ID = "fileId";
    public static final String IS_DOWNLOAD = "isDownload";

    public static final String SITUATION = "situation";
    public static final String SUCCESSFUL = "successful";

    public static final String NEW_FILE_NAME = "newFileName";
    public static final String NEW_COMMENT = "newComment";
    public static final String REGISTER = "register";
    public static final String EDIT_USER = "editUser";
    public static final String EDIT_SHARE_FILE = "editShareFile";
    public static final String DELETE_ACCOUNT = "deleteAccount";
    public static final String LOGIN = "logIn";
    public static final String UPLOAD_TO_SERVER = "uploadToServer";
    public static final String DOWNLOAD_FROM_SERVER = "downloadFromServer";
    public static final String ACCEPT_SHARE_FILE = "acceptShareFile";
    public static final String DELETE_FILE = "deleteFile";
    public static final String EDIT_FILE = "editFile";
    public static final String SHARE_FILE = "shareFile";
    public static final String DELETE_SHARE_FILE = "deleteShareFile";
    public static final String FOLDER_DIRECTORY = "folderDirectory";

    private String username;
    private ClientOperator clientOperator = new ClientOperator();
    private ClientResponse clientResponse = new ClientResponse();
    private TableManager tableManager = new TableManager();

    public ClientRunnable(){
        Database database = new Database();
    }

    public void setUsername(String username){
        this.username = username;
    }


    public HashMap<String,String> registerProcess(HashMap<String , String> command){
        JSONObject response = (JSONObject)clientResponse.register(command).get(REGISTER);
        if(!response.get(SITUATION).equals("repetitive")){
            boolean bool = clientOperator.register(command);
            if(bool) {
                HashMap<String, String> userInfo = new HashMap<>();
                userInfo.put(USERNAME, (String) response.get(USERNAME));
                userInfo.put(IN_APP_NAME, (String) response.get(IN_APP_NAME));
                userInfo.put(PROFILE_PICTURE, (String) response.get(PROFILE_PICTURE));
                return userInfo;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }  //all done

    public boolean deleteAccountProcess(){
        boolean clientResponse = (((JSONObject)this.clientResponse.deleteAccount(username).get(DELETE_ACCOUNT)).get(SITUATION)).equals(SUCCESSFUL);
        if(clientResponse) {
            return clientOperator.deleteAccount();
        } else {
            return false;
        }
    } //all done

    public HashMap<String,String> logInProcess(HashMap<String,String> command){
        JSONObject response = (JSONObject)clientResponse.login(command).get(LOGIN);
        if((response.get("isSuccessful")).equals("wrongPassword") || (response.get("isSuccessful")).equals("wrongUser")){
            return null;
        } else {
            HashMap<String, String> userInfo = new HashMap<>();

            String folderDirectory = clientOperator.logIn();
            userInfo.put(FOLDER_DIRECTORY, folderDirectory);
            userInfo.put(USERNAME, (String) response.get(USERNAME));
            userInfo.put(IN_APP_NAME, (String) response.get(IN_APP_NAME));
            userInfo.put(PROFILE_PICTURE, (String) response.get(PROFILE_PICTURE));
            return userInfo;
        }
    } //all done

    public HashMap<String, String> autoSet(){
        return tableManager.getUserInfo();
    }

    public boolean editUserInfoProcess (String password, String newPassword, String newInAppName, File newProfilePicture){
        boolean clientResponse = ((JSONObject)this.clientResponse.editUserInfo(username,password,newPassword,newInAppName,newProfilePicture).get(EDIT_USER)).get(SITUATION).equals(SUCCESSFUL);
        if(clientResponse){
            return clientOperator.editUserInfo(username, newInAppName, newProfilePicture);
        } else {
            return false;
        }
    } //done

    public boolean editFolderPathProcess(String newFolderPath){
        return clientOperator.editMainFolderAddress(newFolderPath);
    } //done

    public boolean editFileInfoProcess(HashMap<String, String> fileInfo){
        String newFileName = fileInfo.get(NEW_FILE_NAME);
        String newFilePath = fileInfo.get(FILE_PATH);
        String newComment = fileInfo.get(NEW_COMMENT);
        String fileId = fileInfo.get(FILE_ID);

        boolean clientResponse = ((JSONObject)this.clientResponse.editFile(username,newFilePath, newFileName, newComment, fileId).get(EDIT_FILE)).get(SITUATION).equals(SUCCESSFUL);

        if(clientResponse) {
            return clientOperator.editFile(newFilePath, newFileName, newComment, fileId, fileInfo.get(IS_DOWNLOAD));
        } else {
            return false;
        }
    } //done

    public boolean editShareFileInfoProcess(HashMap<String, String> fileInfo){
        String newFileName = fileInfo.get(NEW_FILE_NAME);
        String newComment = fileInfo.get(NEW_COMMENT);
        String fileId = fileInfo.get(FILE_ID);

        boolean clientResponse = ((JSONObject)this.clientResponse.editShareFile(username, newFileName, newComment, fileId).get(EDIT_SHARE_FILE)).get(SITUATION).equals(SUCCESSFUL);

        if(clientResponse) {
            return clientOperator.editShareFile(newFileName, newComment, fileId);
        } else {
            return false;
        }
    } //done

    public boolean deleteFileProcess(String fileId){
        boolean clientResponse = ((JSONObject)this.clientResponse.deleteFile(username, fileId).get(DELETE_FILE)).get(SITUATION).equals(SUCCESSFUL);
        if(clientResponse){
            return clientOperator.deleteFile(fileId);
        } else {
            return false;
        }
    } //done

    public boolean deleteShareFileProcess(String fileId){
        boolean clientResponse = ((JSONObject)this.clientResponse.deleteShareFile(username, fileId).get(DELETE_SHARE_FILE)).get(SITUATION).equals(SUCCESSFUL);
        if(clientResponse){
            return clientOperator.deleteShareFile(fileId);
        } else {
            return false;
        }
    } //done

    public boolean deleteFileFromDeviceProcess(String fileId) {
        return clientOperator.deleteFileFromDevice(fileId);
    } //done

    public boolean acceptProcess(String fileId){
        boolean response = ((JSONObject)clientResponse.acceptFile(username, fileId).get(ACCEPT_SHARE_FILE)).get(SITUATION).equals(SUCCESSFUL);

        if (response){
            return clientOperator.acceptFile(fileId);
        } else {
            return false;
        }
    } //done

    public boolean downloadProcess(String fileId){
        JSONObject response = (JSONObject)clientResponse.downloadFile(username, fileId).get(DOWNLOAD_FROM_SERVER);
        return ((response != null) && clientOperator.downloadFile(response));
    } //done

    public boolean uploadProcess(File file, String directory, String isDownload){
        JSONObject response = clientResponse.uploadFile(username, file, directory);
        return ((JSONObject)response.get(UPLOAD_TO_SERVER)).get(SITUATION).equals(SUCCESSFUL);
    } //done

    public void filesCheckProcess(){
        JSONObject jsonObject = clientResponse.fileCheck(username);
        clientOperator.fileCheck(jsonObject);
    } //done

    public boolean shareFileProcess(String receiverName, String fileId){
        return ((JSONObject)clientResponse.shareFile(username, receiverName, fileId).get(SHARE_FILE)).get(SITUATION).equals(SUCCESSFUL);
    } //done

    public ArrayList<HashMap<String, String>> getAllFilesTable(){
        return tableManager.generateToShow("allFiles");
    } //done

    public ArrayList<HashMap<String, String>> getImagesTable(){
        return tableManager.generateToShowByFormat("images");
    } //done

    public ArrayList<HashMap<String, String>> getVideosTable(){
        return tableManager.generateToShowByFormat("videos");
    } //done

    public ArrayList<HashMap<String, String>> getMusicsTable(){
        return tableManager.generateToShowByFormat("musics");
    } //done

    public ArrayList<HashMap<String, String>> getDocumentsTable(){
        return tableManager.generateToShowByFormat("documents");
    } //done

    public ArrayList<HashMap<String, String>> getShareFilesTable(){
        return tableManager.generateToShow("receivedFiles");
    } //done

    public String exit(){
        return null;
    }

    public void folderDirectoryProcess(String path){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.getProperty("user.dir"));
        stringBuilder.append("\\").append("MainFolderPath.dat");
        editFolderPathProcess(stringBuilder.toString());
    } //done

    public String getUsername(){
        return this.username;
    }
}
