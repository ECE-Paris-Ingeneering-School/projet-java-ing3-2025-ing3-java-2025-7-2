package controleur;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import modele.dao.AttractionDAO;
import modele.dao.ConnexionBDD;
import vue.VueCalendrier;

public class Calendrier {
    private VueCalendrier view;
    private AttractionDAO attractionDAO;

    public Calendrier(VueCalendrier view) {
        this.view = view;

        try {
            Connection conn = ConnexionBDD.getConnexion(); // récupère la connexion
            this.attractionDAO = new AttractionDAO(conn);  // injection de la connexion
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace(); // à remplacer par une vraie gestion d'erreurs si besoin
        }
    }

    public void initializeCalendar() {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);
        view.displayMonth(YearMonth.from(today));
    }

    public List<String> getAttractionsForDay(LocalDate date) {
        try {
            return attractionDAO.getAllAttractionNames();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
