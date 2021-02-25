package DatabaseManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.print.attribute.HashAttributeSet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class TableManager extends Database {

    public static final String IMAGES = "images";
    public static final String VIDEOS = "videos";
    public static final String MUSICS = "musics";
    public static final String DOCUMENTS = "documents";

    public static final String FILE_ID = "fileId";
    public static final String FILE_NAME = "fileName";
    public static final String FILE_PATH = "filePath";
    public static final String FILE_ADDRESS = "fileAddress";
    public static final String FILE_SIZE = "fileSize";
    public static final String FILE_COMMENT = "fileComment";
    public static final String IS_DOWNLOAD = "isDownload";

    public static final String ABSENCE = "absence";

    public void deleteTable(String tableName) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE TABLE " + tableName);
            statement.close();
        } catch (SQLException e) {e.printStackTrace();}
    }

    public ArrayList<HashMap<String, String>> generateFilesInfo(String tableName) {
        ArrayList<HashMap<String, String>> filesInfo = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet fileIdColumn = statement.executeQuery("SELECT * FROM " + tableName + " " + FILE_ID);
            ResultSet fileNameColumn = statement.executeQuery("SELECT * FROM " + tableName + " " + FILE_NAME);
            ResultSet filePathColumn = statement.executeQuery("SELECT * FROM " + tableName + " " + FILE_PATH);
            ResultSet fileSizeColumn = statement.executeQuery("SELECT * FROM " + tableName + " " + FILE_SIZE);
            ResultSet fileCommentColumn = statement.executeQuery("SELECT * FROM " + tableName + " " + FILE_COMMENT);

            while (fileIdColumn.next()) {
                HashMap<String,String> map = new HashMap<>();

                map.put(FILE_ID, fileIdColumn.getString(1) );
                map.put(FILE_ADDRESS, filePathColumn.getString(3) + "\\" + fileNameColumn.getString(2));
                map.put(FILE_SIZE, fileSizeColumn.getString(4));
                map.put(FILE_COMMENT, fileCommentColumn.getString(5));
                filesInfo.add(map);
            }

            fileIdColumn.close();
            fileNameColumn.close();
            filePathColumn.close();
            fileCommentColumn.close();
            fileSizeColumn.close();

            statement.close();
            return filesInfo;

        } catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ArrayList<HashMap<String,String>> generateToShow(String tableName) {
        ArrayList<HashMap<String, String>> filesInfo = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet fileIdColumn = statement.executeQuery("SELECT * FROM " + tableName + " " + FILE_ID);
            ResultSet fileNameColumn = statement.executeQuery("SELECT * FROM " + tableName + " " + FILE_NAME);
            ResultSet filePathColumn = statement.executeQuery("SELECT * FROM " + tableName + " " + FILE_PATH);
            ResultSet fileSizeColumn = statement.executeQuery("SELECT * FROM " + tableName + " " + FILE_SIZE);
            ResultSet fileCommentColumn = statement.executeQuery("SELECT * FROM " + tableName + " " + FILE_COMMENT);
            ResultSet isDownloadColumn = statement.executeQuery("SELECT * FROM " + tableName + " " + IS_DOWNLOAD);

            while (fileIdColumn.next()) {
                HashMap<String,String> map = new HashMap<>();

                map.put(FILE_ID, fileIdColumn.getString(1) );
                map.put(FILE_NAME, fileIdColumn.getString(2) );
                map.put(FILE_PATH, filePathColumn.getString(3));
                map.put(FILE_SIZE, fileSizeColumn.getString(4));
                map.put(FILE_COMMENT, fileCommentColumn.getString(5));
                map.put(IS_DOWNLOAD, isDownloadColumn.getString(6));
                filesInfo.add(map);
            }

            fileIdColumn.close();
            fileNameColumn.close();
            filePathColumn.close();
            fileCommentColumn.close();
            fileSizeColumn.close();
            isDownloadColumn.close();

            statement.close();
            return filesInfo;

        } catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ArrayList<HashMap<String,String>> generateToShowByFormat(String tableName) {
        ArrayList<HashMap<String, String>> filesInfo = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet fileIdColumn = statement.executeQuery("SELECT * FROM " + tableName + " " + FILE_ID);
            ResultSet fileNameColumn = statement.executeQuery("SELECT * FROM " + tableName + " " + FILE_NAME);
            ResultSet filePathColumn = statement.executeQuery("SELECT * FROM " + tableName + " " + FILE_PATH);
            ResultSet fileSizeColumn = statement.executeQuery("SELECT * FROM " + tableName + " " + FILE_SIZE);
            ResultSet fileCommentColumn = statement.executeQuery("SELECT * FROM " + tableName + " " + FILE_COMMENT);

            while (fileIdColumn.next()) {
                HashMap<String,String> map = new HashMap<>();

                map.put(FILE_ID, fileIdColumn.getString(1) );
                map.put(FILE_NAME, fileIdColumn.getString(2) );
                map.put(FILE_PATH, filePathColumn.getString(3));
                map.put(FILE_SIZE, fileSizeColumn.getString(4));
                map.put(FILE_COMMENT, fileCommentColumn.getString(5));
                filesInfo.add(map);
            }

            fileIdColumn.close();
            fileNameColumn.close();
            filePathColumn.close();
            fileCommentColumn.close();
            fileSizeColumn.close();

            statement.close();
            return filesInfo;

        } catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String chooseFormat(String fileAddress) {
        String input = fileFormatGenerator(fileAddress);

        switch (input) {

            case "jpg" :
            case "png" :
            case "gif" :
                return IMAGES;

            case "mp4" :
            case "mkv" :
            case "avi" :
            case "mov" :
            case "wmv" :
                return VIDEOS;

            case "mp3" :
            case "ogg" :
            case "wav" :
                return MUSICS;

            default:
                return DOCUMENTS;
        }
    }

    private String fileFormatGenerator (String fileAddress) {
        char[] address = fileAddress.toCharArray();
        String format = "";

        for (int count = address.length - 1; count >= 0; count--) {
            if (address[count] != '.') {
                format += address[count];
            } else {
                break;
            }
        }

        StringBuffer fileFormat = new StringBuffer(format);
        return fileFormat.reverse().toString();
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

    public HashMap<String, String> fileFinder(String tableName, String field) {
        HashMap<String, String> fileInfo = new HashMap<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE fileId = '" + field + "'");
            ResultSet resultSet = ps.executeQuery();

            fileInfo.put(FILE_NAME, resultSet.getString(2));
            fileInfo.put(FILE_PATH, resultSet.getString(3));
            fileInfo.put(FILE_SIZE, resultSet.getString(4));
            fileInfo.put(FILE_COMMENT, resultSet.getString(5));
            fileInfo.put(IS_DOWNLOAD, resultSet.getString(6));
            ps.close();
            resultSet.close();

            return fileInfo;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String searchOnTable(String tableName, String path, String name) {
            try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM '" + tableName + "' WHERE filePath = '" + path + "' AND fileName = '" + name + "'");
            ResultSet rs = ps.executeQuery();

            String fileId = rs.getString(1);
            ps.close();
            rs.close();
            return fileId;

        } catch (SQLException e) {
            return "";
         }
    }

    public HashMap<String, String> getUserInfo(){
        try {
            HashMap<String, String> userInfo = new HashMap<>();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM userInfo");
            ResultSet rs = ps.executeQuery();

            String username = rs.getString(1);
            String inAppName = rs.getString(2);
            String profilePicture = rs.getString(3);

            ps.close();
            rs.close();

            userInfo.put("username", username);
            userInfo.put("inAppName", inAppName);
            userInfo.put("profilePicture", profilePicture);
            return userInfo;

        } catch (SQLException e) {
            return null;
        }
    }

}
