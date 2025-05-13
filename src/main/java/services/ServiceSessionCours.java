package services;

import utils.*;
import entities.*;
import controllers.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceSessionCours {

    private Connection cnx;

    public ServiceSessionCours() {
        cnx = MyDatabase.getInstance().getConnection();
    }

    // 1. Ajouter une session
    public void ajouter(SessionCours session, int id_cours) {
        String sql = "INSERT INTO sessioncours (nom_session, date_debut, date_fin, id_cours) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setString(1, session.getNom_session());
            ps.setDate(2, Date.valueOf(session.getDateDebut()));
            ps.setDate(3, Date.valueOf(session.getDateFin()));
            ps.setInt(4, id_cours);

            ps.executeUpdate();
            System.out.println("Session ajoutée avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la session : " + e.getMessage());
        }
    }

    // 2. Rechercher une session
    public SessionCours rechercherSession(String critere, String valeur) {
        String sql = "SELECT s.id_session, s.nom_session, s.date_debut, s.date_fin, "
                + "c.id_cours, c.nom, c.description, c.duree, c.prix "
                + "FROM sessioncours s "
                + "JOIN cours c ON s.id_cours = c.id_cours "
                + "WHERE s.id_session = ? OR s.nom_session = ?";

        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            if (critere.equals("ID")) {
                ps.setInt(1, Integer.parseInt(valeur));
                ps.setString(2, "");
            } else {
                ps.setInt(1, -1);
                ps.setString(2, valeur);
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Cours cours = new Cours(
                        rs.getInt("id_cours"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getInt("duree"),
                        rs.getFloat("prix"),
                        null // Pas de catégorie ici
                );

                return new SessionCours(
                        rs.getInt("id_session"),
                        rs.getString("nom_session"),
                        rs.getDate("date_debut").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate(),
                        cours
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la session : " + e.getMessage());
        }
        return null;
    }

    // 3. Modifier une session
    public void modifier(SessionCours session) {
        String checkSql = "SELECT id_session FROM sessioncours WHERE id_session = ? OR nom_session = ?";
        String updateSql = "UPDATE sessioncours SET nom_session = ?, date_debut = ?, date_fin = ?, id_cours = ? WHERE id_session = ?";

        try {
            PreparedStatement checkStmt = cnx.prepareStatement(checkSql);
            checkStmt.setInt(1, session.getId());
            checkStmt.setString(2, session.getNom_session());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int idTrouve = rs.getInt("id_session");

                PreparedStatement ps = cnx.prepareStatement(updateSql);
                ps.setString(1, session.getNom_session());
                ps.setDate(2, Date.valueOf(session.getDateDebut()));
                ps.setDate(3, Date.valueOf(session.getDateFin()));
                ps.setInt(4, session.getCours().getIdCours());
                ps.setInt(5, idTrouve);

                ps.executeUpdate();
                System.out.println("Session modifiée avec succès.");
            } else {
                System.out.println("Aucune session trouvée avec cet ID ou ce nom.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la session : " + e.getMessage());
        }
    }

    // 4. Supprimer une session
    public void supprimer(String critere, String valeur) {
        String sql = "";
        try {
            if (critere.equals("ID")) {
                sql = "DELETE FROM sessioncours WHERE id_session = ?";
                PreparedStatement ps = cnx.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(valeur));
                ps.executeUpdate();
            } else if (critere.equals("Nom")) {
                sql = "DELETE FROM sessioncours WHERE nom_session = ?";
                PreparedStatement ps = cnx.prepareStatement(sql);
                ps.setString(1, valeur);
                ps.executeUpdate();
            }
            System.out.println("Session supprimée avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la session : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 5. Afficher toutes les sessions
    public List<SessionCours> afficher() {
        List<SessionCours> sessions = new ArrayList<>();
        String sql = "SELECT s.id_session, s.nom_session, s.date_debut, s.date_fin, "
                + "c.id_cours, c.nom, c.description, c.duree, c.prix "
                + "FROM sessioncours s "
                + "JOIN cours c ON s.id_cours = c.id_cours";

        try {
            Statement statement = cnx.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                Cours cours = new Cours(
                        rs.getInt("id_cours"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getInt("duree"),
                        rs.getFloat("prix"),
                        null // Pas de catégorie ici
                );

                SessionCours session = new SessionCours(
                        rs.getInt("id_session"),
                        rs.getString("nom_session"),
                        rs.getDate("date_debut").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate(),
                        cours
                );

                sessions.add(session);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'affichage des sessions : " + e.getMessage());
        }

        return sessions;
    }

    // 6. Récupérer tous les cours disponibles
    public List<Cours> getAllCours() {
        List<Cours> coursList = new ArrayList<>();
        String sql = "SELECT id_cours, nom, description, duree, prix FROM cours";

        try {
            Statement statement = cnx.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                Cours cours = new Cours(
                        rs.getInt("id_cours"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getInt("duree"),
                        rs.getFloat("prix"),
                        null // Pas de catégorie ici
                );
                coursList.add(cours);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des cours : " + e.getMessage());
        }

        return coursList;
    }
}