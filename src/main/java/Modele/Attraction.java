package modele;

public class Attraction {

    private int idAttraction;
    private String nom;
    private double prix;
    private String image;

    // Constructeur sans paramètres
    public Attraction() {
    }

    // Constructeur avec tous les paramètres
    public Attraction(int idAttraction, String nom, double prix, String image) {
        this.idAttraction = idAttraction;
        this.nom = nom;
        this.prix = prix;
        this.image = image;
    }

    // Getters et Setters
    public int getIdAttraction() {
        return idAttraction;
    }

    public void setIdAttraction(int idAttraction) {
        this.idAttraction = idAttraction;
    }

    public int getId(){return idAttraction;}

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Méthode toString() pour afficher les informations de l'attraction
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
