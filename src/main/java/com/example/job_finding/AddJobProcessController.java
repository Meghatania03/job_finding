package com.example.job_finding;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddJobProcessController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField jobCategoryField;
    @FXML
    private Button submitButton;

    // Method to handle job submission
    @FXML
    public void handleSubmit() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String jobCategory = jobCategoryField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || jobCategory.isEmpty()) {
            showAlert("Error", "All fields must be filled.");
            return;
        }

        if (validateCompanyCredentials(username, password)) {
            addJobForCompany(username, jobCategory);
        } else {
            showAlert("Error", "Invalid company credentials.");
        }
    }


    private boolean validateCompanyCredentials(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ? AND user_type = 'company'";  // assuming user_role identifies companies

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            showAlert("Error", "Error checking company credentials: " + e.getMessage());
            return false;
        }
    }


    private void addJobForCompany(String companyUsername, String jobCategory) {
        // Fetch company details including the profile picture
        String fetchDetailsQuery = "SELECT * FROM users WHERE username = ? AND user_type = 'Company'";

        try (Connection connection = Database.getConnection();
             PreparedStatement fetchStatement = connection.prepareStatement(fetchDetailsQuery)) {

            fetchStatement.setString(1, companyUsername);
            ResultSet resultSet = fetchStatement.executeQuery();

            if (resultSet.next()) {

                String companyName = resultSet.getString("company_name");
                String email = resultSet.getString("email_id");
                String contact = resultSet.getString("contact_no");
                String address = resultSet.getString("address");
                String industry = resultSet.getString("industry");
                String password = resultSet.getString("password");
                byte[] profilePicture = resultSet.getBytes("profile_picture"); // Profile picture


                String insertQuery = "INSERT INTO users (user_type, company_name, username, email_id, contact_no, address, industry, job_categories, password, profile_picture, created_at) " +
                        "VALUES ('Company', ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                    insertStatement.setString(1, companyName);
                    insertStatement.setString(2, companyUsername);
                    insertStatement.setString(3, email);
                    insertStatement.setString(4, contact);
                    insertStatement.setString(5, address);
                    insertStatement.setString(6, industry);
                    insertStatement.setString(7, jobCategory);
                    insertStatement.setString(8, password);


                    if (profilePicture != null) {
                        insertStatement.setBytes(9, profilePicture);
                    } else {
                        insertStatement.setNull(9, java.sql.Types.BLOB); // No picture
                    }

                    int rowsInserted = insertStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        showAlert("Success", "New job added successfully!");
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("company_dashboard.fxml"));
                        Parent root = loader.load();


                        company_dashboard_controller companydashboardController = loader.getController();
                        companydashboardController.initializeDashboard_company(companyUsername);
                        Stage stage = (Stage) submitButton.getScene().getWindow();

                        stage.setScene(new Scene(root));
                        stage.show();

                    } else {
                        showAlert("Error", "Failed to add new job.");
                    }
                }
            } else {
                showAlert("Error", "Company details not found.");
            }

        } catch (Exception e) {
            showAlert("Error", "Error adding job: " + e.getMessage());
        }
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
