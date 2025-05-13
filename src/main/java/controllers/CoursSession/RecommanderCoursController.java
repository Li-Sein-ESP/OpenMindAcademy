package controllers.CoursSession;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import entities.CategoriesCours;
import entities.Cours;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceCours;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class RecommanderCoursController {

    // Champs de formulaire
    @FXML private ComboBox<String> categorieComboBox; // Affiche uniquement les noms des catégories
    @FXML private TextField budgetTextField;
    @FXML private TextField dureeTextField;
    @FXML private Label resultatLabel;

    // Boutons de navigation
    @FXML private VBox navbar;
    @FXML private Button homeButton, profileButton, coursesButton, gradesButton, evenementButton;
    @FXML private Button avisButton, payementButton, evaluationButton, sallesButton, logoutButton;
    @FXML private Button afficherCoursBtn;
    @FXML private Button afficherSessionsBtn;

    // Service pour les cours
    private ServiceCours serviceCours = new ServiceCours();

    @FXML public void initialize() {
        // Initialisation du ComboBox avec les catégories
        remplirCategorieComboBox();
        resultatLabel.setText("Aucun cours recommandé pour le moment.");
    }

    private void remplirCategorieComboBox() {
        // Récupérer les catégories depuis la base de données via ServiceCours
        List<CategoriesCours> categories = serviceCours.getAllCategories();

        if (categories.isEmpty()) {
            categorieComboBox.setPromptText("Aucune catégorie trouvée");
            return;
        }

        // Ajouter uniquement le nom des catégories au ComboBox
        for (CategoriesCours categorie : categories) {
            categorieComboBox.getItems().add(categorie.getNom());
        }
    }


    @FXML private void recommanderCours() {
        // Récupérer les critères de l'utilisateur
        String categoriePreferee = categorieComboBox.getValue(); // Récupère le nom sélectionné
        String budgetText = budgetTextField.getText();
        String dureeText = dureeTextField.getText();

        // Validation des entrées
        if (categoriePreferee == null || budgetText.isEmpty() || dureeText.isEmpty()) {
            resultatLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        float budgetMax;
        int dureeMax;

        try {
            budgetMax = Float.parseFloat(budgetText);
            dureeMax = Integer.parseInt(dureeText);
        } catch (NumberFormatException e) {
            resultatLabel.setText("Veuillez entrer un budget et une durée valides.");
            return;
        }

        // Charger les cours depuis le service
        List<Cours> coursList = serviceCours.afficher();

        // Définir une tolérance pour le budget et la durée
        float tolerancePrix = 20.0f; // 20€ de tolérance pour le prix
        int toleranceDuree = 5; // 2 heures de tolérance pour la durée

        // Rechercher le cours recommandé en utilisant des tolérances
        Optional<Cours> coursRecommande = coursList.stream()
                .filter(cours -> cours.getCategoriesCours().getNom().equalsIgnoreCase(categoriePreferee)) // Filtrer par catégorie
                .filter(cours -> Math.abs(cours.getPrix() - budgetMax) <= tolerancePrix) // Tolérance sur le prix
                .filter(cours -> Math.abs(cours.getDuree() - dureeMax) <= toleranceDuree) // Tolérance sur la durée
                .sorted((c1, c2) -> {
                    // Trier par proximité au budget et à la durée
                    int prixCompare = Float.compare(Math.abs(c1.getPrix() - budgetMax), Math.abs(c2.getPrix() - budgetMax));
                    if (prixCompare != 0) return prixCompare;
                    return Integer.compare(Math.abs(c1.getDuree() - dureeMax), Math.abs(c2.getDuree() - dureeMax));
                })
                .findFirst(); // Retourner le premier cours correspondant

        // Afficher le résultat
        if (coursRecommande.isPresent()) {
            Cours cours = coursRecommande.get();
            resultatLabel.setText("Cours recommandé : " + cours.getNom() + " (" +
                    cours.getDuree() + "h, " + cours.getPrix() + "€)");
        } else {
            resultatLabel.setText("Aucun cours ne correspond aux critères avec la tolérance définie.");
        }

        // Réinitialiser le formulaire pour permettre une nouvelle tentative
        //resetFormulaire();
    }

    @FXML private void resetFormulaire() {
        // Réinitialiser les champs du formulaire
        categorieComboBox.setValue(null); // Réinitialise le ComboBox
        budgetTextField.clear(); // Efface le champ du budget
        dureeTextField.clear(); // Efface le champ de la durée
        resultatLabel.setText("Aucun cours recommandé pour le moment."); // Réinitialise le label du résultat
    }

    // Méthodes de navigation
    @FXML private void goToHome() {naviguerVers("/fxml/etudiant/accueilEtudiant.fxml", homeButton);}
    @FXML private void goToProfile() {System.out.println("Navigation vers Profil");}
    @FXML private void goToCourses() {naviguerVers("/Cours/GestionCoursAccueilEnseingnant.fxml", coursesButton);}
    @FXML private void goToGrades() {System.out.println("Navigation vers Notes");}
    @FXML private void goToEvenement() {System.out.println("Navigation vers Evénement");}
    @FXML private void goToAvis() {System.out.println("Navigation vers Avis");}
    @FXML private void goToPayement() {System.out.println("Navigation vers Paiement");}
    @FXML private void goToEvaluation() {System.out.println("Navigation vers Évaluation");}
    @FXML private void goToSalles() {System.out.println("Navigation vers Salles");}
    @FXML private void logout() {naviguerVers("/fxml/connexion.fxml", logoutButton);}

    @FXML private void afficherTableCours() {naviguerVers("/Cours/afficherSessionEtudiant.fxml", afficherCoursBtn);}
    @FXML private void afficherTableSessions() {naviguerVers("/Cours/afficherSessionEtudiant.fxml", afficherSessionsBtn);}

    private void naviguerVers(String fxmlPath, Button bouton) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) bouton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.err.println("Erreur lors de la navigation vers : " + fxmlPath);
            e.printStackTrace();
            resultatLabel.setText("Erreur de navigation. Veuillez réessayer.");
        }
    }
}