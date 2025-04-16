package modele;
import java.util.Date;

public class Admin {


    private int id;
    private String mail;
    private String mdp;
    private String nom;
    private String prenom;

    public Admin(String mail, String mdp, String nom, String prenom) {
        this.mail = mail;
        this.mdp = mdp;
        this.nom = nom;
        this.prenom = prenom;
    }

    public Admin (int id, String mail, String mdp, String nom, String prenom) {
        this(mail, mdp, nom, prenom);
        this.id = id;
    }

    public int getId() { return id;}
    public String getMail() { return mail; }
    public String getMdp() {return mdp; }
    public String getNom() { return nom;}
    public String getPrenom() { return prenom; }

    public void setId(int id) { this.id = id;}
    public void setMail(String mail) { this.mail = mail; }
    public void setMdp(String mdp) {this.mdp = mdp; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    @Override
    public String toString() {
        return "id_Admin : " + id + " || " + nom + " " + prenom + " (" + mail + ")";
    }
}