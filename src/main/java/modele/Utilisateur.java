package modele;

public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String role; // "admin" ou "client"

    public Utilisateur( int id, String nom, String prenom, String email, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
        this.id = id;
    }

    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getPrenom() { return prenom; }
    public int getId() { return id; }
}
