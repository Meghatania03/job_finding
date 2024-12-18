package com.example.job_finding;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("page1.fxml"));
        Scene scene=new Scene(root);
        //stage.setTitle("Hello!");
        stage.setScene(scene);
        //stage.setMaximized(true);
        stage.show();
    }


}