package modele.dao;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe DAO pour gérer les opérations de la BDD liées aux factures
 */
public class FactureDAO {

    private Connection connexion;

    /**
     * Constructeur pour initialiser le DAO avec une connexion existante
     * @param connexion la connexion à la BDD
     */
    public FactureDAO(Connection connexion) {
        this.connexion = connexion;
    }

    /**
     * Génère une facture pour un client donné en fonction de ses réservations et de son âge (pour les réductions)
     * Calcule les réductions possibles et insère la facture dans la base de données.
     *
     * @param idClient l'id du client
     * @param age l'âge du client
     * @param reservations liste des réservations sous forme de tableau d'objets
     * @return l'identifiant généré de la facture, ou -1 en cas d'échec
     */
    public int genererFacture(int idClient, int age, List<Object[]> reservations) {

        System.out.println("Tentative de génération  facture - DAO -");

        double total = 0;
        double reduction = 0.0;
        int reservationsDansLeMois = 0;
        System.out.println("objet res : " + reservations);
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
        // réduction enfant ou sénior
        if (age < 12 || age > 60) {
            reduction += 0.15;
        }

        // supplément réservation
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


    /**
     * Récupère les factures d'un client sous forme de {@link ResultSet}.
     *
     * @param idClient l'identifiant du client.
     * @return un {@link ResultSet} contenant les factures du client, ou {@code null} en cas d'erreur.
     */
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

    /**
     * Récupère les factures d'un client sous forme d'une liste d'objets
     *
     * @param idClient l'identifiant du client
     * @return une liste d'objets contenant l'id de la facture, la date et le prix
     */
    public List<Object[]> getFacturesDuClientSousFormeListe(int idClient) {
        List<Object[]> factures = new ArrayList<>();
        try {
            String sql = "SELECT * FROM facture WHERE idClient = ?";
            PreparedStatement ps = connexion.prepareStatement(sql);
            ps.setInt(1, idClient);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idFacture = rs.getInt("idFacture");
                Date dateFacture = rs.getDate("dateFacture");
                double prix = rs.getDouble("prix");
                factures.add(new Object[]{idFacture, dateFacture, prix});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return factures;
    }

}
