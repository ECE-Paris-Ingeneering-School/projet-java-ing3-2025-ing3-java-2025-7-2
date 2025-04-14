package modele.dao;


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
}

