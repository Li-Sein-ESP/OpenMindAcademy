package controllers;

import entities.User;
import enums.Role;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.UserService;
import services.FaceRecognitionService;
import utils.NavigationUtil;

import java.time.LocalDate;

public class inscriptionController {

    @FXML private TextField nomField, prenomField, emailField, adresseField, numTelephoneField, diplomeField, niveauEtudeField, imageField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML private ComboBox<Role> roleCombo;
    @FXML private ComboBox<String> sexeCombo;
    @FXML private PasswordField passwordField, confirmPasswordField;
    @FXML private Label diplomeLabel, niveauEtudeLabel, passwordStrengthLabel;
    @FXML private Button registerButton, saveFaceButton;

    private UserService userService;
    private FaceRecognitionService faceRecognitionService;
    private String faceEncoding;

    @FXML
    public void initialize() {
        userService = new UserService();
        faceRecognitionService = new FaceRecognitionService();

        roleCombo.getItems().addAll(Role.TEACHER, Role.STUDENT);
        sexeCombo.getItems().addAll("MALE", "FEMALE");

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

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            updatePasswordStrength(newValue, confirmPasswordField.getText());
        });
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            updatePasswordStrength(passwordField.getText(), newValue);
        });

        // Initialize saveFaceButton state
        saveFaceButton.setDisable(true);
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveFaceButton.setDisable(newValue.trim().isEmpty());
        });
    }

    @FXML
    private void saveFace() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showWarning("Veuillez entrer un email avant d'enregistrer le visage.");
            return;
        }

        saveFaceButton.setDisable(true);
        saveFaceButton.setText("Enregistrement...");

        try {
            faceEncoding = faceRecognitionService.saveFace(email);
            showWarning("Visage enregistré avec succès pour " + email);
            imageField.setText("Visage enregistré pour " + email);
        } catch (Exception e) {
            String errorMessage = e.getMessage().contains("Error communicating with face recognition server")
                    ? "Erreur : Le serveur de reconnaissance faciale n'est pas disponible. Veuillez vérifier qu'il est en cours d'exécution."
                    : "Erreur lors de l'enregistrement du visage : " + e.getMessage();
            showWarning(errorMessage);
            imageField.setText("Échec de l'enregistrement du visage.");
        } finally {
            saveFaceButton.setDisable(false);
            saveFaceButton.setText("Enregistrer Visage");
        }
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
                newUser.setFaceEncoding(faceEncoding);

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

        String phoneNumber = numTelephoneField.getText().trim();
        if (!phoneNumber.isEmpty() && !phoneNumber.matches("\\d{8}")) {
            showWarning("Le numéro de téléphone doit contenir exactement 8 chiffres !");
            return false;
        }

        return true;
    }

    private boolean validateEmail(String email) {
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

        if (password.length() >= 8) strength++;
        if (password.matches(".*[A-Z].*")) strength++;
        if (password.matches(".*[a-z].*")) strength++;
        if (password.matches(".*\\d.*")) strength++;
        if (password.matches(".*[@#$%^&+=!].*")) strength++;

        if (!password.equals(confirmPassword)) {
            passwordStrengthLabel.setText("Les mots de passe ne correspondent pas !");
            passwordStrengthLabel.setStyle("-fx-text-fill: #FF0000;");
            return;
        }

        switch (strength) {
            case 0:
            case 1:
                passwordStrengthLabel.setText("Force du mot de passe: Très faible");
                passwordStrengthLabel.setStyle("-fx-text-fill: #FF0000;");
                break;
            case 2:
                passwordStrengthLabel.setText("Force du mot de passe: Faible");
                passwordStrengthLabel.setStyle("-fx-text-fill: #FF6600;");
                break;
            case 3:
                passwordStrengthLabel.setText("Force du mot de passe: Moyen");
                passwordStrengthLabel.setStyle("-fx-text-fill: #FFCC00;");
                break;
            case 4:
                passwordStrengthLabel.setText("Force du mot de passe: Fort");
                passwordStrengthLabel.setStyle("-fx-text-fill: #99CC00;");
                break;
            case 5:
                passwordStrengthLabel.setText("Force du mot de passe: Très fort");
                passwordStrengthLabel.setStyle("-fx-text-fill: #009900;");
                break;
        }
    }
}