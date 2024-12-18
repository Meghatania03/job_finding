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

public class Applicant_DashboardController {
    @FXML
    private ImageView dashboard_imageview;
    @FXML
    private Label dashboard_username;
    @FXML
    private VBox jobItemsVBox;

    private String loggedInUsername;


    public void initializeDashboard(String username) {
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

                FXMLLoader loader = new FXMLLoader(getClass().getResource("job_items.fxml"));
                AnchorPane jobItemPane = loader.load();


                JobItemController jobItemController = loader.getController();

                byte[] profilePictureBytes = resultSet.getBytes("profile_picture");



                jobItemController.setJobData(
                        resultSet.getString("username"),
                        resultSet.getString("industry"),
                        resultSet.getString("address"),
                        resultSet.getString("job_categories"),
                        profilePictureBytes,
                        loggedInUsername
                );


                jobItemsVBox.getChildren().add(jobItemPane);
            }
        } catch (Exception e) {
            System.out.println("Error loading job items: " + e.getMessage());
        }
    }
    public void goToAppliedJobs(ActionEvent event) throws IOException {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("applied_jobs_applicant.fxml"));

            AnchorPane appliedJobsPane = loader.load();


            appliedJobsController appliedJobsController = loader.getController();


            appliedJobsController.initializeAppliedJobs(loggedInUsername);


            dashboard_imageview.getScene().setRoot(appliedJobsPane);
//
        } catch (Exception e) {
            System.out.println("Error navigating to Applied Jobs: " + e.getMessage());
        }
    }

    @FXML
    public void goto_profile_applicant(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("applicant_profile.fxml"));  // FXML file for applicant details view
        Parent root = loader.load();


        ApplicantDetailsController applicantDetailsController = loader.getController();


        applicantDetailsController.setApplicantDetails(loggedInUsername);


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void logout1_applicant(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("page1.fxml"));
            Parent logout1 = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(logout1));

            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading page1: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void gotoFAQ_page(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("FAQ.fxml"));
            Parent logout1 = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(logout1));

            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading page1: " + e.getMessage());
            e.printStackTrace();
        }
    }




}
