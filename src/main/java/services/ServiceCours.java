package services;

import enums.Role;
import utils.*;
import entities.*;
import controllers.*;

import java.sql.*;
import java.util.*;

public class ServiceCours {

    private Connection cnx;

    public ServiceCours() {
        cnx = MyDatabase.getInstance().getConnection();
    }

    // 1. Ajouter un cours
    public void ajouter(Cours cours, int id_categories) {
        String sql = "INSERT INTO cours (nom, description, duree, prix, id_categories) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setString(1, cours.getNom());
            ps.setString(2, cours.getDescription());
            ps.setInt(3, cours.getDuree());
            ps.setFloat(4, cours.getPrix());
            ps.setInt(5, id_categories);

            ps.executeUpdate();
            System.out.println("Cours ajouté avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du cours : " + e.getMessage());
        }
    }

    //rechercher cours
    public Cours rechercherCours(String critere, String valeur) {
        String sql =/* "SELECT * FROM cours WHERE id_cours = ? OR nom = ?";*/"SELECT c.id_cours, c.nom, c.description, c.duree, c.prix, cat.id_categories, cat.nom AS nom_categorie, cat.description AS description_categorie " +
                "FROM cours c " +
                "JOIN categoriescours cat ON c.id_categories = cat.id_categories " +
                "WHERE c.id_cours = ? OR c.nom = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            if (critere.equals("ID")) {
                ps.setInt(1, Integer.parseInt(valeur)); // pour id_cours = ?
                ps.setString(2, ""); // valeur neutre pour nom
            } else {
                ps.setInt(1, -1); // valeur qui ne correspond à aucun ID
                ps.setString(2, valeur); // pour nom = ?
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CategoriesCours categorie = new CategoriesCours(
                        rs.getInt("id_categories"),
                        rs.getString("nom_categorie"),
                        rs.getString("description_categorie")
                );
                return new Cours(
                        rs.getInt("id_cours"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getInt("duree"),
                        rs.getFloat("prix"),
                        categorie
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du cours : " + e.getMessage());
        }
        return null;
    }

    // 2. Modifier un cours
    public void modifier(Cours cours) {
        // Requête pour vérifier l'existence du cours par ID ou nom
        String checkSql = "SELECT id_cours FROM cours WHERE id_cours = ? OR nom = ?";
        String updateSql = "UPDATE cours SET nom = ?, description = ?, duree = ?, prix = ?, id_categories = ? WHERE id_cours = ?";

        try {
            PreparedStatement checkStmt = cnx.prepareStatement(checkSql);
            checkStmt.setInt(1, cours.getIdCours());  // On peut utiliser l'ID du cours directement
            checkStmt.setString(2, cours.getNom());   // Ou le nom du cours si non trouvé par ID
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int idTrouve = rs.getInt("id_cours");

                PreparedStatement ps = cnx.prepareStatement(updateSql);
                ps.setString(1, cours.getNom());
                ps.setString(2, cours.getDescription());
                ps.setInt(3, cours.getDuree());
                ps.setFloat(4, cours.getPrix());
                ps.setInt(5, cours.getCategoriesCours().getId_categories());
                ps.setInt(6, idTrouve);

                ps.executeUpdate();
                System.out.println("Cours modifié avec succès.");
            } else {
                System.out.println("Aucun cours trouvé avec cet ID ou ce nom.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du cours : " + e.getMessage());
        }
    }

    // 3. Supprimer un cours
    public void supprimer(String critere, String valeur) {
        String sql = "";
        try {
            if (critere.equals("ID")) {
                sql = "DELETE FROM cours WHERE id_cours = ?";
                PreparedStatement ps = cnx.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(valeur));
                ps.executeUpdate();
            } else if (critere.equals("Nom")) {
                sql = "DELETE FROM cours WHERE nom = ?";
                PreparedStatement ps = cnx.prepareStatement(sql);
                ps.setString(1, valeur);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du cours : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 4. Afficher tous les cours
    public List<Cours> afficher() {
        List<Cours> coursList = new ArrayList<>();
        String sql = "SELECT c.id_cours, c.nom, c.description, c.duree, c.prix, cat.id_categories, cat.nom AS nom_categorie, cat.description AS description_categorie " +
                "FROM cours c " +
                "JOIN categoriescours cat ON c.id_categories = cat.id_categories";

        try {
            Statement statement = cnx.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                // Créer un objet CategoriesCours
                CategoriesCours categorie = new CategoriesCours(
                        rs.getInt("id_categories"),
                        rs.getString("nom_categorie"),
                        rs.getString("description_categorie")
                );

                // Créer un objet Cours
                Cours cours = new Cours(
                        rs.getInt("id_cours"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getInt("duree"),
                        rs.getFloat("prix"),
                        categorie
                );

                coursList.add(cours);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'affichage des cours : " + e.getMessage());
        }

        return coursList;
    }

    // 5. Récupérer toutes les catégories
    public List<CategoriesCours> getAllCategories() {
        List<CategoriesCours> categories = new ArrayList<>();
        String sql = "SELECT id_categories, nom, description FROM categoriescours";

        try {
            Statement statement = cnx.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                CategoriesCours categorie = new CategoriesCours(
                        rs.getInt("id_categories"),
                        rs.getString("nom"),
                        rs.getString("description")
                );
                categories.add(categorie);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des catégories : " + e.getMessage());
        }

        return categories;
    }

    // Recommander un cours pour un étudiant en fonction de critères
    public Optional<Cours> recommanderCoursApproche(List<Cours> coursList, String categoriePreferee, float budget, int duree, float tolerancePrix, int toleranceDuree) {
        // Filtrer les cours selon la catégorie préférée
        return coursList.stream()
                .filter(cours -> cours.getCategoriesCours().getNom().equalsIgnoreCase(categoriePreferee)) // Correspondance de la catégorie
                .filter(cours -> Math.abs(cours.getPrix() - budget) <= tolerancePrix) // Tolérance sur le prix
                .filter(cours -> Math.abs(cours.getDuree() - duree) <= toleranceDuree) // Tolérance sur la durée
                .sorted(Comparator.comparing(cours -> Math.abs(cours.getPrix() - budget) + Math.abs(cours.getDuree() - duree))) // Trier par proximité au budget et à la durée
                .findFirst(); // Retourner le premier cours correspondant
    }

    // Postuler pour devenir mentor et évaluer la probabilité d'acceptation
    public String postulerMentor(User enseignant, Map<String, Integer> criteres) {
        // Vérifiez que l'utilisateur est un enseignant
        if (enseignant.getRole() != Role.TEACHER) {
            return "Seuls les enseignants peuvent postuler pour devenir mentor.";
        }

        // Critères par défaut (pondération en pourcentages si aucun critère n'est fourni)
        int experience = criteres.getOrDefault("experience", 5); // Années d'expérience
        int pedagogie = criteres.getOrDefault("pedagogie", 4);   // Niveau de pédagogie (1 à 5)
        int communication = criteres.getOrDefault("communication", 4); // Niveau de communication (1 à 5)

        // Calcul de la chance d'acceptation (pondération simple)
        int score = (experience * 50) + (pedagogie * 30) + (communication * 20);
        int chanceAcceptation = Math.min(100, score / 10); // Normaliser le score sur 100

        // Déterminer le poste correspondant au profil
        String poste;
        if (chanceAcceptation >= 80) {
            poste = "Mentor Sénior (Projets Avancés)";
        } else if (chanceAcceptation >= 50) {
            poste = "Mentor Intermédiaire (Projets Intermédiaires)";
        } else {
            poste = "Mentor Débutant (Initiation et Modules Simples)";
        }

        // Retourner un résumé
        return String.format(
                "Votre chance d'acceptation : %d%%\nPoste recommandé : %s",
                chanceAcceptation, poste
        );
    }

}

