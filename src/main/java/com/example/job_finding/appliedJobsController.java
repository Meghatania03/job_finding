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


public class appliedJobsController {
    @FXML
    private ImageView dashboard_imageview; // For user's profile picture
    @FXML
    private Label dashboard_username; // For user's username
    @FXML
    private VBox jobItemsVBox; // VBox to hold applied job items

    private String loggedInUsername;


    public void initializeAppliedJobs(String username) {
        this.loggedInUsername = username;
        loadUserProfile();
        loadAppliedJobs();
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


    private void loadAppliedJobs() {
        String query = """
                SELECT DISTINCT a.company_username, u.profile_picture, u.industry, u.address, u.job_categories
                FROM applications a
                INNER JOIN users u ON a.company_username = u.username AND u.job_categories = a.job_category
                WHERE a.applicant_username = ?""";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, loggedInUsername);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("job_items.fxml"));
                AnchorPane jobItemPane = loader.load();


                JobItemController jobItemController = loader.getController();


                jobItemController.setJobData(
                        resultSet.getString("company_username"),
                        resultSet.getString("industry"),
                        resultSet.getString("address"),
                        resultSet.getString("job_categories"),
                        resultSet.getBytes("profile_picture"),
                        loggedInUsername
                );


                jobItemsVBox.getChildren().add(jobItemPane);
            }
        } catch (Exception e) {
            System.out.println("Error loading applied jobs: " + e.getMessage());
        }
    }
    public void home_applicant_dashboard(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("applicant_dashboard.fxml"));
        Parent root = loader.load();


        Applicant_DashboardController applicantDashboardController = loader.getController();
        applicantDashboardController.initializeDashboard(loggedInUsername);


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void logout2_applicant(ActionEvent event) {
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
