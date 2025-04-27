package controleur;

import modele.Utilisateur;

/**
 * ControleurUtilisateur permet d'accéder aux informations
 * d'un utilisateur connecté pour l'affichage dans les vues.
 */
public class ControleurUtilisateur {

    private final Utilisateur utilisateur;

    /**
     * Constructeur du contrôleur utilisateur.
     *
     * @param utilisateur L'utilisateur connecté
     */
    public ControleurUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    /**
     * Récupère le nom complet de l'utilisateur (Prénom + Nom).
     *
     * @return Nom complet
     */
    public String getNomComplet() {
        return utilisateur.getPrenom() + " " + utilisateur.getNom();
    }

    /**
     * Récupère l'adresse email de l'utilisateur.
     *
     * @return Email de l'utilisateur
     */
    public String getEmail() {
        return utilisateur.getEmail();
    }

    /**
     * Récupère le rôle de l'utilisateur (ex: client, admin).
     *
     * @return Rôle de l'utilisateur
     */
    public String getRole() {
        return utilisateur.getRole();
    }

    
}
