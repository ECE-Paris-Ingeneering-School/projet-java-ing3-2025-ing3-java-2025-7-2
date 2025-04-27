package controleur;

import modele.Evenement;
import modele.dao.ConnexionBDD;
import modele.dao.EvenementDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * ControleurEvenement gère la récupération et l'affichage
 * des événements liés aux dates dans l'application.
 */
public class ControleurEvenement {

    private EvenementDAO evenementDAO;

    /**
     * Constructeur du contrôleur, initialise la connexion aux événements.
     */
    public ControleurEvenement() {
        try {
            this.evenementDAO = new EvenementDAO(ConnexionBDD.getConnexion());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère toutes les dates du mois qui contiennent un événement.
     *
     * @param yearMonth Mois et année à parcourir
     * @return Liste de dates avec événements
     */
    public List<LocalDate> getDatesAvecEvenements(YearMonth yearMonth) {
        List<LocalDate> dates = new ArrayList<>();
        try {
            List<String> evenements = evenementDAO.getEvenementsForMonth(yearMonth);
            for (String evt : evenements) {
                String[] parts = evt.split("\\(")[1].replace(")", "").split(" - ");
                LocalDate start = LocalDate.parse(parts[0]);
                LocalDate end = LocalDate.parse(parts[1]);
                while (!start.isAfter(end)) {
                    if (start.getMonth() == yearMonth.getMonth()) {
                        dates.add(start);
                    }
                    start = start.plusDays(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dates;
    }

    /**
     * Récupère l'ID d'un événement à une date donnée.
     *
     * @param date La date recherchée
     * @return ID de l'événement, ou null si aucun
     */
    public Integer getIDEvenementParDate(LocalDate date) {
        try {
            List<Evenement> evenements = EvenementDAO.getAllEvenements();
            for (Evenement ev : evenements) {
                LocalDate debut = ((java.sql.Date) ev.getDateDebut()).toLocalDate();
                LocalDate fin = ((java.sql.Date) ev.getDateFin()).toLocalDate();

                if ((date.isEqual(debut) || date.isAfter(debut)) && (date.isEqual(fin) || date.isBefore(fin))) {
                    return ev.getIdEvenement();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
