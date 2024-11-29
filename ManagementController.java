package com.example.smartwastesegregator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;



public class ManagementController {

    @FXML
    private TableView<Item> plasticTable;
    @FXML
    private TableColumn<Item, Integer> plasticIdColumn;
    @FXML
    private TableColumn<Item, Double> plasticDataColumn;
    @FXML
    private TextField plasticAmountField;

    @FXML
    private TableView<Item> metalTable;
    @FXML
    private TableColumn<Item, Integer> metalIdColumn;
    @FXML
    private TableColumn<Item, Double> metalDataColumn;
    @FXML
    private TextField metalAmountField;

    private ObservableList<Item> plasticData = FXCollections.observableArrayList();
    private ObservableList<Item> metalData = FXCollections.observableArrayList();

    private Item selectedPlasticItem;
    private Item selectedMetalItem;

    @FXML
    public void initialize() {
        // Set up columns for Plastic Table
        plasticIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        plasticDataColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        plasticTable.setItems(plasticData);

        // Set up columns for Metal Table
        metalIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        metalDataColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        metalTable.setItems(metalData);

        // Load data from the database
        loadPlasticDataFromDatabase();
        loadMetalDataFromDatabase();
    }

    private void loadPlasticDataFromDatabase() {
        Connection connection = connectToDatabase();
        if (connection != null) {
            try {
                String query = "SELECT id, amount FROM waste";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                plasticData.clear(); // Clear any previous data

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    double amount = resultSet.getDouble("amount");
                    plasticData.add(new Item(id, amount));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load plastic data.");
            }
        }
    }

    private void loadMetalDataFromDatabase() {
        Connection connection = connectToDatabase();
        if (connection != null) {
            try {
                String query = "SELECT id, amount FROM waste_metal";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                metalData.clear(); // Clear any previous data

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    double amount = resultSet.getDouble("amount");
                    metalData.add(new Item(id, amount));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load metal data.");
            }
        }
    }

    private Connection connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/login"; // Replace with your actual database details
            String user = "root"; // Replace with your DB username
            String password = "Jayant@2004"; // Replace with your DB password
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    public void onPlasticTableClick(MouseEvent event) {
        selectedPlasticItem = plasticTable.getSelectionModel().getSelectedItem();
        if (selectedPlasticItem != null) {
            plasticAmountField.setText(String.valueOf(selectedPlasticItem.getAmount()));
        }
    }

    @FXML
    public void onMetalTableClick(MouseEvent event) {
        selectedMetalItem = metalTable.getSelectionModel().getSelectedItem();
        if (selectedMetalItem != null) {
            metalAmountField.setText(String.valueOf(selectedMetalItem.getAmount()));
        }
    }

    @FXML
    public void updatePlasticData() {
        if (selectedPlasticItem != null) {
            try {
                double newAmount = Double.parseDouble(plasticAmountField.getText());
                selectedPlasticItem.setAmount(newAmount);
                plasticTable.refresh(); // Refresh the table view
                updatePlasticDataInDatabase(selectedPlasticItem);
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid number.");
            }
        } else {
            showAlert("Error", "Please select a plastic item first.");
        }
    }
    @FXML
    public void handleHome(ActionEvent event) {
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
    public void handleBackButtonAction(ActionEvent event) {

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


    @FXML
    public void deletePlasticData() {
        if (selectedPlasticItem != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Data");
            confirmationAlert.setHeaderText("Are you sure you want to delete this plastic item?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                deletePlasticDataFromDatabase(selectedPlasticItem);
                plasticData.remove(selectedPlasticItem);
            }
        } else {
            showAlert("Error", "Please select a plastic item first.");
        }
    }

    @FXML
    public void deleteAllPlasticData() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete All Data");
        confirmationAlert.setHeaderText("Are you sure you want to delete all plastic data?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteAllPlasticDataFromDatabase();
            plasticData.clear();
        }
    }

    @FXML
    public void updateMetalData() {
        if (selectedMetalItem != null) {
            try {
                double newAmount = Double.parseDouble(metalAmountField.getText());
                selectedMetalItem.setAmount(newAmount);
                metalTable.refresh();
                updateMetalDataInDatabase(selectedMetalItem);
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid number.");
            }
        } else {
            showAlert("Error", "Please select a metal item first.");
        }
    }

    @FXML
    public void deleteMetalData() {
        if (selectedMetalItem != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Data");
            confirmationAlert.setHeaderText("Are you sure you want to delete this metal item?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteMetalDataFromDatabase(selectedMetalItem);
                metalData.remove(selectedMetalItem);
            }
        } else {
            showAlert("Error", "Please select a metal item first.");
        }
    }

    @FXML
    public void deleteAllMetalData() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete All Data");
        confirmationAlert.setHeaderText("Are you sure you want to delete all metal data?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteAllMetalDataFromDatabase();
            metalData.clear();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updatePlasticDataInDatabase(Item item) {
        // Update the database with the new plastic amount for the selected item
        Connection connection = connectToDatabase();
        if (connection != null) {
            try {
                String query = "UPDATE waste SET amount = ? WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setDouble(1, item.getAmount());
                preparedStatement.setInt(2, item.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to update plastic data.");
            }
        }
    }

    private void updateMetalDataInDatabase(Item item) {
        // Update the database with the new metal amount for the selected item
        Connection connection = connectToDatabase();
        if (connection != null) {
            try {
                String query = "UPDATE waste_metal SET amount = ? WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setDouble(1, item.getAmount());
                preparedStatement.setInt(2, item.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to update metal data.");
            }
        }
    }

    private void deletePlasticDataFromDatabase(Item item) {
        // Delete the plastic item from the database
        Connection connection = connectToDatabase();
        if (connection != null) {
            try {
                String query = "DELETE FROM waste WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, item.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to delete plastic data.");
            }
        }
    }

    private void deleteAllPlasticDataFromDatabase() {
        // Delete all plastic data from the database
        Connection connection = connectToDatabase();
        if (connection != null) {
            try {
                String query = "DELETE FROM waste";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to delete all plastic data.");
            }
        }
    }

    private void deleteMetalDataFromDatabase(Item item) {
        // Delete the metal item from the database
        Connection connection = connectToDatabase();
        if (connection != null) {
            try {
                String query = "DELETE FROM waste_metal WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, item.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to delete metal data.");
            }
        }
    }

    private void deleteAllMetalDataFromDatabase() {
        // Delete all metal data from the database
        Connection connection = connectToDatabase();
        if (connection != null) {
            try {
                String query = "DELETE FROM waste_metal";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to delete all metal data.");
            }
        }
    }
    public class Item {
        private final Integer id;
        private Double amount;

        public Item(Integer id, Double amount) {
            this.id = id;
            this.amount = amount;
        }

        public Integer getId() {
            return id;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Amount: " + amount;
        }
    }
}

