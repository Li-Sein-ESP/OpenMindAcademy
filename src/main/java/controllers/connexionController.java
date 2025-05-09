package controllers;

import entities.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import services.UserService;
import org.mindrot.jbcrypt.BCrypt;
import utils.NavigationUtil;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class connexionController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button faceLoginButton;
    @FXML private Hyperlink forgotPasswordLink;
    @FXML private Hyperlink registerLink;

    private UserService userService;
    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    @FXML
    public void initialize() {
        userService = new UserService();

        // Ajouter des validations en temps réel
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateForm();
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateForm();
        });

        // Désactiver le bouton de connexion par défaut
        loginButton.setDisable(true);
    }

    private void validateForm() {
        boolean isValid = !emailField.getText().trim().isEmpty() &&
                !passwordField.getText().trim().isEmpty();
        loginButton.setDisable(!isValid);
    }

    @FXML
    public void login() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        loginButton.setDisable(true);
        loginButton.setText("Connexion en cours...");

        // Utiliser CompletableFuture pour ne pas bloquer l'UI
        CompletableFuture.supplyAsync(() -> {
            try {
                return userService.getUserByEmail(email);
            } catch (Exception e) {
                return null;
            }
        }).thenAccept(user -> {
            Platform.runLater(() -> {
                if (user != null) {
                    String storedPassword = user.getPassword();
                    if (storedPassword != null && storedPassword.startsWith("$2a$") &&
                            BCrypt.checkpw(password, storedPassword)) {

                        loggedInUser = user;

                        try {
                            switch (user.getRole()) {
                                case TEACHER:
                                    NavigationUtil.loadSceneWithUser("/fxml/enseignant/accueilEnseignant.fxml", emailField, user);
                                    break;
                                case STUDENT:
                                    NavigationUtil.loadSceneWithUser("/fxml/etudiant/accueilEtudiant.fxml", emailField, user);
                                    break;
                                case ADMIN:
                                    NavigationUtil.loadSceneWithUser("/fxml/admin/UserManagement.fxml", emailField, user);
                                    break;
                                default:
                                    showError("Rôle non pris en charge pour l'instant.");
                            }
                        } catch (Exception e) {
                            showError("Erreur lors du chargement de la page: " + e.getMessage());
                        }
                    } else {
                        showError("Email ou mot de passe incorrect.");
                    }
                } else {
                    showError("Utilisateur non trouvé.");
                }

                loginButton.setDisable(false);
                loginButton.setText("Login");
            });
        }).exceptionally(e -> {
            Platform.runLater(() -> {
                showError("Erreur de connexion: " + e.getMessage());
                loginButton.setDisable(false);
                loginButton.setText("Login");
            });
            return null;
        });
    }

    @FXML
    public void loginWithFace() {
        showError("Face ID login is not implemented yet.");
    }

    @FXML
    public void goToRegister() {
        try {
            NavigationUtil.loadScene("/fxml/inscription.fxml", emailField);
        } catch (Exception e) {
            showError("Erreur lors du chargement de l'inscription : " + e.getMessage());
        }
    }

    @FXML
    public void goToForgotPassword() {
        try {
            NavigationUtil.loadScene("/fxml/motPasseOublier.fxml", emailField);
        } catch (Exception e) {
            showError("Erreur lors du chargement de la récupération de mot de passe : " + e.getMessage());
        }
    }

    private void showError(String message) {
        NavigationUtil.showError(message);
    }
}
