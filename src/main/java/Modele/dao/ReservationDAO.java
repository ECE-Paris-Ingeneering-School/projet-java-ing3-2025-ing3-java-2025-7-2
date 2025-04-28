package modele.dao;

import modele.Evenement;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Classe DAO pour gérer les opérations liées à la BDD et aux réservations
 * Ainsi que génerer les factures des clients
 */
public class ReservationDAO {

    private Connection conn;

    /**
     * Constructeur DAO avec une connexion existante
     * @param conn une connexion
     */
    public ReservationDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Ajoute une nouvelle réservation pour un client à une date donnée
     *
     * @param idClient      L'identifiant du client
     * @param idAttraction  L'identifiant de l'attraction
     * @param dateAttraction La date souhaitée pour l'attraction
     */
    public void ajouterReservation(int idClient, int idAttraction, Date dateAttraction) throws SQLException {
        String sql = "INSERT INTO reservation (idClient, idAttraction, dateAttraction, dateReservation, idFacture) VALUES (?, ?, ?, CURDATE(),0)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            stmt.setInt(2, idAttraction);
            stmt.setDate(3, dateAttraction);
            stmt.executeUpdate();
        }
    }


    /**
     * Supprime une réservation existante par son identifiant
     * @param idReservation L'identifiant de la réservation
     */
    public void supprimerReservation(int idReservation) throws SQLException {
        System.out.println("debug du suppr, id reservation: " + idReservation);
        String sql = "DELETE FROM reservation WHERE idReservation = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReservation);
            stmt.executeUpdate();
        }
    }


    /**
     * Modifie la date d'une réservation existante
     * @param idReservation L'identifiant de la réservation
     * @param nouvelleDate  La nouvelle date pour l'attraction
     */
    public void modifierDateReservation(int idReservation, Date nouvelleDate) throws SQLException {
        String sql = "UPDATE reservation SET dateAttraction = ? WHERE idReservation = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, nouvelleDate);
            stmt.setInt(2, idReservation);
            stmt.executeUpdate();
        }
    }


    /**
     * Récupère la liste des réservations d'un client sous forme de chaînes descriptives.
     *
     * @param idClient L'identifiant du client
     * @return Liste des réservations sous forme de texte
     */
    public List<String> getReservationsParClient(int idClient) throws SQLException {
        List<String> reservations = new ArrayList<>();
        String sql = "SELECT r.idReservation, a.nom, r.dateAttraction " +
                "FROM reservation r " +
                "JOIN attraction a ON r.idAttraction = a.idAttraction " +
                "WHERE r.idClient = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String res = "Réservation #" + rs.getInt("idReservation") +
                        " - Attraction : " + rs.getString("nom") +
                        " - Date : " + rs.getDate("dateAttraction");
                reservations.add(res);
            }
        }
        return reservations;


        /// A AJOUTER DANS LE CONTROLEUR GRAPHIQUE (bouton pr reserver) :
        ///ReservationDAO modele.dao = new ReservationDAO(ConnexionBDD.getConnection());
        ///modele.dao.ajouterReservation(idClient, idAttraction, dateChoisie);


    }

    /**
     * Récupère les réservations d'un client pour une date de réservation précise
     * en filtrant celles qui n'ont pas encore de facture (idFacture = 0)
     *
     * @param idClient       L'identifiant du client
     * @param dateReservation La date de réservation
     * @return Liste des réservations détaillées
     */
    public List<String> getReservationsDetailsParClientEtDate(int idClient, Date dateReservation) throws SQLException {
        List<String> reservations = new ArrayList<>();
        String sql = "SELECT r.idReservation, a.nom, r.dateAttraction, r.dateReservation " +
                "FROM reservation r " +
                "JOIN attraction a ON r.idAttraction = a.idAttraction " +
                "WHERE r.idClient = ? AND r.dateReservation = ? AND r.idFacture = 0"; // Ajouter la condition idFacture = 0
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            stmt.setDate(2, dateReservation);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String ligne = "Réservation #" + rs.getInt("idReservation") +
                        " - Attraction : " + rs.getString("nom") +
                        " - Date Attraction : " + rs.getDate("dateAttraction") +
                        " - Réservée le : " + rs.getDate("dateReservation");
                reservations.add(ligne);
            }
        }
        return reservations;
    }



    /**
     * Crée une facture pour un client basé sur ses réservations non facturées
     * Met à jour les réservations pour les associer à la facture générée
     *
     * @param idClient L'identifiant du client
     * @return L'identifiant de la facture créée
     */
    public int creerFacture(int idClient) throws SQLException {
        int idFacture = 0; // Déclaration en dehors du try

        conn.setAutoCommit(false); // Démarrer la transaction

        try {
            // 1. Récupérer les réservations non facturées
            String sqlReservations = "SELECT r.idReservation, a.prix " +
                    "FROM reservation r " +
                    "JOIN attraction a ON r.idAttraction = a.idAttraction " +
                    "WHERE r.idClient = ? AND r.idFacture = 0";
            PreparedStatement stmtReservations = conn.prepareStatement(sqlReservations);
            stmtReservations.setInt(1, idClient);
            ResultSet rsReservations = stmtReservations.executeQuery();

            double totalPrice = 0;
            List<Integer> reservationIds = new ArrayList<>();

            while (rsReservations.next()) {
                totalPrice += rsReservations.getDouble("prix");
                reservationIds.add(rsReservations.getInt("idReservation"));
            }

            // 2. Ajouter les suppléments éventuels
            double supplement = 0;
            String sqlEvenement = "SELECT supplement FROM evenement WHERE CURDATE() BETWEEN dateDebut AND dateFin";
            PreparedStatement stmtEvenement = conn.prepareStatement(sqlEvenement);
            ResultSet rsEvenement = stmtEvenement.executeQuery();

            while (rsEvenement.next()) {
                supplement += rsEvenement.getDouble("supplement");
            }

            totalPrice += supplement;

            // 3. Créer la facture
            String sqlFacture = "INSERT INTO facture (idClient, dateFacture, prix) VALUES (?, CURDATE(), ?)";
            PreparedStatement stmtFacture = conn.prepareStatement(sqlFacture, Statement.RETURN_GENERATED_KEYS);
            stmtFacture.setInt(1, idClient);
            stmtFacture.setDouble(2, totalPrice);
            stmtFacture.executeUpdate();

            ResultSet rsFacture = stmtFacture.getGeneratedKeys();
            if (rsFacture.next()) {
                idFacture = rsFacture.getInt(1);
            }

            // 4. Mettre à jour les réservations avec l'idFacture
            String sqlUpdate = "UPDATE reservation SET idFacture = ? WHERE idReservation = ?";
            PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
            for (int idReservation : reservationIds) {
                stmtUpdate.setInt(1, idFacture);
                stmtUpdate.setInt(2, idReservation);
                stmtUpdate.addBatch();
            }
            stmtUpdate.executeBatch();

            // 5. Valider la transaction
            conn.commit();
        } catch (SQLException e) {
            conn.rollback(); // Annuler si erreur
            throw e;
        } finally {
            conn.setAutoCommit(true); // Toujours réactiver l'auto-commit
        }

        return idFacture;
    }




    /**
     * Récupère les détails d'une facture (attractions, prix, suppléments) pour un identifiant donné
     *
     * @param idFacture L'identifiant de la facture
     * @return Map contenant la date, les réservations, le nombre et le total de la facture
     */
    public Map<String, Object> getFactureDetailsAvecReservations(int idFacture) throws SQLException {
        Map<String, Object> res = new java.util.HashMap<>();
        List<String> details = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        String sql = "SELECT f.dateFacture, a.nom, a.prix, r.dateAttraction " +
                "FROM reservation r " +
                "JOIN attraction a ON r.idAttraction = a.idAttraction " +
                "JOIN facture f ON f.idFacture = r.idFacture " +
                "WHERE r.idFacture = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFacture);
            ResultSet rs = stmt.executeQuery();
            Date dateFacture = null;

            while (rs.next()) {
                if (dateFacture == null) {
                    dateFacture = rs.getDate("dateFacture");
                }

                String nom = rs.getString("nom");
                BigDecimal prixBase = rs.getBigDecimal("prix");
                Date dateAttr = rs.getDate("dateAttraction");

                // Chercher les suppléments s’il y a un événement actif à la date
                BigDecimal supplement = getSupplementEvenementPourDate(dateAttr);
                BigDecimal prixFinal = prixBase.add(supplement != null ? supplement : BigDecimal.ZERO);
                total = total.add(prixFinal);

                details.add(nom + " (" + dateAttr + ") - Prix : " + prixBase + "€"
                        + (supplement != null && supplement.compareTo(BigDecimal.ZERO) > 0 ? " + Supplément : " + supplement + "€" : "")
                        + " → " + prixFinal + "€");
            }

            res.put("date", dateFacture.toString());
            res.put("reservations", details);
            res.put("nb", details.size());
            res.put("total", total.toPlainString());
        }

        return res;
    }

    /**
     * Récupère le supplément lié à un événement pour une date donnée
     *
     * @param date La date pour laquelle chercher les événements
     * @return Le montant du supplément, ou null s'il n'y a pas de supplément
     */
    private BigDecimal getSupplementEvenementPourDate(Date date) throws SQLException {
        String sql = "SELECT supplement FROM evenement WHERE dateDebut <= ? AND dateFin >= ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, date);
            stmt.setDate(2, date);
            ResultSet rs = stmt.executeQuery();
            BigDecimal totalSupp = BigDecimal.ZERO;
            while (rs.next()) {
                BigDecimal supp = rs.getBigDecimal("supplement");
                if (supp != null) {
                    totalSupp = totalSupp.add(supp);
                }
            }
            return totalSupp.compareTo(BigDecimal.ZERO) > 0 ? totalSupp : null;
        }
    }

    /**
     * Récupère les réservations passées d'un client
     *
     * @param idClient L'identifiant du client
     * @param today    La date du jour
     * @return Liste des réservations passées
     */
    public List<String> getReservationsDetailsParClientEtDatePassee(int idClient, Date today) {
        List<String> reservations = new ArrayList<>();
        String sql = "SELECT r.idReservation, r.dateAttraction, a.nom, a.prix FROM reservation r " +
                "JOIN attraction a ON r.idAttraction = a.idAttraction " +
                "WHERE r.idClient = ? AND r.dateAttraction <= ? ORDER BY r.dateAttraction DESC";


        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            stmt.setDate(2, today);

            EvenementDAO evenementDAO = new EvenementDAO(ConnexionBDD.getConnexion());
            ResultSet rs = stmt.executeQuery();

            System.out.println("fct dans dao - avant while");

            while (rs.next()) {
                int id = rs.getInt("idReservation");
                String nom = rs.getString("nom");
                Date date = rs.getDate("dateAttraction");
                double prixBase = rs.getDouble("prix");

                // Convertir en LocalDate
                LocalDate localDate = date.toLocalDate();

                // Récupérer l'événement (s’il y en a un) pour cette date
                Evenement evt = evenementDAO.getEvenementParDate(localDate);
                // Calcul du prix final avec supplément
                double prixFinal = prixBase;
                if (evt != null) {
                    prixFinal += evt.getSupplement();
                }
                reservations.add("Attraction: " + nom + " (" + date + ") - Prix : " + prixFinal + " €  # " + id);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservations;
    }



    /**
     * Récupère les réservations futures d'un client
     *
     * @param idClient L'identifiant du client
     * @param today    La date du jour (pour comparaison)
     * @return Liste des réservations futures
     */
    public List<String> getReservationsDetailsParClientEtDateFuture(int idClient, Date today) {
        List<String> reservations = new ArrayList<>();
        String sql = "SELECT r.idReservation, r.dateAttraction, a.nom, a.prix FROM reservation r " +
                "JOIN attraction a ON r.idAttraction = a.idAttraction " +
                "WHERE r.idClient = ? AND r.dateAttraction > ? ORDER BY r.dateAttraction ASC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            stmt.setDate(2, today);


            EvenementDAO evenementDAO = new EvenementDAO(ConnexionBDD.getConnexion());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idReservation");
                String nom = rs.getString("nom");
                Date date = rs.getDate("dateAttraction");
                double prix = rs.getDouble("prix");
                LocalDate localDate = date.toLocalDate();

                Evenement evt = evenementDAO.getEvenementParDate(localDate);
                double prixFinal = prix;
                if (evt != null) {
                    prixFinal += evt.getSupplement();
                }

                reservations.add("Attraction: " + nom + " (" + date + ") - Prix : " + prixFinal + " €  # " + id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservations;
    }

    /**
     * Récupère les réservations passées d'un client qui n'ont pas encore été facturées
     *
     * @param idClient L'identifiant du client
     * @return Liste d'objets représentant les réservations à facturer
     */
    public List<Object[]> getReservationsPasseesPourFacturation(int idClient) {
        List<Object[]> reservations = new ArrayList<>();
        String sql = "SELECT r.idReservation, a.nom, r.dateAttraction, a.prix FROM reservation r " +
                "JOIN attraction a ON r.idAttraction = a.idAttraction " +
                "WHERE r.idClient = ? AND r.dateAttraction <= CURDATE() AND r.idFacture = 0";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] ligne = new Object[4];
                ligne[0] = rs.getInt("idReservation");
                ligne[1] = rs.getString("nom");
                ligne[2] = rs.getDate("dateAttraction");
                ligne[3] = rs.getDouble("prix");
                reservations.add(ligne);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

}
