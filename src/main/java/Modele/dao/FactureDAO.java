package modele.dao;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class FactureDAO {

    private Connection connexion;

    public FactureDAO(Connection connexion) {
        this.connexion = connexion;
    }
    public int genererFacture(int idClient, int age, List<Object[]> reservations) {

        System.out.println("Tentative de génération  facture - DAO -");

        double total = 0;
        double reduction = 0.0;
        int reservationsDansLeMois = 0;

        LocalDate today = LocalDate.now();
        YearMonth moisActuel = YearMonth.from(today);

        for (Object[] res : reservations) {
            double prix = ((Number) res[3]).doubleValue();
            total += prix;

            // Vérification si la date est dans le mois courant
            if (res[2] instanceof Date) {
                LocalDate dateReservation = ((Date) res[2]).toLocalDate();
                if (YearMonth.from(dateReservation).equals(moisActuel)) {
                    reservationsDansLeMois++;
                }
            }
        }

        if (age < 12 || age > 60) {
            reduction += 0.15;
        }

        if(reservationsDansLeMois>1 && reservationsDansLeMois<=4) {
            reduction+=((reservationsDansLeMois-1)*0.1);
        }
        double prixFinal = total * (1 - reduction);

        System.out.println("Connexion OK ? " + (connexion != null));

        String sql = "INSERT INTO facture (idClient, dateFacture, prix) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idClient);
            ps.setDate(2, Date.valueOf(today));
            ps.setDouble(3, prixFinal);
            System.out.println("Exécution de l'INSERT...");
            ps.executeUpdate();
            System.out.println("INSERT effectué.");

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de la facture : " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }


    public ResultSet getFacturesDuClient(int idClient) {
        try {
            String sql = "SELECT * FROM facture WHERE idClient = ?";
            PreparedStatement ps = connexion.prepareStatement(sql);
            ps.setInt(1, idClient);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
