package modele.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.*;

import modele.Attraction;

/**
 * Classe DAO pour gérer les opérations de la BDD liées aux attractions
 */
public class AttractionDAO {
    private Connection conn;

    /**
     * constructeur de DAO avec la connexion active de la BDD
     * @param conn connexion active
     */
    public AttractionDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Récupère tous les noms des attractions
     *
     * @return Liste des noms d'attractions
     */
    public List<String> getAllAttractionNames() throws SQLException {
        List<String> attractions = new ArrayList<>();
        String query = "SELECT nom FROM attraction";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                attractions.add(rs.getString("nom"));
            }
        }
        return attractions;
    }

    /**
     * Récupère l'identifiant d'une attraction à partir de son nom
     *
     * @param name Nom de l'attraction
     * @return Identifiant de l'attraction ou -1 si non trouvée
     */
    public int getAttractionIdByName(String name) throws SQLException {
        String query = "SELECT idAttraction FROM attraction WHERE nom = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idAttraction");
                }
            }
        }
        return -1;
    }

    /**
     * Récupère l'identifiant d'une attraction à partir de son nom
     *
     * @param nom Nom de l'attraction
     * @return Identifiant de l'attraction
     */
    public int getIdAttractionParNom(String nom) throws SQLException {
        String sql = "SELECT idAttraction FROM attraction WHERE nom = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idAttraction");
            } else {
                throw new SQLException("Attraction non trouvée : " + nom);
            }
        }
    }

    /**
     * Récupère les attractions les plus réservées
     *
     * @return Map contenant les noms des attractions et leur nombre de réservations
     */
    public Map<String, Integer> getAttractionsLesPlusReservees() throws Exception {
        String sql = "SELECT a.nom, COUNT(*) as total FROM reservation r " +
                "JOIN attraction a ON r.idAttraction = a.idAttraction " +
                "GROUP BY a.nom ORDER BY total DESC LIMIT 5";
        Map<String, Integer> map = new LinkedHashMap<>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            map.put(rs.getString("nom"), rs.getInt("total"));
        }
        return map;
    }


    /**
     * Ajoute une nouvelle attraction dans la BDD
     *
     * @param nom Nom de l'attraction
     * @param prix Prix de l'attraction
     * @param image Chemin ou URL de l'image de l'attraction
     */
    public void ajouterAttraction(String nom, double prix, String image) throws SQLException {
        String sql = "INSERT INTO attraction (nom, prix, image) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setDouble(2, prix);
            stmt.setString(3, image);
            stmt.executeUpdate();
        }
    }

    /**
     * Modifie une attraction existante dans la BDD
     *
     * @param idAttraction Identifiant de l'attraction
     * @param nom Nouveau nom de l'attraction
     * @param prix Nouveau prix
     * @param image Nouvelle image
     */
    public void modifierAttraction(int idAttraction, String nom, double prix, String image) throws SQLException {
        String sql = "UPDATE attraction SET nom = ?, prix = ?, image = ? WHERE idAttraction = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setDouble(2, prix);
            stmt.setString(3, image);
            stmt.setInt(4, idAttraction);
            stmt.executeUpdate();
        }
    }

    /**
     * Supprime une attraction de la BDD
     *
     * @param idAttraction Identifiant de l'attraction à supprimer
     */
    public void supprimerAttraction(int idAttraction) throws SQLException {
        String sql = "DELETE FROM attraction WHERE idAttraction = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAttraction);
            stmt.executeUpdate();
        }
    }

    /**
     * Récupère la liste de toutes les attractions stockées dans la BDD
     *
     * @return Liste d'objets {@link Attraction}
     */
    public List<Attraction> getAllAttractions() throws SQLException {
        List<Attraction> attractions = new ArrayList<>();
        String sql = "SELECT * FROM attraction";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Attraction attraction = new Attraction();
                attraction.setIdAttraction(rs.getInt("idAttraction"));
                attraction.setNom(rs.getString("nom"));
                attraction.setPrix(rs.getDouble("prix"));
                attraction.setImage(rs.getString("image"));
                attractions.add(attraction);
            }
        }
        return attractions;
    }


    /**
     * Récupère une attraction à partir de son identifiant
     *
     * @param idAttraction Identifiant de l'attraction
     * @return L'objet {@link Attraction} correspondant, ou null si non trouvé
     */
    public Attraction getAttractionParNom(int idAttraction) throws SQLException {
        String sql = "SELECT * FROM attraction WHERE idAttraction = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAttraction);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Attraction attraction = new Attraction();
                attraction.setIdAttraction(rs.getInt("idAttraction"));
                attraction.setNom(rs.getString("nom"));
                attraction.setPrix(rs.getDouble("prix"));
                attraction.setImage(rs.getString("image"));
                return attraction;
            }
        }
        return null;  // Si aucune attraction n'est trouvée, retourner null
    }


}

