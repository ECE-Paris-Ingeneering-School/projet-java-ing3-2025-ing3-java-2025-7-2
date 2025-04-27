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

public class ControleurReservations {

    public List<String> getReservationsPour(Utilisateur utilisateur) {  /// retourn reserv selon objet utilisateur passé en paramètre
                                                                            /// ancienne méthode, plus utilisée dans les dernières versions
        try (Connection conn = ConnexionBDD.getConnexion()) {
            ReservationDAO dao = new ReservationDAO(conn);
            Date today = Date.valueOf(LocalDate.now());
            return dao.getReservationsDetailsParClientEtDate(utilisateur.getId(), today);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
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

    public Integer genererFacture(Utilisateur utilisateur) {
        try (Connection conn = ConnexionBDD.getConnexion()) {
            ReservationDAO dao = new ReservationDAO(conn);
            return dao.creerFacture(utilisateur.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Map<String, Object> getFactureDetails(int idFacture) {
        try (Connection conn = ConnexionBDD.getConnexion()) {
            ReservationDAO dao = new ReservationDAO(conn);
            return dao.getFactureDetailsAvecReservations(idFacture);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


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
