package modele;
import java.time.*;
import java.util.Date;

public class Client {


    private int id;
    private String mail;
    private String mdp;
    private String nom;
    private String prenom;
    private Date datedeNaissance;
    private int fidelite;

    public Client(String mail, String mdp, String nom, String prenom, Date datedeNaissance, int fidelite) {
        this.mail = mail;
        this.mdp = mdp;
        this.nom = nom;
        this.prenom = prenom;
        this.datedeNaissance = datedeNaissance;
        this.fidelite = fidelite;
    }

    public Client(int id, String mail, String mdp, String nom, String prenom, Date datedeNaissance, int fidelite) {
        this(mail, mdp, nom, prenom, datedeNaissance,fidelite);
        this.id = id;
    }

    public int getId() { return id;}
    public String getMail() { return mail; }
    public String getMdp() {return mdp; }
    public String getNom() { return nom;}
    public String getPrenom() { return prenom; }
    public Date getDatedeNaissance() { return datedeNaissance; }

    public void setId(int id) { this.id = id;}
    public void setMail(String mail) { this.mail = mail; }
    public void setMdp(String mdp) {this.mdp = mdp; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setDatedeNaissance(Date datedeNaissance) {this.datedeNaissance = datedeNaissance; }

    @Override
    public String toString() {
        return "id_client : " + id+" || "+ nom + " " + prenom + " (" + mail + ")";
    }

    /*public int getAge{
        //...
    }*/
}
