package controleur;

import modele.Attraction;
import modele.dao.AttractionDAO;
import modele.dao.ConnexionBDD;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * ControleurAdminAttractions permet à l'administrateur
 * de gérer les attractions : ajout, modification, suppression.
 */
public class ControleurAdminAttractions {

    private final AttractionDAO dao;

    /**
     * Constructeur du contrôleur, établit la connexion à la base.
     *
     * @throws SQLException si erreur SQL
     * @throws IOException si erreur d'entrée/sortie
     * @throws ClassNotFoundException si classe non trouvée
     */
    public ControleurAdminAttractions() throws SQLException, IOException, ClassNotFoundException {
        Connection conn = ConnexionBDD.getConnexion();
        this.dao = new AttractionDAO(conn);
    }

    /**
     * Récupère toutes les attractions existantes.
     *
     * @return Liste d'attractions
     */
    public List<Attraction> getToutesAttractions() {
        try {
            return dao.getAllAttractions();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Ajoute une nouvelle attraction.
     *
     * @param nom Nom de l'attraction
     * @param prix Prix de l'attraction
     * @param image Image associée
     * @throws SQLException si problème SQL
     */
    public void ajouterAttraction(String nom, double prix, String image) throws SQLException {
        dao.ajouterAttraction(nom, prix, image);
    }

    /**
     * Modifie une attraction existante.
     *
     * @param attraction L'attraction à modifier
     * @param nouveauNom Nouveau nom
     * @param nouveauPrix Nouveau prix
     * @param nouvelleImage Nouvelle image
     * @throws SQLException si problème SQL
     */
    public void modifierAttraction(Attraction attraction, String nouveauNom, double nouveauPrix, String nouvelleImage) throws SQLException {
        dao.modifierAttraction(attraction.getId(), nouveauNom, nouveauPrix, nouvelleImage);
    }

    /**
     * Supprime une attraction existante.
     *
     * @param attraction L'attraction à supprimer
     * @throws SQLException si problème SQL
     */
    public void supprimerAttraction(Attraction attraction) throws SQLException {
        dao.supprimerAttraction(attraction.getId());
    }
}
