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

import java.util.ArrayList;
import java.util.HashMap;

public class ShareTable extends GUI {
    private ArrayList<HashMap<String,String>> fieldsOfTable;
    private String name;
    private String id = null;
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


    public ShareTable(ArrayList<HashMap<String,String>> fieldsOfTable,Stage window, String username){
        this.username = username;
        this.fieldsOfTable = fieldsOfTable;
        for (HashMap<String,String> file: fieldsOfTable) {
            name =  file.get("fileName");
            id =  file.get("fileId");
            size = file.get("fileSize");
            comment = file.get("fileComment");
            filepath = file.get("filePath");
            isDownloaded = file.get("isDownload");
            filesInfos.add(new FilesInfo(id,name,comment,size,filepath,isDownloaded));
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

        Button accept = new Button("ACCEPT");
        accept.setStyle(buttonColor);
        accept.setOnAction( e -> acceptClicked());


        Button editButton = new Button("EDIT");
        editButton.setOnAction(e -> editButtonClicked());
        editButton.setStyle(buttonColor);

        Button back = new Button("BACK");
        back.setStyle(buttonColor);
        back.setOnAction(e -> {
            setDataOnBack();
            mainScene(window);
        });


        HBox hBox = new HBox();
        hBox.getChildren().addAll(back, deleteFromApp, editButton,accept);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tableView);
        borderPane.setBottom(hBox);
        borderPane.setStyle(" -fx-background-color: #161e2b");


        Scene scene = new Scene(borderPane,500,500);
        window.setScene(scene);
        window.show();

        return tableView;

    }

    private void acceptClicked() {
        clientRunnable.setUsername(username);

        String id = tableView.getSelectionModel().getSelectedItem().getId();
        boolean isDone = clientRunnable.acceptProcess(id);
        if(isDone){
            ShowMessage.display("successful","ACCEPTED!");
            ObservableList<FilesInfo> filesSelected;
            filesInfos = tableView.getItems();
            filesSelected = tableView.getSelectionModel().getSelectedItems();
            if(!filesInfos.isEmpty()) {
                filesSelected.forEach(filesInfos::remove);
            }
            refreshTable();
        }else {
            ShowMessage.display("Error","Something went wrong");
        }

    }

    private void editButtonClicked() {
        ObservableList<FilesInfo> fileSelected;
        ArrayList<String> newValues = new ArrayList<>();
        fileSelected = tableView.getSelectionModel().getSelectedItems();
        for (FilesInfo file: fileSelected) {
            setEditScene(file.getName(),file.getComment(),file.getSize(),file.getFilePath(),file.getIsDownloaded());
        }
//        refreshTable();
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
        back.setOnAction(e -> setTable("Received files"));

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

    private void change(String newName, String newComment,String size,String filePath,String isDownloaded,String id) {
        clientRunnable.setUsername(username);
        HashMap<String, String> newRow = new HashMap<>();
        newRow.put("newFileName",newName);
        newRow.put("newComment",newComment);
        newRow.put("fileSize",size);
        newRow.put("filePath",filePath);
        newRow.put("isDownload",isDownloaded);
        newRow.put("fileId", id);

        filesInfos.add(new FilesInfo(null,newName,newComment,size,filePath,isDownloaded));
        deleteFromAppButtonClicked();

        ObservableList<FilesInfo> filesSelected;
        filesSelected = tableView.getSelectionModel().getSelectedItems();
        filesSelected.forEach(filesInfos::remove);
        boolean isDone = clientRunnable.editShareFileInfoProcess(newRow);//boolean
        if(!isDone){
            ShowMessage.display("Error", "Something went wrong.");
        }else {
            refreshTable();
        }
        setTable("SHARED FILES");
    }

    private void deleteFromAppButtonClicked() {
        clientRunnable.setUsername(username);

        ObservableList<FilesInfo> filesSelected;
        filesInfos = tableView.getItems();
        filesSelected = tableView.getSelectionModel().getSelectedItems();

        String id = tableView.getSelectionModel().getSelectedItem().getId();
        System.out.println(username + id);
        boolean isDone = clientRunnable.deleteShareFileProcess(id);
        if(!isDone){
            ShowMessage.display("Error", "Something went wrong.");
        }
        if(filesInfos != null) {
            filesSelected.forEach(filesInfos::remove);
        }
        refreshTable();
    }
}
