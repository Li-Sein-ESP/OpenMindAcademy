package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.UserService;
import utils.ChatBot;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Ensure UserService is instantiated to insert the admin user
            new UserService();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/connexion.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find /fxml/connexion.fxml");
            }
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass().getResource("/fxml/styles.css").toExternalForm());
            primaryStage.setTitle("User Management System");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load connexion.fxml: " + e.getMessage());
            e.printStackTrace();
            // Fallback UI to indicate failure
            javafx.scene.layout.VBox root = new javafx.scene.layout.VBox();
            root.getChildren().add(new javafx.scene.control.Label("Error: Failed to load login screen."));
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setMaximized(true);
        }
    }

    public static void main(String[] args) {

        launch(args);
    }
}