package com.example.smartwastesegregator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Random;

public class ForgotPasswordController {

    @FXML private TextField usernameField;
    @FXML private TextField mobileField;
    @FXML private TextField otpField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private HBox otpSection;
    @FXML private VBox newPasswordSection;
    @FXML private HBox otpDisplaySection;  // This HBox will contain the OTP message
    @FXML private Label otpLabel;  // This Label will display the OTP

    private String generatedOtp;
    private String userMobile;

    // Method to handle OTP sending
    @FXML
    private void sendOtp() {
        String username = usernameField.getText();
        userMobile = mobileField.getText();

        if (username.isEmpty() || userMobile.isEmpty()) {
            showAlert("Error", "Please enter both username and mobile number.", Alert.AlertType.ERROR);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT mobile_number FROM user_interface WHERE username = ? AND mobile_number = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, userMobile);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Generate OTP
                generatedOtp = String.format("%04d", new Random().nextInt(10000));
                System.out.println("Generated OTP: " + generatedOtp); // For testing; remove in production

                // Display the OTP on the screen
                otpLabel.setText("Your OTP: " + generatedOtp);

                // Make OTP display section visible
                otpDisplaySection.setVisible(true);
                otpDisplaySection.setManaged(true);

                // Show OTP input section for the user to enter the OTP
                otpSection.setVisible(true);
                otpSection.setManaged(true);

                // Here you would send the OTP via SMS or Email
                // sendOtpToMobile(userMobile, generatedOtp);
            } else {
                showAlert("Error", "No user found with the given username and mobile number.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error occurred.", Alert.AlertType.ERROR);
        }
    }

    // Method to verify OTP
    @FXML
    private void verifyOtp() {
        String enteredOtp = otpField.getText();

        if (enteredOtp.equals(generatedOtp)) {
            // Show new password section
            newPasswordSection.setVisible(true);
            newPasswordSection.setManaged(true);
            showAlert("Success", "OTP verified successfully. Enter a new password.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Incorrect OTP. Please try again.", Alert.AlertType.ERROR);
        }
    }

    // Method to update password
    @FXML
    private void updatePassword(ActionEvent event) {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Please enter and confirm your new password.", Alert.AlertType.ERROR);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match.", Alert.AlertType.ERROR);
            return;
        }

        // Update password in the database
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE user_interface SET password = ? WHERE username = ? AND mobile_number = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, newPassword); // You may want to hash this before storing
            statement.setString(2, usernameField.getText());
            statement.setString(3, userMobile);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("Success", "Password updated successfully!", Alert.AlertType.INFORMATION);

                // Redirect to login page
                LoginPage(event);

            } else {
                showAlert("Error", "Failed to update password. Please try again.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error occurred.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void LoginPage(ActionEvent event) {
        // Login page redirection logic
    }

    @FXML
    public void BackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
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
