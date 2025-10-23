package com.quickbite.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Label userLabel;

    private String username;

    public void setUsername(String username) {
        this.username = username;
        userLabel.setText("User: " + username);
    }

    // ------------------- LOGOUT -------------------

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        Stage stage = (Stage) userLabel.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("QuickBite - Login");
        stage.show();
    }

    // ------------------- VIEW MENU -------------------

    // inside com.quickbite.controllers.DashboardController
    @FXML
    private void handleViewMenu(ActionEvent event) {
        try {
            // debug: show where Java is looking for the fxml
            java.net.URL url = getClass().getResource("/fxml/menu.fxml");
            System.out.println("DEBUG: menu.fxml URL = " + url);
            if (url == null) {
                throw new RuntimeException("menu.fxml not found at /fxml/menu.fxml");
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            Stage stage = (Stage) userLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("QuickBite - Menu");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // show brief user message as well
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Unable to open Menu");
            a.setContentText("See console for details.");
            a.showAndWait();
        }
    }
}
