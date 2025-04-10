package Modele;

import java.util.*;

public class Client {
    public String mail;
    private String mdp;
    public String nom;
    public String prenom;
    public Date datedeNaissance;

    public Client(String mail, String mdp, String nom, String prenom, Date datedeNaissance) {
        this.mail = mail;
        this.mdp = mdp;
        this.nom = nom;
        this.prenom = prenom;
        this.datedeNaissance = datedeNaissance;
    }


}
