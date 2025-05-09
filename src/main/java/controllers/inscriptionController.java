package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import entities.User;
import enums.Role;
import services.UserService;
import utils.NavigationUtil;

import java.time.LocalDate;

public class inscriptionController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<Role> roleCombo;
    @FXML private TextField adresseField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML private ComboBox<String> sexeCombo;
    @FXML private TextField numTelephoneField;
    @FXML private TextField diplomeField;
    @FXML private TextField niveauEtudeField;
    @FXML private TextField imageField;
    @FXML private Label diplomeLabel;
    @FXML private Label niveauEtudeLabel;
    @FXML private Label passwordStrengthLabel;
    @FXML private Button registerButton;

    private UserService userService;

    @FXML
    public void initialize() {
        userService = new UserService();

        // Initialiser les ComboBox
        roleCombo.getItems().addAll(Role.TEACHER, Role.STUDENT);
        sexeCombo.getItems().addAll("MALE", "FEMALE");

        // Afficher/masquer les champs spécifiques selon le rôle
        roleCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == Role.TEACHER) {
                diplomeLabel.setVisible(true);
                diplomeField.setVisible(true);
                niveauEtudeLabel.setVisible(false);
                niveauEtudeField.setVisible(false);
            } else if (newVal == Role.STUDENT) {
                diplomeLabel.setVisible(false);
                diplomeField.setVisible(false);
                niveauEtudeLabel.setVisible(true);
                niveauEtudeField.setVisible(true);
            } else {
                diplomeLabel.setVisible(false);
                diplomeField.setVisible(false);
                niveauEtudeLabel.setVisible(false);
                niveauEtudeField.setVisible(false);
            }
        });

        // Évaluer la force du mot de passe et confirmation lorsqu'ils changent
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            updatePasswordStrength(newValue, confirmPasswordField.getText());
        });
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            updatePasswordStrength(passwordField.getText(), newValue);
        });
    }

    @FXML
    private void register() {
        if (validateRequiredFields()) {
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            LocalDate dateOfBirth = dateNaissancePicker.getValue();

            if (!password.equals(confirmPassword)) {
                showWarning("Les mots de passe ne correspondent pas !");
                return;
            }

            if (!validateEmail(email)) {
                showWarning("Format d'email invalide. Veuillez entrer un email valide.");
                return;
            }

            if (!validatePassword(password)) {
                return;
            }

            if (!validateDateOfBirth(dateOfBirth)) {
                return;
            }

            if (!isEmailUnique(email)) {
                showWarning("Cet email est déjà utilisé. Veuillez en choisir un autre.");
                return;
            }

            // Proceed with registration logic
            try {
                User newUser = new User();
                newUser.setNom(nomField.getText());
                newUser.setPrenom(prenomField.getText());
                newUser.setEmail(email);
                newUser.setPassword(org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt()));
                newUser.setRole(roleCombo.getValue());
                newUser.setAdresse(adresseField.getText());
                newUser.setDateNaissance(dateOfBirth);
                newUser.setSexe(sexeCombo.getValue());
                newUser.setNumTelephone(numTelephoneField.getText());

                if (roleCombo.getValue() == Role.TEACHER) {
                    newUser.setDiplome(diplomeField.getText());
                } else if (roleCombo.getValue() == Role.STUDENT) {
                    newUser.setNiveauEtude(niveauEtudeField.getText());
                }

                newUser.setImage(imageField.getText());

                userService.addUser(newUser);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Inscription");
                alert.setHeaderText(null);
                alert.setContentText("Inscription réussie ! Veuillez vous connecter.");
                alert.showAndWait();

                goToLogin();
            } catch (Exception e) {
                NavigationUtil.showError("Erreur lors de l'inscription : " + e.getMessage());
            }
        }
    }

    @FXML
    private void goToLogin() {
        NavigationUtil.loadScene("/fxml/connexion.fxml", registerButton);
    }

    private boolean validateRequiredFields() {
        if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() || emailField.getText().isEmpty() ||
                passwordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty() || roleCombo.getValue() == null ||
                sexeCombo.getValue() == null || dateNaissancePicker.getValue() == null) {
            showWarning("Tous les champs obligatoires doivent être remplis !");
            return false;
        }

        if (roleCombo.getValue() == Role.TEACHER && diplomeField.getText().isEmpty()) {
            showWarning("Le champ Diplôme est obligatoire pour les enseignants !");
            return false;
        }

        if (roleCombo.getValue() == Role.STUDENT && niveauEtudeField.getText().isEmpty()) {
            showWarning("Le champ Niveau d'Étude est obligatoire pour les étudiants !");
            return false;
        }

        // Validate phone number (must be 8 digits)
        String phoneNumber = numTelephoneField.getText().trim();
        if (!phoneNumber.isEmpty() && !phoneNumber.matches("\\d{8}")) {
            showWarning("Le numéro de téléphone doit contenir exactement 8 chiffres !");
            return false;
        }

        return true;
    }

    private boolean validateEmail(String email) {
        // Basic email format validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean validatePassword(String password) {
        if (password.length() < 8) {
            showWarning("Le mot de passe doit contenir au moins 8 caractères !");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            showWarning("Le mot de passe doit contenir au moins une lettre majuscule !");
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            showWarning("Le mot de passe doit contenir au moins une lettre minuscule !");
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            showWarning("Le mot de passe doit contenir au moins un chiffre !");
            return false;
        }
        if (!password.matches(".*[@#$%^&+=!].*")) {
            showWarning("Le mot de passe doit contenir au moins un caractère spécial (@#$%^&+=!) !");
            return false;
        }
        return true;
    }

    private boolean validateDateOfBirth(LocalDate date) {
        if (date.isAfter(LocalDate.now().minusYears(18))) {
            showWarning("L'utilisateur doit avoir au moins 18 ans !");
            return false;
        }
        return true;
    }

    private boolean isEmailUnique(String email) {
        try {
            return userService.getUserByEmail(email) == null;
        } catch (Exception e) {
            System.err.println("Error checking email uniqueness: " + e.getMessage());
            return false;
        }
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updatePasswordStrength(String password, String confirmPassword) {
        if (password.isEmpty() || confirmPassword.isEmpty()) {
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

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            passwordStrengthLabel.setText("Les mots de passe ne correspondent pas !");
            passwordStrengthLabel.setStyle("-fx-text-fill: #FF0000;"); // Red
            return;
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
}