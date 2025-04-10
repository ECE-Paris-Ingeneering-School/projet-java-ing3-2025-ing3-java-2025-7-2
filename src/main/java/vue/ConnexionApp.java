package vue;

import modele.Utilisateur;
import controlleur.AuthentificationService;
import dao.ConnexionBDD;

import java.sql.Connection;
import java.util.Scanner;

public class ConnexionApp {

    public static void lancer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Connexion ===");
        System.out.print("Mail : ");
        String mail = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();

        AuthentificationService auth = new AuthentificationService();

        // Essayer de se connecter à la base de données
        try (Connection conn = ConnexionBDD.getConnexion()) {  // Connexion à la base de données
            Utilisateur utilisateur = auth.connecter(mail, motDePasse, conn);

            // Vérification du résultat
            if (utilisateur != null) {
                // Connexion réussie, afficher les infos de l'utilisateur
                VueUtilisateur.afficherInfos(utilisateur);
            } else {
                // Si les identifiants sont incorrects
                System.out.println("Identifiants icorrects");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
