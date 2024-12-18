package com.example.job_finding;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class company_dashboard_controller {
    @FXML
    private ImageView dashboard_imageview;
    @FXML
    private Label dashboard_username;
    @FXML
    private VBox jobItemsVBox;

    private String loggedInUsername;


    public void initializeDashboard_company(String username) {
        this.loggedInUsername = username;
        loadUserProfile();
        loadJobItems();
    }


    private void loadUserProfile() {
        String query = "SELECT username, profile_picture FROM users WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, loggedInUsername);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                dashboard_username.setText(resultSet.getString("username"));


                byte[] profilePicture = resultSet.getBytes("profile_picture");
                if (profilePicture != null) {
                    Image image = new Image(new ByteArrayInputStream(profilePicture));
                    dashboard_imageview.setImage(image);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading user profile: " + e.getMessage());
        }
    }


    private void loadJobItems() {
        String query = "SELECT username, industry, address,job_categories,profile_picture FROM users WHERE user_type = 'Company'";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("job_items_company.fxml"));
                AnchorPane jobItemPane_company = loader.load();


                JobItemController_company jobItemController_company = loader.getController();

                byte[] profilePictureBytes = resultSet.getBytes("profile_picture");



                jobItemController_company.setJobData_company(
                        resultSet.getString("username"),
                        resultSet.getString("industry"),
                        resultSet.getString("address"),
                        resultSet.getString("job_categories"),
                        profilePictureBytes,
                        loggedInUsername
                );


                jobItemsVBox.getChildren().add(jobItemPane_company);
            }
        } catch (Exception e) {
            System.out.println("Error loading job items: " + e.getMessage());
        }
    }
    public void goTomyjobs(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("myjobs_company.fxml"));
            AnchorPane myJobsPane = loader.load();


            myjobs_controller_company myJobsController = loader.getController();


            myJobsController.initializeMyJobs(loggedInUsername);


            dashboard_imageview.getScene().setRoot(myJobsPane);

    } catch (Exception e) {
            System.out.println("Error navigating to Applied Jobs: " + e.getMessage());
        }
    }

    public void logout1_company(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("page1.fxml"));
            Parent logout1 = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(logout1));
            //stage.setTitle("Add New Job");
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading page1: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
