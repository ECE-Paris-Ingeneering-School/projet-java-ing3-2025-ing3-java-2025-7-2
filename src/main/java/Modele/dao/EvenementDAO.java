package modele.dao;


import modele.Evenement;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementDAO {
    private Connection conn;

    public EvenementDAO(Connection conn) {
        this.conn = conn;
    }

    public List<String> getEvenementsForMonth(YearMonth yearMonth) throws SQLException {
        List<String> evenements = new ArrayList<>();
        String sql = "SELECT nom, dateDebut, dateFin FROM evenement WHERE (dateDebut BETWEEN ? AND ?) OR (dateFin BETWEEN ? AND ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDate firstDayOfMonth = yearMonth.atDay(1);
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

            stmt.setDate(1, Date.valueOf(firstDayOfMonth));
            stmt.setDate(2, Date.valueOf(lastDayOfMonth));
            stmt.setDate(3, Date.valueOf(firstDayOfMonth));
            stmt.setDate(4, Date.valueOf(lastDayOfMonth));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String evenement = rs.getString("nom") + " (" + rs.getDate("dateDebut") + " - " + rs.getDate("dateFin") + ")";
                evenements.add(evenement);
            }
        }
        return evenements;
    }

    // Nouvelle méthode pour récupérer les événements pour un jour donné
    public List<String> getEvenementsForDay(LocalDate date) throws SQLException {
        List<String> evenements = new ArrayList<>();
        String sql = "SELECT nom, dateDebut, dateFin FROM evenement WHERE dateDebut <= ? AND dateFin >= ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date)); // dateDebut <= date
            stmt.setDate(2, Date.valueOf(date)); // dateFin >= date
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String evenement = "Événement : " + rs.getString("nom") +
                        " - Du " + rs.getDate("dateDebut") + " au " + rs.getDate("dateFin");
                evenements.add(evenement);
            }
        }
        return evenements;
    }

    // Ajouter un événement
    public void ajouterEvenement(String nom, double supplement, Date dateDebut, Date dateFin) throws SQLException {
        String sql = "INSERT INTO evenement (nom, supplement, dateDebut, dateFin) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setDouble(2, supplement);
            stmt.setDate(3, dateDebut);
            stmt.setDate(4, dateFin);
            stmt.executeUpdate();
        }
    }

    // Modifier un événement
    public void modifierEvenement(int idEvenement, String nom, double supplement, Date dateDebut, Date dateFin) throws SQLException {
        String sql = "UPDATE evenement SET nom = ?, supplement = ?, dateDebut = ?, dateFin = ? WHERE idEvenement = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setDouble(2, supplement);
            stmt.setDate(3, dateDebut);
            stmt.setDate(4, dateFin);
            stmt.setInt(5, idEvenement);
            stmt.executeUpdate();
        }
    }

    // Supprimer un événement
    public void supprimerEvenement(int idEvenement) throws SQLException {
        String sql = "DELETE FROM evenement WHERE idEvenement = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEvenement);
            stmt.executeUpdate();
        }
    }

    // Récupérer tous les événements
    public List<Evenement> getAllEvenements() throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String sql = "SELECT * FROM evenement";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Evenement evenement = new Evenement();
                evenement.setIdEvenement(rs.getInt("idEvenement"));
                evenement.setNom(rs.getString("nom"));
                evenement.setSupplement(rs.getDouble("supplement"));
                evenement.setDateDebut(rs.getDate("dateDebut"));
                evenement.setDateFin(rs.getDate("dateFin"));
                evenements.add(evenement);
            }
        }
        return evenements;
    }

    public Evenement getEvenementParId(int idEvenement) throws SQLException {
        String sql = "SELECT * FROM evenement WHERE idEvenement = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEvenement);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Evenement evenement = new Evenement();
                evenement.setIdEvenement(rs.getInt("idEvenement"));
                evenement.setNom(rs.getString("nom"));
                evenement.setImage(rs.getString("image"));
                evenement.setSupplement(rs.getDouble("supplement"));
                evenement.setDateDebut(rs.getDate("dateDebut"));
                evenement.setDateFin(rs.getDate("dateFin"));
                return evenement;
            }
        }
        return null; // Aucun événement trouvé
    }


}

