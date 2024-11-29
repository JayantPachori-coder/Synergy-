package com.example.smartwastesegregator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Hyperlink;
import java.io.IOException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import java.io.File;
public class Home {
    @FXML
    private MediaView mediaView;
    @FXML
    private Button PlasticButton;
    @FXML
    private Button MetalButton;
    @FXML
    private Hyperlink AboutUsHyperlink;
    @FXML
    private Button CloseButton;

    public void handlePlasticButtonAction(ActionEvent event) {
        loadPage(event, "Plastic.fxml");
    }

    // Navigate to Metal section
    public void handleMetalButtonAction(ActionEvent event) {
        loadPage(event, "Metal1.fxml");
    }

    // Close the application
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void handelHyperlinkAction(ActionEvent event) {
            loadPage(event, "AboutUs.fxml");
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
    public void initialize() {
        String videoPath = new File("F:\\visual\\SmartWasteSegregator\\src\\main\\resources\\com\\example\\smartwastesegregator\\images\\177166-857004203.mp4").toURI().toString(); // Update the path
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
    }

}
