package DatabaseManager.Users;

import DatabaseManager.Database;
import DatabaseManager.personalDatabase.TableManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.HashMap;

public class UserDBManager extends Database{

    public static final String PASSWORD = "password" ;
    public static final String IN_APP_NAME = "inAppName";
    public static final String USER_NAME = "username";
    public static final String PROFILE_PICTURE = "profilePicture";

    public static final String ABSENCE = "User doesn't exist.";

    TableManager tableManager = new TableManager();

    public String searchUser(String need, String userName) {
        switch (need) {

            case PASSWORD:
                try {
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM 'users' WHERE userName = '" + userName + "'" );
                    ResultSet rs = ps.executeQuery();
                    String result =  rs.getString(3);
                    ps.close();
                    rs.close();
                    return result;

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    return ABSENCE;
                }

            case IN_APP_NAME:
                try {
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE userName = '" + userName + "'" );
                    ResultSet rs = ps.executeQuery();
                    String result =  rs.getString(1);
                    ps.close();
                    rs.close();
                    return result;

                } catch (SQLException e) {
                    return ABSENCE;
                }

            case PROFILE_PICTURE :
                try {
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM 'users' WHERE userName = '" + userName + "'" );
                    ResultSet rs = ps.executeQuery();
                    String result =  rs.getString(4);
                    ps.close();
                    rs.close();
                    return result;

                } catch (SQLException e) {
                    return ABSENCE;
                }

            case USER_NAME:
                try {
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM 'users' WHERE userName = '" + userName + "'" );
                    ResultSet rs = ps.executeQuery();
                    String uss = rs.getString(2);
                    ps.close();
                    rs.close();

                    if(uss.equals(userName)) {
                        return "User name taken.";
                    } else {
                        return ABSENCE;
                    }

                } catch (SQLException e) {
                    return ABSENCE;
                }

            default: return null;
        }
    }

    public HashMap<String, String> addUser(String userName, String password, String inAppName, String profilePicture) {
        HashMap<String, String> response = new HashMap<>();
        if (searchUser(USER_NAME,userName).equals(ABSENCE)){
            try {
                String nu = "";
                if (0 == profilePicture.compareTo(nu)) {
                    File defaultPic = new File("E:\\DatabaseServer\\defaultProfilePicture.png");
                    FileInputStream fis = new FileInputStream(defaultPic);

                    profilePicture = Base64.getEncoder().encodeToString(fis.readAllBytes());
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            createTable(userName);
            try {
                Statement statement = connection.createStatement();
                statement.execute("INSERT INTO 'users' VALUES ('" + inAppName + "','" + userName + "','" + password + "','" + profilePicture + "')");
                statement.close();

                response.put(PROFILE_PICTURE, profilePicture);
                response.put("situation", "successful");
                return response;

            } catch (SQLException e) {
                response.put(PROFILE_PICTURE, "");
                response.put("situation", "successful");
                return response;
            }
        } else {
            response.put(PROFILE_PICTURE, "");
            response.put("situation", "user name taken");
            return response;
        }
    }

    public boolean deleteUser(String userName) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM 'users' WHERE userName = '" + userName + "'" );
            statement.execute("DROP TABLE IF EXISTS '" + userName + "'");
            statement.execute("DROP TABLE IF EXISTS '" + userName + "Received" + "'");

            statement.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean updateUserInfo(String need, String userName, String input) {
        if (need.equals(PASSWORD)) {
            try {
                Statement statement = connection.createStatement();
                statement.execute("UPDATE 'users' SET password = '" + input + "' WHERE userName = '" + userName + "'");
                return true;

            } catch (SQLException e) {
                System.out.println("Error in connecting to users table");
                return false;
            }
        } else if (need.equals(IN_APP_NAME)) {
            try {
                Statement statement = connection.createStatement();
                statement.execute("UPDATE 'users' SET inAppName = '" + input + "' WHERE userName = '" + userName + "'");
                return true;

            } catch (SQLException e) {
                System.out.println("Error in connecting to users table");
                return false;
            }
        } else if(need.equals(PROFILE_PICTURE)) {
            try{
                Statement statement = connection.createStatement();
                statement.execute("UPDATE 'users' SET profilePicture = '" + input + "' WHERE userName = '" + userName + "'");
                return true;

            } catch (SQLException e) {
                System.out.println("Error in connecting to users table");
                return false;
            }
        } else {
            return false;
        }
    }


    public HashMap<String, String> logIn (String userName, String password){
        String userNameExistence = searchUser(USER_NAME, userName);
        HashMap<String, String> response = new HashMap<>();
        if (userNameExistence.equals(ABSENCE)){
            response.put("isSuccessful", "wrongUser");
            return response;
        } else {
            if (password.equals(searchUser(PASSWORD, userName))){
                String inAppName = searchUser(IN_APP_NAME, userName);
                String profilePicture = searchUser(PROFILE_PICTURE, userName);
                response.put("isSuccessful", "successful");
                response.put(IN_APP_NAME, inAppName);
                response.put(PROFILE_PICTURE, profilePicture);
                return response;
            } else {
                response.put("isSuccessful", "wrongPassword");
                return response;
            }
        }
    }

    private void createTable(String tableName) {
        tableManager.createTable(tableName);
    }

    public String getAbsence(){
        return ABSENCE;
    }
}
