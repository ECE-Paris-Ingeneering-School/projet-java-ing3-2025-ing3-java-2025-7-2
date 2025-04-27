package controleur;

import modele.Client;
import modele.Utilisateur;
import modele.dao.ClientDAO;
import modele.dao.FactureDAO;
import modele.dao.ReservationDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControleurFactures {

    private final FactureDAO factureDAO;
    private final ReservationDAO reservationDAO;
    private final ClientDAO clientDAO; // DECLA ajoutée

    public ControleurFactures(Connection connexion) {
        this.factureDAO = new FactureDAO(connexion);
        this.reservationDAO = new ReservationDAO(connexion);
        this.clientDAO = new ClientDAO(); // INSTANTIATION ajoutée (sans paramètre car  appelle ConnexionBDD à l’intérieur)
    }

    public int genererFacture(Utilisateur utilisateur) throws Exception {
        System.out.println("Tentative de génération  facture - controleur -");
        Client client = clientDAO.getClientParId(utilisateur.getId());
        System.out.println("Tentative de génération  facture - controleur post client initialisation - ");
        int age = client.getAge();
        if (age < 0) throw new Exception("Impossible de calculer l’âge du client.");
        System.out.println("Tentative de génération  facture - controleur age : " + age);
        List<Object[]> reservations = reservationDAO.getReservationsPasseesPourFacturation(client.getId());
        System.out.println("Tentative de génération  facture - controleur pre return -");
        return factureDAO.genererFacture(client.getId(), age, reservations);

    }

    public List<Object[]> getFacturesDuClient(Utilisateur utilisateur) throws SQLException, IOException, ClassNotFoundException {
        Client client = clientDAO.getClientParId(utilisateur.getId());
        return factureDAO.getFacturesDuClientSousFormeListe(client.getId());
    }


}
