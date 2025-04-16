package modele.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.*;

import modele.Attraction;

public class AttractionDAO {
    private Connection conn;

    public AttractionDAO(Connection conn) {
        this.conn = conn;
    }

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

    // Ajouter une attraction
    public void ajouterAttraction(String nom, double prix, String image) throws SQLException {
        String sql = "INSERT INTO attraction (nom, prix, image) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setDouble(2, prix);
            stmt.setString(3, image);
            stmt.executeUpdate();
        }
    }

    // Modifier une attraction
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

    // Supprimer une attraction
    public void supprimerAttraction(int idAttraction) throws SQLException {
        String sql = "DELETE FROM attraction WHERE idAttraction = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAttraction);
            stmt.executeUpdate();
        }
    }

    // Récupérer toutes les attractions
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
    // Méthode pour récupérer une attraction par son id
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

