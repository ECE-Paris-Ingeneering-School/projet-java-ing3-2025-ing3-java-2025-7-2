package modele;

public class Utilisateur {
    private String nom;
    private String prenom;
    private String email;
    private String role; // "admin" ou "client"

    public Utilisateur(String nom, String prenom, String email, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
    }

    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getPrenom() { return prenom; }
}
