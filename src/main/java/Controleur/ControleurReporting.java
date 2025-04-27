package controleur;

import modele.dao.AttractionDAO;
import modele.Attraction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ControleurReporting permet de récupérer
 * les statistiques sur les attractions les plus réservées.
 */
public class ControleurReporting {

    private AttractionDAO attractionDAO;

    /**
     * Constructeur du contrôleur de reporting.
     *
     * @param attractionDAO DAO des attractions utilisé pour les statistiques
     */
    public ControleurReporting(AttractionDAO attractionDAO) {
        this.attractionDAO = attractionDAO;
    }

    /**
     * Récupère la liste des attractions avec leur nombre de réservations.
     *
     * @return Liste d'attractions avec statistiques de réservations
     */
    public List<Attraction> getAttractionsAvecReservations() {
        List<Attraction> liste = new ArrayList<>();
        try {
            Map<String, Integer> map = attractionDAO.getAttractionsLesPlusReservees();
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                Attraction attraction = new Attraction();
                attraction.setNom(entry.getKey());
                attraction.setNombreReservations(entry.getValue());
                liste.add(attraction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }
}
