package org.example.projectassignement;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    // Sign Up Form Components
    @FXML private TextField fullNameFieldSignUp;
    @FXML private TextField emailFieldSignUp;
    @FXML private PasswordField passwordFieldSignUp;
    @FXML private PasswordField confirmPasswordFieldSignUp;
    @FXML private TextField nationalityFieldSignUp;
    @FXML private Button signUpButtonAction;
    @FXML private ToggleButton signUpButton;
    @FXML private Label emailErrorLabelSignUp;
    @FXML private Label passwordErrorLabelSignUp;
    @FXML private Label nameErrorLabelSignUp;
    @FXML private Label nationalityErrorLabelSignUp;
    @FXML private VBox signUpForm;

    // Sign In Form Components
    @FXML private ComboBox<String> roleComboBoxSignIn;
    @FXML private TextField emailFieldSignIn;
    @FXML private PasswordField passwordFieldSignIn;
    @FXML private Button signInButtonAction;
    @FXML private ToggleButton signInButton;
    @FXML private VBox signInForm;
    @FXML private Label emailErrorLabel;
    @FXML private Label passwordErrorLabel;
    @FXML private Label credintialsErrorLabel;
    @FXML private Label roleErrorLabel;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        roleComboBoxSignIn.getItems().addAll("Admin", "Tourist", "Guide");
        signInButton.setOnAction(e -> switchToSignIn());
        signUpButton.setOnAction(e -> switchToSignUp());
        signUpButtonAction.setOnAction(e -> signUp());
        signInButtonAction.setOnAction(e -> signIn());
    }

    private void switchToSignIn() {
        signInForm.setVisible(true);
        signInForm.setManaged(true);
        signUpForm.setVisible(false);
        signUpForm.setManaged(false);
    }

    private void switchToSignUp() {
        signInForm.setVisible(false);
        signInForm.setManaged(false);
        signUpForm.setVisible(true);
        signUpForm.setManaged(true);
    }

    private void signUp() {
        clearErrorLabels();
        String name = fullNameFieldSignUp.getText();
        String email = emailFieldSignUp.getText();
        String password = passwordFieldSignUp.getText();
        String confirmPassword = confirmPasswordFieldSignUp.getText();
        String nationality = nationalityFieldSignUp.getText();

        boolean hasError = false;

        if (name.isEmpty()) {
            nameErrorLabelSignUp.setText("Name is required!");
            nameErrorLabelSignUp.setVisible(true);
            hasError = true;
        }

        if (email.isEmpty()) {
            emailErrorLabelSignUp.setText("Email is required!");
            emailErrorLabelSignUp.setVisible(true);
            hasError = true;
        } else if (!isValidEmail(email)) {
            emailErrorLabelSignUp.setText("Invalid email format!");
            emailErrorLabelSignUp.setVisible(true);
            hasError = true;
        }

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            passwordErrorLabelSignUp.setText("Password is required!");
            passwordErrorLabelSignUp.setVisible(true);
            hasError = true;
        } else if (!password.equals(confirmPassword)) {
            passwordErrorLabelSignUp.setText("Passwords do not match!");
            passwordErrorLabelSignUp.setVisible(true);
            hasError = true;
        } else if (!isValidPassword(password)) {
            passwordErrorLabelSignUp.setText("Weak password: use letter, number, and special character!");
            passwordErrorLabelSignUp.setVisible(true);
            hasError = true;
        }

        if (nationality.isEmpty()) {
            nationalityErrorLabelSignUp.setText("Nationality is required!");
            nationalityErrorLabelSignUp.setVisible(true);
            hasError = true;
        }

        if (hasError) return;

            try {
                if (userService.isEmailRegistered(email)) {
                    emailErrorLabelSignUp.setText("User already exists.");
                    emailErrorLabelSignUp.setVisible(true);
                } else {
                    userService.saveUser(new User(name, email, password, nationality));
                    changeScene("tourist-dashboard.fxml");
                }
            } catch (Exception e) {
                emailErrorLabelSignUp.setText("Error saving user.");
                emailErrorLabelSignUp.setVisible(true);
            }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=]).{6,}$");
    }

    private void signIn() {
        emailErrorLabel.setVisible(false);
        passwordErrorLabel.setVisible(false);
        credintialsErrorLabel.setVisible(false);
        roleErrorLabel.setVisible(false);

        String email = emailFieldSignIn.getText();
        String password = passwordFieldSignIn.getText();
        String role = roleComboBoxSignIn.getValue();

        if (email.isEmpty()) emailErrorLabel.setVisible(true);
        if (password.isEmpty()) passwordErrorLabel.setVisible(true);
        if (role == null || role.isEmpty()) roleErrorLabel.setVisible(true);

        try {
            if (userService.authenticate(email, password)) {
                System.out.println("Login Success as: " + role);
                // Navigate to dashboard
                changeScene("tourist-dashboard.fxml");
            } else {
                credintialsErrorLabel.setVisible(true);
            }
        } catch (Exception e) {
            credintialsErrorLabel.setText("Error during authentication.");
            credintialsErrorLabel.setVisible(true);
        }
    }

    private void clearErrorLabels() {
        nameErrorLabelSignUp.setVisible(false);
        emailErrorLabelSignUp.setVisible(false);
        passwordErrorLabelSignUp.setVisible(false);
        nationalityErrorLabelSignUp.setVisible(false);
    }

    public void changeScene(String name) throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        Stage stage = (Stage) signUpForm.getScene().getWindow();
        sceneChanger.changeScene(name,stage);
    }
}