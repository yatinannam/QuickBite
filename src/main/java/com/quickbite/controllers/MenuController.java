package com.quickbite.controllers;

import com.quickbite.util.Database;
import com.quickbite.util.CartManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MenuController {

    @FXML
    private TableView<MenuItem> menuTable;
    @FXML
    private TableColumn<MenuItem, String> nameColumn;
    @FXML
    private TableColumn<MenuItem, Double> priceColumn;
    @FXML
    private TableColumn<MenuItem, String> descriptionColumn;
    @FXML
    private ComboBox<String> categoryFilter;
    @FXML
    private TextField searchField;

    private ObservableList<MenuItem> allItems;
    private ObservableList<MenuItem> filteredItems;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("price"));
        descriptionColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("description"));

        List<MenuItem> menuList = Database.getInstance().getMenuItems();
        allItems = FXCollections.observableArrayList(menuList);
        filteredItems = FXCollections.observableArrayList(allItems);
        menuTable.setItems(filteredItems);

        // Category choices
        categoryFilter.setItems(FXCollections.observableArrayList(
                "All", "Starters", "Main Course", "Beverages", "Desserts"));
        categoryFilter.setValue("All");

        // Double-click to add to cart
        menuTable.setRowFactory(tv -> {
            TableRow<MenuItem> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (!row.isEmpty() && e.getClickCount() == 2) {
                    MenuItem item = row.getItem();
                    CartManager.getInstance().addItem(item);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText(item.getName() + " added to cart!");
                    alert.showAndWait();
                }
            });
            return row;
        });
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase();
        String category = categoryFilter.getValue();

        filteredItems.setAll(allItems.stream()
                .filter(i -> (category.equals("All") || matchesCategory(i.getName(), category)))
                .filter(i -> i.getName().toLowerCase().contains(keyword)
                        || i.getDescription().toLowerCase().contains(keyword))
                .collect(Collectors.toList()));
    }

    private boolean matchesCategory(String name, String category) {
        return switch (category) {
            case "Starters" -> List.of("Garlic", "Soup", "Roll").stream().anyMatch(name::contains);
            case "Main Course" ->
                List.of("Biryani", "Rice", "Burger", "Pizza", "Paneer").stream().anyMatch(name::contains);
            case "Beverages" -> List.of("Coffee", "Tea", "Juice").stream().anyMatch(name::contains);
            case "Desserts" -> List.of("Brownie", "Jamun", "Ice").stream().anyMatch(name::contains);
            default -> true;
        };
    }

    @FXML
    private void handleViewCart() throws IOException {
        Stage stage = (Stage) menuTable.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/cart.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("QuickBite - Cart");
        stage.show();
    }

    @FXML
    private void handleBack() throws IOException {
        Stage stage = (Stage) menuTable.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/dashboard.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("QuickBite - Dashboard");
        stage.show();
    }
}
