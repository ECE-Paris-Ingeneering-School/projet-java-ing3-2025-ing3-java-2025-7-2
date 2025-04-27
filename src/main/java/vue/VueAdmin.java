package vue;

import controleur.ControleurFactures;
import controleur.ControleurReporting;
import controleur.ControleurUtilisateur;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import modele.Utilisateur;
import modele.dao.AttractionDAO;
import modele.dao.ConnexionBDD;

import java.io.IOException;
import java.sql.SQLException;

public class VueAdmin {

    public static void afficher(Stage stage, Utilisateur utilisateur) {
        stage.setTitle("Espace Administrateur");

        ControleurUtilisateur controller = new ControleurUtilisateur(utilisateur);

        // ===== Logo =====
        Image logoImage = new Image(VueAdmin.class.getResource("/images/logo_JP.png").toExternalForm());
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(80);
        logoView.setPreserveRatio(true);
        HBox logoBox = new HBox(logoView);
        logoBox.setAlignment(Pos.TOP_LEFT);
        logoBox.setPadding(new Insets(10, 0, 0, 10));

        // ===== Contenu principal =====
        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20));

        Label titre = new Label("Bienvenue, Admin " + controller.getNomComplet());
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label emailLabel = new Label("Email : " + controller.getEmail());
        Label roleLabel = new Label("Rôle : " + controller.getRole());

        emailLabel.setStyle("-fx-font-size: 14px;");
        roleLabel.setStyle("-fx-font-size: 14px;");

        /// ____________modifs dispo aux admins :
        VBox menuAdmin = new VBox(10);
        menuAdmin.setAlignment(Pos.CENTER);

        Button btnAttractions = new Button("🎢 Attractions");
        Button btnClients = new Button("👥 Clients");
        Button btnEvenements = new Button("🎉 Événements");
        Button btnFactures = new Button("÷ Factures");
        Button btnReservations = new Button("📆 Réservations");
        Button btnReporting = new Button("\uD83D\uDCF6 Statistiques");


        for (Button btn : new Button[]{btnAttractions,btnClients, btnEvenements, btnFactures, btnReservations,btnReporting}) {
            btn.setPrefWidth(200);
            btn.setStyle("-fx-font-size: 14px; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 10;");
        }

        menuAdmin.getChildren().addAll(btnAttractions, btnClients, btnEvenements, btnFactures, btnReservations,btnReporting);

        contentBox.getChildren().addAll(titre, emailLabel, roleLabel, menuAdmin);

       btnAttractions.setOnAction(e -> {
           try {    /// bizarre que la connection me force du throw ou try catch => à vérif
                    /// ==> On a un try catch dans ClientDAO pcq une seule méthode,
                    /// on n'a pas mis de try catch dans AttractionsDAO pcq plusieurs méthodes,
                    /// et donc, ca a l'air de se repercuter dans le controleur et la vue
               VueAdminAttractions.afficher(new Stage(), utilisateur);
           } catch (SQLException ex) {
               throw new RuntimeException(ex);
           } catch (IOException ex) {
               throw new RuntimeException(ex);
           } catch (ClassNotFoundException ex) {
               throw new RuntimeException(ex);
           }
           //stage.close();
           /// si on close pas, ouverture nouvelle fenetre, pas necessaire de rajouter de la navigation
        });

        btnClients.setOnAction(e -> {
            VueAdminClients.afficher(new Stage(), utilisateur);     ///pas de catch ici, code AdminAttraction à revoir dans le controleur et la vue
        });

        btnReporting.setOnAction(e -> {
            AttractionDAO attractionDAO = null;
            try {
                attractionDAO = new AttractionDAO(ConnexionBDD.getConnexion());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            ControleurReporting controleurReporting = new ControleurReporting(attractionDAO);
            VueReporting vueReporting = new VueReporting(controleurReporting);
            vueReporting.afficher(new Stage(), utilisateur);

        });
        btnEvenements.setOnAction(e -> {
            try {
                VueAdminEvenements.afficher(new Stage(), utilisateur);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }     ///pas de catch ici, code AdminAttraction à revoir dans le controleur et la vue
        });

        VBox mainLayout = new VBox(logoBox, contentBox);
        mainLayout.setStyle("-fx-background-color: #d0f5c8;");


        // ===== Barre de navigation en bas =====
        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER);
        navBar.setPadding(new Insets(15));
        navBar.setStyle("-fx-background-color: yellow;");

        Button btnHome = creerBoutonNavigation("🏠");
        Button btnCalendar = creerBoutonNavigation("📅");
        Button btnCart = creerBoutonNavigation("🛒");
        Button btnUser = creerBoutonNavigation("👤");

        btnCalendar.setOnAction(e -> {
            VueCalendrier vueCal = new VueCalendrier(utilisateur);
            vueCal.afficher(new Stage());
            stage.close();
        });
        btnCart.setOnAction(e ->{   /// Un peu inutile vu que l'admin n'a ni reservations ni factures mais bon
            try {
                ControleurFactures controleurFactures = new ControleurFactures(ConnexionBDD.getConnexion()); // adapte si c’est déjà instancié ailleurs
                new VueFactures(controleurFactures, utilisateur); // ouvre la vue des factures
                ///stage.close();
                /// ==> Fenêtre sans barre de navigation donc on la laisse en popup jusqu'à implémentation

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });

        navBar.getChildren().addAll(btnHome, btnCalendar, btnCart, btnUser);

        BorderPane root = new BorderPane();
        root.setCenter(mainLayout);
        root.setBottom(navBar);

        Scene scene = new Scene(root, 350, 600);
        stage.setScene(scene);
        stage.show();
    }

    private static Button creerBoutonNavigation(String emoji) {
        Button btn = new Button(emoji);
        btn.setStyle(
                "-fx-background-color: black;" +
                        "-fx-text-fill: yellow;" +
                        "-fx-font-size: 18px;" +
                        "-fx-background-radius: 10;" +
                        "-fx-min-width: 60px;" +
                        "-fx-min-height: 60px;" +
                        "-fx-padding: 10;"
        );
        return btn;
    }
}
