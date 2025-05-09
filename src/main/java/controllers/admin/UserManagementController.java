package controllers.admin;

import entities.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.UserService;
import utils.NavigationUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserManagementController {

    @FXML private VBox navbar;
    @FXML private Button homeButton;
    @FXML private Button listTeachersButton;
    @FXML private Button listStudentsButton;
    @FXML private Button logoutButton;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> nomColumn;
    @FXML private TableColumn<User, String> prenomColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, Void> actionColumn;

    private User currentUser;
    private UserService userService;
    private List<User> allUsers;

    @FXML
    public void initialize() {
        userService = new UserService();

        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole().toString()));

        // Configure action column with edit and delete buttons
        setupActionColumn();

        // Load all users
        loadUsers();
    }
    public void setUser(User user) {
        this.currentUser = user;
    }

    @FXML
    public void goHome() {
        NavigationUtil.loadSceneWithUser("/fxml/profile.fxml", homeButton, currentUser);
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

    private void showError(String message) {
        NavigationUtil.showError(message);
    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Modifier");
            private final Button deleteBtn = new Button("Supprimer");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);

            {
                // Configure edit button
                editBtn.getStyleClass().add("small-button");
                editBtn.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    editUser(user);
                });

                // Configure delete button
                deleteBtn.getStyleClass().add("small-button");
                deleteBtn.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    deleteUser(user);
                });

                pane.setAlignment(javafx.geometry.Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void loadUsers() {
        try {
            allUsers = userService.getAllUsers();
            userTable.getItems().setAll(allUsers);
        } catch (SQLException e) {
            showError("Erreur lors du chargement des utilisateurs: " + e.getMessage());
        }
    }

    @FXML
    public void searchUsers() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        if (searchTerm.isEmpty()) {
            userTable.getItems().setAll(allUsers);
            return;
        }

        List<User> filteredUsers = allUsers.stream()
            .filter(user -> 
                String.valueOf(user.getId()).contains(searchTerm) ||
                (user.getNom() != null && user.getNom().toLowerCase().contains(searchTerm)) ||
                (user.getPrenom() != null && user.getPrenom().toLowerCase().contains(searchTerm)) ||
                (user.getEmail() != null && user.getEmail().toLowerCase().contains(searchTerm)) ||
                (user.getRole() != null && user.getRole().toString().toLowerCase().contains(searchTerm))
            )
            .collect(java.util.stream.Collectors.toList());

        userTable.getItems().setAll(filteredUsers);
    }

    private void editUser(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/editUserAdmin.fxml"));
            Parent root = loader.load();

            editUserAdminController controller = loader.getController();
            controller.setUsers(currentUser, user);

            Stage stage = (Stage) userTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors de l'ouverture de la page d'édition: " + e.getMessage());
        }
    }

    private void deleteUser(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet utilisateur ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    userService.deleteUser(user.getId());
                    loadUsers(); // Reload the table
                } catch (SQLException e) {
                    showError("Erreur lors de la suppression: " + e.getMessage());
                }
            }
        });
    }
}
