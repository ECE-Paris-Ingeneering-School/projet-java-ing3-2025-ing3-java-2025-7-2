package controleur;

import modele.dao.AttractionDAO;
import modele.dao.ConnexionBDD;
import vue.VueCalendrier;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class Calendrier {
    private VueCalendrier view;
    private AttractionDAO attractionDAO;

    public Calendrier(VueCalendrier view) {
        this.view = view;

        try {
            Connection conn = ConnexionBDD.getConnexion(); // récupère la connexion
            this.attractionDAO = new AttractionDAO(conn);  // injection de la connexion
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace(); // à améliorer pour une meilleure UX si nécessaire
        }
    }

    public void initializeCalendar() {
        LocalDate today = LocalDate.now();
        view.displayMonth(YearMonth.from(today));
    }

    public void navigateMonth(int months) {
        YearMonth newMonth = view.getCurrentYearMonth().plusMonths(months);
        view.displayMonth(newMonth);
    }

    public List<String> getAttractionsForDay(LocalDate date) {
        try {
            return attractionDAO.getAllAttractionNames();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of(); // retour vide en cas d’erreur
        }
    }
}
