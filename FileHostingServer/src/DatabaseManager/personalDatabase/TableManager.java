package DatabaseManager.personalDatabase;

import DatabaseManager.Database;
import DatabaseManager.Users.UserDBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TableManager extends Database {

    public static final String FILE_ID = "fileId";
    public static final String FILE_ADDRESS = "fileAddress";
    public static final String FILE_SIZE = "fileSize";
    public static final String FILE_BYTES = "fileBytes";
    public static final String FILE_COMMENT = "fileComment";
    public static final String FILE_NAME = "fileName";
    public static final String FILE_PATH = "filePath";

    public void createTable(String tableName) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS '" + tableName + "' (fileId INTEGER PRIMARY KEY, fileAddress TEXT, fileSize REAL, fileBytes TEXT, fileComment TEXT)");
            statement.execute("CREATE TABLE IF NOT EXISTS '" + tableName + "Received' " + " (fileId INTEGER PRIMARY KEY, fileAddress TEXT, fileSize REAL, fileBytes TEXT, fileComment TEXT)");

            statement.close();

        } catch (SQLException e) {e.printStackTrace();}
    }

    public ArrayList<HashMap<String, String>> generateFilesInfo(String userName) {
        ArrayList<HashMap<String , String>> filesInfo = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();

            ResultSet fileIdColumn = statement.executeQuery("SELECT * FROM '" + userName + "' " + FILE_ID);
            ResultSet fileAddressColumn = statement.executeQuery("SELECT * FROM '" + userName + "' " + FILE_ADDRESS);
            ResultSet fileSizeColumn = statement.executeQuery("SELECT * FROM '" + userName + "' " + FILE_SIZE);
            ResultSet fileCommentColumn = statement.executeQuery("SELECT * FROM '" + userName + "' " + FILE_COMMENT);

            while (fileAddressColumn.next()) {
                HashMap<String,String> map = new HashMap<>();

                String fileAddress = fileAddressColumn.getString(2);

                if (fileAddress != null) {
                    map.put(FILE_ID, Integer.toString(fileIdColumn.getInt(1)));
                    map.put(FILE_ADDRESS, fileAddress);
                    map.put(FILE_SIZE, fileSizeColumn.getString(3));
                    map.put(FILE_COMMENT, fileCommentColumn.getString(5));
                    filesInfo.add(map);
                }
            }

            fileAddressColumn.close();
            fileCommentColumn.close();
            fileSizeColumn.close();

            statement.close();

            return filesInfo;

        } catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public HashMap<String,String> generateSingleInfo(String userName, String fileId, String receiverUserName) {

        UserDBManager userDBManager = new UserDBManager();
        String userNameExistence = userDBManager.searchUser(UserDBManager.USER_NAME, receiverUserName);
        if (userNameExistence.equals(userDBManager.getAbsence())){
            return null;
        } else {
            try {
                Statement statement = connection.createStatement();

                ResultSet fileInfoColumn = statement.executeQuery("SELECT * FROM '" + userName + "' WHERE fileId = " + Integer.parseInt(fileId));

                HashMap<String, String> map = new HashMap<>();
                String fileName = fileNameAndPathGenerator(fileInfoColumn.getString(2)).get(FILE_NAME);
                String filePath = fileNameAndPathGenerator(fileInfoColumn.getString(2)).get(FILE_PATH);
                map.put(FILE_ADDRESS, filePath + "\\" + fileId + "0000" + fileName);
                map.put(FILE_SIZE, fileInfoColumn.getString(3));
                map.put(FILE_BYTES, fileInfoColumn.getString(4));
                map.put(FILE_COMMENT, fileInfoColumn.getString(5));
                map.put(FILE_ID, "");

                statement.close();

                return map;

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
    }

    public HashMap<String, String> fileNameAndPathGenerator (String fileAddress) {
        HashMap<String, String> nameAndPath = new HashMap<>();
        char[] address = fileAddress.toCharArray();
        String name = "";

        for (int count = address.length - 1; count >= 0; count--) {
            if (address[count] != '\\') {
                name += address[count];
            } else {
                StringBuffer fileName = new StringBuffer(name);
                nameAndPath.put(FILE_NAME, fileName.reverse().toString());
                nameAndPath.put(FILE_PATH, fileAddress.substring(0,count));
                break;
            }
        }
        return nameAndPath;
    }
}
