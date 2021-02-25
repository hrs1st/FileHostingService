package DatabaseManager;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class EditDB extends Database {
    public EditDB() {
        super();
    }

    public boolean updateFileName (String tableName, String fileId, String newFileName) {
        try {
            Statement statement = super.connection.createStatement();
            statement.execute("UPDATE " + tableName + " SET fileName = '" + newFileName + "'" + " WHERE fileId = "  + "'" + fileId + "'");
            statement.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error in connecting to files table");
            return false;
        }
    }

    public boolean updateFilePath (String tableName, String fileId, String newFilePath) {
        try {
            Statement statement = super.connection.createStatement();
            statement.execute("UPDATE " + tableName + " SET filePath = '" + newFilePath + "'" + " WHERE fileId = "  + "'" + fileId + "'");
            statement.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error in connecting to files table");
            return false;
        } //TODO
    } //todo

    public boolean updateFileSize (String tableName, String fileId, String newFileSize) {
        try {
            Statement statement = super.connection.createStatement();
            statement.execute("UPDATE " + tableName + " SET fileSize = '" + newFileSize + "'" + " WHERE fileId = "  + "'" + fileId + "'");
            statement.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error in connecting to files table");
            return false;
        }
    }

    public boolean updateFileComment (String tableName, String newFileComment, String fileId) {
        try {
            Statement statement = super.connection.createStatement();
            statement.execute("UPDATE '" + tableName + "' SET fileComment = '" + newFileComment + "'" + " WHERE fileId = " + "'" + fileId + "'");
            statement.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error in connecting to files table");
            return false;
        }
    }

    public boolean updateDownloadStatus(String tableName, String fileId, String newStatus) {
        try {
            Statement statement = super.connection.createStatement();
            statement.execute("UPDATE '" + tableName + "' SET isDownload = '" + newStatus + "'" + " WHERE fileId = " + "'" + fileId + "'");
            statement.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error in connecting to files table");
            return false;
        }
    }

    public boolean deleteFromTable(String tableName, String fileId) {
        try {
            Statement statement = super.connection.createStatement();
            statement.execute("DELETE FROM '" + tableName + "' WHERE fileId = '"+ fileId +"'");
            statement.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error in connecting to files table");
            return false;
        }
    }

    public boolean updateUserInfo(String username, String newInAppName, String profilePicture){
        try {
            Statement statement = super.connection.createStatement();
            statement.execute("UPDATE 'userInfo' SET inAppName = '" + newInAppName + "'" + " WHERE username = " + "'" + username + "'");
            statement.execute("UPDATE 'userInfo' SET profilePicture = '" + profilePicture + "'" + " WHERE username = " + "'" + username + "'");
            statement.close();

            return true;
        } catch (SQLException e){
            System.out.println("Error in connecting to files table");
            return false;
        }
    }
}
