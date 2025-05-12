package services;

import entities.User;
import enums.Role;
import org.mindrot.jbcrypt.BCrypt;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserService {
    private Connection connection;

    public UserService() {
        connection = MyDatabase.getInstance().getConnection();
        if (connection == null) {
            throw new IllegalStateException("Failed to initialize UserService: Database connection is null");
        }
    }

    public void addUser(User user) throws SQLException {
        ensureConnection();
        String query = "INSERT INTO users (nom, prenom, adresse, date_naissance, role, email, sexe, num_telephone, diplome, niveau_etude, password, face_encoding) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getNom());
            stmt.setString(2, user.getPrenom());
            stmt.setString(3, user.getAdresse());
            stmt.setObject(4, user.getDateNaissance() != null ? Date.valueOf(user.getDateNaissance()) : null);
            stmt.setString(5, user.getRole().toString());
            stmt.setString(6, user.getEmail());
            stmt.setString(7, user.getSexe());
            stmt.setString(8, user.getNumTelephone());
            stmt.setString(9, user.getDiplome());
            stmt.setString(10, user.getNiveauEtude());
            stmt.setString(11, encryptPassword(user.getPassword()));
            stmt.setString(12, user.getFaceEncoding()); // Add face encoding
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<User> getAllUsers() throws SQLException {
        ensureConnection();
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }

    public User getUserByEmail(String email) throws SQLException {
        ensureConnection();
        String query = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Table 'user_management.users' doesn't exist")) {
                throw new SQLException("The 'users' table does not exist in the database. Please create it.", e);
            }
            throw e;
        }
        return null;
    }

    public User getUserById(int userId) throws SQLException {
        ensureConnection();
        String query = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }
        return null;
    }

    public List<User> getUsersByRole(Role role) throws SQLException {
        ensureConnection();
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, role.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
        }
        return users;
    }

    public void updateUser(User user) throws SQLException {
        ensureConnection();
        String query = "UPDATE users SET nom = ?, prenom = ?, adresse = ?, date_naissance = ?, role = ?, email = ?, sexe = ?, num_telephone = ?, diplome = ?, niveau_etude = ?, password = ?, face_encoding = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getNom());
            stmt.setString(2, user.getPrenom());
            stmt.setString(3, user.getAdresse());
            stmt.setObject(4, user.getDateNaissance() != null ? Date.valueOf(user.getDateNaissance()) : null);
            stmt.setString(5, user.getRole().toString());
            stmt.setString(6, user.getEmail());
            stmt.setString(7, user.getSexe());
            stmt.setString(8, user.getNumTelephone());
            stmt.setString(9, user.getDiplome());
            stmt.setString(10, user.getNiveauEtude());
            stmt.setString(11, user.getPassword());
            stmt.setString(12, user.getFaceEncoding());
            stmt.setInt(13, user.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        ensureConnection();
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    public String generateAndResetPassword(int userId) throws SQLException {
        String newPassword = generateRandomPassword();
        String encryptedPassword = encryptPassword(newPassword);
        String query = "UPDATE users SET password = ?, reset_token = NULL, reset_token_expiry = NULL WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, encryptedPassword);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
        return newPassword;
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    private String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setNom(rs.getString("nom"));
        user.setPrenom(rs.getString("prenom"));
        user.setAdresse(rs.getString("adresse"));
        Date dateNaissance = rs.getDate("date_naissance");
        user.setDateNaissance(dateNaissance != null ? dateNaissance.toLocalDate() : null);
        user.setRole(Role.valueOf(rs.getString("role")));
        user.setEmail(rs.getString("email"));
        user.setSexe(rs.getString("sexe"));
        user.setNumTelephone(rs.getString("num_telephone"));
        user.setDiplome(rs.getString("diplome"));
        user.setNiveauEtude(rs.getString("niveau_etude"));
        user.setPassword(rs.getString("password"));
        user.setResetToken(rs.getString("reset_token"));
        user.setFaceEncoding(rs.getString("face_encoding")); // Add face encoding
        return user;
    }

    private void ensureConnection() throws SQLException {
        connection = MyDatabase.getInstance().getConnection();
        if (connection == null) {
            throw new SQLException("Database connection is null");
        }
    }
}