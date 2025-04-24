package controleur;

import modele.Client;
import modele.dao.ClientDAO;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ControleurInscription {

    public boolean inscrire(String nom, String prenom, String email, String motDePasse, java.sql.Date dateNaissance) {
        try {
            // Date de naissance temporaire
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Client client = new Client(0, email, motDePasse, nom, prenom, dateNaissance, 0);

            ClientDAO clientDAO = new ClientDAO();
            clientDAO.ajouterClient(client);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
