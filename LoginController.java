package com.example.smartwastesegregator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;  // Import this for FXMLLoader
import javafx.scene.Scene;       // Import this for Scene
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;        // Import this for Stage
import javafx.scene.Parent;
import javafx.scene.Node;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private AnchorPane loginPane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label invalidLoginLabel;

    @FXML
    private Button loginButton;

    @FXML
    private Button newUserButton;

    @FXML
    private Hyperlink forgotPasswordLink;

    @FXML
    private void handleManagement(ActionEvent event) {
        try {
            // Load the FXML for the management screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/smartwastesegregator/hello-view.fxml"));

            Parent root = loader.load();

            // Retrieve the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Action event for login button
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            invalidLoginLabel.setText("Please enter username and password");
            invalidLoginLabel.setVisible(true);
            return;
        }


        // Verify the user's credentials with the database
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM user_interface WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Credentials are correct; proceed to the next screen (e.g., home page)
                loadHomePage(event);

                // Close the current window or redirect to the home page
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.close();

                // Load the home page (implement this method based on your application structure)
                // loadHomePage();
            } else {
                // Credentials are incorrect
                invalidLoginLabel.setText("Invalid Username/Password");
                invalidLoginLabel.setVisible(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            invalidLoginLabel.setText("Database error. Try again.");
            invalidLoginLabel.setVisible(true);
        }
    }
    private void loadHomePage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
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

    // Action event for the "New User Registration" button
    @FXML
    private void handleNewUserButtonAction(ActionEvent event) {
        // Load the registration page
        try {
            // This will load your new registration FXML
            try {
                // Ensure that the path to the FXML file is correct relative to the resources folder.
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UserRegistration.fxml"));
                Parent root = loader.load();

                // You can switch the scene or open a new window depending on your logic
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleForgotPasswordLinkAction(ActionEvent event) {
        try {
            // Load the Forgot Password FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Password.fxml"));
            Parent forgotPasswordRoot = loader.load();

            // Open the Forgot Password scene in the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(forgotPasswordRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            invalidLoginLabel.setText("Failed to load Forgot Password page.");
            invalidLoginLabel.setVisible(true);
        }
    }
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



