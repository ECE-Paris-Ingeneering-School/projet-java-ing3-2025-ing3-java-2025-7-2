package modele.dao;

import modele.Client;

import java.io.IOException;
import java.sql.*;

import java.util.*;
import java.util.Date;

/**
 * Classe DAO pour gérer les opérationsde la BDD liées aux clients
 */
public class ClientDAO {

    /**
     * Récupère tous les clients existants dans la BDD
     * @return liste d'objets {@link Client}
     */
    public List<Client> getAllClients() {
        List<Client> liste_clients = new ArrayList<>();

        try (Connection conn = ConnexionBDD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM client")) {

            while (rs.next()) {
                int id = rs.getInt("idClient");
                String mail = rs.getString("mail");
                String mdp = rs.getString("motDePasse");
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

    /**
     * Récupère un client spécifique à partir de son identifiant.
     *
     * @param idClient l'identifiant du client.
     * @return l'objet {@link Client} correspondant, ou {@code null} si non trouvé.
     */
    public Client getClientParId(int idClient) throws SQLException, IOException, ClassNotFoundException {
        Client client = null;
        String sql = "SELECT * FROM client WHERE idClient = ?";

        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idClient);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String mail = rs.getString("mail");
                    String mdp = rs.getString("motDePasse");
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    Date dateNaissance = rs.getDate("datedeNaissance");

                    client = new Client(idClient, mail, mdp, nom, prenom, dateNaissance, 0);
                }
            }
        }

        return client;
    }

    /**
     * Ajoute un nouveau client dans la base de données.
     *
     * @param client l'objet {@link Client} à ajouter.
     */
    public void ajouterClient(Client client) throws Exception {
        try (Connection conn = ConnexionBDD.getConnexion()) {
            String sql = "INSERT INTO client (mail, motDePasse, nom, prenom, datedeNaissance) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, client.getMail());
                stmt.setString(2, client.getMdp());
                stmt.setString(3, client.getNom());
                stmt.setString(4, client.getPrenom());
                stmt.setDate(5, new java.sql.Date(client.getDatedeNaissance().getTime()));
                stmt.executeUpdate();
            }
        }
    }

    /**
     * Modifie les informations d'un client existant.
     *
     * @param client l'objet {@link Client} contenant les informations à mettre à jour.
     */
    public void modifierClient(Client client) throws Exception {
        try (Connection conn = ConnexionBDD.getConnexion()) {
            String sql = "UPDATE client SET mail=?, motDePasse=?, nom=?, prenom=?, datedeNaissance=? WHERE idClient=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, client.getMail());
                stmt.setString(2, client.getMdp());
                stmt.setString(3, client.getNom());
                stmt.setString(4, client.getPrenom());
                stmt.setDate(5, new java.sql.Date(client.getDatedeNaissance().getTime()));
                stmt.setInt(6, client.getId());
                stmt.executeUpdate();
            }
        }
    }

    /**
     * Supprime un client de la base de données en fonction de son identifiant.
     * @param idClient l'identifiant du client à supprimer.
     */
    public void supprimerClient(int idClient) throws Exception {
        try (Connection conn = ConnexionBDD.getConnexion()) {
            String sql = "DELETE FROM client WHERE idClient = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idClient);
                stmt.executeUpdate();
            }
        }
    }


}
