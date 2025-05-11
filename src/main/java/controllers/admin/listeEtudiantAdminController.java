package controllers.admin;

import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.UserService;
import utils.NavigationUtil;

import java.io.IOException;
import java.sql.SQLException;

public class listeEtudiantAdminController {

    @FXML private TableView<User> etudiantTable;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> nomColumn;
    @FXML private TableColumn<User, String> prenomColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> niveauEtudeColumn;
    @FXML private TableColumn<User, Void> actionColumn;
    @FXML private Button homeButton;
    @FXML private Button listTeachersButton;
    @FXML private Button listStudentsButton;
    @FXML private Button logoutButton;
    @FXML private Button sallesButton;
    @FXML private Button evenementButton;
    @FXML private Button payementButton;
    @FXML private Button avisButton;
    @FXML private Button evaluationButton;

    private UserService userService = new UserService();
    private User currentUser;

    @FXML
    public void initialize() {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            niveauEtudeColumn.setCellValueFactory(new PropertyValueFactory<>("niveauEtude"));

            actionColumn.setCellFactory(param -> new TableCell<User, Void>() {
                private final Button editButton = new Button("Modifier");
                private final Button deleteButton = new Button("Supprimer");

                {
                    editButton.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white;");
                    deleteButton.setStyle("-fx-background-color: #FF4444; -fx-text-fill: white;");
                    editButton.setOnAction(event -> {
                        User user = getTableView().getItems().get(getIndex());
                        try {
                            editUser(user);
                        } catch (IOException e) {
                            NavigationUtil.showError("Erreur lors de la modification : " + e.getMessage());
                        }
                    });
                    deleteButton.setOnAction(event -> {
                        User user = getTableView().getItems().get(getIndex());
                        deleteUser(user);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        User user = getTableView().getItems().get(getIndex());
                        boolean isCurrentUser = currentUser != null && user.getId() == currentUser.getId();
                        editButton.setDisable(isCurrentUser);
                        deleteButton.setDisable(isCurrentUser);
                        HBox hbox = new HBox(5, editButton, deleteButton);
                        setGraphic(hbox);
                    }
                }
            });

            loadStudents();
    }

    public void setUser(User user) {
        this.currentUser = user;
        loadStudents();
    }

    private void loadStudents() {
        try {
            etudiantTable.getItems().setAll(userService.getUsersByRole(enums.Role.STUDENT));
        } catch (SQLException e) {
            NavigationUtil.showError("Erreur lors du chargement des étudiants : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void editUser(User user) throws IOException {
        if (user.getRole() != enums.Role.STUDENT) {
            NavigationUtil.showError("Seuls les étudiants peuvent être modifiés ici.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/editUserAdmin.fxml"));
        Parent root = loader.load();
        editUserAdminController controller = loader.getController();
        controller.setUsers(currentUser, user);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        Stage stage = (Stage) etudiantTable.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void deleteUser(User user) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cet étudiant ? Cette action est irréversible.", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    userService.deleteUser(user.getId());
                    loadStudents();
                } catch (SQLException e) {
                    NavigationUtil.showError("Erreur lors de la suppression : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void goToHome() {
        NavigationUtil.loadSceneWithUser("/fxml/admin/UserManagement.fxml", homeButton, currentUser);
    }

    @FXML
    public void goToTeacherList() {
        NavigationUtil.loadSceneWithUser("/fxml/admin/listeEnseignant.fxml", listTeachersButton, currentUser);
    }

    @FXML
    public void goToStudentList() {
        NavigationUtil.loadSceneWithUser("/fxml/admin/listeEtudiant.fxml", listStudentsButton, currentUser);
    }

    @FXML
    public void logout() {
        NavigationUtil.loadScene("/fxml/connexion.fxml", logoutButton);
    }

    @FXML
    public void goToSalles() {
        // TODO: Replace with actual navigation path
        NavigationUtil.loadSceneWithUser("/fxml/admin/salles.fxml", sallesButton, currentUser);
    }

    @FXML
    public void goToEvenement() {
        // TODO: Replace with actual navigation path
        NavigationUtil.loadSceneWithUser("/fxml/admin/evenement.fxml", evenementButton, currentUser);
    }

    @FXML
    public void goToPayement() {
        // TODO: Replace with actual navigation path
        NavigationUtil.loadSceneWithUser("/fxml/admin/payement.fxml", payementButton, currentUser);
    }

    @FXML
    public void goToAvis() {
        // TODO: Replace with actual navigation path
        NavigationUtil.loadSceneWithUser("/fxml/admin/avis.fxml", avisButton, currentUser);
    }

    @FXML
    public void goToEvaluation() {
        // TODO: Replace with actual navigation path
        NavigationUtil.loadSceneWithUser("/fxml/admin/evaluation.fxml", evaluationButton, currentUser);
    }
}
