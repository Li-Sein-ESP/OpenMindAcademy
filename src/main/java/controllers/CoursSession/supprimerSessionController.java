package controllers.CoursSession;

import entities.SessionCours;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceSessionCours;

import java.io.IOException;

public class supprimerSessionController {

    // Boutons de navigation
    @FXML private VBox navbar;
    @FXML private Button homeButton, profileButton, coursesButton, gradesButton, evenementButton;
    @FXML private Button avisButton, payementButton, evaluationButton, sallesButton, logoutButton;

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

    @FXML private Button afficherCoursBtn ;
    @FXML private Button afficherSessionsBtn ;
    @FXML private Button modifierSessionBtn ;
    @FXML private Button AjouterSessionBtn ,supprimerButton;

    @FXML private ComboBox<String> critereComboBox;
    @FXML private TextField valeurRechercheField;
    @FXML private Button btnRechercher;
    @FXML private TableView<SessionCours> sessionTable;
    @FXML private TableColumn<SessionCours, Integer> idCol;
    @FXML private TableColumn<SessionCours, String> nomCol;
    @FXML private TableColumn<SessionCours, String> dateDebutCol;
    @FXML private TableColumn<SessionCours, String> dateFinCol;
    @FXML private TableColumn<SessionCours, String> coursCol;

    private final ServiceSessionCours serviceSession = new ServiceSessionCours();
    private final ObservableList<SessionCours> sessionList = FXCollections.observableArrayList();

    @FXML private void afficherTableSessions() {naviguerVers("/Cours/GestionCoursAccueilEnseingnant.fxml" , afficherSessionsBtn);}
    @FXML public void handleModifierSession(){naviguerVers("/Cours/modifierSession.fxml", modifierSessionBtn);}
    @FXML public void handleAjouterSession() {naviguerVers("/Cours/ajouterSession.fxml", AjouterSessionBtn);}

    @FXML private void initialize() {
        critereComboBox.getItems().setAll("ID", "Nom");

        critereComboBox.setOnAction(event -> {
            valeurRechercheField.setVisible(true);
            btnRechercher.setVisible(true);
        });

        // Initialisation des colonnes avec SimpleXxxProperty
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom_session()));
        dateDebutCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDateDebut().toString()));
        dateFinCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDateFin().toString()));
        coursCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCours().getNom()));

        sessionTable.setItems(sessionList);
    }
    @FXML private void rechercherSession() {
        String critere = critereComboBox.getValue();
        String valeur = valeurRechercheField.getText();

        SessionCours session = serviceSession.rechercherSession(critere, valeur);

        if (session != null) {
            sessionList.clear();
            sessionList.add(session);
        } else {
            showAlert("Erreur", "Session non trouvée");
        }
    }
    @FXML private void supprimerSession() {
        SessionCours selectionne = sessionTable.getSelectionModel().getSelectedItem();

        if (selectionne == null) {
            showAlert("Avertissement", "Veuillez sélectionner une session à supprimer");
            return;
        }

        serviceSession.supprimer("ID", String.valueOf(selectionne.getId()));
        sessionList.remove(selectionne);
        showAlert("Succès", "Session supprimée avec succès");
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }






}
