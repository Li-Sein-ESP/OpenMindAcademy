package controllers.CoursSession;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import entities.*;
import services.*;

public class ajouterCoursController {


    // Boutons de navigation
    @FXML private VBox navbar;
    @FXML private Button homeButton, profileButton, coursesButton, gradesButton, evenementButton;
    @FXML private Button avisButton, payementButton, evaluationButton, sallesButton, logoutButton;

        @FXML private Spinner<Integer> dureeSpinner;
        @FXML private Button afficherCoursBtn;
        @FXML private Button modifierCoursBtn;
        @FXML private Button supprimerCoursBtn;
        @FXML private Button ajouterButton;
        @FXML private TextField nomField;
        @FXML private TextField descriptionField;
        @FXML private TextField prixField;
        @FXML private ComboBox<String> categorieComboBox;


        private ServiceCours coursService = new ServiceCours();

        @FXML
        private void initialize() {
            // Récupérer les catégories depuis la base de données
            List<CategoriesCours> categories = coursService.getAllCategories();
            for (CategoriesCours categorie : categories) {
                categorieComboBox.getItems().add(categorie.getNom());
            }

            // Initialisation du Spinner pour la durée
            dureeSpinner.setValueFactory(
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(20, 100, 1));}
        @FXML private void ajouterCours() {
            String nom = nomField.getText();
            String description = descriptionField.getText();
            String prixTexte = prixField.getText();
            String categorie = categorieComboBox.getValue();
            Integer duree = dureeSpinner.getValue();

            if (nom.isEmpty() || description.isEmpty() || prixTexte.isEmpty() || categorie == null || duree == null) {
                System.out.println("Veuillez remplir tous les champs !");
                return;
            }

            try {
                double prix = Double.parseDouble(prixTexte);

                System.out.println("Cours ajouté :");
                System.out.println("Nom: " + nom);
                System.out.println("Description: " + description);
                System.out.println("Prix: " + prix);
                System.out.println("Durée: " + duree + " heures");
                System.out.println("Catégorie: " + categorie);

                // Trouver l'id de la catégorie
                List<CategoriesCours> categories = coursService.getAllCategories();
                int idCategorie = -1;
                for (CategoriesCours cat : categories) {
                    if (cat.getNom().equals(categorie)) {
                        idCategorie = cat.getId_categories();
                        break;
                    }
                }

                if (idCategorie != -1) {
                    // Créer l'objet Cours
                    Cours cours = new Cours();
                    cours.setNom(nom);
                    cours.setDescription(description);
                    cours.setDuree(duree);
                    cours.setPrix((float) prix);

                    // Ajouter dans la base
                    coursService.ajouter(cours, idCategorie);

                    System.out.println("Cours ajouté en base de données.");
                } else {
                    System.out.println("Catégorie introuvable !");
                }

            } catch (NumberFormatException e) {
                System.out.println("Le prix doit être un nombre valide.");
            }
        }
        // Méthodes de navigation existantes
        private void naviguerVers(String fxmlPath, Button bouton) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
                Stage stage = (Stage) bouton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                System.out.println("Erreur Erreur de navigation: " + e.getMessage());
            }
        }
        // Méthodes non implémentées (conservées pour compatibilité)

        @FXML private void afficherTableCours() {naviguerVers("/Cours/GestionCoursAccueilEnseingnant.fxml", afficherCoursBtn);}
        @FXML private void handleModifierCours() {naviguerVers("/Cours/modifierCours.fxml", modifierCoursBtn);}
        @FXML private void handleSupprimerCours() {naviguerVers("/Cours/supprimerCours.fxml", supprimerCoursBtn);}

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
