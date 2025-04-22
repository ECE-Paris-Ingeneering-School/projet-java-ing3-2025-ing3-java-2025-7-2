package controleur;

import javafx.scene.control.Label;
import javafx.stage.Stage; ///tolérable dans un controleur pour modif label ou msg à envoyer à la vue :

import modele.Utilisateur;
import modele.Admin;
import modele.Attraction;
import modele.dao.ConnexionBDD;
import modele.dao.AttractionDAO;
import vue.VueCalendrier;

import java.sql.Connection;
import java.util.List;
import modele.AuthentificationService;

public class ControleurConnexion {

    public void connecter(String email, String mdp, Stage stage, Label messageLabel) {
        try (Connection conn = ConnexionBDD.getConnexion()) {
            AuthentificationService auth = new AuthentificationService();
            Utilisateur utilisateur = auth.connecter(email, mdp, conn);

            if (utilisateur != null) {
                messageLabel.setText("Connexion réussie");
                messageLabel.setStyle("-fx-text-fill: green;");

                // Transition vers vue calendrier
                VueCalendrier vueCalendrier = new VueCalendrier(utilisateur);
                vueCalendrier.afficher(stage);

            } else {
                messageLabel.setText("Identifiants incorrects");
                messageLabel.setStyle("-fx-text-fill: red;");
            }

        } catch (Exception e) {
            messageLabel.setText("Erreur de connexion");
            messageLabel.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
        System.out.println("Connexion bien gérée par le controleur");
    }
}


