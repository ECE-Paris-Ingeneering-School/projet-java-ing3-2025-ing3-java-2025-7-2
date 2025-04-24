package modele;

import java.time.LocalDate;
import java.util.Date;

public class Evenement {

    private int idEvenement;
    private String nom;
    private int nbReservations;
    private double supplement;
    private Date dateDebut;
    private Date dateFin;
    private String image;

    // Getters and setters
    public int getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNbReservations() {
        return nbReservations;
    }

    public void setNbReservations(int nbReservations) {
        this.nbReservations = nbReservations;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }


    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
    public void setSupplement(double supplement) {
        this.supplement = supplement;
    }

    public double getSupplement() {
        return supplement;
    }

    public Date getDateDebut() {
        return dateDebut;
    }
    public Date getDateFin() {
        return dateFin;
    }

    @Override
    public String toString() {
        return "Evenement [idEvenement=" + idEvenement + ", nom=" + nom + ", nbReservations=" + nbReservations + "]";
    }

    public void setImage(String image) {
        this.image = image;
    }
}
