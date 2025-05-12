package controllers.CoursSession;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import services.*;
import entities.*;

import java.io.IOException;
import java.util.List;


public class GestionCoursAccueilEnseingnant {

    // Boutons de navigation
    @FXML private VBox navbar;
    @FXML private Button homeButton, profileButton, coursesButton, gradesButton, evenementButton;
    @FXML private Button avisButton, payementButton, evaluationButton, sallesButton, logoutButton;

    // Containers
    @FXML private HBox coursButtonsContainer;
    @FXML private HBox sessionsButtonsContainer;

    // Boutons d'affichage
    @FXML private Button afficherCoursBtn;
    @FXML private Button afficherSessionsBtn;

    @FXML private Button ajouterCoursBtn;
    @FXML private Button modifierCoursBtn;
    @FXML private Button supprimerCoursBtn;
    @FXML private Button ajouterSessionBtn;
    @FXML private Button modifierSessionBtn;
    @FXML private Button supprimerSessionBtn;

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



    @FXML public void initialize() {
        configureCoursColumns();
        configureSessionColumns();
        loadCours();
        tableCours.setVisible(true);
        tableSessionCours.setVisible(false);
        coursButtonsContainer.setVisible(true);
        sessionsButtonsContainer.setVisible(false);
    }
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

    private void naviguerVers(String fxmlPath, Button bouton) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) bouton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace(); // Pour avoir l'erreur complète
            System.out.println("Erreur Erreur de navigation: " + fxmlPath + " — " + e.getMessage());
        }
    }

    // Méthodes de navigation
    @FXML private void goToHome() {naviguerVers("/fxml/enseignant/accueilEnseignant.fxml", homeButton);}
    @FXML private void goToProfile() {System.out.println("Navigation vers Profil");}
    @FXML private void goToCourses() {naviguerVers("/Cours/GestionCoursAccueilEnseingnant.fxml", coursesButton);}
    @FXML private void goToGrades() {System.out.println("Navigation vers Notes");}
    @FXML private void goToEvenement() {System.out.println("Navigation vers Evenement");}
    @FXML private void goToAvis() {System.out.println("Navigation vers Avis");}
    @FXML private void goToPayement() {System.out.println("Navigation vers Payement");}
    @FXML private void goToEvaluation() {System.out.println("Navigation vers Evaluation");}
    @FXML private void goToSalles() {System.out.println("Navigation vers Salles");}
    @FXML private void logout() {System.out.println("Déconnexion...");}
    // Affichage de tables
    @FXML private void afficherTableCours() {
        tableCours.setVisible(true);
        tableSessionCours.setVisible(false);
        coursButtonsContainer.setVisible(true);
        sessionsButtonsContainer.setVisible(false);
        System.out.println("Affichage de la table des cours.");
        loadCours();
    }
    @FXML private void afficherTableSessions() {
        tableCours.setVisible(false);
        tableSessionCours.setVisible(true);
        coursButtonsContainer.setVisible(false);
        sessionsButtonsContainer.setVisible(true);
        loadSessions();
    }

    // Actions sur les cours
    @FXML private void handleAjouterCours() {naviguerVers("/Cours/ajouterCours.fxml", ajouterCoursBtn);}
    @FXML private void handleModifierCours() {naviguerVers("/Cours/modifierCours.fxml", modifierCoursBtn);}
    @FXML private void handleSupprimerCours() {naviguerVers("/Cours/supprimerCours.fxml", supprimerCoursBtn);}

    // Actions sur les sessions
    @FXML private void handleAjouterSession() {naviguerVers("/Cours/ajouterSession.fxml",afficherSessionsBtn);}
    @FXML private void handleModifierSession() {naviguerVers("/Cours/modifierSession.fxml", modifierSessionBtn);}
    @FXML private void handleSupprimerSession() {naviguerVers("/Cours/supprimerSession.fxml",supprimerSessionBtn);}

}
