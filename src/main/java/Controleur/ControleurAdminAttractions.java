package controleur;

import modele.Attraction;
import modele.dao.AttractionDAO;
import modele.dao.ConnexionBDD;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ControleurAdminAttractions {

    private final AttractionDAO dao;

    public ControleurAdminAttractions() throws SQLException, IOException, ClassNotFoundException {
        Connection conn = ConnexionBDD.getConnexion();
        this.dao = new AttractionDAO(conn);
    }

    public List<Attraction> getToutesAttractions() {
        try {
            return dao.getAllAttractions();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void ajouterAttraction(String nom, double prix, String image) throws SQLException {
        dao.ajouterAttraction(nom, prix, image);
    }

    public void modifierAttraction(Attraction attraction, String nouveauNom, double nouveauPrix, String nouvelleImage) throws SQLException {
        dao.modifierAttraction(attraction.getId(), nouveauNom, nouveauPrix, nouvelleImage);
    }

    public void supprimerAttraction(Attraction attraction) throws SQLException {
        dao.supprimerAttraction(attraction.getId());
    }
}
