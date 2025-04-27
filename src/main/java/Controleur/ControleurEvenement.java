package controleur;

import modele.Evenement;
import modele.dao.ConnexionBDD;
import modele.dao.EvenementDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControleurEvenement {

    private EvenementDAO evenementDAO;

    public ControleurEvenement(){
        try {
            this.evenementDAO = new EvenementDAO(ConnexionBDD.getConnexion());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<LocalDate> getDatesAvecEvenements(YearMonth yearMonth){

       List<LocalDate> dates = new ArrayList<>();
        try {
            List<String> evenements = evenementDAO.getEvenementsForMonth(yearMonth);
            for (String evt : evenements) {
                String[] parts = evt.split("\\(")[1].replace(")", "").split(" - ");
                LocalDate start = LocalDate.parse(parts[0]);
                LocalDate end = LocalDate.parse(parts[1]);
                while (!start.isAfter(end)) {
                    if (start.getMonth() == yearMonth.getMonth()) {
                        dates.add(start);
                    }
                    start = start.plusDays(1);
                }
            }
        } catch (SQLException e){
           e.printStackTrace();
        }
        return dates;
    }

    public Integer getIDEvenementParDate(LocalDate date){
        try{
            List<Evenement> evenements = EvenementDAO.getAllEvenements();

            for(Evenement ev : evenements) {
                LocalDate debut = ((java.sql.Date) ev.getDateDebut()).toLocalDate();
                LocalDate fin = ((java.sql.Date) ev.getDateFin()).toLocalDate();


                if((date.isEqual(debut) || date.isAfter(debut)) && (date.isEqual(fin) || date.isBefore(fin))) {
                    /// debug du if System.out.println(date + " evenement entre "+debut + "et"+ fin + " [id:] " + ev.getIdEvenement());
                   return ev.getIdEvenement();
               }
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
    return null;
    }

}
