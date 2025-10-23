package com.quickbite.controllers;

import com.quickbite.util.CartManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class CartController {

    @FXML
    private TableView<MenuItem> cartTable;
    @FXML
    private TableColumn<MenuItem, String> nameColumn;
    @FXML
    private TableColumn<MenuItem, Double> priceColumn;
    @FXML
    private TableColumn<MenuItem, String> descColumn;
    @FXML
    private TableColumn<MenuItem, Integer> quantityColumn; // new column (Qty controls)
    @FXML
    private TableColumn<MenuItem, Void> removeColumn; // remove button column
    @FXML
    private Label totalLabel;

    private ObservableList<MenuItem> cartItems;

    @FXML
    public void initialize() {
        // set up basic columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // populate data
        cartItems = FXCollections.observableArrayList(CartManager.getInstance().getItems());
        cartTable.setItems(cartItems);

        // interactive columns (qty + remove)
        if (quantityColumn != null)
            addQuantityButtonsToTable();
        if (removeColumn != null)
            addRemoveButtonToTable();

        updateTotal();
    }

    // Adds + / - controls in the quantity column
    private void addQuantityButtonsToTable() {
        quantityColumn.setCellFactory(col -> new TableCell<MenuItem, Integer>() {
            private final Button minusBtn = new Button("-");
            private final Label qtyLabel = new Label();
            private final Button plusBtn = new Button("+");
            private final HBox box = new HBox(5, minusBtn, qtyLabel, plusBtn);

            {
                box.setStyle("-fx-alignment: center;"); // center the HBox
                minusBtn.setStyle("-fx-background-color: #ff8080; -fx-text-fill: white; -fx-font-weight: bold;");
                plusBtn.setStyle("-fx-background-color: #80c080; -fx-text-fill: white; -fx-font-weight: bold;");

                // minus action
                minusBtn.setOnAction(e -> {
                    int idx = getIndex();
                    if (idx >= 0 && idx < getTableView().getItems().size()) {
                        MenuItem item = getTableView().getItems().get(idx);
                        if (item.getQuantity() > 1) {
                            item.setQuantity(item.getQuantity() - 1);
                            qtyLabel.setText(String.valueOf(item.getQuantity()));
                            updateTotal();
                        }
                    }
                });

                // plus action
                plusBtn.setOnAction(e -> {
                    int idx = getIndex();
                    if (idx >= 0 && idx < getTableView().getItems().size()) {
                        MenuItem item = getTableView().getItems().get(idx);
                        item.setQuantity(item.getQuantity() + 1);
                        qtyLabel.setText(String.valueOf(item.getQuantity()));
                        updateTotal();
                    }
                });
            }

            @Override
            protected void updateItem(Integer qty, boolean empty) {
                super.updateItem(qty, empty);
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    MenuItem item = getTableView().getItems().get(getIndex());
                    qtyLabel.setText(String.valueOf(item.getQuantity()));
                    setGraphic(box);
                }
            }
        });
    }

    // Adds a Remove button into each row
    private void addRemoveButtonToTable() {
        Callback<TableColumn<MenuItem, Void>, TableCell<MenuItem, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<MenuItem, Void> call(final TableColumn<MenuItem, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Remove");

                    {
                        btn.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-weight: bold;");
                        btn.setOnAction(event -> {
                            int idx = getIndex();
                            if (idx >= 0 && idx < getTableView().getItems().size()) {
                                MenuItem item = getTableView().getItems().get(idx);
                                // remove from manager and observable list
                                CartManager.getInstance().removeItem(item);
                                cartItems.remove(item);
                                updateTotal();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }
                };
            }
        };
        removeColumn.setCellFactory(cellFactory);
    }

    private void updateTotal() {
        double total = CartManager.getInstance().getItems().stream()
                .mapToDouble(i -> i.getPrice() * (i.getQuantity() <= 0 ? 1 : i.getQuantity()))
                .sum();
        totalLabel.setText(String.format("Total: ₹%.2f", total));
    }

    @FXML
    private void handleClear() {
        CartManager.getInstance().clear();
        cartItems.clear();
        updateTotal();
    }

    @FXML
    private void handleCheckout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Placed!");
        alert.setHeaderText(null);
        alert.setContentText("Thank you for your order!\nYour food will be ready soon.");
        alert.showAndWait();

        CartManager.getInstance().clear();
        cartItems.clear();
        updateTotal();
    }

    @FXML
    private void handleBack() throws IOException {
        Stage stage = (Stage) cartTable.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/menu.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("QuickBite - Menu");
        stage.show();
    }
}
