package Controleur;

import Modele.Admin;
import Modele.modele;
import java.util.*;

public class controleur {

    public controleur(){
    }
/*
    public void lancerProgTest(){

        System.out.println("Lancer prog test");

        int choix=0;
        Scanner sc = new Scanner(System.in);
        while(stop==0){
            System.out.println("        ~~MENU TEST~~");
            System.out.println("1. QUITTER");
            System.out.println("2. Nouvel Admin");
            System.out.println("3. Afficher Admins");
            System.out.println("==>");
            choix=sc.nextInt();
            if(choix==1){
                stop=1;
                System.out.println("Fin?");
            }
            if(choix==2){
                System.out.println("Saisir nom");
                String nom = sc.nextLine();
                System.out.println("Saisir prenom");
                String prenom = sc.nextLine();
                System.out.println("Saisir email");
                String email = sc.nextLine();
                System.out.println("Saisir MDP :");
                String mdp = sc.nextLine();

                Admin a = new Admin(email,mdp,nom,prenom);
                m.ajouterAdmin(a);
            }
            if(choix==3){
                System.out.println("Affichage admin : ");
                ArrayList<Admin> liste = m.getAdmins();
                for (Admin admin : liste) {
                    System.out.println("Nom : " + admin.getNom());
                    System.out.println("Prénom : " + admin.getPrenom());
                    System.out.println("Email : " + admin.getMail());
                    System.out.println("----------------------");
                }
            }
        }

        System.out.println("FIN");

    }

 */
}
