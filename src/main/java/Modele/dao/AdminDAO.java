package modele.dao;

import modele.Admin;

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
}
