package controleur;

import modele.Client;
import modele.dao.ClientDAO;

import java.sql.Date;
import java.util.List;

/**
 * ControleurAdminClients permet à l'administrateur
 * de gérer les clients : ajout, modification et suppression.
 */
public class ControleurAdminClients {

    private final ClientDAO dao;

    /**
     * Constructeur du contrôleur, initialise le DAO client.
     */
    public ControleurAdminClients() {
        this.dao = new ClientDAO();
    }

    /**
     * Récupère tous les clients enregistrés.
     *
     * @return Liste des clients
     */
    public List<Client> getTousLesClients() {
        return dao.getAllClients();
    }

    /**
     * Ajoute un nouveau client.
     *
     * @param mail Email du client
     * @param mdp Mot de passe du client
     * @param nom Nom du client
     * @param prenom Prénom du client
     * @param dateNaissance Date de naissance
     * @throws Exception si erreur d'insertion
     */
    public void ajouterClient(String mail, String mdp, String nom, String prenom, Date dateNaissance) throws Exception {
        Client nouveau = new Client(mail, mdp, nom, prenom, dateNaissance, 0);
        dao.ajouterClient(nouveau);
    }

    /**
     * Modifie les informations d'un client existant.
     *
     * @param client Client à modifier
     * @param mail Nouvel email
     * @param mdp Nouveau mot de passe
     * @param nom Nouveau nom
     * @param prenom Nouveau prénom
     * @param dateNaissance Nouvelle date de naissance
     * @throws Exception si erreur de mise à jour
     */
    public void modifierClient(Client client, String mail, String mdp, String nom, String prenom, Date dateNaissance) throws Exception {
        client.setMail(mail);
        client.setMdp(mdp);
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setDatedeNaissance(dateNaissance);
        dao.modifierClient(client);
    }

    /**
     * Supprime un client de la base de données.
     *
     * @param client Client à supprimer
     * @throws Exception si erreur de suppression
     */
    public void supprimerClient(Client client) throws Exception {
        dao.supprimerClient(client.getId());
    }
}
