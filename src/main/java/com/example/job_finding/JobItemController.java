package com.example.job_finding;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JobItemController {

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
    private ImageView imageview_ji;
    private String loggedInUsername;
    private String companyUsername;





    public void setJobData(String companyName, String industry, String address, String jobType, byte[] profilePicture,String loggedInUser) {

        this.companyUsername = companyName;
        this.loggedInUsername = loggedInUser;

        company_name_ji.setText(companyName);
        industry_ji.setText(industry);
        address_ji.setText(address);
        job_type_ji.setText(jobType);
        if (profilePicture != null) {
            Image image = new Image(new ByteArrayInputStream(profilePicture));
            imageview_ji.setImage(image);
            if (hasUserAlreadyApplied()) {
                apply_ji.setText("Applied");
                apply_ji.setDisable(true);
            }

        }
    }



        @FXML
        private void apply () {
            if (saveApplicationToDatabase()) {

                apply_ji.setText("Applied!");
                apply_ji.setDisable(true);
            } else {
                System.out.println("Error saving application.");
            }
        }

    private boolean hasUserAlreadyApplied() {
        String query = "SELECT 1 FROM applications WHERE applicant_username = ? AND company_username = ? AND job_category = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, loggedInUsername);
            preparedStatement.setString(2, companyUsername);
            preparedStatement.setString(3, job_type_ji.getText());

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            System.out.println("Error checking application status: " + e.getMessage());
            return false;
        }
    }


    private boolean saveApplicationToDatabase() {
        String query = "INSERT INTO applications (applicant_username, company_username, job_category) VALUES (?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, loggedInUsername);
            preparedStatement.setString(2, companyUsername);
            preparedStatement.setString(3, job_type_ji.getText());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Error saving application to database: " + e.getMessage());
            return false;
        }
    }
    }

