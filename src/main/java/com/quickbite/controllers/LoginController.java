package com.quickbite.controllers;

import java.io.IOException;

import com.quickbite.util.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both fields.");
            return;
        }

        boolean valid = Database.getInstance().validateLogin(username, password);

        if (valid) {
            try {
                // Load dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
                Parent root = loader.load();

                // Pass username to dashboard
                DashboardController dashboardController = loader.getController();
                dashboardController.setUsername(username);

                // Switch scene
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("QuickBite - Dashboard");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                messageLabel.setText("Error loading dashboard.");
            }
        } else {
            messageLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    private void handleRegister() throws IOException {
        // Switch to the Register screen
        Stage stage = (Stage) usernameField.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/register.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("QuickBite - Register");
        stage.show();
    }
}
