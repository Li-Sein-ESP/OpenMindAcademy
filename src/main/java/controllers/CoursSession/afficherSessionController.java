package controllers.CoursSession;

import entities.Cours;
import entities.SessionCours;
import entities.User;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceCours;
import services.ServiceSessionCours;
import utils.NavigationUtil;

import java.io.IOException;
import java.util.List;

public class afficherSessionController {

    @FXML
    private VBox navbar;
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
    @FXML public void goToHome() {NavigationUtil.loadSceneWithUser("/fxml/etudiant/accueilEtudiant.fxml", homeButton, currentUser);}
    @FXML public void goToProfile() {NavigationUtil.loadSceneWithUser("/fxml/etudiant/profilEtudiant.fxml", profileButton, currentUser);}
    private void naviguerVers(String fxmlPath, Button bouton) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) bouton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur Erreur de navigation: " + e.getMessage());
        }
    }
    @FXML public void goToCourses() {
        naviguerVers("/Cours/GestionCoursAccueilEnseingnant.fxml", coursesButton);
    }
    @FXML public void goToGrades() {
        NavigationUtil.showError("Fonctionnalité des notes non implémentée.");
    }
    @FXML public void logout() {
        NavigationUtil.loadScene("/fxml/connexion.fxml", logoutButton);
    }
    @FXML public void goToEvenement() {
        NavigationUtil.showError("Fonctionnalité Evenement non implémentée.");
    }
    @FXML public void goToAvis() {
        NavigationUtil.showError("Fonctionnalité Avis non implémentée.");
    }
    @FXML public void goToPayement() {
        NavigationUtil.showError("Fonctionnalité Payement non implémentée.");
    }
    @FXML public void goToEvaluation() {
        NavigationUtil.showError("Fonctionnalité Evaluation non implémentée.");
    }
    @FXML public void goToSalles() {
        NavigationUtil.showError("Fonctionnalité Salles non implémentée.");
    }

    @FXML private Button afficherCoursBtn;
    @FXML private Button afficherSessionsBtn;
    // Tables
    @FXML private TableView<Cours> tableCours;
    @FXML private TableView<SessionCours> tableSessionCours;

    // Colonnes Cours
    @FXML private TableColumn<Cours, Integer> idCoursCol;
    @FXML private TableColumn<Cours, String> nomCoursCol;
    @FXML private TableColumn<Cours, String> descriptionCol;
    @FXML private TableColumn<Cours, Integer>dureeCol;
    @FXML private TableColumn<Cours, Float>prixCol;
    @FXML private TableColumn<Cours, String>categorieCol;

    // Colonnes Session
    @FXML private TableColumn<SessionCours, Integer> idSessionCol;
    @FXML private TableColumn<SessionCours,String >nomSessionCol;
    @FXML private TableColumn<SessionCours, String>dateDebutCol;
    @FXML private TableColumn<SessionCours, String>dateFinCol;
    @FXML private TableColumn<SessionCours, String>coursSessionCol;
    @FXML private TableColumn<SessionCours, String>descriptionCoursCol;


    private ServiceCours serviceCours = new ServiceCours();
    private ServiceSessionCours serviceSessionCours = new ServiceSessionCours();

    // Affichage de tables
    @FXML private void afficherTableCours() {
        tableCours.setVisible(true);
        tableSessionCours.setVisible(false);
        loadCours();
    }
    @FXML private void afficherTableSessions() {
        tableCours.setVisible(false);
        tableSessionCours.setVisible(true);
        loadSessions();
    }
    @FXML public void initialize() {
        configureCoursColumns();
        configureSessionColumns();
        loadCours();
        tableCours.setVisible(true);
        tableSessionCours.setVisible(false);}
    private void configureCoursColumns() {
        idCoursCol.setCellValueFactory(new PropertyValueFactory<>("idCours"));
        nomCoursCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        dureeCol.setCellValueFactory(new PropertyValueFactory<>("duree"));
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
        categorieCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoriesCours() != null ? cellData.getValue().getCategoriesCours().getNom() : "N/A"));

    }
    private void configureSessionColumns() {
        // Correspondance exacte avec vos fx:id
        idSessionCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nomSessionCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNom_session()));
        dateDebutCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDateDebut().toString()));
        dateFinCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDateFin().toString()));

        coursSessionCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCours().getNom()));

        descriptionCoursCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCours().getDescription()));
    }

    // Chargement des données
    private void loadCours() {
        List<Cours> coursList = serviceCours.afficher();
        System.out.println("Nombre de cours chargés : " + coursList.size()); // Debug
        tableCours.getItems().setAll(coursList);
        for (Cours c : coursList) {
            System.out.println("Cours: " + c.getIdCours() + ", " + c.getNom());
        }

    }
    private void loadSessions() {
        List<SessionCours> sessionsList = serviceSessionCours.afficher();
        tableSessionCours.getItems().setAll(sessionsList);
    }
}
