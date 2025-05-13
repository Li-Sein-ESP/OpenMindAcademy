package controllers.CoursSession;

import entities.SessionCours;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import services.ServiceCours;
import services.ServiceSessionCours;
import entities.*;
import java.io.IOException;

public class modifierSessionController {

    // Boutons de navigation
    @FXML
    private VBox navbar;
    @FXML private Button homeButton, profileButton, coursesButton, gradesButton, evenementButton;
    @FXML private Button avisButton,btnRechercher,modifierButton, payementButton, evaluationButton, sallesButton, logoutButton;

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



    @FXML private Button afficherSessionsBtn;
    @FXML private Button afficherCoursBtn ;
    @FXML private Button ajouterSessionBtn ;
    @FXML private Button supprimerSessionBtn ;

    @FXML private ComboBox<String> critereComboBox;
    @FXML private TextField valeurRechercheField;
    @FXML private TextField nomField;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;
    @FXML private ComboBox<Cours> coursComboBox;
    @FXML private TextArea descriptionCoursArea;

    private void naviguerVers(String fxmlPath, Button bouton) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) bouton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur Erreur de navigation: " + e.getMessage());
        }
    }
    @FXML private void afficherTableSessions() {naviguerVers("/Cours/GestionCoursAccueilEnseingnant.fxml" , afficherSessionsBtn);}
    @FXML public void handleAjouterSession() {naviguerVers("/Cours/ajouterSession.fxml", ajouterSessionBtn);}
    @FXML public void handleSupprimerSession() {naviguerVers("/Cours/supprimerSession.fxml", supprimerSessionBtn);}

    private final ServiceSessionCours serviceSession = new ServiceSessionCours();
    private final ServiceCours serviceCours = new ServiceCours();
    private Stage stage;
    private SessionCours sessionToModify;

    @FXML public void initialize() {
        // Configuration des ComboBox
        configureCoursComboBox();
        configureRechercheComboBox();
        // Chargement des cours disponibles
        loadCours();
        // Désactiver les champs initialement
        setFieldsEditable(false);
    }
    private void configureCoursComboBox() {
        // Configurer l'affichage des noms de cours seulement
        coursComboBox.setConverter(new StringConverter<Cours>() {
            @Override
            public String toString(Cours cours) {
                return cours == null ? "" : cours.getNom();
            }

            @Override
            public Cours fromString(String string) {
                return null;
            }
        });

        // Mise à jour automatique de la description quand le cours change
        coursComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                descriptionCoursArea.setText(newVal.getDescription());
            } else {
                descriptionCoursArea.clear();
            }
        });
    }
    private void configureRechercheComboBox() {
        critereComboBox.getItems().setAll("ID", "Nom");
        critereComboBox.getSelectionModel().selectFirst();
    }
    private void loadCours() {
        coursComboBox.getItems().setAll(serviceCours.afficher());
    }
    @FXML private void handleRechercher() {
        String critere = critereComboBox.getValue();
        String valeur = valeurRechercheField.getText();

        if (valeur.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer une valeur de recherche");
            return;
        }

        SessionCours session = serviceSession.rechercherSession(critere, valeur);

        if (session != null) {
            sessionToModify = session;
            populateFields(session);
            setFieldsEditable(true);
        } else {
            showAlert("Information", "Aucune session trouvée");
            setFieldsEditable(false);
        }
    }
    private void populateFields(SessionCours session) {
        nomField.setText(session.getNom_session());
        dateDebutPicker.setValue(session.getDateDebut());
        dateFinPicker.setValue(session.getDateFin());
        coursComboBox.setValue(session.getCours());
        // La description sera mise à jour automatiquement via le listener
    }
    private void setFieldsEditable(boolean editable) {
        nomField.setDisable(!editable);
        dateDebutPicker.setDisable(!editable);
        dateFinPicker.setDisable(!editable);
        coursComboBox.setDisable(!editable);
    }
    @FXML private void handleModifier() {
        try {
            // Validation des champs
            if (!validateFields()) return;

            // Mise à jour de la session
            updateSessionFromFields();

            serviceSession.modifier(sessionToModify);

            showAlert("Succès", "Session modifiée avec succès");
            stage.close();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la modification: " + e.getMessage());
        }
    }
    private boolean validateFields() {
        if (nomField.getText().isEmpty() ||
                dateDebutPicker.getValue() == null ||
                dateFinPicker.getValue() == null ||
                coursComboBox.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs");
            return false;
        }

        if (dateFinPicker.getValue().isBefore(dateDebutPicker.getValue())) {
            showAlert("Erreur", "La date de fin doit être après la date de début");
            return false;
        }

        return true;
    }
    private void updateSessionFromFields() {
        sessionToModify.setNom_session(nomField.getText());
        sessionToModify.setDateDebut(dateDebutPicker.getValue());
        sessionToModify.setDateFin(dateFinPicker.getValue());
        sessionToModify.setCours(coursComboBox.getValue());
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
