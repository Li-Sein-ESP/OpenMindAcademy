package controllers;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.UserService;
import services.EmailService;
import utils.NavigationUtil;

import java.sql.SQLException;

public class motPasseOublierController {

    @FXML private TextField emailField;
    @FXML private Button resetButton;
    @FXML private Button backButton;

    private UserService userService;

    @FXML
    public void initialize() {
        userService = new UserService();
    }

    @FXML
    private void resetPassword() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showWarning("Veuillez entrer votre email !");
            return;
        }

        try {
            User user = userService.getUserByEmail(email);
            if (user != null) {
                String newPassword = userService.generateAndResetPassword(user.getId());
                if (EmailService.sendNewPasswordEmail(email, newPassword)) {
                    showInfo("Un nouveau mot de passe a été envoyé à votre email.");
                } else {
                    showError("Échec de l'envoi du nouveau mot de passe. Veuillez réessayer.");
                }
            } else {
                showError("Aucun utilisateur trouvé avec cet email !");
            }
        } catch (SQLException e) {
            showError("Erreur lors de la réinitialisation du mot de passe : " + e.getMessage());
        }
    }

    @FXML
    private void goToLogin() {
        NavigationUtil.loadScene("/fxml/connexion.fxml", emailField);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}