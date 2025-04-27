package modele;

/**
 * Classe objet qui représente une attraction du parc et ses infos :
 * id, nom, prix, image (chemin), nombre de réservation
 */
public class Attraction {

    private int idAttraction; // son identifiant
    private String nom;
    private double prix;
    private String image;
    private int nombreReservations; // son nombre de réservation

    /**
     * Constructeur sans paramètres pour la classe Attraction
     * Utilisé pour créer une attraction vide
     */
    public Attraction() {
    }

    /**
     * Constructeur de la classe Attraction, initialisant tout les paramètres
     * Le nombre de réservations est initialisé à zéro par défaut
     *
     * @param idAttraction L'identifiant unique de l'attraction
     * @param nom Le nom de l'attraction
     * @param prix Le prix de l'attraction
     * @param image L'image de l'attraction
     */
    public Attraction(int idAttraction, String nom, double prix, String image) {
        this.idAttraction = idAttraction;
        this.nom = nom;
        this.prix = prix;
        this.image = image;
        this.nombreReservations = 0;
    }

    // Getters et Setters :

    /**
     * Récupère l'identifiant unique de l'attraction.
     *
     * @return L'ID de l'attraction
     */
    public int getIdAttraction() {
        return idAttraction;
    }

    /**
     * Modifie l'identifiant unique de l'attraction.
     *
     * @param idAttraction L'identifiant de l'attraction
     */
    public void setIdAttraction(int idAttraction) {
        this.idAttraction = idAttraction;
    }

    /**
     * Récupère l'identifiant de l'attraction.
     * Alias de {@link #getIdAttraction()}.
     *
     * @return L'ID de l'attraction
     */
    public int getId() {
        return idAttraction;
    }

    /**
     * Récupère le nom de l'attraction.
     *
     * @return Le nom de l'attraction
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modifie le nom de l'attraction.
     *
     * @param nom Le nom de l'attraction
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Récupère le prix de l'attraction.
     *
     * @return Le prix de l'attraction
     */
    public double getPrix() {
        return prix;
    }

    /**
     * Modifie le prix de l'attraction.
     *
     * @param prix Le prix de l'attraction
     */
    public void setPrix(double prix) {
        this.prix = prix;
    }

    /**
     * Récupère l'image de l'attraction.
     *
     * @return L'image de l'attraction
     */
    public String getImage() {
        return image;
    }

    /**
     * Modifie l'image de l'attraction.
     *
     * @param image L'image de l'attraction
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Récupère le nombre de réservations pour l'attraction.
     *
     * @return Le nombre de réservations
     */
    public int getNombreReservations() {
        return nombreReservations;
    }

    /**
     * Modifie le nombre de réservations pour l'attraction.
     *
     * @param nombreReservations Le nombre de réservations
     */
    public void setNombreReservations(int nombreReservations) {
        this.nombreReservations = nombreReservations;
    }

    /**
     * Retourne une représentation sous forme de chaîne de l'attraction
     *
     * @return La représentation sous forme de chaîne de l'attraction
     */
    @Override
    public String toString() {
        return "Attraction{" +
                "idAttraction=" + idAttraction +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                ", image='" + image + '\'' +
                '}';
    }
}
