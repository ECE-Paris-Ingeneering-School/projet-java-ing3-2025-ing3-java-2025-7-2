package modele;
import java.time.*;
import java.util.Date;

/**
 * Classe objet qui réprésente un client et ses infos :
 * identifiant, email, mot de passe, nom, prénom, date de naissance, fidelité
 */
public class Client {


    private int id;
    private String mail;
    private String mdp;
    private String nom;
    private String prenom;
    private Date datedeNaissance;
    private int fidelite;

    /**
     * Constructeur pour créer un client sans ID
     *
     * @param mail L'email du client
     * @param mdp Le mot de passe du client
     * @param nom Le nom du client
     * @param prenom Le prénom du client
     * @param datedeNaissance La date de naissance du client
     * @param fidelite Le niveau de fidélité du client
     */
    public Client(String mail, String mdp, String nom, String prenom, Date datedeNaissance, int fidelite) {
        this.mail = mail;
        this.mdp = mdp;
        this.nom = nom;
        this.prenom = prenom;
        this.datedeNaissance = datedeNaissance;
        this.fidelite = fidelite;
    }

    /**
     * Constructeur pour créer un client avec un ID (utilisé lors de la récupération d'un client depuis la BDD)
     *
     * @param id L'ID unique du client
     * @param mail L'email du client
     * @param mdp Le mot de passe du client
     * @param nom Le nom du client
     * @param prenom Le prénom du client
     * @param datedeNaissance La date de naissance du client
     * @param fidelite Le niveau de fidélité du client
     */
    public Client(int id, String mail, String mdp, String nom, String prenom, Date datedeNaissance, int fidelite) {
        this(mail, mdp, nom, prenom, datedeNaissance,fidelite);
        this.id = id;
    }

    /// Getters et Setters :

    /**
     * @return L'ID du client.
     */
    public int getId() { return id; }

    /**
     * @return L'email du client.
     */
    public String getMail() { return mail; }

    /**
     * @return Le mot de passe du client.
     */
    public String getMdp() { return mdp; }

    /**
     * @return Le nom du client.
     */
    public String getNom() { return nom; }

    /**
     * @return Le prénom du client.
     */
    public String getPrenom() { return prenom; }

    /**
     * @return La date de naissance du client.
     */
    public Date getDatedeNaissance() { return datedeNaissance; }

    /**
     * Modifie l'ID du client.
     *
     * @param id L'ID à attribuer au client.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Modifie l'email du client.
     *
     * @param mail L'email à attribuer au client.
     */
    public void setMail(String mail) { this.mail = mail; }

    /**
     * Modifie le mot de passe du client.
     *
     * @param mdp Le mot de passe à attribuer au client.
     */
    public void setMdp(String mdp) { this.mdp = mdp; }

    /**
     * Modifie le nom du client.
     *
     * @param nom Le nom à attribuer au client.
     */
    public void setNom(String nom) { this.nom = nom; }

    /**
     * Modifie le prénom du client.
     *
     * @param prenom Le prénom à attribuer au client.
     */
    public void setPrenom(String prenom) { this.prenom = prenom; }

    /**
     * Modifie la date de naissance du client.
     *
     * @param datedeNaissance La date de naissance à attribuer au client.
     */
    public void setDatedeNaissance(Date datedeNaissance) { this.datedeNaissance = datedeNaissance; }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'objet Client
     *
     * @return Une chaîne représentant les informations du client (ID, nom, prénom, email)
     */
    @Override
    public String toString() {
        return "id_client : " + id+" || "+ nom + " " + prenom + " (" + mail + ")";
    }

    /**
     * Calcule et retourne l'âge du client selon de sa date de naissance
     * @return L'âge du client en années
     * @throws IllegalStateException Si la date de naissance est null.
     */
    public int getAge() {
        System.out.println("entrée fct getAge");
        if (datedeNaissance == null) {
            System.err.println("[ERREUR] Date de naissance est null pour le client id = " + id);
            throw new IllegalStateException("Date de naissance manquante");
        }

        try {
            // Forcer la conversion en SQL Date puis LocalDate
            LocalDate birth = ((java.sql.Date) datedeNaissance).toLocalDate();
            System.out.println("sortie 'fonctionnelle' fct getAge");

            return Period.between(birth, LocalDate.now()).getYears();

        } catch (Exception e) {
            System.err.println("[ERREUR] Échec du calcul de l’âge pour client id = " + id);
            e.printStackTrace();
            throw e;
        }
    }



}
