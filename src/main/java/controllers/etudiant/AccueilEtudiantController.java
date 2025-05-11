package controllers.etudiant;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import utils.NavigationUtil;

public class AccueilEtudiantController {

    @FXML private VBox navbar;
    @FXML private Button homeButton;
    @FXML private Button profileButton;
    @FXML private Button coursesButton;
    @FXML private Button gradesButton;
    @FXML private Button logoutButton;
    @FXML private Button evenementButton;
    @FXML private Button avisButton;
    @FXML private Button payementButton;
    @FXML private Button evaluationButton;
    @FXML private Button sallesButton;
    @FXML private Label welcomeLabel;

    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        if (user != null) {
            welcomeLabel.setText("Bienvenue, " + user.getPrenom() + " " + user.getNom());
        }
    }

    @FXML
    public void goToHome() {
        NavigationUtil.loadSceneWithUser("/fxml/etudiant/accueilEtudiant.fxml", homeButton, currentUser);
    }

    @FXML
    public void goToProfile() {
        NavigationUtil.loadSceneWithUser("/fxml/etudiant/profilEtudiant.fxml", profileButton, currentUser);
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
    public void logout() {
        NavigationUtil.loadScene("/fxml/connexion.fxml", logoutButton);
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
}
