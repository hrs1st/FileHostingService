package DatabaseManager;

import DatabaseManager.Users.UserDBManager;
import DatabaseManager.personalDatabase.DownloadFromDB;
import DatabaseManager.personalDatabase.EditDB;
import DatabaseManager.personalDatabase.UploadToDB;
import DatabaseManager.personalDatabase.TableManager;
import JSON.JSONMaker;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseOperator {

    UserDBManager userDBManager = new UserDBManager();
    TableManager tableManager = new TableManager();
    EditDB editDB = new EditDB();
    JSONMaker jsonMaker = new JSONMaker();

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

    public static final String RECEIVER_USER_NAME = "receiverUserName";
    public static final String NEW_PASSWORD = "newPassword";
    public static final String NEW_IN_APP_NAME = "newInAppName";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String IN_APP_NAME = "inAppName";
    public static final String PROFILE_PICTURE = "profilePicture";
    public static final String NEW_PROFILE_PICTURE = "profilePicture";


    public static final String NEW_FILE_ADDRESS = "newFileAddress";
    public static final String NEW_COMMENT = "newComment";

    public static final String FILE_ADDRESS = "fileAddress";
    public static final String FILE_NAME = "fileName";
    public static final String FILE_SIZE = "fileSize";
    public static final String FILE_BYTES = "fileBytes";
    public static final String FILE_COMMENT = "fileComment";
    public static final String FILE_ID = "fileId";

    public String operateDB (String jsonInput) {
        JSONObject jsonObject = parser(jsonInput);
        return (jsonObject != null) ? doAction(jsonObject) : jsonMaker.wrongInputJSON();
    }

    private JSONObject parser (String jsonInput) {
        try {
            return (JSONObject) new JSONParser().parse(jsonInput);
        } catch (ParseException e){
           System.out.println(e.getMessage());
            return null;
        }
    }

    private String doAction (JSONObject jsonObject){
        switch ((String) jsonObject.get(FUNCTION)){
            case REGISTER :
                return registerResponse((JSONObject) jsonObject.get(REGISTER));
            case EDIT_USER :
                return editUserResponse((JSONObject) jsonObject.get(EDIT_USER));
            case DELETE_ACCOUNT :
                return deleteAccountResponse((JSONObject) jsonObject.get(DELETE_ACCOUNT));
            case LOGIN :
                return logInResponse((JSONObject) jsonObject.get(LOGIN));
            case FILE_CHECKER :
                return fileCheckerResponse ((JSONObject) jsonObject.get(FILE_CHECKER));
            case UPLOAD_TO_SERVER:
                return uploadToServerResponse((JSONObject) jsonObject.get(UPLOAD_TO_SERVER));
            case DOWNLOAD_FROM_SERVER:
                return downloadFromServerResponse((JSONObject) jsonObject.get(DOWNLOAD_FROM_SERVER));
            case ACCEPT_SHARE_FILE:
                return acceptShareFile((JSONObject) jsonObject.get(ACCEPT_SHARE_FILE));
            case EDIT_FILE :
                return editFileResponse((JSONObject) jsonObject.get(EDIT_FILE));
            case EDIT_SHARE_FILE :
                return editShareFileResponse((JSONObject) jsonObject.get(EDIT_SHARE_FILE));
            case DELETE_FILE :
                return deleteFileResponse((JSONObject) jsonObject.get(DELETE_FILE));
            case DELETE_SHARE_FILE:
                return deleteShareFileResponse((JSONObject) jsonObject.get(DELETE_SHARE_FILE));
            case SHARE_FILE :
                return shareFileResponse((JSONObject) jsonObject.get(SHARE_FILE));
            case EXIT_PROGRAM :
                return exitProgram();
            default:
                return jsonMaker.wrongInputJSON();
        }
    }

    private String registerResponse (JSONObject jsonObject) {
        String userName = (String)jsonObject.get(USER_NAME);
        String password = (String)jsonObject.get(PASSWORD);
        String inAppName = (String)jsonObject.get(IN_APP_NAME);
        String profilePicture = (String)jsonObject.get(PROFILE_PICTURE);

        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put(USER_NAME, userName);
        userInfo.put(IN_APP_NAME, inAppName);
        userInfo.put(PROFILE_PICTURE, profilePicture);
        HashMap<String,String> response = userDBManager.addUser(userName, password, inAppName, profilePicture);
        return jsonMaker.registerJSON(userInfo, response);
    } //all done -{auto pp is wrong}

    private String editUserResponse (JSONObject jsonObject){
        String userName = (String)jsonObject.get(USER_NAME);
        String password = (String)jsonObject.get(PASSWORD);
        String newPassword = (String)jsonObject.get(NEW_PASSWORD);
        String newIAppName = (String)jsonObject.get(NEW_IN_APP_NAME);
        String newProfile = (String)jsonObject.get(NEW_PROFILE_PICTURE);

        boolean checkPassword = password.equals(userDBManager.searchUser(PASSWORD, userName));

        if (checkPassword) {
            boolean passwordBool = userDBManager.updateUserInfo(PASSWORD, userName, newPassword);
            boolean inAppNameBool = userDBManager.updateUserInfo(IN_APP_NAME, userName, newIAppName);
            boolean profileBool = userDBManager.updateUserInfo(PROFILE_PICTURE, userName, newProfile);
            return jsonMaker.editUserJSON(passwordBool && inAppNameBool && profileBool);
        } else {
            return jsonMaker.editUserJSON(false);
        }
    }

    private String deleteAccountResponse (JSONObject jsonObject) {
        String userName = (String)jsonObject.get(USER_NAME);

        boolean response = userDBManager.deleteUser(userName);
        return jsonMaker.deleteAccountJSON(response);
    } //all done

    private String logInResponse (JSONObject jsonObject){
        HashMap<String, String> response = userDBManager.logIn((String)jsonObject.get(USER_NAME), (String)jsonObject.get(PASSWORD));
        return jsonMaker.loginJSON(response);
    } //all done

    private String fileCheckerResponse (JSONObject jsonObject){
        ArrayList<HashMap<String, String>> mainFiles = tableManager.generateFilesInfo((String)jsonObject.get(USER_NAME));
        ArrayList<HashMap<String, String>> shareFiles = tableManager.generateFilesInfo(((String)jsonObject.get(USER_NAME) + "Received"));
        return jsonMaker.fileCheckerJSON(mainFiles, shareFiles);
    }

    private String uploadToServerResponse(JSONObject jsonObject){
        UploadToDB uploadToDB = new UploadToDB();
        String userName = (String) jsonObject.get(USER_NAME);
        uploadToDB.setFileAddress((String) jsonObject.get(FILE_ADDRESS));
        uploadToDB.setFileSize((String) jsonObject.get(FILE_SIZE));
        uploadToDB.setFileBytes((String) jsonObject.get(FILE_BYTES));
        uploadToDB.setFileComment((String) jsonObject.get(FILE_COMMENT));
        uploadToDB.setFileId((String) jsonObject.get(FILE_ID));

        boolean response = uploadToDB.uploadFile(userName);
        return jsonMaker.uploadToServerJSON(response);
    }

    private String downloadFromServerResponse(JSONObject jsonObject){
        DownloadFromDB downloadFromDB = new DownloadFromDB();

        String userName = (String) jsonObject.get(USER_NAME);
        downloadFromDB.setFileId((String) jsonObject.get(FILE_ID));

        HashMap<String, String> response =  downloadFromDB.download(userName);

        return jsonMaker.downloadFromServerJSON(response);
    }

    private String acceptShareFile(JSONObject jsonObject){
        DownloadFromDB downloadFromDB = new DownloadFromDB();
        UploadToDB uploadToDB = new UploadToDB();

        String userName = (String) jsonObject.get(USER_NAME);
        downloadFromDB.setFileId((String) jsonObject.get(FILE_ID));
        HashMap<String, String> downloadResponse =  downloadFromDB.download((userName + "Received"));

        if (downloadResponse != null) {
            uploadToDB.setFileAddress(downloadResponse.get(FILE_ADDRESS));
            uploadToDB.setFileSize(downloadResponse.get(FILE_SIZE));
            uploadToDB.setFileBytes(downloadResponse.get(FILE_BYTES));
            uploadToDB.setFileComment(downloadResponse.get(FILE_COMMENT));
            uploadToDB.setFileId("");
            boolean uploadResponse = uploadToDB.uploadFile(userName);

            if (uploadResponse) {
                boolean deleteResponse = editDB.deleteFromTable((userName + "Received"), (String) jsonObject.get(FILE_ID));
                return jsonMaker.acceptShareFile(deleteResponse);
            }
        }
        return jsonMaker.acceptShareFile(false);
    }

    private String editFileResponse (JSONObject jsonObject){
        String userName = (String) jsonObject.get(USER_NAME);
        String fileID = (String) jsonObject.get(FILE_ID);
        String newFileAddress = (String) jsonObject.get(NEW_FILE_ADDRESS);
        String newFileComment = (String) jsonObject.get(NEW_COMMENT);

        boolean fileAddressEdit = editDB.updateFileAddress(userName, fileID, newFileAddress);
        boolean fileCommentEdit = editDB.updateFileComment(userName, fileID, newFileComment);

        return jsonMaker.editFileJSON(fileAddressEdit && fileCommentEdit);
    }

    private String editShareFileResponse (JSONObject jsonObject){
        String userName = (String) jsonObject.get(USER_NAME);
        String fileID = (String) jsonObject.get(FILE_ID);
        String newFileAddress = (String) jsonObject.get(NEW_FILE_ADDRESS);
        String newFileComment = (String) jsonObject.get(NEW_COMMENT);

        boolean fileAddressEdit = editDB.updateFileAddress((userName + "Received"), fileID, newFileAddress);
        boolean fileCommentEdit = editDB.updateFileComment((userName + "Received"), fileID, newFileComment);

        return jsonMaker.editShareFileJSON(fileAddressEdit && fileCommentEdit);    }

    private String deleteFileResponse (JSONObject jsonObject){
        String userName = (String) jsonObject.get(USER_NAME);
        String fileID = (String) jsonObject.get(FILE_ID);

        boolean response = editDB.deleteFromTable(userName,fileID);

        return jsonMaker.deleteFileJSON(response);
    }

    private String deleteShareFileResponse (JSONObject jsonObject){
        String userName = (String) jsonObject.get(USER_NAME);
        String fileID = (String) jsonObject.get(FILE_ID);

        boolean response = editDB.deleteFromTable((userName + "Received"), fileID);

        return jsonMaker.deleteShareFileJSON(response);
    }

    private String shareFileResponse (JSONObject jsonObject){
        UploadToDB uploadToDB = new UploadToDB();

        String userName = (String) jsonObject.get(USER_NAME);
        String fileId = (String) jsonObject.get(FILE_ID);
        String receiverUserName = (String) jsonObject.get(RECEIVER_USER_NAME);

        HashMap<String, String> file = tableManager.generateSingleInfo(userName, fileId, receiverUserName);

        if (file != null){
            uploadToDB.setFileAddress(file.get(FILE_ADDRESS));
            uploadToDB.setFileSize(file.get(FILE_SIZE));
            uploadToDB.setFileBytes(file.get(FILE_BYTES));
            uploadToDB.setFileComment(file.get(FILE_COMMENT));
            uploadToDB.setFileId(file.get(FILE_ID));
            boolean response = uploadToDB.uploadFile((receiverUserName + "Received"));
            if(!response) {
                return jsonMaker.shareFileJSON(false);
            } else {
                return jsonMaker.shareFileJSON(true);
            }
        } else {
            return jsonMaker.shareFileJSON(false);
        }
    }

    private String exitProgram (){
        return EXIT_PROGRAM;
    }

}
