package entities;
import java.time.LocalDate;

public class SessionCours {
    private int id_session;
    String nom_session;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    private Cours cours; // Relation: SessionCours appartient à un Cours

    // Constructeurs
    public SessionCours() {}

    public SessionCours(int id_session,String nom_session ,LocalDate dateDebut, LocalDate dateFin, Cours cours) {
        this.id_session = id_session;
        this.nom_session = nom_session;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.cours = cours;
    }

    public int getId() {
        return id_session;
    }

    public void setId(int id) {
        this.id_session = id;
    }

    public String getNom_session() {return nom_session;}

    public void setNom_session(String nom_session) {this.nom_session = nom_session;}

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Cours getCours() {
        return cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    @Override
    public String toString() {
        return "SessionCours{" +
                "id=" + id_session +
                "nom_session" + nom_session +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", cours=" + cours +
                '}';
    }
}
