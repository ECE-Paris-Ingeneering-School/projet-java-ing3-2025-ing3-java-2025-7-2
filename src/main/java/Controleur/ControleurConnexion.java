package controleur;

import modele.Utilisateur;
import modele.dao.ConnexionBDD;
import modele.AuthentificationService;

import java.sql.Connection;

/**
 * ControleurConnexion gère la logique de connexion des utilisateurs.
 */
public class ControleurConnexion {

    /**
     * Tente de connecter un utilisateur en vérifiant ses identifiants.
     *
     * @param email Adresse email saisie
     * @param mdp Mot de passe saisi
     * @return Utilisateur connecté si succès, sinon null
     */
    public Utilisateur connecter(String email, String mdp) {
        try (Connection conn = ConnexionBDD.getConnexion()) {
            AuthentificationService auth = new AuthentificationService();
            return auth.connecter(email, mdp, conn);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
