package controleur;

import modele.dao.AttractionDAO;
import modele.dao.ConnexionBDD;
import modele.dao.ReservationDAO;

import vue.VueCalendrier;

import modele.Utilisateur;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.sql.Date;

/**
 * Le contrôleur Calendrier gère l'affichage et la réservation d'attractions
 * via le calendrier pour les utilisateurs.
 */
public class Calendrier {
    private VueCalendrier view;
    private AttractionDAO attractionDAO;

    /**
     * Constructeur de Calendrier.
     *
     * @param view La vue associée (VueCalendrier)
     */
    public Calendrier(VueCalendrier view) {
        this.view = view;

        try {
            Connection conn = ConnexionBDD.getConnexion(); // Récupère la connexion
            this.attractionDAO = new AttractionDAO(conn);  // Injection de la connexion
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace(); // À améliorer pour un meilleur UX
        }
    }

    /**
     * Initialise le calendrier pour le mois actuel.
     */
    public void initializeCalendar() {
        LocalDate today = LocalDate.now();
        view.displayMonth(YearMonth.from(today));
    }

    /**
     * Permet la navigation dans le calendrier par mois.
     *
     * @param months Nombre de mois à avancer ou reculer
     */
    public void navigationparMois(int months) {
        YearMonth newMonth = view.getCurrentYearMonth().plusMonths(months);
        view.displayMonth(newMonth);
    }

    /**
     * Récupère la liste des attractions pour un jour donné.
     *
     * @param date Date choisie
     * @return Liste des noms d'attractions
     */
    public List<String> getAttractionsForDay(LocalDate date) {
        try {
            return attractionDAO.getAllAttractionNames();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of(); 
        }
    }

    /**
     * Permet à un utilisateur de réserver une attraction pour une date donnée.
     *
     * @param nomAttraction Le nom de l'attraction
     * @param date Date souhaitée
     * @param utilisateur Utilisateur qui réserve
     * @return true si la réservation a réussi, false sinon
     */
    public boolean reserverAttraction(String nomAttraction, LocalDate date, Utilisateur utilisateur) {
        if ("admin".equalsIgnoreCase(utilisateur.getRole())) {
            return false; // Les admins ne peuvent pas réserver
        }
        try (Connection conn = ConnexionBDD.getConnexion()) {
            AttractionDAO attractionDAO = new AttractionDAO(conn);
            ReservationDAO reservationDAO = new ReservationDAO(conn);

            int idAttraction = attractionDAO.getAttractionIdByName(nomAttraction);
            reservationDAO.ajouterReservation(utilisateur.getId(), idAttraction, Date.valueOf(date));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
