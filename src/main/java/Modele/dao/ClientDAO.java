package modele.dao;

import modele.Client;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class ClientDAO {
    public List<Client> getAllClients() {
        List<Client> liste_clients = new ArrayList<>();

        try (Connection conn = ConnexionBDD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM client")) {

            while (rs.next()) {
                int id = rs.getInt("idClient");
                String mail = rs.getString("mail");
                String mdp = rs.getString("mdp");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                Date dateNaissance = rs.getDate("datedeNaissance");

                /// (int id, String mail, String mdp, String nom, String prenom, Date datedeNaissance, int fidelite)
            /// fidelite initialisé à 0, à incrémenter dans un set_fidelite
                Client c = new Client(id, mail, mdp, nom, prenom, dateNaissance,0);
                liste_clients.add(c);
            }

        } catch (SQLException e) {
            System.err.println(" Erreur lors de la récupération des clients : " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return liste_clients;
    }
}
