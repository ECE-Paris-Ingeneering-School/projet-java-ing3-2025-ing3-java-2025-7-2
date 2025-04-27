package modele.dao;


import modele.Evenement;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour gérer les évènements dans la BDD
 */
public class EvenementDAO {
    private static Connection conn;

    /**
     * Constructeur DAO avec la connexion à la BDD
     * @param conn connexion active à la BDD
     */
    public EvenementDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Récupère les événements dont les dates de début ou de fin sont comprises dans le mois donné
     *
     * @param yearMonth Mois et année ciblés
     * @return Liste de descriptions des événements
     */
    public List<Evenement> getEvenementsForMonth(YearMonth yearMonth) throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String sql = "SELECT * FROM evenement WHERE (dateDebut BETWEEN ? AND ?) OR (dateFin BETWEEN ? AND ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDate firstDayOfMonth = yearMonth.atDay(1);
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

            stmt.setDate(1, Date.valueOf(firstDayOfMonth));
            stmt.setDate(2, Date.valueOf(lastDayOfMonth));
            stmt.setDate(3, Date.valueOf(firstDayOfMonth));
            stmt.setDate(4, Date.valueOf(lastDayOfMonth));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Evenement evenement = new Evenement();
                evenement.setIdEvenement(rs.getInt("idEvenement"));
                evenement.setNom(rs.getString("nom"));
                evenement.setImage(rs.getString("image"));
                evenement.setSupplement(rs.getDouble("supplement"));
                evenement.setDateDebut(rs.getDate("dateDebut"));
                evenement.setDateFin(rs.getDate("dateFin"));
                evenements.add(evenement);
            }
        }
        return evenements;
    }

    /**
     * Récupère les événements actifs pour un jour donné
     *
     * @param date Date pour laquelle récupérer les événements
     * @return Liste de descriptions des événements
     */
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

    /**
     * Ajoute un nouvel événement dans la base de données.
     *
     * @param nom  Nom de l'événement
     * @param supplement Supplément de l'événement
     * @param dateDebut Date de début de l'événement
     * @param dateFin Date de fin de l'événement
     */
    public void ajouterEvenement(String nom, double supplement, Date dateDebut, Date dateFin,String image) throws SQLException {
        String sql = "INSERT INTO evenement (nom, supplement, dateDebut, dateFin, image) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setDouble(2, supplement);
            stmt.setDate(3, dateDebut);
            stmt.setDate(4, dateFin);
            stmt.setString(5, image);
            stmt.executeUpdate();
        }
    }


    /**
     * Modifie un événement existant dans la base de données
     *
     * @param idEvenement Identifiant de l'événement
     * @param nom Nouveau nom de l'événement
     * @param supplement Nouveau supplément tarifaire
     * @param dateDebut Nouvelle date de début
     * @param dateFin Nouvelle date de fin
     */
    /// ancienne méthode
/**    public void modifierEvenement(int idEvenement, String nom, double supplement, Date dateDebut, Date dateFin) throws SQLException {
        String sql = "UPDATE evenement SET nom = ?, supplement = ?, dateDebut = ?, dateFin = ? WHERE idEvenement = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setDouble(2, supplement);
            stmt.setDate(3, dateDebut);
            stmt.setDate(4, dateFin);
            stmt.setInt(5, idEvenement);
            stmt.executeUpdate();
        }
    }**/
public void modifierEvenement(Evenement evenement) throws SQLException {
    String sql = "UPDATE evenement SET nom = ?, supplement = ?, dateDebut = ?, dateFin = ?, image = ? WHERE idEvenement = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, evenement.getNom());
        stmt.setDouble(2, evenement.getSupplement());
        stmt.setDate(3, new java.sql.Date(evenement.getDateDebut().getTime()));
        stmt.setDate(4, new java.sql.Date(evenement.getDateFin().getTime()));
        stmt.setString(5, evenement.getImage());
        stmt.setInt(6, evenement.getIdEvenement());
        stmt.executeUpdate();
    }
}
    /**
     * Supprime un événement de la base de données.
     *
     * @param idEvenement Identifiant de l'événement à supprimer
     */
    public void supprimerEvenement(int idEvenement) throws SQLException {
        String sql = "DELETE FROM evenement WHERE idEvenement = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEvenement);
            stmt.executeUpdate();
        }
    }

    /**
     * Récupère la liste de tous les événements stockés dans la base de données.
     *
     * @return Liste d'objets {@link Evenement}
     */
    public static List<Evenement> getAllEvenements() throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String sql = "SELECT * FROM evenement";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Evenement evenement = new Evenement();
                evenement.setIdEvenement(rs.getInt("idEvenement"));
                evenement.setNom(rs.getString("nom"));
                evenement.setImage(rs.getString("image"));
                evenement.setSupplement(rs.getDouble("supplement"));
                evenement.setDateDebut(rs.getDate("dateDebut"));
                evenement.setDateFin(rs.getDate("dateFin"));
                evenements.add(evenement);
            }
        }
        return evenements;
    }

    /**
     * Récupère un événement à partir de son identifiant.
     *
     * @param idEvenement Identifiant de l'événement
     * @return L'objet {@link Evenement} correspondant, ou null s'il n'existe pas
     */
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


    /**
     * Récupère un événement actif pour une date spécifique (entre dateDebut et dateFin).
     *
     * @param date Date à vérifier
     * @return L'objet {@link Evenement} correspondant, ou null si aucun événement trouvé
     */
    public Evenement getEvenementParDate(LocalDate date) {
        String sql = "SELECT * FROM evenement WHERE dateDebut <= ? AND dateFin >= ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            java.sql.Date sqlDate = java.sql.Date.valueOf(date);

            stmt.setDate(1, sqlDate); // dateDebut <= date
            stmt.setDate(2, sqlDate); // dateFin >= date

            System.out.println(" Vérification requête pour date : " + sqlDate); // DEBUG

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Evenement evt = new Evenement();
                evt.setIdEvenement(rs.getInt("idEvenement"));
                evt.setNom(rs.getString("nom"));
                evt.setImage(rs.getString("image"));
                evt.setSupplement(rs.getDouble("supplement"));
                evt.setDateDebut(rs.getDate("dateDebut"));
                evt.setDateFin(rs.getDate("dateFin"));

                // Affiche l'évènement si trouvé
                System.out.println("✅ Événement trouvé : " + evt.getNom() + " | Supplément : " + evt.getSupplement());
                return evt;
            } else {
                System.out.println("❌ Aucun événement pour cette date.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }



}

