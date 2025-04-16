package modele;

import java.util.*;

public class modele {
    private ArrayList<Admin> admins = new ArrayList<>();

    public modele(ArrayList<Admin> admins){
    }
    ///  à supprimer ==> ajout admins, clients etc dans bdd via requete sql puis mise à jour des tableaux de classe via recuperations infos bdd
    public void ajouterAdmin(Admin admin) {
        admins.add(admin);
    }

    /// DANGEREUX CAR RETOURNE LE MDP aussi.....
    public ArrayList<Admin> getAdmins() {
        return admins;
    }
}
