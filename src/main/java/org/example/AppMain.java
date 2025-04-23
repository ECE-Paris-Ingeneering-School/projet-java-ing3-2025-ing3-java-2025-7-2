package org.example;

import vue.vueConnexion;
import javafx.application.Application;

import modele.Admin;
import modele.Utilisateur;
import modele.Attraction;
import modele.AuthentificationService;
import java.util.Scanner;

public class AppMain {
    public static void main(String[] args) {


        /// AFFICHAGE CONSOLE LISTES CLIENTS ET ADMINS (pr tester)
        /**modele.dao.ClientDAO clientDAO = new modele.dao.ClientDAO();
        java.util.List<modele.Client> clients = clientDAO.getAllClients();

        System.out.println("Affichage console de la liste des clients :");
        for (modele.Client c : clients) {
            System.out.println(c);
        }


        modele.dao.AdminDAO adminDAO = new modele.dao.AdminDAO();
        java.util.List<modele.Admin> admins = adminDAO.getAllAdmins();
        System.out.println("Affichage console de la liste des admins :");
        for (modele.Admin a : admins) {
            System.out.println(a);
        }**/


        Application.launch(vueConnexion.class, args);  // Lancement JavaFX propre

    }
}

/*
lola.coignard@gmail.com
triceratops

pacome.golvet@edu.ece.fr
Linette
 */