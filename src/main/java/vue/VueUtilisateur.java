package vue;

import modele.Attraction;
import modele.Evenement;
import modele.Utilisateur;
import modele.dao.AttractionDAO;
import modele.dao.EvenementDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VueUtilisateur {

    private static Scanner scanner = new Scanner(System.in);

    // Cette méthode affiche les informations de l'utilisateur
    public static void afficherInfos(Utilisateur utilisateur, Connection conn) {
        System.out.println("\n=== Informations de l'Utilisateur ===");
        System.out.println("Nom : " + utilisateur.getNom());
        System.out.println("Prénom : " + utilisateur.getPrenom());
        System.out.println("Email : " + utilisateur.getEmail());
        System.out.println("Role : " + utilisateur.getRole());

        // Si l'utilisateur est un admin, afficher les options de gestion des attractions et événements
        if (utilisateur.getRole().equalsIgnoreCase("admin")) {
            afficherMenuAdmin(conn);
        }
    }

    // Afficher les attractions populaires
    public static void afficherAttractionsPopulaires(AttractionDAO attractionDAO) {
        Map<String, Integer> attractionsPopulaires;
        try {
            attractionsPopulaires = attractionDAO.getAttractionsLesPlusReservees();
            System.out.println("\n=== Attractions les plus populaires ===");
            attractionsPopulaires.forEach((nom, total) -> {
                System.out.println("Attraction: " + nom + " - Réservations: " + total);
            });
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des attractions populaires : " + e.getMessage());
        }
    }

    // Afficher les événements en cours
    public static void afficherEvenements(EvenementDAO evenementDAO) {
        try {
            List<String> evenements = evenementDAO.getEvenementsForDay(java.time.LocalDate.now());
            System.out.println("\n=== Événements en cours aujourd'hui ===");
            for (String evenement : evenements) {
                System.out.println(evenement);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }
    }

    // Afficher le menu de gestion admin
    private static void afficherMenuAdmin(Connection conn) {
        System.out.println("\n=== Menu Admin ===");
        System.out.println("1. Gérer les attractions");
        System.out.println("2. Gérer les événements");
        System.out.print("Choisissez une option : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne après le choix

        switch (choix) {
            case 1:
                gererAttractions(new AttractionDAO(conn));
                break;
            case 2:
                gererEvenements(new EvenementDAO(conn));
                break;
            default:
                System.out.println("Option invalide.");
        }
    }

    // Méthode pour gérer les attractions : ajouter, modifier, supprimer
    public static void gererAttractions(AttractionDAO attractionDAO) {
        System.out.println("\n=== Gestion des Attractions ===");
        System.out.println("1. Ajouter une nouvelle attraction");
        System.out.println("2. Modifier une attraction existante");
        System.out.println("3. Supprimer une attraction");
        System.out.print("Choisissez une option : ");

        int choix = scanner.nextInt();
        scanner.nextLine();  // Consommer la nouvelle ligne après le choix

        switch (choix) {
            case 1:
                ajouterAttraction(attractionDAO);
                break;
            case 2:
                modifierAttraction(attractionDAO);
                break;
            case 3:
                supprimerAttraction(attractionDAO);
                break;
            default:
                System.out.println("Option invalide.");
                break;
        }
    }

    // Ajouter une nouvelle attraction
    private static void ajouterAttraction(AttractionDAO attractionDAO) {
        System.out.print("Nom de l'attraction : ");
        String nom = scanner.nextLine();
        System.out.print("Prix de l'attraction : ");
        double prix = scanner.nextDouble();
        scanner.nextLine();  // Consommer la nouvelle ligne
        System.out.print("Image de l'attraction (URL) : ");
        String image = scanner.nextLine();

        try {
            attractionDAO.ajouterAttraction(nom, prix, image);
            System.out.println("Attraction ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'attraction : " + e.getMessage());
        }
    }

    // Modifier une attraction existante
    private static void modifierAttraction(AttractionDAO attractionDAO) {
        System.out.print("Entrez l'ID de l'attraction à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consommer la nouvelle ligne

        try {
            Attraction attraction = attractionDAO.getAttractionParNom(id);
            if (attraction != null) {
                System.out.print("Nouveau nom de l'attraction (actuel : " + attraction.getNom() + ") : ");
                String nom = scanner.nextLine();
                System.out.print("Nouveau prix de l'attraction (actuel : " + attraction.getPrix() + ") : ");
                double prix = scanner.nextDouble();
                scanner.nextLine();  // Consommer la nouvelle ligne
                System.out.print("Nouvelle image de l'attraction (actuelle : " + attraction.getImage() + ") : ");
                String image = scanner.nextLine();

                attractionDAO.modifierAttraction(id, nom, prix, image);
                System.out.println("Attraction modifiée avec succès !");
            } else {
                System.out.println("Attraction non trouvée.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'attraction : " + e.getMessage());
        }
    }

    // Supprimer une attraction
    private static void supprimerAttraction(AttractionDAO attractionDAO) {
        System.out.print("Entrez l'ID de l'attraction à supprimer : ");
        int id = scanner.nextInt();

        try {
            attractionDAO.supprimerAttraction(id);
            System.out.println("Attraction supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'attraction : " + e.getMessage());
        }
    }

    // Méthode pour gérer les événements : ajouter, modifier, supprimer
    public static void gererEvenements(EvenementDAO evenementDAO) {
        System.out.println("\n=== Gestion des Événements ===");
        System.out.println("1. Ajouter un nouvel événement");
        System.out.println("2. Modifier un événement existant");
        System.out.println("3. Supprimer un événement");
        System.out.print("Choisissez une option : ");

        int choix = scanner.nextInt();
        scanner.nextLine();  // Consommer la nouvelle ligne après le choix

        switch (choix) {
            case 1:
                ajouterEvenement(evenementDAO);
                break;
            case 2:
                modifierEvenement(evenementDAO);
                break;
            case 3:
                supprimerEvenement(evenementDAO);
                break;
            default:
                System.out.println("Option invalide.");
                break;
        }
    }

    // Ajouter un nouvel événement
    private static void ajouterEvenement(EvenementDAO evenementDAO) {
        System.out.print("Nom de l'événement : ");
        String nom = scanner.nextLine();
        System.out.print("Supplément de l'événement : ");
        double supplement = scanner.nextDouble();
        scanner.nextLine();  // Consommer la nouvelle ligne
        System.out.print("Date de début de l'événement (yyyy-mm-dd) : ");
        String dateDebut = scanner.nextLine();
        System.out.print("Date de fin de l'événement (yyyy-mm-dd) : ");
        String dateFin = scanner.nextLine();

        try {
            evenementDAO.ajouterEvenement(nom, supplement, Date.valueOf(dateDebut), Date.valueOf(dateFin));
            System.out.println("Événement ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'événement : " + e.getMessage());
        }
    }

    // Modifier un événement existant
    private static void modifierEvenement(EvenementDAO evenementDAO) {
        System.out.print("Entrez l'ID de l'événement à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consommer la nouvelle ligne

        try {
            Evenement evenement = evenementDAO.getEvenementParId(id);
            if (evenement != null) {
                System.out.print("Nouveau nom de l'événement (actuel : " + evenement.getNom() + ") : ");
                String nom = scanner.nextLine();
                System.out.print("Nouveau supplément de l'événement (actuel : " + evenement.getSupplement() + ") : ");
                double supplement = scanner.nextDouble();
                scanner.nextLine();  // Consommer la nouvelle ligne
                System.out.print("Nouvelle date de début de l'événement (actuelle : " + evenement.getDateDebut() + ") : ");
                String dateDebut = scanner.nextLine();
                System.out.print("Nouvelle date de fin de l'événement (actuelle : " + evenement.getDateFin() + ") : ");
                String dateFin = scanner.nextLine();

                evenementDAO.modifierEvenement(id, nom, supplement, Date.valueOf(dateDebut), Date.valueOf(dateFin));
                System.out.println("Événement modifié avec succès !");
            } else {
                System.out.println("Événement non trouvé.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'événement : " + e.getMessage());
        }
    }

    // Supprimer un événement
    private static void supprimerEvenement(EvenementDAO evenementDAO) {
        System.out.print("Entrez l'ID de l'événement à supprimer : ");
        int id = scanner.nextInt();

        try {
            evenementDAO.supprimerEvenement(id);
            System.out.println("Événement supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'événement : " + e.getMessage());
        }
    }
}
