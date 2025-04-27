package modele;
import java.util.Date;
import modele.dao.AdminDAO;

/**
 * Classe objet qui représente un admin avec ses infos :
 * id, prénom, nom, mail, mot de passe
 */
public class Admin {


    private int id; // Identifiant unique
    private String mail; // Email de l'Admin
    private String mdp; // Mot de passe de l'admin
    private String nom;
    private String prenom;

    /**
     * Constructeur de l'Admin sans id
     * @param mail email de l'admin
     * @param mdp mot de passe de l'admin
     * @param nom nom de l'admin
     * @param prenom prénom de l'admin
     */
    public Admin(String mail, String mdp, String nom, String prenom) {
        this.mail = mail;
        this.mdp = mdp;
        this.nom = nom;
        this.prenom = prenom;
    }

    /**
     * Constructeur de l'Admin avec id
     * @param id identifiant de l'admin
     * @param mail email de l'admin
     * @param mdp mot de passe de l'admin
     * @param nom nom de l'admin
     * @param prenom prenom de l'admin
     */
    public Admin (int id, String mail, String mdp, String nom, String prenom) {
        this(mail, mdp, nom, prenom);
        this.id = id;
    }

    ///  Getter :

    /**
     * Récupère l'identifiant de l'admin
     * @return l'identifiant de l'admin
     */
    public int getId() { return id;}

    /**
     * Récupère l'email de l'admin
     * @return l'email de l'admin
     */
    public String getMail() { return mail; }

    /**
     * Récupère le mot de passe de l'admin
     * @return le mot de passe de l'admin
     */
    public String getMdp() {return mdp; }

    /**
     * Récupère le nom de l'admin
     * @return le nom de l'admin
     */
    public String getNom() { return nom;}

    /**
     * Récupère le prénom de l'admin
     * @return le prénom de l'admin
     */
    public String getPrenom() { return prenom; }


    /// Setter :

    /**
     * Modifie l'identifiant de l'Admin
     * @param id l'identifiant de l'Admin
     */
    public void setId(int id) { this.id = id;}

    /**
     * Modifie l'email de l'Admin
     * @param mail l'email de l'Admin
     */
    public void setMail(String mail) { this.mail = mail; }

    /**
     * Modifie le mot de passe de l'Admin
     * @param mdp le mot de passe de l'Admin
     */
    public void setMdp(String mdp) {this.mdp = mdp; }

    /**
     * Modifie le nom de l'Admin
     * @param nom le nom de l'Admin
     */
    public void setNom(String nom) { this.nom = nom; }

    /**
     * Modifie le prénom de l'Admin
     * @param prenom Prénom de l'Admin
     */
    public void setPrenom(String prenom) { this.prenom = prenom; }


    /**
     * Retourne une représentation sous forme de chaîne de l'administrateur
     * La chaîne contient l'ID de l'administrateur, son nom, prénom et son email.
     *
     * @return La représentation sous forme de chaîne de l'administrateur
     */
    @Override
    public String toString() {
        return "id_Admin : " + id + " || " + nom + " " + prenom + " (" + mail + ")";
    }


    /**
     * Permet à l'admin de modifier une attraction via le DAO
     *
     * @param a L'attraction à modifier
     */
    public void modifierAttraction(Attraction a) {
        AdminDAO dao = new AdminDAO();
        dao.modifAttraction(a);
    }


}