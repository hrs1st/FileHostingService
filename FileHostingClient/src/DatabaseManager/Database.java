package DatabaseManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class Database {
    protected String databaseAddress = "jdbc:sqlite:\\" + System.getProperty("user.dir") + "\\" + "client.DB";
    protected Connection connection;

    public Database() {
        try {
            connection = DriverManager.getConnection(databaseAddress);
            Statement statement = connection.createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS userInfo (username TEXT, inAppName TEXT, profilePicture TEXT)");
            statement.execute("CREATE TABLE IF NOT EXISTS allFiles (fileId TEXT, fileName TEXT, filePath TEXT, fileSize TEXT, fileComment TEXT, isDownload TEXT)");
            statement.execute("CREATE TABLE IF NOT EXISTS receivedFiles (fileId TEXT, fileName TEXT, filePath TEXT, fileSize TEXT, fileComment TEXT, isDownload TEXT)");

            statement.execute("CREATE TABLE IF NOT EXISTS images (fileId TEXT, fileName TEXT, filePath TEXT, fileSize TEXT, fileComment TEXT)");
            statement.execute("CREATE TABLE IF NOT EXISTS videos (fileId TEXT, fileName TEXT, filePath TEXT, fileSize TEXT, fileComment TEXT)");
            statement.execute("CREATE TABLE IF NOT EXISTS musics (fileId TEXT, fileName TEXT, filePath TEXT, fileSize TEXT, fileComment TEXT)");
            statement.execute("CREATE TABLE IF NOT EXISTS documents (fileId TEXT, fileName TEXT, filePath TEXT, fileSize TEXT, fileComment TEXT)");

            statement.close();
        } catch (SQLException e) {System.out.println("Error in connecting with database");}
    }

    public void deleteClientDB() {
        try {
            Statement statement = connection.createStatement();

            statement.execute("DROP TABLE IF EXISTS 'allFiles'");
            statement.execute("DROP TABLE IF EXISTS 'userInfo'");
            statement.execute("DROP TABLE IF EXISTS 'receivedFiles'");
            statement.execute("DROP TABLE IF EXISTS 'images'");
            statement.execute("DROP TABLE IF EXISTS 'videos'");
            statement.execute("DROP TABLE IF EXISTS 'musics'");
            statement.execute("DROP TABLE IF EXISTS 'documents'");

            statement.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
