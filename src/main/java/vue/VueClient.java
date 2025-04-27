package vue;

import controleur.ControleurFactures;
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
import modele.dao.ConnexionBDD;

public class VueClient {

    public static void afficher(Stage stage, Utilisateur utilisateur) {
        stage.setTitle("Mon Profil Client");

        ControleurUtilisateur controller = new ControleurUtilisateur(utilisateur);

        // ===== Logo =====
        Image logoImage = new Image(VueClient.class.getResource("/images/logo_JP.png").toExternalForm());
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(80);
        logoView.setPreserveRatio(true);
        HBox logoBox = new HBox(logoView);
        logoBox.setAlignment(Pos.TOP_LEFT);
        logoBox.setPadding(new Insets(10, 0, 0, 10));

        // ===== Profil Client =====
        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20));

        Label titre = new Label("Bienvenue, " + controller.getNomComplet() + " !");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label emailLabel = new Label("Email : " + controller.getEmail());
        Label roleLabel = new Label("Rôle : " + controller.getRole());

        emailLabel.setStyle("-fx-font-size: 14px;");
        roleLabel.setStyle("-fx-font-size: 14px;");

        contentBox.getChildren().addAll(titre, emailLabel, roleLabel);

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
        btnCart.setOnAction(e ->{
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
