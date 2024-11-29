package com.example.smartwastesegregator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class Message {
    @FXML
    private MediaView mediaView; // MediaView for displaying video

    private MediaPlayer mediaPlayer; // MediaPlayer to control playback

    // Initialize method to set up media playback
    @FXML
    public void initialize() {
        String videoPath = new File("F:\\visual\\SmartWasteSegregator\\src\\main\\resources\\com\\example\\smartwastesegregator\\images\\Message.mp4").toURI().toString(); // Update with your video path
        Media media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.play(); // Start playing the video
    }
    // Handle OK button action
    @FXML
    private void handleOkButtonAction(ActionEvent event) {
        loadPage(event, "PreviousPage.fxml"); // Replace with your actual previous page's FXML name
    }

    // Helper method to load the FXML page
    private void loadPage(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Smart Waste Management System");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
