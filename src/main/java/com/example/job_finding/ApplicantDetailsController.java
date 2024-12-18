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
import java.sql.ResultSet;

public class ApplicantDetailsController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label Firstname_applicant_details;
    @FXML
    private Label lastname_applicant_details;
    @FXML
    private Label address_applicant_details;
    @FXML
    private Label gender_applicant_details;
    @FXML
    private Label field_of_study_applicant_details;
    @FXML
    private Label url_applicant_details;
    @FXML
    private Label graduation_year_applicant_details;
    @FXML
    private Label years_of_experience_applicant_details;
    String loggedInUsername;

    @FXML
    private Label pref_job_title_applicant_details;
    @FXML
    private Label highest_qualification_applicant_details;
    @FXML
    private Label Institution_name_applicant_details;
    @FXML
    private Label emailid_applicant_details;
    @FXML
    private Label contactno_applicant_details;
    @FXML
    private ImageView profile_imageview;
    @FXML
    private Button delete_account_button;

    @FXML
    private Button go_back_applicant_list_view;



    public void setApplicantDetails(String applicantUsername) {

        String query = "SELECT first_name, last_name, address, preferred_job_title, highest_qualification, institution_name, email_id, contact_no, profile_picture,gender,graduation_year,field_of_study,github_linkedin_url,years_of_experience " +
                "FROM users WHERE username = ?";
        loggedInUsername=applicantUsername;

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, applicantUsername);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                Firstname_applicant_details.setText(resultSet.getString("first_name"));
                lastname_applicant_details.setText(resultSet.getString("last_name"));
                address_applicant_details.setText(resultSet.getString("address"));
                pref_job_title_applicant_details.setText(resultSet.getString("preferred_job_title"));
                highest_qualification_applicant_details.setText(resultSet.getString("highest_qualification"));
                Institution_name_applicant_details.setText(resultSet.getString("institution_name"));
                emailid_applicant_details.setText(resultSet.getString("email_id"));
                contactno_applicant_details.setText(resultSet.getString("contact_no"));
                gender_applicant_details.setText(resultSet.getString("gender"));
                graduation_year_applicant_details.setText(resultSet.getString("graduation_year"));
                field_of_study_applicant_details.setText(resultSet.getString("field_of_study"));
                url_applicant_details.setText(resultSet.getString("github_linkedin_url"));
                years_of_experience_applicant_details.setText(resultSet.getString("years_of_experience"));
                //contactno_applicant_details.setText(resultSet.getString("contact_no"));



                byte[] profilePicture = resultSet.getBytes("profile_picture");
                if (profilePicture != null) {
                    Image image = new Image(new ByteArrayInputStream(profilePicture));
                    profile_imageview.setImage(image);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading applicant details: " + e.getMessage());
        }
    }
    @FXML
    public void deleteAccount() {

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Account");
        confirmationAlert.setHeaderText("Are you sure you want to delete your account?");
        confirmationAlert.setContentText("This action cannot be undone.");


        if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            String deleteQuery = "DELETE FROM users WHERE username = ?";
            String deleteUserQuery = "DELETE FROM applications WHERE applicant_username = ?";


            try (Connection connection = Database.getConnection();

                 PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            PreparedStatement deleteUserStmt = connection.prepareStatement(deleteUserQuery)) {



                preparedStatement.setString(1, loggedInUsername);


                preparedStatement.executeUpdate();

                deleteUserStmt.setString(1, loggedInUsername);
                int rowsAffected = deleteUserStmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Account deleted successfully!");


                    FXMLLoader loader = new FXMLLoader(getClass().getResource("page1.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) delete_account_button.getScene().getWindow(); // Use the delete button to get the stage
                    stage.setScene(new Scene(root));
                    stage.show();
                } else {
                    System.out.println("Account deletion failed. User not found.");
                }

            } catch (Exception e) {
                System.out.println("Error deleting account: " + e.getMessage());
            }
        }
    }
    public void goback_from_applicant_profile(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("applicant_dashboard.fxml"));
         Parent root = loader.load();


        Applicant_DashboardController applicant_dashboardController = loader.getController();
        applicant_dashboardController.initializeDashboard(loggedInUsername);


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        //stage.setMaximized(true);
        stage.show();
    }
    public void go_back_applicant_details(ActionEvent event) throws IOException {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("page1.fxml"));
            Parent root = loader.load();

            go_back_applicant_list_view.getScene().setRoot(root);


        } catch (Exception e) {
            System.out.println("Error navigating to Applied Jobs: " + e.getMessage());
        }
    }


}
