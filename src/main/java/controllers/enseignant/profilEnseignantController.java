package controllers.enseignant;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import utils.NavigationUtil;

public class profilEnseignantController {

    @FXML private VBox navbar;
    @FXML private Button homeButton;
    @FXML private Button studentsButton;
    @FXML private Button profileButton;
    @FXML private Button coursesButton;
    @FXML private Button logoutButton;
    @FXML private Button editButton;
    @FXML private Button salleButton;
    @FXML private Button evenementButton;
    @FXML private Button evaluationButton;
    @FXML private Label nomLabel;
    @FXML private Label prenomLabel;
    @FXML private Label emailLabel;
    @FXML private Label adresseLabel;
    @FXML private Label numTelephoneLabel;
    @FXML private Label diplomeLabel;

    private User currentUser;

    @FXML
    public void initialize() {
        // Any initialization if needed
    }

    public void setUser(User user) {
        this.currentUser = user;
        if (user != null) {
            nomLabel.setText("Nom: " + (user.getNom() != null ? user.getNom() : "Non défini"));
            prenomLabel.setText("Prénom: " + (user.getPrenom() != null ? user.getPrenom() : "Non défini"));
            emailLabel.setText("Email: " + (user.getEmail() != null ? user.getEmail() : "Non défini"));
            adresseLabel.setText("Adresse: " + (user.getAdresse() != null ? user.getAdresse() : "Non défini"));
            numTelephoneLabel.setText("Numéro de téléphone: " + (user.getNumTelephone() != null ? user.getNumTelephone() : "Non défini"));
            diplomeLabel.setText("Diplôme: " + (user.getDiplome() != null ? user.getDiplome() : "Non défini"));
        }
    }

    @FXML
    public void goToHome() {
        NavigationUtil.loadSceneWithUser("/fxml/enseignant/accueilEnseignant.fxml", homeButton, currentUser);
    }

    @FXML
    public void goToStudentsList() {
        NavigationUtil.loadSceneWithUser("/fxml/enseignant/listeEtudiant.fxml", studentsButton, currentUser);
    }

    @FXML
    public void goToProfile() {
        NavigationUtil.loadSceneWithUser("/fxml/profile.fxml", profileButton, currentUser);
    }

    @FXML
    public void goToCourses() {
        NavigationUtil.showError("Fonctionnalité des cours non implémentée.");
    }

    @FXML
    public void goToEditProfile() {
        NavigationUtil.loadSceneWithUser("/fxml/enseignant/editProfilEnseignant.fxml", editButton, currentUser);
    }

    @FXML
    public void goToSalle() {
        NavigationUtil.showError("Fonctionnalité Salle non implémentée.");
    }

    @FXML
    public void goToEvenement() {
        NavigationUtil.showError("Fonctionnalité Evenement non implémentée.");
    }

    @FXML
    public void goToEvaluation() {
        NavigationUtil.showError("Fonctionnalité Evaluation non implémentée.");
    }

    @FXML
    public void logout() {
        NavigationUtil.loadScene("/fxml/connexion.fxml", logoutButton);
    }
}
