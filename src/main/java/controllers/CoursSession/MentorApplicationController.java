package controllers.CoursSession;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MentorApplicationController {

    @FXML public Button afficherCoursBtn,afficherSessionsBtn;
    // Barre de navigation
    @FXML private VBox navbar;
    @FXML private Button homeButton, studentsButton, profileButton, coursesButton, salleButton;
    @FXML private Button evenementButton, evaluationButton, logoutButton;

    // Formulaire de candidature pour devenir mentor
    @FXML private ComboBox<String> experienceComboBox;
    @FXML private ComboBox<String> pedagogieComboBox;
    @FXML private ComboBox<String> communicationComboBox;
    @FXML private Label resultLabel;
    @FXML private Button applyButton;
    @FXML private Button resetButton;

    @FXML
    public void initialize() {
        // Initialisation des ComboBox
        resultLabel.setText("Aucun résultat pour le moment.");
    }



    // Méthode pour postuler en tant que mentor
    @FXML
    private void postulerMentor() {
        // Vérifier si tous les champs sont remplis
        if (experienceComboBox.getValue() == null || pedagogieComboBox.getValue() == null || communicationComboBox.getValue() == null) {
            resultLabel.setText("Veuillez remplir tous les critères avant de postuler.");
            return;
        }

        // Récupérer les valeurs des critères
        int experience = Integer.parseInt(experienceComboBox.getValue());
        int pedagogie = Integer.parseInt(pedagogieComboBox.getValue());
        int communication = Integer.parseInt(communicationComboBox.getValue());

        // Calculer la probabilité et le poste recommandé
        Map<String, Integer> criteres = new HashMap<>();
        criteres.put("experience", experience);
        criteres.put("pedagogie", pedagogie);
        criteres.put("communication", communication);

        String resultat = calculerCandidature(criteres);
        resultLabel.setText(resultat);
    }

    // Méthode pour réinitialiser le formulaire
    @FXML
    private void resetFormulaire() {
        experienceComboBox.setValue(null);
        pedagogieComboBox.setValue(null);
        communicationComboBox.setValue(null);
        resultLabel.setText("Aucun résultat pour le moment.");
    }

    // Méthode interne pour calculer la probabilité et le poste recommandé
    private String calculerCandidature(Map<String, Integer> criteres) {
        // Critères par défaut
        int experience = criteres.getOrDefault("experience", 5);
        int pedagogie = criteres.getOrDefault("pedagogie", 4);
        int communication = criteres.getOrDefault("communication", 4);

        // Calcul de la chance d'acceptation
        int score = (experience * 50) + (pedagogie * 30) + (communication * 20);
        int chanceAcceptation = Math.min(100, score / 10);

        // Déterminer le poste recommandé
        String poste;
        if (chanceAcceptation >= 80) {
            poste = "Mentor Sénior (Projets Avancés)";
        } else if (chanceAcceptation >= 50) {
            poste = "Mentor Intermédiaire (Projets Intermédiaires)";
        } else {
            poste = "Mentor Débutant (Initiation et Modules Simples)";
        }

        // Retourner un résumé
        return String.format("Votre chance d'acceptation : %d%%\nPoste recommandé : %s", chanceAcceptation, poste);
    }

    // Méthodes de navigation
    @FXML private void goToHome() { naviguerVers("/fxml/enseignant/accueilEnseignant.fxml", homeButton); }
    @FXML private void goToStudentsList() { System.out.println("Navigation vers la liste des étudiants."); }
    @FXML private void goToProfile() { System.out.println("Navigation vers le profil."); }
    @FXML private void goToCourses() { System.out.println("Navigation vers les cours."); }
    @FXML private void goToSalle() { System.out.println("Navigation vers la salle."); }
    @FXML private void goToEvenement() { System.out.println("Navigation vers les événements."); }
    @FXML private void goToEvaluation() { System.out.println("Navigation vers l'évaluation."); }
    @FXML private void logout() { System.out.println("Déconnexion..."); }

    private void naviguerVers(String fxmlPath, Button bouton) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) bouton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            resultLabel.setText("Erreur de navigation. Veuillez réessayer.");
        }
    }

    @FXML private void afficherTableCours() {naviguerVers("/Cours/GestionCoursAccueilEnseingnant.fxml", afficherCoursBtn); }
    @FXML private void afficherTableSessions() {naviguerVers("/Cours/GestionCoursAccueilEnseingnant.fxml",afficherSessionsBtn); }
}