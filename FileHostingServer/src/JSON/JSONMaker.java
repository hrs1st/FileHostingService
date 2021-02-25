package JSON;

import java.util.ArrayList;
import java.util.HashMap;

public class JSONMaker {

    private StringBuilder stringBuilder;

    public static final String IN_APP_NAME = "inAppName";
    public static final String PROFILE_PICTURE = "profilePicture";

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
    public static final String WRONG_INPUT = "wrongInput";

    public static final String USER_NAME = "username";
    public static final String SITUATION = "situation";
    public static final String SUCCESSFUL = "successful";
    public static final String ERROR = "error";

    public static final String FILE_ID = "fileId";
    public static final String FILE_ADDRESS = "fileAddress";
    public static final String FILE_SIZE = "fileSize";
    public static final String FILE_BYTES = "fileBytes";
    public static final String FILE_COMMENT = "fileComment";

    public String registerJSON(HashMap<String,String> userInfo, HashMap<String,String> string) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(REGISTER).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(REGISTER).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append(SITUATION).append("\"");
        stringBuilder.append(":");

        if(string.get(SITUATION).equals(SUCCESSFUL)){
            stringBuilder.append("\"").append(SUCCESSFUL).append("\"");
        } else if (string.get(SITUATION).equals("user name taken")) {
            stringBuilder.append("\"").append("repetitive").append("\"");
        } else {
            stringBuilder.append("\"").append(ERROR).append("\"");
        }
        stringBuilder.append(",");
        stringBuilder.append("\"").append(USER_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(userInfo.get(USER_NAME)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(IN_APP_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(userInfo.get(IN_APP_NAME)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(PROFILE_PICTURE).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(string.get(PROFILE_PICTURE)).append("\"");
        stringBuilder.append("}");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public String editUserJSON(boolean bool) {
        return booleanJSON(bool, EDIT_USER);
    }

    public String deleteAccountJSON(boolean bool) {
        return booleanJSON(bool, DELETE_ACCOUNT);
    }

    public String loginJSON(HashMap<String, String> login) { //todo inAppName
        stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(LOGIN).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(LOGIN).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append("isSuccessful").append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(login.get("isSuccessful")).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(IN_APP_NAME).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(login.get(IN_APP_NAME)).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(PROFILE_PICTURE).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(login.get(PROFILE_PICTURE)).append("\"");
        stringBuilder.append("}");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public String fileCheckerJSON(ArrayList<HashMap<String, String>> mainFiles, ArrayList<HashMap<String, String>> shareFiles) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(FILE_CHECKER).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append("mainFiles").append("\"");
        stringBuilder.append(":");
        stringBuilder.append("[");

        for (int counter = 0; counter < mainFiles.size(); counter++) {
            stringBuilder.append("{");
            stringBuilder.append("\"").append(FILE_ID).append("\"");
            stringBuilder.append(":");
            stringBuilder.append("\"").append(mainFiles.get(counter).get(FILE_ID)).append("\"");
            stringBuilder.append(",");
            stringBuilder.append("\"").append(FILE_ADDRESS).append("\"");
            stringBuilder.append(":");
            stringBuilder.append("\"").append(mainFiles.get(counter).get(FILE_ADDRESS)).append("\"");
            stringBuilder.append(",");
            stringBuilder.append("\"").append(FILE_SIZE).append("\"");
            stringBuilder.append(":");
            stringBuilder.append("\"").append(mainFiles.get(counter).get(FILE_SIZE)).append("\"");
            stringBuilder.append(",");
            stringBuilder.append("\"").append(FILE_COMMENT).append("\"");
            stringBuilder.append(":");
            stringBuilder.append("\"").append(mainFiles.get(counter).get(FILE_COMMENT)).append("\"");
            stringBuilder.append("}");

            if (counter == mainFiles.size() - 1) {
                stringBuilder.append("]");
            } else {
                stringBuilder.append(",");
            }
        }
        if(mainFiles.size() == 0){
            stringBuilder.append("]");
        }

        stringBuilder.append(",");
        stringBuilder.append("\"").append("shareFiles").append("\"");
        stringBuilder.append(":");
        stringBuilder.append("[");

        for (int counter = 0; counter < shareFiles.size(); counter++) {
            stringBuilder.append("{");
            stringBuilder.append("\"").append(FILE_ID).append("\"");
            stringBuilder.append(":");
            stringBuilder.append("\"").append(shareFiles.get(counter).get(FILE_ID)).append("\"");
            stringBuilder.append(",");
            stringBuilder.append("\"").append(FILE_ADDRESS).append("\"");
            stringBuilder.append(":");
            stringBuilder.append("\"").append(shareFiles.get(counter).get(FILE_ADDRESS)).append("\"");
            stringBuilder.append(",");
            stringBuilder.append("\"").append(FILE_SIZE).append("\"");
            stringBuilder.append(":");
            stringBuilder.append("\"").append(shareFiles.get(counter).get(FILE_SIZE)).append("\"");
            stringBuilder.append(",");
            stringBuilder.append("\"").append(FILE_COMMENT).append("\"");
            stringBuilder.append(":");
            stringBuilder.append("\"").append(shareFiles.get(counter).get(FILE_COMMENT)).append("\"");
            stringBuilder.append("}");

            if (counter == shareFiles.size() - 1) {
                stringBuilder.append("]");
            } else {
                stringBuilder.append(",");
            }
        }
        if(shareFiles.size() == 0){
            stringBuilder.append("]");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public String uploadToServerJSON(boolean bool) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(UPLOAD_TO_SERVER).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(UPLOAD_TO_SERVER).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append(SITUATION).append("\"");
        stringBuilder.append(":");

        if (bool) {
            stringBuilder.append("\"").append(SUCCESSFUL).append("\"");
        } else {
            stringBuilder.append("\"").append(ERROR).append("\"");
        }

        stringBuilder.append("}");
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    public String downloadFromServerJSON(HashMap<String, String> fileInformation) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(DOWNLOAD_FROM_SERVER).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(DOWNLOAD_FROM_SERVER).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
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
    }

    public String editFileJSON(boolean bool) {
        return booleanJSON(bool, EDIT_FILE);
    }

    public String editShareFileJSON(boolean bool) {
        return booleanJSON(bool, EDIT_SHARE_FILE);
    }

    public String shareFileJSON(boolean bool) {
        return booleanJSON(bool, SHARE_FILE);
    }

    public String deleteFileJSON(boolean bool) {
        return booleanJSON(bool, DELETE_FILE);
    }

    public String deleteShareFileJSON(boolean bool) {
        return booleanJSON(bool, DELETE_SHARE_FILE);
    }

    public String acceptShareFile(boolean bool) {
        return booleanJSON(bool, ACCEPT_SHARE_FILE);
    }

    public String wrongInputJSON() {
        stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(WRONG_INPUT).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(WRONG_INPUT).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(ERROR).append("\"");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private String booleanJSON(boolean bool, String function) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"").append(FUNCTION).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"").append(function).append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"").append(function).append("\"");
        stringBuilder.append(":");
        stringBuilder.append("{");
        stringBuilder.append("\"").append(SITUATION).append("\"");
        stringBuilder.append(":");

        if (bool) {
            stringBuilder.append("\"").append(SUCCESSFUL).append("\"");
        } else {
            stringBuilder.append("\"").append(ERROR).append("\"");
        }
        stringBuilder.append("}");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}