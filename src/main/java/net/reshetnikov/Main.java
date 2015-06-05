package net.reshetnikov;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage primaryStage;
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage=primaryStage;
            FXMLLoader loader = new FXMLLoader();
            loader.load(getClass().getClassLoader().getResourceAsStream("fxml/mainView.fxml"));
            Parent root = loader.getRoot();

            primaryStage.setTitle("Complex Value");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
