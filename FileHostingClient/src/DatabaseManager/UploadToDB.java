package DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;


public class UploadToDB extends Database {

    public static final String FILE_ID = "fileId";
    public static final String FILE_NAME = "fileName";
    public static final String FILE_PATH = "filePath";
    public static final String FILE_SIZE = "fileSize";
    public static final String FILE_COMMENT = "fileComment";
    public static final String IS_DOWNLOAD = "isDownload";

    public UploadToDB() {
        super();
    }

    public boolean uploadFile(String tableName, HashMap<String,String> fileInfo) {
        if (fileInfo.get(FILE_ID) == ""){
            EditDB updateDB = new EditDB();
            updateDB.updateFileSize(tableName, fileInfo.get(FILE_ID), fileInfo.get(FILE_SIZE));
            updateDB.updateFileComment(tableName, fileInfo.get(FILE_ID), fileInfo.get(FILE_COMMENT));
            return true;

        } else {
            try{
                String query = "INSERT INTO '" + tableName + "' VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, fileInfo.get(FILE_ID));
                ps.setString(2, fileInfo.get(FILE_NAME));
                ps.setString(3, fileInfo.get(FILE_PATH));
                ps.setString(4, fileInfo.get(FILE_SIZE));
                ps.setString(5, fileInfo.get(FILE_COMMENT));
                ps.setString(6, fileInfo.get(IS_DOWNLOAD));

                ps.execute();
                ps.close();
                return true;

            } catch (SQLException e) {
                System.out.println("Error in connecting to files table");
                return false;
            }
        }

    }

    public boolean uploadByFormat(String tableName, HashMap<String,String> fileInfo) {
        if (fileInfo.get(FILE_ID) == ""){
            EditDB updateDB = new EditDB();
            updateDB.updateFileSize(tableName, fileInfo.get(FILE_ID), fileInfo.get(FILE_SIZE));
            updateDB.updateFileComment(tableName, fileInfo.get(FILE_ID), fileInfo.get(FILE_COMMENT));
            return true;

        } else {
            try{
                String query = "INSERT INTO '" + tableName + "' VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, fileInfo.get(FILE_ID));
                ps.setString(2, fileInfo.get(FILE_NAME));
                ps.setString(3, fileInfo.get(FILE_PATH));
                ps.setString(4, fileInfo.get(FILE_SIZE));
                ps.setString(5, fileInfo.get(FILE_COMMENT));

                ps.execute();
                ps.close();
                return true;

            } catch (SQLException e) {
                System.out.println("Error in connecting to files table");
                return false;
            }
        }
    }

    public boolean uploadUserInfo(String username, String inAppName, String profilePicture){
        try{
            String query = "INSERT INTO 'userInfo' VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, inAppName);
            ps.setString(3, profilePicture);
            ps.execute();

            ps.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error in connecting to userInfo table");
            return false;
        }
    }
}
