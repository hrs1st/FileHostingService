package ClientManager.UI;
import ClientManager.ClientRunnable;
import ClientManager.SignFolder;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.plaf.nimbus.State;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

public class GUI extends Application {

    ArrayList<HashMap<String,String>> allFiles = new ArrayList<>();
    ArrayList<HashMap<String,String>> shareFiles = new ArrayList<>();
    ArrayList<HashMap<String,String>> imagesFiles = new ArrayList<>();
    ArrayList<HashMap<String,String>> musicFiles = new ArrayList<>();
    ArrayList<HashMap<String,String>> videoFiles = new ArrayList<>();
    ArrayList<HashMap<String,String>> documentFiles = new ArrayList<>();
    private String folderDirectoryPath;
    private File profilePicture;

    private String directory = generateDirectory();

    private String documentIcon = directory + "\\icons\\document.jpg";
    private String musicIcon =  directory + "\\icons\\music.png";
    private String photoIcon =  directory + "\\icons\\photo.png";
    private String videoIcon =  directory + "\\icons\\video.jpg";

    boolean valid = true;
    ArrayList<HashMap<String,String>> hashMaps;
    private ArrayList<File> chosenFiles;
    private HashMap<String, String> login;
    private HashMap<String, String> register;
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String IN_APP_NAME = "inAppName";
    public  String username = "";
    private String inAppName;
    private Button selectFolder;
    private String buttonColor = "-fx-background-color: #7cafc2;\n" +
            "    -fx-text-fill: #FFFF;\n" +
            "    -fx-background-radius: 4;";

    ClientRunnable clientRunnable = new ClientRunnable();


    private File profilePhoto;
    private String mainFolderPath;//todo

    Stage window;
    TreeView<String> tree;
    FileChooser fileChooser = null;
    TableView tableView1;




    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        setLoginScene();
    }

    public void mainScene(Stage window){
        this.window = window;
        refreshTable();
        TreeItem<String> root, setting, files;

        //Root
        root = new TreeItem<>();
        root.setExpanded(true);

        //Setting
        setting = makeBranch("Setting", root);
        makeBranch("Delete Account", setting);
        makeBranch("Edit Account", setting);
        makeBranch("Edit FolderPath", setting);

        //Files
        files = makeBranch("Files", root);
        makeBranch("Shared Files", files);
        makeBranch("All", files);

        VBox vBox = new VBox();
        vBox.setStyle(" -fx-background-color: #161e2b");

        Label inAppLabel = new Label();
        inAppLabel.setText(inAppName);

        Label userLabel = new Label();
        userLabel.setText("@"+username);
        userLabel.setStyle("-fx-text-fill: grey");

        inAppLabel.setStyle("-fx-text-fill: #FFFF");
        vBox.getChildren().addAll(inAppLabel,userLabel);

        //Create the tree and hide the main Root
        tree = new TreeView<>(root);
        tree.setShowRoot(false);
        tree.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if (newValue != null)
                        setSelectedScene(newValue.getValue());
                });
        tree.setStyle("-fx-control-inner-background: #161e2b;");

        ListView<Object> listView = new ListView<>();
        listView.setMinHeight(200);
        listView.setMinWidth(250);
        listView.setStyle("   -fx-background-color: #161e2b;\n" +
                "    -fx-text-fill: white;");
        ObservableList<Object> items = FXCollections.observableArrayList (vBox,"UploadFile","Refresh", tree);
        listView.setStyle("-fx-control-inner-background: #161e2b;");
        listView.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue)->{
            if(newValue.equals("UploadFile")){
//                String parentDirectory = clientRunnable.folderDirectoryGenerator(folderDirectoryPath);

                DirectoryChooser directoryChooser = new DirectoryChooser();
                File parentDirectory = directoryChooser.showDialog(window);

                fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(window);

                boolean isUploaded = clientRunnable.uploadProcess(file, parentDirectory.getAbsolutePath(), "0");

                if(isUploaded){
                    ShowMessage.display("Message","your file successfully uploaded!");
                    clientRunnable.filesCheckProcess();
                    refreshTable();
                }else{
                    ShowMessage.display("Message","something went wrong!");
                }
            }else if(newValue.equals("Refresh")){
                clientRunnable.filesCheckProcess();
                refreshTable();
            }

        });
        listView.setItems((ObservableList<Object>) items);

        //Layout
        StackPane layout = new StackPane();
        layout.setMaxWidth(150);
        layout.getChildren().addAll(listView);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        Button music = new Button();
        try {
            music = imageButton(music,new Image(new FileInputStream(musicIcon)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        music.setOnAction(e ->{
            DownloadFilesTable downloadFilesTable = new DownloadFilesTable(musicFiles,window, username);
            tableView1 = downloadFilesTable.setTable("Music files");

        });
        GridPane.setConstraints(music, 0, 0);

        Button photos = new Button();
        try {
            photos = imageButton(photos,new Image(new FileInputStream(photoIcon)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        photos.setOnAction(e ->{
            DownloadFilesTable downloadFilesTable = new DownloadFilesTable(imagesFiles,window, username);
            tableView1 = downloadFilesTable.setTable("Image files");

        });
        GridPane.setConstraints(photos, 1, 0);

        Button video = new Button();
        try {
            video = imageButton(video ,new Image(new FileInputStream(videoIcon)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        video.setOnAction(e ->{
            DownloadFilesTable downloadFilesTable = new DownloadFilesTable(videoFiles,window, username);
            tableView1 = downloadFilesTable.setTable("Video files");

        });
        GridPane.setConstraints(video, 0, 1);

        Button doc = new Button();
        try {
            doc = imageButton(doc,new Image(new FileInputStream(documentIcon)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        doc.setOnAction(e ->{
            DownloadFilesTable downloadFilesTable = new DownloadFilesTable(documentFiles,window, username);
            tableView1 = downloadFilesTable.setTable("Document files");

        });
        GridPane.setConstraints(doc, 1, 1);

        grid.getChildren().addAll(music, photos, video,doc);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle(" -fx-background-color: #161e2b");


        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(layout);
        borderPane.setCenter(grid);

        Scene scene = new Scene(borderPane, 800, 600);
        window.setScene(scene);
        window.show();
    }

    private void setShareFileScene() {
        ShareTable shareTable = new ShareTable(shareFiles,window, username);
        tableView1 = shareTable.setTable("Received files");
    }

    private void setSelectedScene(String selected) {

        switch (selected){
            case "Delete Account":
                clientRunnable.setUsername(username);
                boolean delete = ShowMessage.areYouSure("ARE YOU SURE?" ,"DELETE YOUR ACCOUNT?");
                if(delete){
                    clientRunnable.deleteAccountProcess();
                    System.exit(0);
                }

                break;
            case "Edit Account":
                editAccountScene(inAppName);
                break;
            case "Edit FolderPath":
                editFolderPathScene();
                break;
            case "All":
                System.out.println(allFiles);
                setAllFileTable("All files",allFiles);
                break;
            case "Shared Files":
                setShareFileScene();

        }
    }

    private void setAllFileTable(String title, ArrayList<HashMap<String,String>> allFiles) {
        clientRunnable.setUsername(username);
        AllFilesTable allFileTable = new AllFilesTable(allFiles,window, username);
        tableView1 = allFileTable.setTable(title);
    }

    private void editFolderPathScene() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectDirectory = directoryChooser.showDialog(window);
        clientRunnable.folderDirectoryProcess(selectDirectory.getAbsolutePath());
        folderDirectoryPath = selectDirectory.getAbsolutePath();
    }

    public String folderDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectDirectory = directoryChooser.showDialog(window);
        String path = selectDirectory.getAbsolutePath();
        return path;
    }

    private void editAccountScene(String inAppName) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        Label password = new Label("your password: ");
        GridPane.setConstraints(password,0,0);
        password.setStyle("-fx-text-fill: #FFFF");

        TextField passInput = new TextField();
        GridPane.setConstraints(passInput,1,0);

        //new Pass
        Label newPass = new Label("New Password");
        GridPane.setConstraints(newPass,0,1);
        newPass.setStyle("-fx-text-fill: #FFFF");

        TextField newPassInput = new TextField();
        GridPane.setConstraints(newPassInput,1,1);


        Label newInAppName = new Label("Name :");
        GridPane.setConstraints(newInAppName, 0, 2);
        newInAppName.setStyle("-fx-text-fill: #FFFF");

        //Name Input
        TextField nameInput = new TextField(inAppName);
        GridPane.setConstraints(nameInput, 1, 2);

        Button submit = new Button("SUBMIT");
        submit.setStyle(buttonColor);
        submit.setMinWidth(160);
        GridPane.setConstraints(submit,1,4);

        Button back = new Button("BACK");
        back.setStyle(buttonColor);
        GridPane.setConstraints(back,1,5);
        back.setOnAction(e -> mainScene(window));

        submit.setOnAction(e -> {
           boolean bool = clientRunnable.editUserInfoProcess(passInput.getText(),newPassInput.getText(),nameInput.getText(), null);
            if(bool) {
                this.inAppName = nameInput.getText();
            }
        });

        grid.getChildren().addAll(password,passInput,newPass,newPassInput,newInAppName,nameInput,submit,back);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle(" -fx-background-color: #161e2b");

        Scene scene = new Scene(grid,500,500);
        window.setScene(scene);
        window.show();
    }


    public Button imageButton(Button button, Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: transparent; -fx-padding: 2, 2, 2, 2;");

        button.setOnMousePressed(e -> button.setStyle("-fx-background-color: transparent; -fx-padding: 3 1 1 3;"));
        button.setOnMouseReleased(e -> button.setStyle("-fx-background-color: transparent; -fx-padding: 2, 2, 2, 2;"));

        return button;
    }


    //Create branches
    public TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    public void setLoginScene() {
        //GridPane with 10px padding around edge
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        //Name Label - constrains use (child, column, row)
        javafx.scene.control.Label nameLabel = new javafx.scene.control.Label("Username:");
        nameLabel.setId("bold-label");
        GridPane.setConstraints(nameLabel, 0, 0);
        nameLabel.setStyle("-fx-text-fill: #FFFF");

        //Name Input
        javafx.scene.control.TextField nameInput = new javafx.scene.control.TextField();
        GridPane.setConstraints(nameInput, 1, 0);

        //Password Label
        javafx.scene.control.Label passLabel = new Label("Password:");
        GridPane.setConstraints(passLabel, 0, 1);
        passLabel.setStyle("-fx-text-fill: #FFFF");

        //Password Input
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle("*");
        GridPane.setConstraints(passwordField, 1, 1);

        //Log In Button
        Button loginButton = new Button("Log In");
        loginButton.setStyle(buttonColor);
        loginButton.setMinWidth(160);
        GridPane.setConstraints(loginButton, 1, 2);
        loginButton.setOnAction(e ->
        {
            login = new HashMap<>();
            login.put(USER_NAME, nameInput.getText());
            login.put(PASSWORD, passwordField.getText());

            HashMap<String,String> userInfo =  clientRunnable.logInProcess(login);
            if (userInfo == null) {
                ShowMessage.display("error","Wrong input");
                setLoginScene();
            } else {
                this.username = nameInput.getText();
                inAppName = userInfo.get("inAppName");
                clientRunnable.setUsername(username);
                folderDirectoryPath = userInfo.get("folderDirectory");
//                try {
//                    FileOutputStream fos = new FileOutputStream(profilePicture);
//                    fos.write(Base64.getDecoder().decode((userInfo.get("profilePicture"))));
//                } catch (IOException ex){
//                    System.out.println(ex.getMessage());
//                }

//                String profile = userInfo.get("profilePicture");
//                byte[] profilePicBytes = Base64.getDecoder().decode(profile);
//                try {
//                    FileOutputStream fos = new FileOutputStream(profilePicture);
//                    fos.write(profilePicBytes);
//
//                } catch (IOException ex) {
//                    System.out.println(ex.getMessage());;
//                }

                if(folderDirectoryPath == ""){
                    folderDirectoryPath = folderDirectory();
                }

                SignFolder signFolder = new SignFolder();
                signFolder.setPath(folderDirectoryPath);
                signFolder.setUsername(username);
                Thread thread = new Thread(signFolder);
                thread.start();

                clientRunnable.filesCheckProcess();
                refreshTable();
                mainScene(window);
            }
        });

        //Sign Up Button
        Button signUp = new Button("Sign Up");
        signUp.setStyle(buttonColor);
        signUp.setMinWidth(160);
        GridPane.setConstraints(signUp, 1, 3);
        signUp.setOnAction(e -> signUpScene());

        grid.getChildren().addAll(nameLabel, nameInput, passLabel, passwordField, loginButton, signUp);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle(" -fx-background-color: #161e2b");

        Scene scene = new Scene(grid, 500, 500);
        window.setScene(scene);
        window.show();
    }

    public void signUpScene() {

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);


        //name in app label
        Label nameLabel = new Label("Name");
        nameLabel.setStyle("-fx-text-fill: #FFFF");
        GridPane.setConstraints(nameLabel, 0, 0);

        //name Input
        TextField nameInput = new TextField();
        GridPane.setConstraints(nameInput, 1, 0);


        //Username Label
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-text-fill: #FFFF");
        usernameLabel.setId("bold-label");
        GridPane.setConstraints(usernameLabel, 0, 1);

        //username Input
        TextField usernameInput = new TextField();
        GridPane.setConstraints(usernameInput, 1, 1);

        //Password Label
        Label passLabel = new Label("New Password:");
        passLabel.setStyle("-fx-text-fill: #FFFF");
        GridPane.setConstraints(passLabel, 0, 2);

        //Password Input
        TextField passInput = new TextField();
        passInput.setPromptText("new Password");
        GridPane.setConstraints(passInput, 1, 2);

        // Repeat Password Label
        Label rPassLabel = new Label("Repeat PassWord:");
        rPassLabel.setStyle("-fx-text-fill: #FFFF");
        GridPane.setConstraints(rPassLabel, 0, 3);

        //Repeat Password Input
        javafx.scene.control.TextField rPassInput = new TextField();
        rPassInput.setPromptText("repeat password");
        GridPane.setConstraints(rPassInput, 1, 3);

        //folder Directory
        Button folderDirectory = new Button("Folder Directory");
        GridPane.setConstraints(folderDirectory,1,5);
        folderDirectory.setStyle("-fx-background-color: black;\n" +
                "    -fx-text-fill: #FFFF;\n" +
                "    -fx-background-radius: 4;");
        folderDirectory.setMinWidth(160);
        folderDirectory.setOnAction(e -> {
            folderDirectoryPath = folderDirectory();
        });


        //profile picture
        Button profile = new Button("Set Profile");
        profile.setMinWidth(160);
        GridPane.setConstraints(profile,1,4);
        profile.setStyle("-fx-background-color: black;\n" +
                "    -fx-text-fill: #FFFF;\n" +
                "    -fx-background-radius: 4;");
        profile.setOnAction(e -> profilePicture = setProfile());

        //SignUp button
        Button signUp = new Button("Sign Up");
        signUp.setStyle(buttonColor);
        signUp.setMinWidth(160);
        GridPane.setConstraints(signUp, 1, 6);
        signUp.setOnAction(e -> {
            validate(folderDirectoryPath,profilePicture,usernameInput.getText(), passInput.getText(), rPassInput.getText(), nameInput.getText());
        });


        if (!valid) {
            Label incorrect = new Label("Invalid Password!");
            GridPane.setConstraints(incorrect, 1, 5);
            grid.getChildren().addAll(usernameLabel, usernameInput, passLabel, passInput, rPassLabel, rPassInput, signUp, incorrect, nameInput, nameLabel,folderDirectory,profile);
        } else {
            grid.getChildren().addAll(usernameLabel, usernameInput, passLabel, passInput, rPassLabel, rPassInput, signUp, nameInput, nameLabel,folderDirectory,profile);
        }
        grid.setAlignment(Pos.CENTER);
        grid.setStyle(" -fx-background-color: #161e2b");

        //back to login scene
        Button back = new Button("Back");
        back.setStyle("-fx-background-color: #161e2b;\n" +
                "    -fx-text-fill: #FFFF;\n" +
                "    -fx-background-radius: 4;");
        back.setOnAction(e -> setLoginScene());

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(grid);
        borderPane.setBottom(back);
        borderPane.setStyle(" -fx-background-color: #161e2b");

        //set new scene for sign up
        Scene scene = new Scene(borderPane, 500, 500);
//        scene.getStylesheets().add(getClass().getResource("Viper.css").toExternalForm());
        window.setScene(scene);
        window.show();

    }

    private void validate(String folderDirectoryPath,File profile,String username, String pass, String repeatPass, String nameInApp) {
       if (pass.equals(repeatPass) && username != "" && pass != "") {

            String profilePic = "";
            if(profile != null) {
                try {
                    FileInputStream fis = new FileInputStream(profile);
                    byte[] profileByteArray = fis.readAllBytes();
                    profilePic = Base64.getEncoder().encodeToString(profileByteArray);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            valid = true;
            register = new HashMap<>();
            register.put(USER_NAME, username);
            register.put(PASSWORD, pass);
            register.put(IN_APP_NAME, nameInApp);
            register.put("profilePicture", "");
            register.put("folderDirectory",folderDirectoryPath);

            this.username = username;
            HashMap<String,String> userInfo = clientRunnable.registerProcess(register);
            if(userInfo == null){
                signUpScene();
            }else {
                username = userInfo.get("username");
                inAppName = userInfo.get("inAppName");
                clientRunnable.setUsername(username);
                SignFolder signFolder = new SignFolder();
                signFolder.setPath(folderDirectoryPath);
                signFolder.setUsername(username);
                Thread thread = new Thread(signFolder);
                thread.start();
                System.out.println("sign up");
                String pp = userInfo.get("profilePicture");
                byte[] profilePicBytes = Base64.getDecoder().decode(pp);
                try {
                    FileOutputStream fos = new FileOutputStream(profilePicture);
                    fos.write(profilePicBytes);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());;
                }
                refreshTable();
                mainScene(window);
            }


        } else {
            valid = false;
            ShowMessage.display("Register Error", "Username taken before.");
            signUpScene();
        }

    }
    private Button proButton(Button button, Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
        imageView.setPreserveRatio(true);
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: transparent; -fx-padding: 2, 2, 2, 2;");

        button.setOnMousePressed(e -> button.setStyle("-fx-background-color: transparent; -fx-padding: 3 1 1 3;"));
        button.setOnMouseReleased(e -> button.setStyle("-fx-background-color: transparent; -fx-padding: 2, 2, 2, 2;"));

        return button;
    }

    private File setProfile() {
        fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(window);

        return file;
    }

    public void refreshTable(){
        allFiles = clientRunnable.getAllFilesTable();
        imagesFiles = clientRunnable.getImagesTable();
        videoFiles = clientRunnable.getVideosTable();
        musicFiles = clientRunnable.getMusicsTable();
        documentFiles = clientRunnable.getDocumentsTable();
        shareFiles = clientRunnable.getShareFilesTable();
    }

    public String generateDirectory() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.getProperty("user.dir"));
        return stringBuilder.toString();
    }

    public void setDataOnBack(){
        HashMap<String,String> userInfo = clientRunnable.autoSet();
        this.username = userInfo.get(USER_NAME);
        this.inAppName = userInfo.get(IN_APP_NAME);
        this.profilePicture = null;
    }
}