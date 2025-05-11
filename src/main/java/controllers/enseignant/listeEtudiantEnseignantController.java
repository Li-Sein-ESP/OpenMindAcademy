package controllers.enseignant;

import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import services.UserService;
import utils.NavigationUtil;

import java.sql.SQLException;

public class listeEtudiantEnseignantController {

    @FXML private VBox navbar;
    @FXML private Button homeButton;
    @FXML private Button studentsButton;
    @FXML private Button profileButton;
    @FXML private Button coursesButton;
    @FXML private Button logoutButton;
    @FXML private Button salleButton;
    @FXML private Button evenementButton;
    @FXML private Button evaluationButton;
    @FXML private ListView<User> etudiantListView;
    @FXML private Button backButton;

    private ObservableList<User> etudiants = FXCollections.observableArrayList();
    private UserService userService = new UserService();
    private User currentUser;

    @FXML
    public void initialize() throws SQLException {
        etudiantListView.setItems(etudiants);
        loadStudents();
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    private void loadStudents() throws SQLException {
        etudiants.setAll(userService.getUsersByRole(enums.Role.STUDENT));
        etudiantListView.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getPrenom() + " " + user.getNom() + " (" + user.getEmail() + ")");
                }
            }
        });
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
    public void goBack() {
        NavigationUtil.loadSceneWithUser("/fxml/enseignant/accueilEnseignant.fxml", backButton, currentUser);
    }

    @FXML
    public void logout() {
        NavigationUtil.loadScene("/fxml/connexion.fxml", logoutButton);
    }
}
