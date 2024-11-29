package com.example.smartwastesegregator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutUs {

    @FXML
    private Hyperlink MailHyperlink;
    @FXML
    private Button BackButton;
    @FXML
    private Hyperlink Morelink;

    @FXML
    private ImageView instagramImage; // New ImageView for Instagram
    @FXML
    private ImageView linkedinImage;  // New ImageView for LinkedIn

    @FXML
    private void handleMailLinkClick(ActionEvent event) {
        if (Desktop.isDesktopSupported()) {
            try {
                // Create a mailto link
                URI mailto = new URI("mailto:SmartDustbin@gmail.com");

                // Open the default mail application with the link
                Desktop.getDesktop().mail(mailto);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleBackButtonAction(ActionEvent event) {
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

    @FXML
    private void handleMoreAboutLink(ActionEvent event) {
        openWebPage("https://sites.google.com/view/smart-sorter12/home");
    }

    // Click event handler for Instagram icon
    @FXML
    private void handleInstagramLinkClick() {
        openWebPage("https://www.instagram.com/smart.dustbin?utm_source=ig_web_button_share_sheet&igsh=ZDNlZDc0MzIxNw=="); // Replace with actual Instagram URL
    }

    // Click event handler for LinkedIn icon
    @FXML
    private void handleLinkedInLinkClick() {
        openWebPage("https://www.linkedin.com/in/jayant-pachori-44b763275/"); // Replace with actual LinkedIn URL
    }

    // Helper method to open URLs in the default browser
    private void openWebPage(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper method to load additional pages
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
