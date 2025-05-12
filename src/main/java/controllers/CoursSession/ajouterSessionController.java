package controllers.CoursSession;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import entities.*;
import services.*;

public class ajouterSessionController {

    // Boutons de navigation
    @FXML private VBox navbar;
    @FXML private Button homeButton, profileButton, coursesButton, gradesButton, evenementButton,supprimerSessionsBtn;
    @FXML private Button avisButton, payementButton, evaluationButton, sallesButton, logoutButton, modifierSessionBtn ,ajouterSessionBtn;

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
    // Éléments du formulaire
    @FXML private TextField nomSessionField;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;
    @FXML private ComboBox<String> coursComboBox; // Changé pour String si vous voulez juste afficher les noms
    @FXML private TextArea descriptionCoursArea;
    @FXML private Button afficherSessionsBtn;
    // Services
    private final ServiceSessionCours sessionService = new ServiceSessionCours();
    private final ServiceCours coursService = new ServiceCours();

    @FXML private void initialize() {
        // Charger les cours dans la ComboBox
        List<Cours> coursList = coursService.afficher();
        for (Cours cours : coursList) {
            coursComboBox.getItems().add(cours.getNom());
        }

        // Écouteur pour afficher la description
        coursComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Cours selected = coursList.stream()
                        .filter(c -> c.getNom().equals(newVal))
                        .findFirst()
                        .orElse(null);
                if (selected != null) {
                    descriptionCoursArea.setText(selected.getDescription());
                }
            }
        });
    }
    @FXML private void handleAjouterSession() {
        try {
            // Validation
            String nomSession = nomSessionField.getText();
            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();
            String nomCours = coursComboBox.getValue();

            if (nomSession.isEmpty() || dateDebut == null || dateFin == null || nomCours == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs");
                return;
            }

            if (dateFin.isBefore(dateDebut)) {
                showAlert("Erreur", "La date de fin doit être après la date de début");
                return;
            }

            // Trouver l'ID du cours
            Cours cours = coursService.afficher().stream()
                    .filter(c -> c.getNom().equals(nomCours))
                    .findFirst()
                    .orElse(null);

            if (cours != null) {
                SessionCours session = new SessionCours(0, nomSession, dateDebut, dateFin, cours);
                sessionService.ajouter(session, cours.getIdCours());
                showAlert("Succès", "Session ajoutée avec succès");
                clearForm();
            } else {
                showAlert("Erreur", "Cours introuvable");
            }
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout: " + e.getMessage());
        }
    }
    private void clearForm() {
        nomSessionField.clear();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        coursComboBox.setValue(null);
        descriptionCoursArea.clear();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthodes de navigation (conservées inchangées)
    @FXML private void afficherTableSessions() {naviguerVers("/Cours/GestionCoursAccueilEnseingnant.fxml", afficherSessionsBtn);}
    public void modifierSession() {naviguerVers("/Cours/modifierSession.fxml", modifierSessionBtn);}
    public void supprimerSessions() {naviguerVers("/Cours/supprimerSession.fxml",supprimerSessionsBtn);}


}

