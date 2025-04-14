package modele.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    private Connection conn;

    public ReservationDAO(Connection conn) {
        this.conn = conn;
    }
    public void ajouterReservation(int idClient, int idAttraction, Date dateAttraction) throws SQLException {
        String sql = "INSERT INTO reservation (idClient, idAttraction, dateAttraction, dateReservation) VALUES (?, ?, ?, CURDATE())";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            stmt.setInt(2, idAttraction);
            stmt.setDate(3, dateAttraction);
            stmt.executeUpdate();
        }
    }


    public void supprimerReservation(int idReservation) throws SQLException {
        String sql = "DELETE FROM reservation WHERE idReservation = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReservation);
            stmt.executeUpdate();
        }
    }


    public void modifierDateReservation(int idReservation, Date nouvelleDate) throws SQLException {
        String sql = "UPDATE reservation SET dateAttraction = ? WHERE idReservation = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, nouvelleDate);
            stmt.setInt(2, idReservation);
            stmt.executeUpdate();
        }
    }


    public List<String> getReservationsParClient(int idClient) throws SQLException {
        List<String> reservations = new ArrayList<>();
        String sql = "SELECT r.idReservation, a.nom, r.dateAttraction " +
                "FROM reservation r " +
                "JOIN attraction a ON r.idAttraction = a.idAttraction " +
                "WHERE r.idClient = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String res = "Réservation #" + rs.getInt("idReservation") +
                        " - Attraction : " + rs.getString("nom") +
                        " - Date : " + rs.getDate("dateAttraction");
                reservations.add(res);
            }
        }
        return reservations;


        /// A AJOUTER DANS LE CONTROLEUR GRAPHIQUE (bouton pr reserver) :
        ///ReservationDAO modele.dao = new ReservationDAO(ConnexionBDD.getConnection());
        ///modele.dao.ajouterReservation(idClient, idAttraction, dateChoisie);


    }

    public List<String> getReservationsDetailsParClientEtDate(int idClient, Date dateReservation) throws SQLException {
        List<String> reservations = new ArrayList<>();
        String sql = "SELECT r.idReservation, a.nom, r.dateAttraction, r.dateReservation " +
                "FROM reservation r " +
                "JOIN attraction a ON r.idAttraction = a.idAttraction " +
                "WHERE r.idClient = ? AND r.dateReservation = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            stmt.setDate(2, dateReservation);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String ligne = "Réservation #" + rs.getInt("idReservation") +
                        " - Attraction : " + rs.getString("nom") +
                        " - Date Attraction : " + rs.getDate("dateAttraction") +
                        " - Réservée le : " + rs.getDate("dateReservation");
                reservations.add(ligne);
            }
        }
        return reservations;
    }


    public void creerFacture(int idClient) throws SQLException {
        String sql = "INSERT INTO facture (idClient, dateFacture) VALUES (?, CURDATE())";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            stmt.executeUpdate();
        }
    }






}
