package modele;

/**
 * Classe objet qui représente le client et ses infos :
 * Identifiant, nom , prénom, role, email
 */
public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String role; // "admin" ou "client"

    /**
     * Constructeur de la classe Utilisateur
     *
     * @param id L'ID de l'utilisateur
     * @param nom Le nom de l'utilisateur
     * @param prenom Le prénom de l'utilisateur
     * @param email L'email de l'utilisateur
     * @param role Le rôle de l'utilisateur (soit "admin" ou "client")
     */
    public Utilisateur( int id, String nom, String prenom, String email, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
        this.id = id;
    }

    /// Getters et Setters :

    /**
     * @return Le nom de l'utilisateur.
     */
    public String getNom() {return nom;}

    /**
     * @return L'email de l'utilisateur.
     */
    public String getEmail() {return email;}

    /**
     * @return Le rôle de l'utilisateur ("admin" ou "client").
     */
    public String getRole() {return role;}

    /**
     * @return Le prénom de l'utilisateur.
     */
    public String getPrenom() {return prenom;}

    /**
     * @return L'ID de l'utilisateur.
     */
    public int getId() {return id;}
}
