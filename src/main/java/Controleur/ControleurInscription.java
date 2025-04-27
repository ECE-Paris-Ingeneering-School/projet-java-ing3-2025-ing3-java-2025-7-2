package controleur;

import modele.Client;
import modele.dao.ClientDAO;

/**
 * ControleurInscription gère l'inscription d'un nouvel utilisateur (client) dans la base de données.
 */
public class ControleurInscription {

    /**
     * Inscrit un nouvel utilisateur avec les informations fournies.
     *
     * @param nom Nom du client
     * @param prenom Prénom du client
     * @param email Email du client
     * @param motDePasse Mot de passe du client
     * @param dateNaissance Date de naissance du client
     * @return true si l'inscription est réussie, false sinon
     */
    public boolean inscrire(String nom, String prenom, String email, String motDePasse, java.sql.Date dateNaissance) {
        try {
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
