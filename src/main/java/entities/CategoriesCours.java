package entities;

public class CategoriesCours {
    private int id_categories;
    private String nom;
    private String description;

    // Constructeurs
    public CategoriesCours() {}

    public CategoriesCours(int id_categories, String nom, String description) {
        this.id_categories= id_categories;
        this.nom = nom;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getId_categories() {
        return id_categories;
    }

    public String getNom() {
        return nom;
    }

    public void setId_categories(int id_categories) {
        this.id_categories = id_categories;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "CategoriesCours{" +
                "id=" + id_categories +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
