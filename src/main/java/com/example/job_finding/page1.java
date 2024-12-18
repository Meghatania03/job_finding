package com.example.job_finding;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class page1 {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    private TextField email_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private PasswordField confirm_password_field;

    @FXML
    public void login(ActionEvent event) throws IOException, SQLException {
    String user= username.getText();
    String pass= password.getText();
    if(user.isEmpty() || pass.isEmpty()){
        showAlert(Alert.AlertType.WARNING, "Validation Error", "Username or Password is empty.");
        return;
    }
    String query= "SELECT user_type FROM users WHERE username=? AND password=?";
    try(Connection connection= Database.getConnection();
    PreparedStatement preparedStatement= connection.prepareStatement(query)){
        preparedStatement.setString(1, user);
        preparedStatement.setString(2, pass);
        ResultSet resultSet= preparedStatement.executeQuery();

        if(resultSet.next()){
            String role= resultSet.getString("user_type");
            if(role.equals("Applicant")){

                System.out.println("User is applicant");
                gotoApplicantDashboard(event,user);
            }
            else if(role.equals("Company")){
                System.out.println("User is Company");
                gotocompanydashboard(event,user);
            }
        }
        else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Username or Password is incorrect.");
        }

    }
    catch(Exception e){
        showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
        e.printStackTrace();
    }


    }
    public void gotoApplicantDashboard(ActionEvent event, String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("applicant_dashboard.fxml"));
        root = loader.load();


        Applicant_DashboardController applicant_dashboardController = loader.getController();
        applicant_dashboardController.initializeDashboard(username);


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        //stage.setMaximized(true);
        stage.show();
    }

    public void gotocompanydashboard(ActionEvent event,String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("company_dashboard.fxml"));
        root = loader.load();

        company_dashboard_controller company_dashboardController = loader.getController();
        company_dashboardController.initializeDashboard_company(username);
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        //stage.setMaximized(true);
        stage.show();
    }
    public void signup(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("signup_page1.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        //stage.setMaximized(true);
        stage.show();
    }
    @FXML
    private void handleForgotPassword(ActionEvent event) throws IOException {


            FXMLLoader loader = new FXMLLoader(getClass().getResource("forgot_password.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();




    }

    @FXML
    private void handleForgotPasswordSubmit(ActionEvent event) {
        String email = email_field.getText();
        String newPassword = password_field.getText();
        String confirmPassword = confirm_password_field.getText();


        if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Password Mismatch", "The passwords do not match.");
            return;
        }

        String checkUserQuery = "SELECT * FROM users WHERE email_id = ?";
        String updatePasswordQuery = "UPDATE users SET password = ? WHERE email_id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement checkUserStmt = connection.prepareStatement(checkUserQuery);
             PreparedStatement updatePasswordStmt = connection.prepareStatement(updatePasswordQuery)) {

            // Check if email exists
            checkUserStmt.setString(1, email);
            ResultSet resultSet = checkUserStmt.executeQuery();

            if (!resultSet.next()) {
                showAlert(Alert.AlertType.ERROR, "User Not Found", "No account found with the provided email.");
                return;
            }

            // Update the password
            updatePasswordStmt.setString(1, newPassword); // Ideally, hash the password before saving
            updatePasswordStmt.setString(2, email);

            int rowsAffected = updatePasswordStmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Password Reset Successful", "Your password has been updated.");
                clearFields();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("page1.fxml"));
                Parent root = loader.load();


                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed", "Unable to update the password. Please try again.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred: " + e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }


    private void clearFields() {
        email_field.clear();
        password_field.clear();
        confirm_password_field.clear();
    }







}
