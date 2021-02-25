package DatabaseManager.personalDatabase;

import DatabaseManager.Database;
import java.sql.SQLException;
import java.sql.Statement;

public class EditDB extends Database {
    public EditDB() {
        super();
    }

    public boolean updateFileAddress (String tableName, String fileId, String newFileAddress) {
        try {
            Statement statement = super.connection.createStatement();
            statement.execute("UPDATE '" + tableName + "' SET fileAddress = '" + newFileAddress + "'" + " WHERE fileId = " + Integer.parseInt(fileId));
            statement.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error in connecting to files table");
            return false;
        }
    }

    public boolean updateFileSize (String tableName, String fileId, String newFileSize) {
        try {
            Statement statement = super.connection.createStatement();
            statement.execute("UPDATE '" + tableName + "' SET fileSize = '" + newFileSize + "'" + " WHERE fileId = " + Integer.parseInt(fileId));
            statement.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error in connecting to files table");
            return false;
        }
    }

    public boolean updateFileBytes (String tableName, String fileId, String newFileBytes) {
        try {
            Statement statement = super.connection.createStatement();
            statement.execute("UPDATE '" + tableName + "' SET fileBytes = '" + newFileBytes + "'" + " WHERE fileId = " + Integer.parseInt(fileId));
            statement.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error in connecting to files table");
            return false;
        }
    }

    public boolean updateFileComment (String tableName, String fileId, String newFileComment) {
        try {
            Statement statement = super.connection.createStatement();
            statement.execute("UPDATE '" + tableName + "' SET fileComment = '" + newFileComment + "'" + " WHERE fileId = " + Integer.parseInt(fileId));
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
            statement.execute("DELETE FROM '" + tableName + "' WHERE fileId = "+ Integer.parseInt(fileId));
            statement.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error in connecting to files table");
            return false;
        }
    }
}
