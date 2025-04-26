package controleur;

import modele.dao.AttractionDAO;
import modele.Attraction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControleurReporting {

    private AttractionDAO attractionDAO;

    public ControleurReporting(AttractionDAO attractionDAO) {
        this.attractionDAO = attractionDAO;
    }

    /**
     * Récupère la liste des attractions avec leur nombre de réservations.
     * @return liste des attractions avec le nombre de réservations renseigné
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
