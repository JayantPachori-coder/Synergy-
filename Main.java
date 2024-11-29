package com.example.smartwastesegregator;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.scene.input.KeyCode;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("Login.fxml"));

        // Load the scene graph from the FXML file
        Parent root = loader.load();

        // Get the screen size
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Set the scene to fill most of the screen dynamically
        Scene scene = new Scene(root, screenBounds.getWidth() * 0.52, screenBounds.getHeight() * 0.64); // 80% of screen width and height

        // Add an event filter to close the application when the "Esc" key is pressed
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                primaryStage.close(); // Close the stage when 'Esc' is pressed
            }
        });

        // Set the title and scene
        primaryStage.setTitle("User - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true); // Allow resizing if needed
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);  // Launches the JavaFX application
    }
}
