package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/user_management?autoReconnect=true&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private Connection connection;
    private static MyDatabase instance;

    private MyDatabase() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connexion établie à MySQL");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            // Check if the connection is null or closed
            if (connection == null || connection.isClosed()) {
                System.out.println("Connexion fermée ou non initialisée, tentative de reconnexion...");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connexion rétablie à MySQL");
            }
            // Optionally, check if the connection is still valid
            if (!connection.isValid(2)) { // 2-second timeout for validation
                System.out.println("Connexion invalide, tentative de reconnexion...");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connexion rétablie à MySQL");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification ou de la reconnexion : " + e.getMessage());
            e.printStackTrace();
            // Return null to indicate failure (caller should handle this)
            return null;
        }
        return connection;
    }

    // Optional: Method to close the connection explicitly (use with caution)
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion à MySQL fermée manuellement");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }
}