package controllers.etudiant;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import utils.NavigationUtil;

public class profilEtudiantController {

    @FXML private VBox navbar;
    @FXML private Button homeButton;
    @FXML private Button profileButton;
    @FXML private Button coursesButton;
    @FXML private Button gradesButton;
    @FXML private Button logoutButton;
    @FXML private Button editButton;
    @FXML private Button evenementButton;
    @FXML private Button avisButton;
    @FXML private Button payementButton;
    @FXML private Button evaluationButton;
    @FXML private Button sallesButton;
    @FXML private Label nomLabel;
    @FXML private Label prenomLabel;
    @FXML private Label emailLabel;
    @FXML private Label adresseLabel;
    @FXML private Label numTelephoneLabel;
    @FXML private Label niveauEtudeLabel;

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
            niveauEtudeLabel.setText("Niveau d'étude: " + (user.getNiveauEtude() != null ? user.getNiveauEtude() : "Non défini"));
        }
    }

    @FXML
    public void goToHome() {
        NavigationUtil.loadSceneWithUser("/fxml/etudiant/accueilEtudiant.fxml", homeButton, currentUser);
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
    public void goToGrades() {
        NavigationUtil.showError("Fonctionnalité des notes non implémentée.");
    }

    @FXML
    public void goToEditProfile() {
        NavigationUtil.loadSceneWithUser("/fxml/etudiant/editProfilEtudiant.fxml", editButton, currentUser);
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

    @FXML
    public void logout() {
        NavigationUtil.loadScene("/fxml/connexion.fxml", logoutButton);
    }
}
