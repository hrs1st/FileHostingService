package ClientManager.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DownloadFilesTable extends GUI {
    private ArrayList<HashMap<String,String>> fieldsOfTable;
    private String name;
    private String id = null;
    private String size;
    private String filepath;
    private String comment;
    private Desktop desktop = Desktop.getDesktop();

    Stage window ;
    ObservableList<FilesInfo> filesInfos = FXCollections.observableArrayList();
    private String buttonColor = "-fx-background-color: #7cafc2;\n" +
            "    -fx-text-fill: #FFFF;\n" +
            "    -fx-background-radius: 4;";
    TableView<FilesInfo> tableView;

    private String username = "";

    public DownloadFilesTable(ArrayList<HashMap<String,String>> fieldsOfTable, Stage window, String username){
        this.username = username;
        this.fieldsOfTable = fieldsOfTable;
        for (HashMap<String,String> file: fieldsOfTable) {
            name =  file.get("fileName");
            id =  file.get("fileId");
            size = file.get("fileSize");
            comment = file.get("fileComment");
            filepath = file.get("filePath");
            filesInfos.add(new FilesInfo(id,name,comment,size,filepath,"True"));
        }
        this.window = window;


    }

    public TableView setTable(String title){


        window.setTitle(title);

        //Name column
        TableColumn<FilesInfo,String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));


        TableColumn<FilesInfo,String> commentColumn = new TableColumn<>("Comment");
        commentColumn.setMinWidth(100);
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("Comment"));


        TableColumn<FilesInfo,String> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setMinWidth(100);
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("Size"));

        TableColumn<FilesInfo,String> filePathColumn = new TableColumn<>("Filepath");
        filePathColumn.setMinWidth(100);
        filePathColumn.setCellValueFactory(new PropertyValueFactory<>("FilePath"));

        TableColumn<FilesInfo,String> isDownloaded = new TableColumn<>("IsDownloaded");
        isDownloaded.setMinWidth(100);
        isDownloaded.setCellValueFactory(new PropertyValueFactory<>("isDownloaded"));


        tableView = new TableView<>();
        tableView.setItems(filesInfos);
        tableView.setStyle("-fx-control-inner-background: #161e2b;");


        tableView.getColumns().addAll(nameColumn,commentColumn,sizeColumn,filePathColumn,isDownloaded);

        Button deleteFromApp = new Button("DELETE FROM APP");
        deleteFromApp.setOnAction(e -> deleteFromAppButtonClicked());
        deleteFromApp.setStyle(buttonColor);

        Button deleteFromDevice = new Button("DELETE FROM DEVICE");
        deleteFromDevice.setOnAction( e -> deleteFromDeviceClicked());
        deleteFromDevice.setStyle(buttonColor);

        Button shareFile = new Button("SHARE");
        shareFile.setStyle(buttonColor);
        shareFile.setOnAction(e -> shareFileClicked());


        Button editButton = new Button("EDIT");
        editButton.setOnAction(e -> editButtonClicked());
        editButton.setStyle(buttonColor);

        Button openButton = new Button("OPEN");
        openButton.setOnAction(e -> openFile(tableView.getSelectionModel().getSelectedItem().getFilePath(),tableView.getSelectionModel().getSelectedItem().getName()));
        openButton.setStyle(buttonColor);


        Button back = new Button("BACK");
        back.setStyle(buttonColor);
        back.setOnAction(e -> {
            setDataOnBack();
            mainScene(window);
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(back,deleteFromApp,deleteFromDevice,editButton,openButton,shareFile);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tableView);
        borderPane.setBottom(hBox);
        borderPane.setStyle(" -fx-background-color: #161e2b");



        Scene scene = new Scene(borderPane,500,500);
        window.setScene(scene);
        window.show();

        return tableView;

    }

    private void shareFileClicked() {
        clientRunnable.setUsername(username);
        String id = tableView.getSelectionModel().getSelectedItem().getId();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        Label username = new Label("Username");
        GridPane.setConstraints(username, 0, 0);
        username.setStyle("-fx-text-fill: #FFFF");

        //Name Input
        javafx.scene.control.TextField userInput = new javafx.scene.control.TextField();
        GridPane.setConstraints(userInput, 1, 0);

        Button send = new Button("SEND");
        GridPane.setConstraints(send,1,1);
        send.setMinWidth(160);
        send.setStyle(buttonColor);
        send.setOnAction( e-> {
            if(clientRunnable.shareFileProcess(userInput.getText(),id)){
                ShowMessage.display("SUCCESSFUL","SENT!");
            }else {
                ShowMessage.display("ERROR","SOMETHING WENT WRONG");
            }
        });


        Button back = new Button("Back");
        back.setStyle("-fx-background-color: #161e2b;\n" +
                "    -fx-text-fill: #FFFF;\n" +
                "    -fx-background-radius: 4;");
        back.setOnAction(e -> setTable("Downloaded Files"));

        grid.getChildren().addAll(username,send, userInput);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle(" -fx-background-color: #161e2b");

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle(" -fx-background-color: #161e2b");
        borderPane.setCenter(grid);
        borderPane.setBottom(back);

        Scene scene = new Scene(borderPane,500,500);
        window.setScene(scene);
        window.show();

    }

    private void deleteFromDeviceClicked() {
        clientRunnable.setUsername(username);
        ObservableList<FilesInfo> filesSelected;
        filesInfos = tableView.getItems();
        String id = tableView.getSelectionModel().getSelectedItem().getId();

        boolean isDone = clientRunnable.deleteFileFromDeviceProcess(id);
        if(!isDone){
            ShowMessage.display("ERROR","SOMETHING WENT WRONG!");
        }
        refreshTable();
    }

    private void openFile(String filePath, String name) {

        File file = new File(filePath + "\\" + name);
        try{
            this.desktop.open(file);
        }catch (IOException i){
            ShowMessage.display("error","Something went wrong");
        }

    }

    private void editButtonClicked() {
        clientRunnable.setUsername(username);
        ObservableList<FilesInfo> fileSelected;
        ArrayList<String> newValues = new ArrayList<>();
        fileSelected = tableView.getSelectionModel().getSelectedItems();
        for (FilesInfo file: fileSelected) {
            setEditScene(file.getName(),file.getComment(),file.getSize(),file.getFilePath(),file.getIsDownloaded());

        }

    }

    private void setEditScene(String name, String comment,String size,String filePath,String isDownloaded) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        //Name Label
        Label nameLabel = new Label("Name :");
        nameLabel.setId("bold-label");
        GridPane.setConstraints(nameLabel, 0, 0);
        nameLabel.setStyle("-fx-text-fill: #FFFF");

        //Name Input
        TextField nameInput = new TextField(name);
        GridPane.setConstraints(nameInput, 1, 0);

        Label commentLabel = new Label("Comment :");
        GridPane.setConstraints(commentLabel, 0, 1);


        TextField commentInput = new TextField(comment);
        GridPane.setConstraints(commentInput, 1, 1);

        Button editButton = new Button("EDIT");
        GridPane.setConstraints(editButton,0,2);
        editButton.setOnAction(e -> change(nameInput.getText(),commentInput.getText(),size,filePath, isDownloaded));

        Button back = new Button("Back");
        back.setStyle("-fx-background-color: #161e2b;\n" +
                "    -fx-text-fill: #FFFF;\n" +
                "    -fx-background-radius: 4;");
        back.setOnAction(e -> setTable("downloadFiles"));

        grid.getChildren().addAll(nameLabel,nameInput,commentInput,commentLabel,editButton,back);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle(" -fx-background-color: #161e2b");

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(grid);
        borderPane.setBottom(back);
        borderPane.setStyle(" -fx-background-color: #161e2b");

        Scene scene = new Scene(borderPane,500,500);
        window.setScene(scene);
        window.show();
    }

    private void change(String newName, String newComment,String size,String filePath, String isDownload) {
        HashMap<String,String> newRow = new HashMap<>();
        newRow.put("newFileName",newName);
        newRow.put("newComment",newComment);
        newRow.put("fileSize",size);
        newRow.put("filePath",filePath);
        newRow.put("fileId",id);
        newRow.put("isDownload", isDownload);

        boolean isDone = clientRunnable.editFileInfoProcess(newRow);
        if(!isDone){
            ShowMessage.display("ERROR","something went wrong");
        }else{
            filesInfos.add(new FilesInfo(null,newName,newComment,size,filePath,"TRUE"));
            ObservableList<FilesInfo> filesSelected;
            filesSelected = tableView.getSelectionModel().getSelectedItems();
            filesSelected.forEach(filesInfos::remove);
            refreshTable();
        }

        setTable("Download files");
    }

    private void deleteFromAppButtonClicked() {
        clientRunnable.setUsername(username);
        ObservableList<FilesInfo> filesSelected;
        filesInfos = tableView.getItems();
        filesSelected = tableView.getSelectionModel().getSelectedItems();
        String id = tableView.getSelectionModel().getSelectedItem().getId();

        if(ShowMessage.areYouSure("are you sure?","DELETE THIS FILE")){
            boolean isDone =clientRunnable.deleteFileProcess(id);
            if(filesInfos != null) {
                filesSelected.forEach(filesInfos::remove);
            }
            refreshTable();
        }

    }
}