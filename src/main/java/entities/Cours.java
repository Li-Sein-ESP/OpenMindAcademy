package entities;

public class Cours {

    private int idCours;
    private String nom;
    private String description;
    private int duree; // en heures ou jours selon ton choix
    private float prix;

    private CategoriesCours categoriesCours; // Relation: Cours appartient à une CatégorieCours

    // Constructeurs
    public Cours() {}

    public Cours(int idCours, String nom, String description, int duree, float prix, CategoriesCours categoriesCours) {
        this.idCours = idCours;
        this.nom = nom;
        this.description = description;
        this.duree = duree;
        this.prix = prix;
        this.categoriesCours = categoriesCours;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "idCours=" + idCours +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", duree=" + duree +
                ", prix=" + prix +
                ", categoriesCours=" + categoriesCours +
                '}';
    }

    public int getIdCours() {
        return idCours;
    }

    public void setIdCours(int idCours) {
        this.idCours = idCours;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public CategoriesCours getCategoriesCours() {
        return categoriesCours;
    }

    public void setCategoriesCours(CategoriesCours categoriesCours) {
        this.categoriesCours = categoriesCours;
    }
}
