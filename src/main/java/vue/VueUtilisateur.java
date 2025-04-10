package vue;

import modele.Utilisateur;

public class VueUtilisateur {

    // Cette méthode affiche les informations de l'utilisateur
    public static void afficherInfos(Utilisateur utilisateur) {
        System.out.println("\n=== Informations de l'Utilisateur ===");
        System.out.println("Nom : " + utilisateur.getNom());
        System.out.println("Prenom : " + utilisateur.getPrenom());
        System.out.println("Email : " + utilisateur.getEmail());
        System.out.println("Role : " + utilisateur.getRole());
    }
}

