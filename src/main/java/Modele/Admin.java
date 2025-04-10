package Modele;

import java.util.Date;

public class Admin {
    public String mail;
    private String mdp;
    public String nom;
    public String prenom;

    public Admin(String mail, String mdp, String nom, String prenom) {
        this.mail = mail;
        this.mdp = mdp;
        this.nom = nom;
        this.prenom = prenom;
    }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getMail() { return mail; }


}
