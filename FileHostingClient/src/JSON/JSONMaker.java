package JSON;

import java.util.ArrayList;
import java.util.HashMap;

public class JSONMaker {
    public static final String FILE_ID = "fileId";
    public static final String FILE_ADDRESS = "fileAddress";
    public static final String FILE_SIZE = "fileSize";
    public static final String FILE_BYTES = "fileBytes";
    public static final String FILE_COMMENT = "fileComment";

    public static final String FUNCTION = "function";
    public static final String REGISTER = "register";
    public static final String EDIT_USER = "editUser";
    public static final String DELETE_ACCOUNT = "deleteAccount";
    public static final String LOGIN = "logIn";
    public static final String FILE_CHECKER = "fileChecker";
    public static final String UPLOAD_TO_SERVER = "uploadToServer";
    public static final String DOWNLOAD_FROM_SERVER = "downloadFromServer";
    public static final String ACCEPT_SHARE_FILE = "acceptShareFile";
    public static final String DELETE_FILE = "deleteFile";
    public static final String DELETE_SHARE_FILE = "deleteShareFile";
    public static final String EDIT_FILE = "editFile";
    public static final String EDIT_SHARE_FILE = "editShareFile";
    public static final String SHARE_FILE = "shareFile";
    public static final String EXIT_PROGRAM = "exit";

    public static final String PROFILE_PICTURE = "profilePicture";
    public static final String NEW_PROFILE_PICTURE = "newProfilePicture";
    public static final String RECEIVER_USER_NAME = "receiverUserName";
    public static final String NEW_PASSWORD = "newPassword";
    public static final String NEW_IN_APP_NAME = "newInAppName";
    public static final String NEW_FILE_ADDRESS = "newFileAddress";
    public static final String NEW_FILE_NAME = "newFileName";
    public static final String NEW_COMMENT = "newComment";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String IN_APP_NAME = "inAppName";

    public String registerJSON (HashMap<String,String> command){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(REGISTER).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(REGISTER).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append(USER_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(command.get(USER_NAME)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(PASSWORD).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(command.get(PASSWORD)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(IN_APP_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(command.get(IN_APP_NAME)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(PROFILE_PICTURE).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(command.get(PROFILE_PICTURE)).append("\"");
        stringBuilder.append("}");
        stringBuilder.append("}");

        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    } //done

    public String loginJSON (HashMap<String, String> command){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append("logIn").append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append("logIn").append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append(USER_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(command.get(USER_NAME)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(PASSWORD).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(command.get(PASSWORD)).append("\"");
        stringBuilder.append("}");
        stringBuilder.append("}");

        return stringBuilder.toString();
    } //todo

    public String editUserInfoJSON(HashMap<String, String> userInfo){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(EDIT_USER).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(EDIT_USER).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append(USER_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(userInfo.get(USER_NAME)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(PASSWORD).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(userInfo.get(PASSWORD)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(NEW_PASSWORD).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(userInfo.get(NEW_PASSWORD)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(NEW_IN_APP_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(userInfo.get(NEW_IN_APP_NAME)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(NEW_PROFILE_PICTURE).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(userInfo.get(NEW_PROFILE_PICTURE)).append("\"");
        stringBuilder.append("}");
        stringBuilder.append("}");
        return stringBuilder.toString();
    } //done

    public String deleteAccountJSON(String username) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(DELETE_ACCOUNT).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(DELETE_ACCOUNT).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append(USER_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(username).append("\"");
        stringBuilder.append("}");
        stringBuilder.append("}");
        return stringBuilder.toString();
    } //done

    public String deleteFileJSON(String username, String fileId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(DELETE_FILE).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(DELETE_FILE).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append(USER_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(username).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(FILE_ID).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(fileId).append("\"");
        stringBuilder.append("}");
        stringBuilder.append("}");
        return stringBuilder.toString();
    } //done

    public String deleteShareFileJSON(String username, String fileId){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(DELETE_SHARE_FILE).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(DELETE_SHARE_FILE).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append(USER_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(username).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(FILE_ID).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(fileId).append("\"");
        stringBuilder.append("}");
        stringBuilder.append("}");
        return stringBuilder.toString();
    } //done

    public String fileCheckerJSON (String username){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(FILE_CHECKER).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(FILE_CHECKER).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append(USER_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(username).append("\"");
        stringBuilder.append("}");
        stringBuilder.append("}");
        return stringBuilder.toString();
    } //done

    public String uploadToServerJSON (HashMap<String,String> fileInformation){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(UPLOAD_TO_SERVER).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(UPLOAD_TO_SERVER).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append(USER_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(fileInformation.get(USER_NAME)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(FILE_ID).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(fileInformation.get(FILE_ID)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(FILE_ADDRESS).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(fileInformation.get(FILE_ADDRESS)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(FILE_SIZE).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(fileInformation.get(FILE_SIZE)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(FILE_BYTES).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(fileInformation.get(FILE_BYTES)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(FILE_COMMENT).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(fileInformation.get(FILE_COMMENT)).append("\"");
        stringBuilder.append("}");
        stringBuilder.append("}");
        return stringBuilder.toString();
    } //done

    public String downloadFileJSON (String username, String fileId){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(DOWNLOAD_FROM_SERVER).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(DOWNLOAD_FROM_SERVER).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append(USER_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(username).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(FILE_ID).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(fileId).append("\"");
        stringBuilder.append("}");
        stringBuilder.append("}");
        return stringBuilder.toString();
    } //done

    public String shareFileJSON (String username, String receivedName, String fileId){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(SHARE_FILE).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(SHARE_FILE).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append(USER_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(username).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(FILE_ID).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(fileId).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(RECEIVER_USER_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(receivedName).append("\"");
        stringBuilder.append("}");
        stringBuilder.append("}");

        return stringBuilder.toString();
    }  //done

    public String editShareFileJSON(HashMap<String,String> shareFileInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(EDIT_SHARE_FILE).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(EDIT_SHARE_FILE).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append(USER_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(shareFileInfo.get(USER_NAME)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(FILE_ID).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(shareFileInfo.get(FILE_ID)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(NEW_FILE_ADDRESS).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(shareFileInfo.get(NEW_FILE_NAME)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(NEW_COMMENT).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(shareFileInfo.get(NEW_COMMENT)).append("\"");
        stringBuilder.append("}");
        stringBuilder.append("}");
        return stringBuilder.toString();
    } //done

    public String editFileJSON(HashMap<String,String> shareFileInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(EDIT_FILE).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(EDIT_FILE).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append(USER_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(shareFileInfo.get(USER_NAME)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(FILE_ID).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(shareFileInfo.get(FILE_ID)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(NEW_FILE_ADDRESS).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(shareFileInfo.get(NEW_FILE_ADDRESS)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(NEW_COMMENT).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(shareFileInfo.get(NEW_COMMENT)).append("\"");
        stringBuilder.append("}");
        stringBuilder.append("}");
        return stringBuilder.toString();
    } //done

    public String acceptFileJSON (String username, String fileId){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(ACCEPT_SHARE_FILE).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(ACCEPT_SHARE_FILE).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append(USER_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(username).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(FILE_ID).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(fileId).append("\"");
        stringBuilder.append("}");
        stringBuilder.append("}");

        return stringBuilder.toString();
    } //done
}
