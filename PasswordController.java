package com.example.smartwastesegregator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class PasswordController {

    @FXML
    private PasswordField passwordField;  // Password field for user input
    @FXML
    private Label errorLabel;  // Label to show error messages
    @FXML
    private MediaView mediaView;  // MediaView to show the media (video/audio)

    private final String correctPassword = "admin";  // Correct password for verification
    private MediaPlayer mediaPlayer;  // MediaPlayer to control media playback

    // Initialize method to set up the media and play it when the page is loaded
    @FXML
    public void initialize() {
        String videoPath = new File("F:\\visual\\Segragator\\src\\main\\resources\\com\\example\\smartwastesegregator\\images\\Pass.mp4").toURI().toString();
        Media media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setAutoPlay(true);  // Start playing automatically
    }

    // Method to handle password verification
    @FXML
    protected void onVerifyButtonClick() {
        String enteredPassword = passwordField.getText();  // Get entered password

        if (enteredPassword.equals(correctPassword)) {
            // Password is correct, load the new page
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("metal.fxml"));  // New page FXML
                Scene newScene = new Scene(loader.load());
                Stage stage = (Stage) passwordField.getScene().getWindow();  // Get the current stage
                stage.setScene(newScene);  // Set the new scene (new page)
                stage.show();

                // Stop the media when navigating to the new page
                mediaPlayer.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Password does not match, show an error message
            errorLabel.setText("Password not matched. Please try again.");
        }
    }

    // Clean up media resources when this controller is no longer in use (e.g., stop playing media)

    @FXML
    public void handleBackButtonAction(ActionEvent event) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();  // Stop the media player
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent homePageRoot = loader.load();

            // Get the current stage and set the home page scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(homePageRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
