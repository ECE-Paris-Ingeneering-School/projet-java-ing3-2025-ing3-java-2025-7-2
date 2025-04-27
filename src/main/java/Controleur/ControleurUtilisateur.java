package controleur;

import modele.Utilisateur;

public class ControleurUtilisateur {

    private final Utilisateur utilisateur;

    public ControleurUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getNomComplet() {
        return utilisateur.getPrenom() + " " + utilisateur.getNom();
    }
    public String getEmail() {
        return utilisateur.getEmail();
    }
    public String getRole() {
        return utilisateur.getRole();

    }

    // Tu peux ajouter ici d'autres données à afficher (historique, stats, etc.)
}
