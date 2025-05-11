package controllers.enseignant;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import utils.NavigationUtil;

public class AccueilEnseignantController {

    @FXML private VBox navbar;
    @FXML private Button homeButton;
    @FXML private Button studentsButton;
    @FXML private Button profileButton;
    @FXML private Button coursesButton;
    @FXML private Button logoutButton;
    @FXML private Label welcomeLabel;
    @FXML private Button salleButton;
    @FXML private Button evenementButton;
    @FXML private Button evaluationButton;

    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        if (user != null) {
            welcomeLabel.setText("Bienvenue, " + user.getPrenom() + " " + user.getNom());
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
        NavigationUtil.loadSceneWithUser("/fxml/enseignant/profilEnseignant.fxml", profileButton, currentUser);
    }

    @FXML
    public void goToCourses() {
        NavigationUtil.showError("Fonctionnalité des cours non implémentée.");
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
