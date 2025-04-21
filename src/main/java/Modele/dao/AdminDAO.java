package modele.dao;

import modele.Admin;
import modele.Attraction;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();

        try (Connection conn = ConnexionBDD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM admin")) {

            while (rs.next()) {
                int id = rs.getInt("idAdmin");
                String mail = rs.getString("mail");
                String motDePasse = rs.getString("motDePasse"); // adapte si autre nom
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");

                Admin admin = new Admin(id, mail, motDePasse, nom, prenom);
                admins.add(admin);
            }

        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors de la récupération des admins : " + e.getMessage());
        }

        return admins;
    }

    /// ajout / suppr client ?
    /// ajout / suppr attractions / evenements ?

    public void modifAttraction(Attraction attraction) {
        try (Connection conn = ConnexionBDD.getConnexion()) {
            String sql = "UPDATE attractions SET nom = ?, prix = ?, description = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, attraction.getNom());
            stmt.setDouble(2, attraction.getPrix());
            stmt.setInt(3, attraction.getId());
            stmt.executeUpdate();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors de la récupération des admins : " + e.getMessage());
        }
    }

}
