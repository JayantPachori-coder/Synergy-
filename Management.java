package com.example.smartwastesegregator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import java.io.IOException;

public class Management {

    @FXML
    private PasswordField passwordField;
    @FXML
    private Label invalidLoginLabel;
    @FXML
    private MediaView mediaView; // Reference to the MediaView in FXML

    private static final String VALID_PASSWORD = "admin"; // Password validation
    private MediaPlayer mediaPlayer; // MediaPlayer to control video playback

    @FXML
    public void initialize() {
        // Set up the media file and the MediaPlayer
        String mediaFile = "\"F:\\visual\\Segragator\\src\\main\\resources\\com\\example\\smartwastesegregator\\images\\Pass.mp4\""; // Update with the correct path to your media file
        Media media = new Media(mediaFile);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        // Start playing the media (e.g., a video) when the scene is loaded
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the video if desired
    }

    @FXML
    public void handleLogin() {
        String enteredPassword = passwordField.getText();

        if (enteredPassword.equals(VALID_PASSWORD)) {
            // If the password is correct, load the metal management page
            loadMetalPage();
        } else {
            // Show error message if password is incorrect
            showAlert("Login Error", "Incorrect password. Please try again.");
            invalidLoginLabel.setText("Invalid password. Please try again.");
        }
    }

    private void loadMetalPage() {
        try {
            // Load the metal page (metal.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/smartwastesegregator/metal.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) passwordField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Metal Data Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Metal Management page.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Stop media when needed (e.g., during logout or scene change)
        }
    }
}
