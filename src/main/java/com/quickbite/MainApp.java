package com.quickbite;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.quickbite.util.Database;
import javafx.scene.image.Image;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Database.getInstance().init();

        primaryStage.getIcons().add(
            new Image(getClass().getResourceAsStream("/images/icon.png"))
        );

        // Try to load the FXML
        var fxmlUrl = MainApp.class.getResource("/fxml/login.fxml");
        System.out.println("FXML Path: " + fxmlUrl);

        if (fxmlUrl == null) {
            throw new RuntimeException("⚠️ FXML file not found! Check the path /fxml/login.fxml");
        }

        Parent root = FXMLLoader.load(fxmlUrl);
        primaryStage.setTitle("QuickBite - Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Database.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
