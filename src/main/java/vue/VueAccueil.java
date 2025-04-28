package vue;

import controleur.ControleurFactures;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import modele.Utilisateur;
import modele.dao.ConnexionBDD;

public class VueAccueil {

    private final Utilisateur utilisateur;

    public VueAccueil(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void afficher(Stage stage) {
        // Top : logo à gauche, nom à droite
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10, 15, 10, 15));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #d0f5c8;");

        // Logo
        ImageView logoView = null;
        try {
            Image logoImage = new Image(getClass().getResource("/images/logo_JP.png").toExternalForm());
            logoView = new ImageView(logoImage);
            logoView.setFitHeight(50);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {
            // pas de logo si erreur
            logoView = new ImageView();
        }

        Label userLabel = new Label("Connecté : " + utilisateur.getPrenom() + " " + utilisateur.getNom());
        userLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #145a32;");
        HBox.setHgrow(userLabel, Priority.ALWAYS);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(logoView, spacer, userLabel);

        // Titre
        Label titre = new Label("Jurassic Park");
        titre.setFont(Font.font("Arial", 32));
        titre.setStyle("-fx-text-fill: #229954;");
        titre.setAlignment(Pos.CENTER);
        titre.setMaxWidth(Double.MAX_VALUE);

        // Boîte d'actualités
        Label actuTitre = new Label("Actualités :");
        actuTitre.setFont(Font.font("Arial", 17));
        actuTitre.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        TextArea actualites = new TextArea("Bienvenue au parc !\nProfitez des nouvelles attractions ce week-end !");
        actualites.setWrapText(true);
        actualites.setPrefRowCount(4);
        actualites.setEditable(false);
        actualites.setStyle("-fx-background-radius: 12; -fx-font-size: 14px;");

        VBox actuBox = new VBox(6, actuTitre, actualites);
        actuBox.setAlignment(Pos.CENTER_LEFT);
        actuBox.setPadding(new Insets(12, 0, 12, 0));
        actuBox.setStyle("-fx-background-color: #fff4e5; -fx-background-radius: 16;");

        // Bouton Attractions
        Button btnAttractions = new Button("Voir les attractions");
        btnAttractions.setStyle(
                "-fx-background-color: #2ecc71;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 17px;" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 10 28 10 28;"
        );

        btnAttractions.setOnAction(e -> {
            System.out.println("Voir les attractions");
            // Ouvre la page Attractions
            try {
                VueListeAttractions.afficher(new Stage(), utilisateur);
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox centerBox = new VBox(18, titre, actuBox, btnAttractions);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(25, 0, 25, 0));

        // Barre de navigation mobile en bas
        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER);
        navBar.setPadding(new Insets(15));
        navBar.setStyle("-fx-background-color: yellow;");

        Button btnHome = creerBoutonNavigation("🏠");
        Button btnCalendar = creerBoutonNavigation("📅");
        Button btnCart = creerBoutonNavigation("🛒");
        Button btnUser = creerBoutonNavigation("👤");
        navBar.getChildren().addAll(btnHome, btnCalendar, btnCart, btnUser);

        btnCalendar.setOnAction(e -> {
            VueCalendrier vueCal = new VueCalendrier(utilisateur);
            vueCal.afficher(new Stage());
            stage.close();
        });

        btnCart.setOnAction(e -> {
            try {
                ControleurFactures controleurFactures = new ControleurFactures(ConnexionBDD.getConnexion());
                new VueFactures(controleurFactures, utilisateur);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //parcours icone du bas
        btnUser.setOnAction(e -> {
            try {
                if ("client".equalsIgnoreCase(utilisateur.getRole())) {
                    VueClient.afficher(new Stage(), utilisateur);
                }
                else if ("admin".equalsIgnoreCase(utilisateur.getRole())) {
                    VueAdmin.afficher(new Stage(), utilisateur);
                }
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // BorderPane principal
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #d0f5c8;");
        root.setTop(topBar);
        root.setCenter(centerBox);
        root.setBottom(navBar);

        Scene scene = new Scene(root, 350, 600);
        stage.setScene(scene);
        stage.setTitle("Accueil - Jurassic Park");
        stage.show();
    }

    private Button creerBoutonNavigation(String emoji) {
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
