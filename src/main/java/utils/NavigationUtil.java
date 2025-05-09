package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Node;
import entities.User;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Classe utilitaire pour gérer la navigation entre les écrans
 */
public class NavigationUtil {

    /**
     * Charge une nouvelle scène FXML
     * @param fxmlPath Chemin du fichier FXML
     * @param node Nœud de la scène actuelle
     * @return Le contrôleur de la nouvelle scène
     */
    public static Object loadScene(String fxmlPath, Node node) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(NavigationUtil.class.getResource("/fxml/styles.css").toExternalForm());
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            return loader.getController();
        } catch (IOException e) {
            showError("Erreur lors du chargement de la page: " + e.getMessage());
            return null;
        }
    }

    /**
     * Charge une nouvelle scène FXML et passe l'utilisateur au contrôleur
     * @param fxmlPath Chemin du fichier FXML
     * @param node Nœud de la scène actuelle
     * @param user Utilisateur à passer au contrôleur
     * @return Le contrôleur de la nouvelle scène
     */
    public static Object loadSceneWithUser(String fxmlPath, Node node, User user) {
        Object controller = loadScene(fxmlPath, node);
        if (controller != null) {
            try {
                Method setUserMethod = controller.getClass().getMethod("setUser", User.class);
                setUserMethod.invoke(controller, user);
            } catch (Exception e) {
                showError("Erreur lors de la configuration de l'utilisateur: " + e.getMessage());
            }
        }
        return controller;
    }

    /**
     * Affiche une boîte de dialogue d'erreur
     * @param message Message d'erreur à afficher
     */
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}