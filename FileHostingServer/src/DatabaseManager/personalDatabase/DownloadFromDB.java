package DatabaseManager.personalDatabase;

import DatabaseManager.Database;
import java.sql.*;
import java.util.HashMap;

public class DownloadFromDB extends Database {
    private String fileId;

    public static final String FILE_ID = "fileId";
    public static final String FILE_ADDRESS = "fileAddress";
    public static final String FILE_SIZE = "fileSize";
    public static final String FILE_BYTES = "fileBytes";
    public static final String FILE_COMMENT = "fileComment";

    public DownloadFromDB() {
        super();
    }

    public HashMap<String, String> download(String tableName) {
        HashMap<String, String> fileInformation = new HashMap<>();

        try{
            PreparedStatement ps = super.connection.prepareStatement("SELECT * FROM '" + tableName +"' WHERE fileId = " + fileId);
            ResultSet rs = ps.executeQuery();

            fileInformation.put(FILE_ID, fileId);
            fileInformation.put(FILE_ADDRESS, rs.getString(2));
            fileInformation.put(FILE_SIZE, rs.getString(3));
            fileInformation.put(FILE_BYTES, rs.getString(4));
            fileInformation.put(FILE_COMMENT, rs.getString(5));

            ps.close();
            rs.close();

        } catch(Exception e){
            System.out.println(e.getMessage());
        }

        return fileInformation;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}