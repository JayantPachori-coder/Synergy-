package com.example.smartwastesegregator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRegistration {
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField userIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField MobileField; // Mobile field

    @FXML
    private Label MessageLabel;
    @FXML
    private Label Mute;
    @FXML
    private Button registerButton;
    @FXML
    private AnchorPane registrationPane;
    @FXML
    private Label invalidLoginLabel;
    @FXML
    private Button MuteButton;
    @FXML
    private Button VerifyButton;
    @FXML
    private Button BackButton;
    @FXML
    private MediaView mediaView;

    private MediaPlayer mediaPlayer;
    private boolean isMuted = false;

    @FXML
    public void initialize() {
        String videoPath = new File("F:\\visual\\SmartWasteSegregator\\src\\main\\resources\\com\\example\\smartwastesegregator\\images\\invideo-ai-1080 How to Register a New User in 60 Seconds 2024-10-17.mp4").toURI().toString();
        Media media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(mediaPlayer.getStartTime()));

        mediaPlayer.setMute(true);
        mediaPlayer.play();
    }

    @FXML
    private void handleMuteButtonAction(ActionEvent event) {
        isMuted = !isMuted;
        mediaPlayer.setMute(isMuted);
        Button muteButton = (Button) event.getSource();
        muteButton.setText(isMuted ? "Unmute" : "Mute");
    }

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String userId = userIdField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String mobile = MobileField.getText(); // Get mobile field value

        if (firstName.isEmpty() || lastName.isEmpty() || userId.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || mobile.isEmpty()) {
            MessageLabel.setText("Please enter complete details");
            invalidLoginLabel.setVisible(true);
            return;
        } else {
            handleVerifyButtonAction();
            MessageLabel.setText("Got it");
            invalidLoginLabel.setVisible(false);
        }

        if (password.equals(confirmPassword)) {
            try {
                Connection connection = DatabaseConnection.getConnection();
                String countQuery = "SELECT COUNT(*) AS rowcount FROM user_interface";
                PreparedStatement countStatement = connection.prepareStatement(countQuery);

                ResultSet resultSet = countStatement.executeQuery();
                resultSet.next();
                int rowCount = resultSet.getInt("rowcount");

                String query = "INSERT INTO user_interface (firstname, lastname, username, password, mobile_number) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, userId);
                preparedStatement.setString(4, password);
                preparedStatement.setString(5, mobile); // Set mobile field value

                preparedStatement.executeUpdate();
                connection.close();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
                redirectToLoginPage();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            MessageLabel.setText("Successfully Registered!");
        } else {
            VerifyButton.setText("Password not match!!");
            MessageLabel.setText("Please try again.");
        }
    }

    @FXML
    public void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent homePageRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(homePageRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectToLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent loginPane = loader.load();

            Stage stage = new Stage();
            stage.setTitle("User - Login");
            Scene scene = new Scene(loginPane, 600, 400);
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) registerButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleVerifyButtonAction() {
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (password.equals(confirmPassword)) {
            VerifyButton.setText("Password match");
        } else {
            VerifyButton.setText("Password not match!!");
        }
    }
}
