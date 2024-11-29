package com.example.smartwastesegregator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Metal {
    @FXML
    private PieChart wastePieChart;

    @FXML
    private Text filledText;  // Text to show the filled waste percentage

    @FXML
    private Text unfilledText;  // Text to show the unfilled waste percentage

    @FXML
    private Button backButton;

    private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    private static final double TOTAL_CAPACITY = 100.0;  // Fixed capacity for plastic
    private List<Double> filledQuantities = new ArrayList<>();  // List to hold filled quantities
    private int currentIndex = 0;  // Track the current row index

    @FXML
    public void initialize() {
        System.out.println("Initializing the pie chart update");

        // Fetch data from the database initially
        fetchData();

        // Set up the pie chart with empty data at first
        wastePieChart.setData(pieChartData);

        // Start a timeline to refresh the pie chart every 5 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            System.out.println("Updating the pie chart...");
            updatePieChart();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
        timeline.play();  // Start the timeline
    }

    private void fetchData() {
        String query = "SELECT amount FROM waste_metal";  // Ensure this is correct

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Clear the list before populating
            filledQuantities.clear();

            // Populate the filledQuantities list with values from the database
            while (resultSet.next()) {
                double filledQuantity = resultSet.getDouble("amount");
                filledQuantities.add(filledQuantity);
            }
        } catch (SQLException e) {
            System.err.println("Database access error: " + e.getMessage());
            e.printStackTrace(); // Log the stack trace for debugging
        }
    }

    private void updatePieChart() {
        // Check if we have any data to display
        if (filledQuantities.isEmpty()) {
            System.out.println("No data available to display.");
            return;
        }

        // Get the current filled quantity and calculate unfilled
        double filledQuantity = filledQuantities.get(currentIndex);
        double unfilledQuantity = TOTAL_CAPACITY - filledQuantity;

        System.out.println("Filled Quantity (row " + (currentIndex + 1) + "): " + filledQuantity);
        System.out.println("Unfilled Quantity (row " + (currentIndex + 1) + "): " + unfilledQuantity);

        // Clear previous data
        pieChartData.clear();

        // Add data to the pie chart for this row
        pieChartData.add(new PieChart.Data("Filled", filledQuantity));
        pieChartData.add(new PieChart.Data("Unfilled", unfilledQuantity));

        // Update the text labels
        filledText.setText(String.format("Filled: %.2f%%", filledQuantity));
        unfilledText.setText(String.format("Unfilled: %.2f%%", unfilledQuantity));

        // Update the chart with the new data
        wastePieChart.setData(pieChartData);

        // Move to the next row for the next update
        currentIndex = (currentIndex + 1) % filledQuantities.size(); // Cycle through the list
    }

    @FXML
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
}
