package modele;

import java.time.LocalDate;
import java.util.Date;

/**
 * Classe objet qui représente les évènements du parc et ses infos :
 * identifiant, nom, nombre de réservations pendant l'event, supplement, date de début et de fin
 */
public class Evenement {

    private int idEvenement;
    private String nom;
    private int nbReservations;
    private double supplement;
    private Date dateDebut;
    private Date dateFin;
    private String image;

    // Getters and setters :
    /**
     * @return L'ID de l'événement
     */
    public int getIdEvenement() {return idEvenement;}

    /**
     * Modifie l'ID de l'événement
     *
     * @param idEvenement L'ID à attribuer à l'événement
     */
    public void setIdEvenement(int idEvenement) {this.idEvenement = idEvenement;}

    /**
     * @return Le nom de l'événement
     */
    public String getNom() {return nom;}

    /**
     * Modifie le nom de l'événement
     *
     * @param nom Le nom à attribuer à l'événement
     */
    public void setNom(String nom) {this.nom = nom;}

    /**
     * @return Le nombre de réservations pour cet événement
     */
    public int getNbReservations() {return nbReservations;}

    /**
     * Modifie le nombre de réservations pour l'événement
     *
     * @param nbReservations Le nombre de réservations à attribuer à l'événement
     */
    public void setNbReservations(int nbReservations) {this.nbReservations = nbReservations;}

    /**
     * Modifie la date de début de l'événement
     *
     * @param dateDebut La date de début de l'événement
     */
    public void setDateDebut(Date dateDebut) {this.dateDebut = dateDebut;}

    /**
     * Modifie la date de fin de l'événement
     *
     * @param dateFin La date de fin de l'événement
     */
    public void setDateFin(Date dateFin) {this.dateFin = dateFin;}

    /**
     * @return Le supplément applicable pour l'événement
     */
    public double getSupplement() {return supplement;}

    /**
     * Modifie le supplément de l'événement
     *
     * @param supplement Le supplément à attribuer à l'événement
     */
    public void setSupplement(double supplement) {this.supplement = supplement;}

    /**
     * @return La date de début de l'événement
     */
    public Date getDateDebut() {return dateDebut;}

    /**
     * @return La date de fin de l'événement
     */
    public Date getDateFin() {return dateFin;}

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'événement
     *
     * @return Une chaîne représentant les informations de l'événement
     */
    @Override
    public String toString() {return "Evenement [idEvenement=" + idEvenement + ", nom=" + nom + ", nbReservations=" + nbReservations + "]";}

    public void setImage(String image) {this.image = image;}
}
