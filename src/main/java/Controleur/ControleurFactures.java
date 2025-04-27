package controleur;

import modele.Client;
import modele.Utilisateur;
import modele.dao.ClientDAO;
import modele.dao.FactureDAO;
import modele.dao.ReservationDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * ControleurFactures gère la génération et la récupération
 * des factures pour les utilisateurs (clients).
 */
public class ControleurFactures {

    private final FactureDAO factureDAO;
    private final ReservationDAO reservationDAO;
    private final ClientDAO clientDAO;

    /**
     * Constructeur du contrôleur des factures.
     *
     * @param connexion Connexion à la base de données
     */
    public ControleurFactures(Connection connexion) {
        this.factureDAO = new FactureDAO(connexion);
        this.reservationDAO = new ReservationDAO(connexion);
        this.clientDAO = new ClientDAO();
    }

    /**
     * Génère une facture pour un utilisateur client en fonction de ses réservations passées.
     *
     * @param utilisateur Utilisateur pour lequel générer la facture
     * @return L'ID de la facture générée
     * @throws Exception Si une erreur survient pendant la génération
     */
    public int genererFacture(Utilisateur utilisateur) throws Exception {
        System.out.println("Tentative de génération facture - controleur -");
        Client client = clientDAO.getClientParId(utilisateur.getId());
        System.out.println("Tentative de génération facture - controleur post client initialisation - ");
        int age = client.getAge();
        if (age < 0) throw new Exception("Impossible de calculer l’âge du client.");
        System.out.println("Tentative de génération facture - controleur age : " + age);
        List<Object[]> reservations = reservationDAO.getReservationsPasseesPourFacturation(client.getId());
        System.out.println("Tentative de génération facture - controleur pre return -");
        return factureDAO.genererFacture(client.getId(), age, reservations);
    }

    /**
     * Récupère la liste des factures d'un utilisateur client.
     *
     * @param utilisateur Utilisateur dont on récupère les factures
     * @return Liste d'objets représentant les factures
     * @throws SQLException si erreur SQL
     * @throws IOException si erreur d'entrée/sortie
     * @throws ClassNotFoundException si classe manquante
     */
    public List<Object[]> getFacturesDuClient(Utilisateur utilisateur) throws SQLException, IOException, ClassNotFoundException {
        Client client = clientDAO.getClientParId(utilisateur.getId());
        return factureDAO.getFacturesDuClientSousFormeListe(client.getId());
    }
}
