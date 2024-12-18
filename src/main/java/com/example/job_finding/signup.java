package com.example.job_finding;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class signup {
    public ImageView pfp_applicant_imageview;
    public ImageView pfp_company_imageview;

    @FXML
    public void initialize() {
        System.out.println(first_name_applicant);
    }
    @FXML
    private File selectedImageFile;




    @FXML
    private TextField first_name_applicant;

    @FXML
    private TextField last_name_applicant;

    @FXML
    private TextField username_applicant;
    @FXML
    private TextField address_company;

    @FXML
    private TextField username_company;

    @FXML
    private TextField emailid_applicant;

    @FXML
    private TextField contactno_applicant;

    @FXML
    private TextField address_applicant;

    @FXML
    private TextField gender_applicant;

    @FXML
    private TextField highest_qualification_applicant;

    @FXML
    private TextField graduation_year_applicant;

    @FXML
    private TextField institution_name_applicant;

    @FXML
    private TextField field_of_study_applicant;

    @FXML
    private TextField url_applicant;

    @FXML
    private TextField pref_job_applicant;

    @FXML
    private TextField experience_applicant;

    @FXML
    private TextField password_applicant;
    @FXML
    private TextField company_name;

    @FXML
    private TextField emailid_company;

    @FXML
    private TextField contactno_company;

    @FXML
    private TextField URLcompany;

    @FXML
    private TextField industry_company;

    @FXML
    private TextField job_category_company;

    @FXML
    private TextField password_company;


    private Stage stage;
    private Scene scene;
    private Parent root;
    public void signup_applicant(ActionEvent event) throws Exception{
        root= FXMLLoader.load(getClass().getResource("signup_applicant.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        //stage.setMaximized(true);
        stage.show();
    }
    public void signup_applicant_final(ActionEvent event) throws Exception{
        String firstName = first_name_applicant.getText();
        String lastName = last_name_applicant.getText();
        String username = username_applicant.getText();
        String emailId = emailid_applicant.getText();
        String contactNo = contactno_applicant.getText();
        String address = address_applicant.getText();
        String gender = gender_applicant.getText();
        String highestQualification = highest_qualification_applicant.getText();
        int graduationYear = 0;
        int yearsOfExperience = 0;

        try {
            graduationYear = Integer.parseInt(graduation_year_applicant.getText());
            yearsOfExperience = Integer.parseInt(experience_applicant.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Graduation year and Years Of Experience must be a valid number.");
            return;
        }

        String institutionName = institution_name_applicant.getText();
        String fieldOfStudy = field_of_study_applicant.getText();
        String url = url_applicant.getText();
        String preferredJobTitle = pref_job_applicant.getText();
        String password = password_applicant.getText();
        byte[] profilePicture = null;
        if (selectedImageFile != null) {
            try (FileInputStream inputStream = new FileInputStream(selectedImageFile)) {
                profilePicture = inputStream.readAllBytes();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "File Error", "Error reading image file: " + e.getMessage());
                return;
            }
        }

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || emailId.isEmpty() ||
                contactNo.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "All mandatory fields must be filled.");
            return;
        }


        String insertQuery = "INSERT INTO users (user_type, first_name, last_name, username, email_id, contact_no, " +
                "address, gender, highest_qualification, graduation_year, institution_name, field_of_study, " +
                "github_linkedin_url, preferred_job_title, years_of_experience, password,profile_picture) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        try (Connection connection = Database.getConnection(); // Get database connection
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {


            preparedStatement.setString(1, "Applicant"); // Role is applicant
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, username);
            preparedStatement.setString(5, emailId);
            preparedStatement.setString(6, contactNo);
            preparedStatement.setString(7, address);
            preparedStatement.setString(8, gender);
            preparedStatement.setString(9, highestQualification);
            preparedStatement.setInt(10, graduationYear);
            preparedStatement.setString(11, institutionName);
            preparedStatement.setString(12, fieldOfStudy);
            preparedStatement.setString(13, url);
            preparedStatement.setString(14, preferredJobTitle);
            preparedStatement.setInt(15, yearsOfExperience);
            preparedStatement.setString(16, password);

            if (profilePicture != null) {
                preparedStatement.setBytes(17, profilePicture);
            } else {
                preparedStatement.setNull(17, java.sql.Types.BLOB);
            }

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Signup Success", "Applicant signup successful!");
                root= FXMLLoader.load(getClass().getResource("page1.fxml"));
                stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                scene=new Scene(root);
                stage.setScene(scene);
                //stage.setMaximized(true);
                stage.show();
            } else {
                showAlert(Alert.AlertType.ERROR, "Signup Error", "Signup failed. Please try again.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void signup_company(ActionEvent event) throws Exception{
        root= FXMLLoader.load(getClass().getResource("signup_company.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        //stage.setMaximized(true);
        stage.show();
    }
    public void signup_company_final(ActionEvent event) throws Exception {
        // Retrieve input values from TextFields
        String companyName = company_name.getText();
        String emailId = emailid_company.getText();
        String contactNo = contactno_company.getText();
        String websiteUrl = URLcompany.getText();
        String industry = industry_company.getText();
        String jobCategories = job_category_company.getText();
        String password = password_company.getText();
        String username=username_company.getText();
        String address = address_company.getText();

        byte[] profilePicture = null;
        if (selectedImageFile != null) {
            try (FileInputStream inputStream = new FileInputStream(selectedImageFile)) {
                profilePicture = inputStream.readAllBytes();
            } catch (Exception e) {
                System.out.println("Error reading image file: " + e.getMessage());
                return;
            }
        }


        if (companyName.isEmpty() || emailId.isEmpty() || contactNo.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "All mandatory fields must be filled.");
            return;
        }


        String insertQuery = "INSERT INTO Users (user_type, company_name,username, email_id, contact_no,address, website_url, " +
                "industry, job_categories, password,profile_picture) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        try (Connection connection = Database.getConnection(); // Get database connection
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // Set values for the query
            preparedStatement.setString(1, "Company"); // Role is company
            preparedStatement.setString(2, companyName);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, emailId);
            preparedStatement.setString(5, contactNo);
            preparedStatement.setString(6, address);
            preparedStatement.setString(7, websiteUrl);
            preparedStatement.setString(8, industry);
            preparedStatement.setString(9, jobCategories);
            preparedStatement.setString(10, password);


            if (profilePicture != null) {
                preparedStatement.setBytes(11, profilePicture);
            } else {
                preparedStatement.setNull(11, java.sql.Types.BLOB);
            }



            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Signup Success", "Company signup successful!");
                root= FXMLLoader.load(getClass().getResource("page1.fxml"));
                stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                scene=new Scene(root);
                stage.setScene(scene);
                //stage.setMaximized(true);
                stage.show();

            } else {
                showAlert(Alert.AlertType.ERROR, "Signup Error", "Signup failed. Please try again.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void pfp_upload_button_applicant(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        selectedImageFile = fileChooser.showOpenDialog(stage);
        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            pfp_applicant_imageview.setImage(image);
        }
    }
    public void pfp_upload_button_company(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        selectedImageFile = fileChooser.showOpenDialog(stage);
        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            pfp_company_imageview.setImage(image);
        }
    }
    public void gobackpage1(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("page1.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        //stage.setMaximized(true);
        stage.show();

    }
    public void gobacksignup(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("signup_page1.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        //stage.setMaximized(true);
        stage.show();

    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }






}
