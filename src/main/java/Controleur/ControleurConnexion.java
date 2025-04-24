package controleur;

import modele.Utilisateur;
import modele.dao.ConnexionBDD;
import modele.AuthentificationService;

import java.sql.Connection;

public class ControleurConnexion {

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
