package ClientManager;

import DatabaseManager.TableManager;
import JSON.JSONMaker;
import JSON.Parser;
import org.json.simple.JSONObject;

import javax.print.attribute.HashAttributeSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

public class ClientResponse {
    private TableManager tableManager = new TableManager();
    private HashMap<String, String> fileInfo = new HashMap<>();
    private JSONMaker jsonMaker = new JSONMaker();
    private ConnectionManager connectionManager = new ConnectionManager();
    private Parser parser = new Parser();

    public static final String NEW_PASSWORD = "newPassword";
    public static final String NEW_IN_APP_NAME = "newInAppName";
    public static final String NEW_FILE_NAME = "newFileName";
    public static final String NEW_COMMENT = "newComment";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String IN_APP_NAME = "inAppName";
    public static final String PROFILE_PICTURE = "profilePicture";
    public static final String NEW_PROFILE_PICTURE = "profilePicture";

    public static final String NEW_FILE_ADDRESS = "newFileAddress";
    public static final String FILE_NAME = "fileName";
    public static final String FILE_PATH = "filePath";
    public static final String FILE_ADDRESS = "fileAddress";
    public static final String FILE_SIZE = "fileSize";
    public static final String FILE_BYTES = "fileBytes";
    public static final String FILE_COMMENT = "fileComment";
    public static final String FILE_ID = "fileId";

    public JSONObject register(HashMap<String, String> userInfo){
        return senderReceiver(jsonMaker.registerJSON(userInfo));
    }

    public JSONObject login(HashMap<String, String> userInfo){
        return senderReceiver(jsonMaker.loginJSON(userInfo));
    }

    public JSONObject editUserInfo(String username, String password, String newPassword, String newInAppName, File newProfilePicture) {
        String profilePicture = "";
        if (newProfilePicture != null){
            profilePicture = castFileToString(newProfilePicture);
        }
        HashMap<String, String> userInfo = new HashMap<>();

        userInfo.put(USER_NAME, username);
        userInfo.put(PASSWORD, password);
        userInfo.put(NEW_PASSWORD, newPassword);
        userInfo.put(NEW_IN_APP_NAME, newInAppName);
        userInfo.put(NEW_PROFILE_PICTURE, profilePicture);

        return senderReceiver(jsonMaker.editUserInfoJSON(userInfo));
    } //done

    public JSONObject deleteAccount(String username) {
        return senderReceiver(jsonMaker.deleteAccountJSON(username));
    } //done

    public JSONObject deleteFile(String username, String fileId) {
        return senderReceiver(jsonMaker.deleteFileJSON(username,fileId));
    } //done

    public JSONObject deleteShareFile(String username, String fileId){
        return senderReceiver(jsonMaker.deleteShareFileJSON(username, fileId));
    } //done

    public JSONObject acceptFile(String username, String fileId){
        return senderReceiver(jsonMaker.acceptFileJSON(username, fileId));
    } //done

    public JSONObject uploadFile(String username, File file, String filePath) {
        fileInfo = new HashMap<>();
        String fileName = file.getName();
        String fileBytes = castFileToString(file);

        fileInfo.put(USER_NAME, username);
        fileInfo.put(FILE_ADDRESS, filePath + "\\" + fileName);
        fileInfo.put(FILE_SIZE, Long.toString(file.length()));
        fileInfo.put(FILE_BYTES, fileBytes);
        fileInfo.put(FILE_COMMENT, "");

        String fileId = tableManager.searchOnTable("allFiles", filePath, fileName);
        fileInfo.put(FILE_ID, fileId);

        return senderReceiver(jsonMaker.uploadToServerJSON(fileInfo));
    } //done

    public JSONObject downloadFile(String username, String fileId){
        return senderReceiver(jsonMaker.downloadFileJSON(username, fileId));
    } //done

    public JSONObject fileCheck(String username) {
        return senderReceiver(jsonMaker.fileCheckerJSON(username));
    } //done

    public JSONObject shareFile(String username, String receiverName, String fileId) {
        return senderReceiver(jsonMaker.shareFileJSON(username, receiverName, fileId));
    } //done

    public JSONObject editFile(String username,String newFilePath, String newFileName, String newComment, String fileId) {
        HashMap<String,String> fileInfo = new HashMap<>();

        fileInfo.put(USER_NAME, username);
        fileInfo.put(NEW_FILE_ADDRESS, newFilePath + "\\" + newFileName);
        fileInfo.put(NEW_COMMENT, newComment);
        fileInfo.put(FILE_ID, fileId);

        return senderReceiver(jsonMaker.editFileJSON(fileInfo));

    } //done

    public JSONObject editShareFile(String username, String newFileName, String newComment, String fileId) {
        HashMap<String,String> shareFileInfo = new HashMap<>();

        shareFileInfo.put(USER_NAME, username);
        shareFileInfo.put(NEW_FILE_NAME, newFileName);
        shareFileInfo.put(NEW_COMMENT, newComment);
        shareFileInfo.put(FILE_ID, fileId);

        return senderReceiver(jsonMaker.editShareFileJSON(shareFileInfo));

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

    private JSONObject senderReceiver (String command){
        connectionManager.setCommand(command);
System.out.println(command);
        String response = connectionManager.start();
System.out.println(response);
        return parser.parser(response);
    } //done
}
