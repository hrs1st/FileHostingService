package DatabaseManager.personalDatabase;

import DatabaseManager.Database;
import java.sql.*;


public class UploadToDB extends Database {

    private String fileId;
    private String fileAddress;
    private String fileSize;
    private String fileBytes;
    private String fileComment;

    public UploadToDB() {
        super();
    }

    public boolean uploadFile (String tableName) {
        if (0 != fileId.compareTo("")){
            EditDB updateDB = new EditDB();
            updateDB.updateFileSize(tableName, fileId, fileSize);
            updateDB.updateFileBytes(tableName, fileId, fileBytes);
            updateDB.updateFileComment(tableName, fileId, fileComment);
            return true;

        } else {
            try{
                String query = "INSERT INTO '" + tableName + "' VALUES (null, ?, ?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(2, fileSize);
                ps.setString(3, fileBytes);
                ps.setString(4, fileComment);
                ps.setString(1, fileAddress);
                ps.execute();
                ps.close();
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                System.out.println("Error in connecting to files table");
                return false;
            }
        }
    }

    public void setFileId(String fileId){
        this.fileId = fileId;
    }

    public String getFileId(){
        return fileId;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(String fileBytes) {
        this.fileBytes = fileBytes;
    }

    public String getFileComment() {
        return fileComment;
    }

    public void setFileComment(String fileComment) {
        this.fileComment = fileComment;
    }
}
