package controllers.etudiant;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.mindrot.jbcrypt.BCrypt;
import services.UserService;
import utils.NavigationUtil;

import java.sql.SQLException;

public class editProfilEtudiantController {

    @FXML private VBox navbar;
    @FXML private Button homeButton;
    @FXML private Button profileButton;
    @FXML private Button logoutButton;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField roleField;
    @FXML private TextField adresseField;
    @FXML private TextField numTelephoneField;
    @FXML private TextField niveauEtudeField;
    @FXML private Button saveButton;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label passwordStrengthLabel;
    @FXML private Button changePasswordButton;
    @FXML private Label errorLabel;
    @FXML private Button evenementButton;
    @FXML private Button avisButton;
    @FXML private Button payementButton;
    @FXML private Button evaluationButton;
    @FXML private Button sallesButton;

    private User currentUser;
    private UserService userService;

    @FXML
    public void initialize() {
        userService = new UserService();

        // Add listener to update password strength when new password changes
        newPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            updatePasswordStrength(newValue);
        });
    }

    public void setUser(User user) {
        this.currentUser = user;
        if (user != null) {
            nomField.setText(user.getNom());
            prenomField.setText(user.getPrenom());
            emailField.setText(user.getEmail());
            roleField.setText(user.getRole().toString());
            roleField.setDisable(true);
            adresseField.setText(user.getAdresse());
            numTelephoneField.setText(user.getNumTelephone());
            niveauEtudeField.setText(user.getNiveauEtude());
        }
    }

    @FXML
    public void saveProfile() {
        try {
            currentUser.setNom(nomField.getText());
            currentUser.setPrenom(prenomField.getText());
            currentUser.setEmail(emailField.getText());
            currentUser.setAdresse(adresseField.getText());
            currentUser.setNumTelephone(numTelephoneField.getText());
            currentUser.setNiveauEtude(niveauEtudeField.getText());
            userService.updateUser(currentUser);
            goToProfile();
        } catch (SQLException e) {
            NavigationUtil.showError("Erreur lors de la mise à jour du profil : " + e.getMessage());
        }
    }

    @FXML
    public void goHome() {
        NavigationUtil.loadSceneWithUser("/fxml/etudiant/accueilEtudiant.fxml", homeButton, currentUser);
    }

    @FXML
    public void goToProfile() {
        NavigationUtil.loadSceneWithUser("/fxml/etudiant/profilEtudiant.fxml", profileButton, currentUser);
    }

    @FXML
    public void logout() {
        NavigationUtil.loadScene("/fxml/connexion.fxml", logoutButton);
    }

    @FXML
    public void changePassword() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate inputs
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Tous les champs doivent être remplis !");
            return;
        }

        // Verify current password
        if (!BCrypt.checkpw(currentPassword, currentUser.getPassword())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le mot de passe actuel est incorrect !");
            return;
        }

        // Verify new password matches confirmation
        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Le nouveau mot de passe et sa confirmation ne correspondent pas !");
            return;
        }

        // Validate new password strength
        if (!validatePassword(newPassword)) {
            return;
        }

        try {
            // Hash the new password
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            // Update the user's password
            currentUser.setPassword(hashedPassword);
            userService.updateUser(currentUser);

            // Clear password fields
            currentPasswordField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Votre mot de passe a été changé avec succès !");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du changement de mot de passe : " + e.getMessage());
        }
    }

    @FXML
    public void goToEvenement() {
        NavigationUtil.showError("Fonctionnalité Evenement non implémentée.");
    }

    @FXML
    public void goToAvis() {
        NavigationUtil.showError("Fonctionnalité Avis non implémentée.");
    }

    @FXML
    public void goToPayement() {
        NavigationUtil.showError("Fonctionnalité Payement non implémentée.");
    }

    @FXML
    public void goToEvaluation() {
        NavigationUtil.showError("Fonctionnalité Evaluation non implémentée.");
    }

    @FXML
    public void goToSalles() {
        NavigationUtil.showError("Fonctionnalité Salles non implémentée.");
    }

    private boolean validatePassword(String password) {
        if (password.length() < 8) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Le mot de passe doit contenir au moins 8 caractères !");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Le mot de passe doit contenir au moins une lettre majuscule !");
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Le mot de passe doit contenir au moins une lettre minuscule !");
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Le mot de passe doit contenir au moins un chiffre !");
            return false;
        }
        if (!password.matches(".*[@#$%^&+=!].*")) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Le mot de passe doit contenir au moins un caractère spécial (@#$%^&+=!) !");
            return false;
        }
        return true;
    }

    private void updatePasswordStrength(String password) {
        if (password.isEmpty()) {
            passwordStrengthLabel.setText("");
            return;
        }

        int strength = 0;

        // Length check
        if (password.length() >= 8) {
            strength++;
        }

        // Uppercase check
        if (password.matches(".*[A-Z].*")) {
            strength++;
        }

        // Lowercase check
        if (password.matches(".*[a-z].*")) {
            strength++;
        }

        // Digit check
        if (password.matches(".*\\d.*")) {
            strength++;
        }

        // Special character check
        if (password.matches(".*[@#$%^&+=!].*")) {
            strength++;
        }

        // Update label based on strength
        switch (strength) {
            case 0:
            case 1:
                passwordStrengthLabel.setText("Force du mot de passe: Très faible");
                passwordStrengthLabel.setStyle("-fx-text-fill: #FF0000;"); // Red
                break;
            case 2:
                passwordStrengthLabel.setText("Force du mot de passe: Faible");
                passwordStrengthLabel.setStyle("-fx-text-fill: #FF6600;"); // Orange
                break;
            case 3:
                passwordStrengthLabel.setText("Force du mot de passe: Moyen");
                passwordStrengthLabel.setStyle("-fx-text-fill: #FFCC00;"); // Yellow
                break;
            case 4:
                passwordStrengthLabel.setText("Force du mot de passe: Fort");
                passwordStrengthLabel.setStyle("-fx-text-fill: #99CC00;"); // Light Green
                break;
            case 5:
                passwordStrengthLabel.setText("Force du mot de passe: Très fort");
                passwordStrengthLabel.setStyle("-fx-text-fill: #009900;"); // Green
                break;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
