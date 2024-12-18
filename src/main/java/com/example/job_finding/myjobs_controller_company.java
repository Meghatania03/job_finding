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

public class myjobs_controller_company {

    //@FXML
    //private VBox companyJobItemsVBox; // VBox to hold the company's job items

    private String loggedInCompanyUsername;

    @FXML
    private VBox jobItemsVBox;

    @FXML
    private ImageView dashboard_imageview; // For user's profile picture
    @FXML
    private Label dashboard_username;




    // Initialize method to set up the page for the company
    public void initializeMyJobs(String username) {
        this.loggedInCompanyUsername = username;
        loadUserProfile();
        loadCompanyJobs();
    }
    public void initializeapplicant_list(String username,String job_type) {
        this.loggedInCompanyUsername = username;
        System.out.println("initialize");
        loadUserProfile();
        loadApplicantsForJob(job_type);
    }
    public void loadUserProfile() {
        System.out.println("loadUserProfile");
        String query = "SELECT username, profile_picture FROM users WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1,loggedInCompanyUsername );
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



    public void loadCompanyJobs() {
        String query = """
                SELECT username, job_categories, industry, address,profile_picture
                FROM users
                WHERE username = ?""";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1,loggedInCompanyUsername );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Load the job item FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("job_items_company_own.fxml"));
                AnchorPane jobItemPane = loader.load();

                // Get the job item controller
                JobItemController_company jobItemController = loader.getController();
                byte[] profilePictureBytes = resultSet.getBytes("profile_picture");

                // Set job details to the controller
                jobItemController.setJobDataForOwnJobs(
                        resultSet.getString("username"),
                        resultSet.getString("industry"),
                        resultSet.getString("address"),
                        resultSet.getString("job_categories"),
                        profilePictureBytes,
                        loggedInCompanyUsername
                );


                // Add the job item to the VBox
                jobItemsVBox.getChildren().add(jobItemPane);
            }
            FXMLLoader addJobLoader = new FXMLLoader(getClass().getResource("add_job.fxml"));
            AnchorPane addJobPane = addJobLoader.load();

            jobItemsVBox.getChildren().add(addJobPane);
        } catch (Exception e) {
            System.out.println("Error loading company jobs: " + e.getMessage());
        }
    }
    public void loadApplicantsForJob(String jobType) {
        // // Clear the VBox to display only applicants
        System.out.println("loadApplicantsForJob");
        String query = """
            SELECT u.first_name, u.last_name, u.username, u.address, u.preferred_job_title, u.highest_qualification, u.institution_name, u.profile_picture
            FROM applications a
            INNER JOIN users u ON a.applicant_username = u.username
            WHERE a.company_username = ? AND a.job_category = ?
            """;

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, loggedInCompanyUsername);
            preparedStatement.setString(2, jobType);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString("first_name") + " " + resultSet.getString("last_name") + " " + resultSet.getString("address"));

                FXMLLoader loader = new FXMLLoader(getClass().getResource("applicant_list_view.fxml"));
                AnchorPane applicantPane = loader.load();


                JobItemController_company applicantController = loader.getController();


                byte[] profilePictureBytes = resultSet.getBytes("profile_picture");
                applicantController.setApplicantData(
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("username"),
                        resultSet.getString("address"),
                        resultSet.getString("preferred_job_title"),
                        resultSet.getString("highest_qualification"),
                        resultSet.getString("institution_name"),
                        profilePictureBytes
                );


                jobItemsVBox.getChildren().add(applicantPane);
            }
            if (jobItemsVBox.getChildren().isEmpty()) {
                System.out.println("No applicants found for job type: " + jobType);
            }
        } catch (Exception e) {
            System.out.println("Error loading applicants: " + e.getMessage());
        }


    }

    public void home_company_dashboard(ActionEvent event) throws IOException {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("company_dashboard.fxml"));
            Parent root = loader.load();
            //AnchorPane appliedJobsPane = loader.load();


            company_dashboard_controller companydashboardController = loader.getController();
            companydashboardController.initializeDashboard_company(loggedInCompanyUsername);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            //Scene scene = new Scene(root);
            stage.setScene(new Scene(root));
            stage.show();


        } catch (Exception e) {
            System.out.println("Error navigating to Applied Jobs: " + e.getMessage());
        }
    }

    public void logout3_company(ActionEvent event) {
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
