package controllers.admin;

import entities.User;
import enums.Role;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import services.UserService;
import utils.NavigationUtil;

import java.sql.SQLException;

public class editUserAdminController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField adresseField;
    @FXML private TextField numTelephoneField;
    @FXML private TextField niveauEtudeField;
    @FXML private TextField diplomeField;
    @FXML private HBox niveauEtudeBox;
    @FXML private HBox diplomeBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button homeButton;
    @FXML private Button listTeachersButton;
    @FXML private Button listStudentsButton;
    @FXML private Button logoutButton;
    @FXML private Button sallesButton;
    @FXML private Button evenementButton;
    @FXML private Button payementButton;
    @FXML private Button avisButton;
    @FXML private Button evaluationButton;

    private UserService userService = new UserService();
    private User currentUser; // The logged-in admin
    private User userToEdit;  // The user being edited

    @FXML
    public void initialize() {
        // Any initialization if needed
    }

    public void setUsers(User currentUser, User userToEdit) {
        this.currentUser = currentUser;
        this.userToEdit = userToEdit;
        populateFields();
    }

    private void populateFields() {
        if (userToEdit != null) {
            nomField.setText(userToEdit.getNom() != null ? userToEdit.getNom() : "");
            prenomField.setText(userToEdit.getPrenom() != null ? userToEdit.getPrenom() : "");
            emailField.setText(userToEdit.getEmail() != null ? userToEdit.getEmail() : "");
            adresseField.setText(userToEdit.getAdresse() != null ? userToEdit.getAdresse() : "");
            numTelephoneField.setText(userToEdit.getNumTelephone() != null ? userToEdit.getNumTelephone() : "");

            // Show/hide role-specific fields
            if (userToEdit.getRole() == Role.STUDENT) {
                niveauEtudeBox.setVisible(true);
                niveauEtudeBox.setManaged(true);
                niveauEtudeField.setText(userToEdit.getNiveauEtude() != null ? userToEdit.getNiveauEtude() : "");
            } else if (userToEdit.getRole() == Role.TEACHER) {
                diplomeBox.setVisible(true);
                diplomeBox.setManaged(true);
                diplomeField.setText(userToEdit.getDiplome() != null ? userToEdit.getDiplome() : "");
            }
        }
    }

    @FXML
    public void saveUser() {
        try {
            // Update the user's details
            userToEdit.setNom(nomField.getText());
            userToEdit.setPrenom(prenomField.getText());
            userToEdit.setEmail(emailField.getText());
            userToEdit.setAdresse(adresseField.getText());
            userToEdit.setNumTelephone(numTelephoneField.getText());

            // Update role-specific fields
            if (userToEdit.getRole() == Role.STUDENT) {
                userToEdit.setNiveauEtude(niveauEtudeField.getText());
            } else if (userToEdit.getRole() == Role.TEACHER) {
                userToEdit.setDiplome(diplomeField.getText());
            }

            // Save changes via UserService
            userService.updateUser(userToEdit);

            // Navigate back to the appropriate list
            String fxmlPath = userToEdit.getRole() == Role.STUDENT ?
                    "/fxml/admin/listeEtudiant.fxml" :
                    "/fxml/admin/listeEnseignant.fxml";
            NavigationUtil.loadSceneWithUser(fxmlPath, saveButton, currentUser);
        } catch (SQLException e) {
            NavigationUtil.showError("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void cancel() {
        // Navigate back to the appropriate list without saving
        String fxmlPath = userToEdit.getRole() == Role.STUDENT ?
                "/fxml/admin/listeEtudiant.fxml" :
                "/fxml/admin/listeEnseignant.fxml";
        NavigationUtil.loadSceneWithUser(fxmlPath, cancelButton, currentUser);
    }

    @FXML
    public void goToHome() {
        NavigationUtil.loadSceneWithUser("/fxml/admin/UserManagement.fxml", homeButton, currentUser);
    }

    @FXML
    public void goToTeacherList() {
        NavigationUtil.loadSceneWithUser("/fxml/admin/listeEnseignant.fxml", listTeachersButton, currentUser);
    }

    @FXML
    public void goToStudentList() {
        NavigationUtil.loadSceneWithUser("/fxml/admin/listeEtudiant.fxml", listStudentsButton, currentUser);
    }

    @FXML
    public void logout() {
        NavigationUtil.loadScene("/fxml/connexion.fxml", logoutButton);
    }

    @FXML
    public void goToSalles() {
        // TODO: Replace with actual navigation path
        NavigationUtil.loadSceneWithUser("/fxml/admin/salles.fxml", sallesButton, currentUser);
    }

    @FXML
    public void goToEvenement() {
        // TODO: Replace with actual navigation path
        NavigationUtil.loadSceneWithUser("/fxml/admin/evenement.fxml", evenementButton, currentUser);
    }

    @FXML
    public void goToPayement() {
        // TODO: Replace with actual navigation path
        NavigationUtil.loadSceneWithUser("/fxml/admin/payement.fxml", payementButton, currentUser);
    }

    @FXML
    public void goToAvis() {
        // TODO: Replace with actual navigation path
        NavigationUtil.loadSceneWithUser("/fxml/admin/avis.fxml", avisButton, currentUser);
    }

    @FXML
    public void goToEvaluation() {
        // TODO: Replace with actual navigation path
        NavigationUtil.loadSceneWithUser("/fxml/admin/evaluation.fxml", evaluationButton, currentUser);
    }
}
