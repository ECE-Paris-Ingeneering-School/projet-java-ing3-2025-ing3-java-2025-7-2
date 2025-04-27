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

public class Calendrier{
    private VueCalendrier view;
    private AttractionDAO attractionDAO;


    public Calendrier( VueCalendrier view){
        this.view = view;

        try {
            Connection conn = ConnexionBDD.getConnexion(); //récupère la connexion
            this.attractionDAO = new AttractionDAO(conn);   // injection de la connexio

        }catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace(); // à améliorer pour meilleur UX si nécessaire
        }
    }


    public void initializeCalendar() {
        LocalDate today = LocalDate.now();
        view.displayMonth(YearMonth.from(today));

    }


    public void navigationparMois (int months){
        YearMonth newMonth = view.getCurrentYearMonth().plusMonths(months);
        view.displayMonth(newMonth);
    }

    public List<String> getAttractionsForDay(LocalDate date){
        try {
            return attractionDAO.getAllAttractionNames();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of(); // retour vide en cas d’erreur
        }
    }
    public boolean reserverAttraction  (String nomAttraction, LocalDate date, Utilisateur utilisateur) {
        if ("admin".equalsIgnoreCase(utilisateur.getRole())) {
            return false; // Les admins ne peuvent pas résérver pas
        }
        try (Connection conn = ConnexionBDD.getConnexion()){
            AttractionDAO attractionDAO = new AttractionDAO(conn);
            ReservationDAO reservationDAO = new ReservationDAO(conn);


            int idAttraction = attractionDAO.getAttractionIdByName(nomAttraction);
            reservationDAO.ajouterReservation(utilisateur.getId(), idAttraction, Date.valueOf(date));

            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
