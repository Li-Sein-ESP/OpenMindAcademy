package entities;

import enums.Role;
import enums.Sexe;

import java.time.LocalDate;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String adresse;
    private LocalDate dateNaissance;
    private Role role;
    private String email;
    private String sexe;
    private String numTelephone;
    private String diplome; // Only for TEACHER
    private String niveauEtude; // Only for STUDENT
    private String password;
    private String image;
    private String resetToken;

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getResetToken() {
        return resetToken;
    }

    // No-argument constructor
    public User() {
    }

    // Full constructor
    public User(int id, String nom, String prenom, String adresse, LocalDate dateNaissance, Role role, String email,
                Sexe sexe, String numTelephone, String diplome, String niveauEtude, String password, String image) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.role = role; // Set role first to avoid validation issues
        this.email = email;
        this.sexe = String.valueOf(sexe);
        this.numTelephone = numTelephone;
        this.diplome = (role == Role.TEACHER) ? diplome : null; // Direct validation
        this.niveauEtude = (role == Role.STUDENT) ? niveauEtude : null; // Direct validation
        this.password = password;
        this.image = image;

    }

    // Minimal constructor
    public User(int id, String nom, String prenom, String email, Role role, String password) {
        this(id, nom, prenom, null, null, role, email, null, null, null, null, password, null);
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getAdresse() { return adresse; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public Role getRole() { return role; }
    public String getEmail() { return email; }
    public String getSexe() {
        return sexe != null ? String.valueOf(Sexe.valueOf(sexe)) : null;
    }
    public String getNumTelephone() { return numTelephone; }
    public String getDiplome() { return diplome; }
    public String getNiveauEtude() { return niveauEtude; }
    public String getPassword() { return password; }
    public String getImage() { return image; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    public void setRole(Role role) { this.role = role; }
    public void setEmail(String email) { this.email = email; }
    public void setSexe(String sexe) { this.sexe = sexe; }
    public void setNumTelephone(String numTelephone) { this.numTelephone = numTelephone; }
    public void setPassword(String password) { this.password = password; }
    public void setImage(String image) { this.image = image; }

    public void setDiplome(String diplome) {
        if (role == null) {
            throw new IllegalStateException("Role must be set before setting diplome");
        }
        if (role == Role.TEACHER) {
            this.diplome = diplome;
        } else if (diplome != null) {
            throw new IllegalArgumentException("Diplome is only applicable for TEACHER role");
        } else {
            this.diplome = null;
        }
    }

    public void setNiveauEtude(String niveauEtude) {
        if (role == null) {
            throw new IllegalStateException("Role must be set before setting niveauEtude");
        }
        if (role == Role.STUDENT) {
            this.niveauEtude = niveauEtude;
        } else if (niveauEtude != null) {
            throw new IllegalArgumentException("NiveauEtude is only applicable for STUDENT role");
        } else {
            this.niveauEtude = null;
        }
    }
}
