package com.quickbite.controllers;

import com.quickbite.util.Database;
import org.mindrot.jbcrypt.BCrypt;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmField;
    @FXML
    private Label messageLabel;

    private final String DB_URL = "jdbc:sqlite:db/quickbite.db";

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String pass = passwordField.getText();
        String confirm = confirmField.getText();

        if (username.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            messageLabel.setText("All fields required!");
            return;
        }
        if (!pass.equals(confirm)) {
            messageLabel.setText("Passwords do not match!");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // check if user exists
            PreparedStatement check = conn.prepareStatement("SELECT id FROM users WHERE username=?");
            check.setString(1, username);
            if (check.executeQuery().next()) {
                messageLabel.setText("Username already exists!");
                return;
            }

            // insert new user
            String hashed = BCrypt.hashpw(pass, BCrypt.gensalt(12));
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO users (username, password, displayname) VALUES (?,?,?)");
            ps.setString(1, username);
            ps.setString(2, hashed);
            ps.setString(3, username);
            ps.executeUpdate();

            messageLabel.setText("Registration successful!");
            usernameField.clear();
            passwordField.clear();
            confirmField.clear();
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error saving user!");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("QuickBite - Login");
        stage.show();
    }
}
