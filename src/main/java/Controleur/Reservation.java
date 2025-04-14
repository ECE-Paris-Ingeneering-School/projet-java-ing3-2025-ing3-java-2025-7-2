package controleur;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import modele.dao.AttractionDAO;
import modele.dao.ConnexionBDD;

public class Reservation {
    private AttractionDAO attractionDAO;

    public Reservation() {
        try {
            Connection conn = ConnexionBDD.getConnexion(); // Récupération de la connexion
            this.attractionDAO = new AttractionDAO(conn);  // Injection de la connexion dans le DAO
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace(); // Peut être remplacé par une gestion plus propre
        }
    }

    public boolean addReservation(String attractionName, LocalDate date, int clientId) {
        try {
            int attractionId = attractionDAO.getAttractionIdByName(attractionName);
            if (attractionId == -1) return false;

            // Simuler l'insertion dans une table reservation
            System.out.println("Réservation simulée - Attraction: " + attractionId +
                    ", Date: " + date + ", Client: " + clientId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
