package controleur;

import modele.Utilisateur;
import modele.dao.ConnexionBDD;
import modele.dao.ReservationDAO;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ControleurReservations gère toutes les opérations
 * liées aux réservations et factures des utilisateurs.
 */
public class ControleurReservations {

    /**
     * Récupère toutes les réservations actuelles d'un utilisateur (ancienne version).
     *
     * @param utilisateur Utilisateur concerné
     * @return Liste de descriptions de réservations
     */
    public List<String> getReservationsPour(Utilisateur utilisateur) {
        try (Connection conn = ConnexionBDD.getConnexion()) {
            ReservationDAO dao = new ReservationDAO(conn);
            Date today = Date.valueOf(LocalDate.now());
            return dao.getReservationsDetailsParClientEtDate(utilisateur.getId(), today);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Supprime une réservation selon son identifiant.
     *
     * @param idReservation Identifiant de la réservation
     * @return true si suppression réussie, false sinon
     */
    public boolean supprimerReservation(int idReservation) {
        System.out.println("debug du suppr, id reservation: " + idReservation);
        try (Connection conn = ConnexionBDD.getConnexion()) {
            ReservationDAO dao = new ReservationDAO(conn);
            dao.supprimerReservation(idReservation);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Génère une facture pour un utilisateur.
     *
     * @param utilisateur Utilisateur concerné
     * @return ID de la facture générée ou null en cas d'erreur
     */
    public Integer genererFacture(Utilisateur utilisateur) {
        try (Connection conn = ConnexionBDD.getConnexion()) {
            ReservationDAO dao = new ReservationDAO(conn);
            return dao.creerFacture(utilisateur.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère les détails d'une facture donnée.
     *
     * @param idFacture Identifiant de la facture
     * @return Map contenant les informations de la facture
     */
    public Map<String, Object> getFactureDetails(int idFacture) {
        try (Connection conn = ConnexionBDD.getConnexion()) {
            ReservationDAO dao = new ReservationDAO(conn);
            return dao.getFactureDetailsAvecReservations(idFacture);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère toutes les réservations futures d'un utilisateur.
     *
     * @param utilisateur Utilisateur concerné
     * @return Liste de réservations futures
     */
    public List<String> getReservationsFutures(Utilisateur utilisateur) {
        try (Connection conn = ConnexionBDD.getConnexion()) {
            ReservationDAO dao = new ReservationDAO(conn);
            Date today = Date.valueOf(LocalDate.now());
            return dao.getReservationsDetailsParClientEtDateFuture(utilisateur.getId(), today);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Récupère toutes les réservations passées d'un utilisateur.
     *
     * @param utilisateur Utilisateur concerné
     * @return Liste de réservations passées
     */
    public List<String> getReservationsPassees(Utilisateur utilisateur) {
        try (Connection conn = ConnexionBDD.getConnexion()) {
            ReservationDAO dao = new ReservationDAO(conn);
            Date today = Date.valueOf(LocalDate.now());
            return dao.getReservationsDetailsParClientEtDatePassee(utilisateur.getId(), today);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
