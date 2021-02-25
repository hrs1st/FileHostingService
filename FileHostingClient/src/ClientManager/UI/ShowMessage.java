package ClientManager.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;


public class ShowMessage {

    private static boolean answer;


    public static void display(String title,String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(250);

        Label label = new Label();
        label.setText(message);
        label.setStyle("-fx-text-fill: #FFFF");

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle(" -fx-background-color: #161e2b");
        borderPane.setCenter(label);

        Scene scene = new Scene(borderPane,200,200);
        window.setScene(scene);
        window.showAndWait();


    }

    public static boolean areYouSure(String title,String message){

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);
        label.setStyle("-fx-text-fill: #FFFF");

        //Create two buttons
        Button yesButton = new Button("Yes");
        yesButton.setStyle("-fx-background-color: #7cafc2;\n" +
                "    -fx-text-fill: #FFFF;\n" +
                "    -fx-background-radius: 4;");
        Button noButton = new Button("No");
        noButton.setStyle("-fx-background-color: #7cafc2;\n" +
                "    -fx-text-fill: #FFFF;\n" +
                "    -fx-background-radius: 4;");

        //Clicking will set answer and close window
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();

        });
        noButton.setOnAction(e -> {
            answer = false;
            window.close();

        });

        VBox layout = new VBox(10);
        layout.setStyle(" -fx-background-color: #161e2b");

        //Add buttons
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout,200,200);
        window.setScene(scene);
        window.showAndWait();

        //Make sure to return answer
        return answer;





    }

}
