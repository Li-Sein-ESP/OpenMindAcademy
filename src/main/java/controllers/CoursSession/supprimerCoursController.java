package controllers.CoursSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import entities.*;
import services.*;

public class supprimerCoursController {


    @FXML public VBox navbar;
    // Boutons de navigation
    @FXML private Button homeButton, profileButton, coursesButton, gradesButton, evenementButton;
    @FXML private Button avisButton, payementButton, evaluationButton, sallesButton, logoutButton;

    @FXML private Button afficherCoursBtn;
    @FXML private Button modifierCoursBtn;
    @FXML private Button supprimerButton;
    @FXML private Button ajouterCoursBtn;

    @FXML private ComboBox<String> critereComboBox;
    @FXML private TextField valeurRechercheField;
    @FXML private Button btnRechercher;
    @FXML private TableView<Cours> coursTable;
    @FXML private TableColumn<Cours, Integer> idCol;
    @FXML private TableColumn<Cours, String> nomCol;
    @FXML private TableColumn<Cours, String> descCol;
    @FXML private TableColumn<Cours, Integer> dureeCol;
    @FXML private TableColumn<Cours, Float> prixCol;
    @FXML private TableColumn<Cours, String> categorieCol;

    private final ServiceCours serviceCours = new ServiceCours();
    private final ObservableList<Cours> coursList = FXCollections.observableArrayList();


    private void naviguerVers(String fxmlPath, Button bouton) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) bouton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur Erreur de navigation: " + e.getMessage());
        }
    }
    @FXML private void afficherTableCours() {naviguerVers("/Cours/GestionCoursAccueilEnseingnant.fxml", afficherCoursBtn);}
    @FXML private void handleModifierCours() {naviguerVers("/Cours/modifierCours.fxml", modifierCoursBtn); }
    @FXML public void handleAjouterCours() {naviguerVers("/Cours/ajouterCours.fxml", ajouterCoursBtn);}
    @FXML private void initialize() {
        if (valeurRechercheField == null) {
            System.out.println("valeurRechercheField is null!");
        }

        critereComboBox.getItems().setAll("ID", "Nom");
        critereComboBox.setOnAction(event -> {
            if (valeurRechercheField != null) {
                valeurRechercheField.setVisible(true);
                btnRechercher.setVisible(true);
            }
        });
        // Initialisation des colonnes de la TableView
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdCours()).asObject());
        nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
        descCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        dureeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getDuree()).asObject());
        prixCol.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getPrix()).asObject());
        categorieCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategoriesCours().getNom()));

        coursTable.setItems(coursList);
    }
    @FXML private void rechercherCours() {
        String critere = critereComboBox.getValue();
        String valeur = valeurRechercheField.getText();

        // Appel à la méthode rechercherCours pour obtenir le cours
        Cours coursActuel = serviceCours.rechercherCours(critere, valeur);

        if (coursActuel != null) {
            coursList.clear();  // Vide la liste existante avant d'ajouter un cours
            coursList.add(coursActuel);  // Ajoute le cours trouvé
        } else {
            // Message d'erreur si aucun cours trouvé
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cours non trouvé.");
            alert.show();
        }
    }
    @FXML private void SupprimerCourss() {
        Cours selectionne = coursTable.getSelectionModel().getSelectedItem();

        if (selectionne == null) {
            new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un cours à supprimer.").show();
            return;
        }

        // Supprimer le cours via le service
        serviceCours.supprimer("ID", String.valueOf(selectionne.getIdCours()));

        // Rafraîchir la liste des cours dans la TableView
        coursList.remove(selectionne);
        new Alert(Alert.AlertType.INFORMATION, "Cours supprimé avec succès.").show();
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


}


