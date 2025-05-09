package controllers.etudiant;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import utils.NavigationUtil;

public class AccueilEtudiantController {

    @FXML private HBox navbar;
    @FXML private Button homeButton;
    @FXML private Button profileButton;
    @FXML private Button coursesButton;
    @FXML private Button gradesButton;
    @FXML private Button logoutButton;
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
}