package com.example.job_finding;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;

public class JobItemController_company {
    @FXML
    private Label company_name_ji;
    @FXML
    private Label job_type_ji;
    @FXML
    private Label industry_ji;
    @FXML
    private Label address_ji;
    @FXML
    private Button apply_ji;
    @FXML
    private ImageView imageview_ji;  // Add this for the company profile pictureprivate String loggedInUsername; // Applicant username

    private String loggedInUsername; // Applicant username
    private String companyUsername;
    private String jobtype;
    @FXML
    private Button viewApplicantsBtn;

    //@FXML
    //private Button viewJobsBtn;

    @FXML
    private Label first_name_ji;
    @FXML
    private Label last_name_ji1;
    @FXML
    private Label address_ji_applicant;
    @FXML
    private Label pref_job_applicant;
    @FXML
    private Label qualification_applicant;
    @FXML
    private Label uni_applicant;
    @FXML
    private ImageView dashboard_imageview;

    private String applicantUsername;














    public void setJobData_company(String companyName, String industry, String address, String jobType, byte[] profilePicture,String loggedInUser) {

        this.companyUsername = companyName;
        this.loggedInUsername = loggedInUser;

        company_name_ji.setText(companyName);
        industry_ji.setText(industry);
        address_ji.setText(address);
        job_type_ji.setText(jobType);
        if (profilePicture != null) {
            Image image = new Image(new ByteArrayInputStream(profilePicture));
            imageview_ji.setImage(image);

        }


    }

    public void setJobDataForOwnJobs(String companyName, String industry, String address, String jobType, byte[] profilePicture, String loggedInUser) {
        company_name_ji.setText(companyName);
        industry_ji.setText(industry);
        address_ji.setText(address);
        job_type_ji.setText(jobType);

        this.companyUsername = companyName;
        this.loggedInUsername = loggedInUser;
        this.jobtype = jobType;
        if (profilePicture != null) {
            Image image = new Image(new ByteArrayInputStream(profilePicture));
            imageview_ji.setImage(image);
        }



    }

    public void setApplicantData(String firstName, String lastName,String applicant_Username, String address, String preferredJob, String qualification, String university, byte[] profilePicture ) {
        first_name_ji.setText(firstName);
        last_name_ji1.setText(lastName);
        applicantUsername = applicant_Username;
        System.out.println(applicantUsername);

        address_ji_applicant.setText(address);
        pref_job_applicant.setText(preferredJob);
        qualification_applicant.setText(qualification);
        uni_applicant.setText(university);

        if (profilePicture != null) {
            Image image = new Image(new ByteArrayInputStream(profilePicture));
            imageview_ji.setImage(image);
        }
    }
    public void goTo_view_applicant_list(ActionEvent event) throws IOException {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("company_view_applicant_list.fxml"));
            //root = loader.load();
            AnchorPane appliedJobsPane = loader.load();


            myjobs_controller_company viewJobsController = loader.getController();
            viewJobsController.initializeapplicant_list(loggedInUsername, jobtype);
            viewApplicantsBtn.getScene().setRoot(appliedJobsPane);


        } catch (Exception e) {
            System.out.println("Error navigating to Applied Jobs: " + e.getMessage());
        }
    }

    @FXML
    public void viewDetails(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("applicant_details.fxml"));  // FXML file for applicant details view
        Parent root = loader.load();


        ApplicantDetailsController applicantDetailsController = loader.getController();


        applicantDetailsController.setApplicantDetails(applicantUsername);


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void handleAddJobButtonClick(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_job_process.fxml"));
            Parent addJobPage = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(addJobPage));
            stage.setTitle("Add New Job");
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading Add Job process page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private Button delete_own_job_company; // The delete button

    public void deleteOwnJob(ActionEvent event) {


        String jobtype = job_type_ji.getText();

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to delete this job?");
        confirmationAlert.setContentText("This action cannot be undone.");


        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            String deleteQuery = "DELETE FROM users WHERE username = ? AND job_categories = ?";
            String deleteQuery2 = "DELETE FROM applications WHERE company_username = ? AND job_category = ?";

            try (Connection connection = Database.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                 PreparedStatement preparedStatement2 = connection.prepareStatement(deleteQuery2)) {


                preparedStatement.setString(1, companyUsername);
                preparedStatement.setString(2, jobtype);

                preparedStatement2.setString(1, companyUsername);
                preparedStatement2.setString(2, jobtype);

                preparedStatement2.executeUpdate();
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Deletion Successful");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Job deleted successfully.");
                    successAlert.show();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("myjobs_company.fxml"));
                        Parent root = loader.load();


                        myjobs_controller_company myJobsController = loader.getController();


                        myJobsController.initializeMyJobs(loggedInUsername);

                        Stage stage = (Stage) delete_own_job_company.getScene().getWindow();
                        //Scene scene = new Scene(root);
                        stage.setScene(new Scene(root));
                        stage.show();


//                    // Remove the job item from the UI
//                    Node sourceNode = (Node) event.getSource();
//                    AnchorPane jobItemPane = (AnchorPane) sourceNode.getParent();
//                    VBox parentVBox = (VBox) jobItemPane.getParent();
//                    parentVBox.getChildren().remove(jobItemPane);
                } else {

                    Alert failureAlert = new Alert(Alert.AlertType.WARNING);
                    failureAlert.setTitle("Deletion Failed");
                    failureAlert.setHeaderText(null);
                    failureAlert.setContentText("No job found to delete.");
                    failureAlert.show();
                }
            } catch (Exception e) {

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("An error occurred while deleting the job.");
                errorAlert.setContentText(e.getMessage());
                errorAlert.show();
            }
        } else {

            System.out.println("Job deletion canceled by user.");
        }
    }








}
