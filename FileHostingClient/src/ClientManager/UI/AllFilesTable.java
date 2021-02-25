package ClientManager.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class AllFilesTable extends GUI {
    private ArrayList<HashMap<String,String>> fieldsOfTable;
    private String name;
    private String id = "";
    private String size;
    private String filepath;
    private String comment;
    private String isDownloaded;

    Stage window ;
    ObservableList<FilesInfo> filesInfos = FXCollections.observableArrayList();
    private String buttonColor = "-fx-background-color: #7cafc2;\n" +
            "    -fx-text-fill: #FFFF;\n" +
            "    -fx-background-radius: 4;";
    TableView<FilesInfo> tableView;
    private String username = "";

    public AllFilesTable(ArrayList<HashMap<String,String>> fieldsOfTable,Stage window, String username){
        this.window = window;
        this.fieldsOfTable = fieldsOfTable;
        for (HashMap<String,String> file: fieldsOfTable) {
            name =  file.get("fileName");
            id =  file.get("fileId");
            size = file.get("fileSize");
            comment = file.get("fileComment");
            filepath = file.get("filePath");
            isDownloaded = file.get("isDownload");
            filesInfos.add(new FilesInfo(id,name,comment,size,filepath,isDownloaded));
            this.username = username;
        }

    }

    public TableView setTable(String title){

        this.fieldsOfTable = clientRunnable.getAllFilesTable();
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
        deleteFromDevice.setOnAction(e -> deleteFromDeviceClicked());
        deleteFromDevice.setStyle(buttonColor);


        Button editButton = new Button("EDIT");
        editButton.setOnAction(e -> editButtonClicked());
        editButton.setStyle(buttonColor);

        Button downloadButton = new Button("DOWNLOAD");
        downloadButton.setOnAction(e -> downloadButtonClicked(tableView.getSelectionModel().getSelectedItem().getId()));
        downloadButton.setStyle(buttonColor);

        Button back = new Button("BACK");
        back.setStyle(buttonColor);
        back.setOnAction(e -> {
            setDataOnBack();
            mainScene(window);
        });

        Button share = new Button("SHARE");
        share.setStyle(buttonColor);
        share.setOnAction( e -> shareFileClicked());

        HBox hBox = new HBox();
        hBox.getChildren().addAll(back, deleteFromApp, deleteFromDevice, editButton, downloadButton, share);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tableView);
        borderPane.setBottom(hBox);
        borderPane.setStyle(" -fx-background-color: #161e2b");


        Scene scene = new Scene(borderPane,500,500);
        window.setScene(scene);
        window.show();

        return tableView;

    }

    private void deleteFromDeviceClicked() {
        clientRunnable.setUsername(username);
        String id = tableView.getSelectionModel().getSelectedItem().getId();
        String isDownloaded = tableView.getSelectionModel().getSelectedItem().getIsDownloaded();
        boolean isDone;

        if(isDownloaded.equals("1")) {
            filesInfos = tableView.getItems();
            isDone = clientRunnable.deleteFileFromDeviceProcess(id);
            if(!isDone){
                ShowMessage.display("ERROR","SOMETHING WENT WRONG!");
                refreshTable();
                setTable("ALL FILES");
            } else {
                refreshTable();
                setTable("ALL FILES");
            }
        } else {
            ShowMessage.display("Error", "This file downloaded before.");
            refreshTable();
            setTable("ALL FILES");
        }
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
            System.out.println(id);
            if(clientRunnable.shareFileProcess(userInput.getText(),id)){
                ShowMessage.display("SUCCESSFUL","SENT!");
                refreshTable();
                setTable("ALL FILES");
            }else {
                ShowMessage.display("ERROR","SOMETHING WENT WRONG");
//                refreshTable();
                setTable("ALL FILES");
            }
        });

        Button back = new Button("Back");
        back.setStyle("-fx-background-color: #161e2b;\n" +
                "    -fx-text-fill: #FFFF;\n" +
                "    -fx-background-radius: 4;");
        back.setOnAction(e -> setTable("all files"));


        grid.getChildren().addAll(username,userInput,send);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle(" -fx-background-color: #161e2b");

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(grid);
        borderPane.setBottom(back);

        Scene scene = new Scene(borderPane,500,500);
        window.setScene(scene);
        window.show();

    }

    private void downloadButtonClicked(String id) {
        clientRunnable.setUsername(username);
        String isDownloaded = tableView.getSelectionModel().getSelectedItem().getIsDownloaded();
        if(isDownloaded.equals("0")){
            clientRunnable.downloadProcess(id);
            refreshTable();
            setTable("ALL FILES");
        }else{
            ShowMessage.display("error","This file downloaded before");
            refreshTable();
            setTable("ALL FILES");
        }

    }


    private void editButtonClicked() {
        ObservableList<FilesInfo> fileSelected;
        ArrayList<String> newValues = new ArrayList<>();
        fileSelected = tableView.getSelectionModel().getSelectedItems();
        for (FilesInfo file: fileSelected) {
            setEditScene(file.getName(),file.getComment(),file.getSize(),file.getFilePath(),file.getIsDownloaded());
        }
        refreshTable();
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
        editButton.setOnAction(e -> change(nameInput.getText(),commentInput.getText(),size,filePath,isDownloaded, id));

        Button back = new Button("Back");
        back.setStyle("-fx-background-color: #161e2b;\n" +
                "    -fx-text-fill: #FFFF;\n" +
                "    -fx-background-radius: 4;");
        back.setOnAction(e -> setTable("all files"));

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

    private void change(String newName, String newComment,String size,String filePath,String isDownloaded, String id) {
        clientRunnable.setUsername(username);
        HashMap<String, String> newRow = new HashMap<>();
        newRow.put("newFileName",newName);
        newRow.put("newComment",newComment);
        newRow.put("fileSize",size);
        newRow.put("filePath",filePath);
        newRow.put("isDownload",isDownloaded);
        newRow.put("fileId", id);

        filesInfos.add(new FilesInfo(id,newName,newComment,size,filePath,isDownloaded));
        ObservableList<FilesInfo> filesSelected;
        filesSelected = tableView.getSelectionModel().getSelectedItems();
        filesSelected.forEach(filesInfos::remove);

        boolean isDone = clientRunnable.editFileInfoProcess(newRow);//boolean
        if(!isDone){
            ShowMessage.display("Error", "Something went wrong.");
        }else {
            refreshTable();
        }
        setTable("ALL FILES");
    }

    private void deleteFromAppButtonClicked() {
        clientRunnable.setUsername(username);

        ObservableList<FilesInfo> filesSelected;
        filesInfos = tableView.getItems();
        filesSelected = tableView.getSelectionModel().getSelectedItems();

        String id = tableView.getSelectionModel().getSelectedItem().getId();
        boolean isDone = clientRunnable.deleteFileProcess(id);
        if(!isDone){
            ShowMessage.display("Error", "Something went wrong.");
        }
        if(filesInfos != null) {
            filesSelected.forEach(filesInfos::remove);
        }
        refreshTable();
        setTable("ALL FILES");
    }
}
