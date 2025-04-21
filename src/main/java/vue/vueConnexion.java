package vue;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import modele.Utilisateur;
import controleur.AuthentificationService;
import modele.dao.ConnexionBDD;

import java.sql.Connection;


import java.util.*;
import modele.Attraction;
import modele.dao.AttractionDAO;
import modele.Admin;    ///POUR LES TESTS, à retirer

public class vueConnexion extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Connexion");

        // ===== Logo en haut à gauche =====
        Image logoImage = new Image(getClass().getResource("/images/logo_JP.png").toExternalForm());
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(80);
        logoView.setPreserveRatio(true);
        HBox logoBox = new HBox(logoView);
        logoBox.setAlignment(Pos.TOP_LEFT);
        logoBox.setPadding(new Insets(10, 0, 0, 10));

        // ===== Titre =====
        Label titreLabel = new Label("Bienvenue !");
        titreLabel.setFont(Font.font("Arial", 26));
        titreLabel.setTextFill(Color.web("#2c3e50"));

        // ===== Champs de connexion =====
        TextField emailField = new TextField();
        emailField.setPromptText("Adresse mail");
        styliserChamp(emailField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        styliserChamp(passwordField);

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.RED);

        Button loginButton = new Button("Se connecter");
        loginButton.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 8 20 8 20;"
        );

        loginButton.setOnAction(event -> connecter(emailField, passwordField, messageLabel, primaryStage));

        // ===== Ajout du gestionnaire d'événement pour "Entrée" =====
        emailField.setOnAction(event -> connecter(emailField, passwordField, messageLabel, primaryStage));
        passwordField.setOnAction(event -> connecter(emailField, passwordField, messageLabel, primaryStage));

        // ===== Navigation style mobile =====
        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER);
        navBar.setPadding(new Insets(15));
        navBar.setStyle("-fx-background-color: yellow;");

        Button btnHome = creerBoutonNavigation("🏠");
        Button btnCalendar = creerBoutonNavigation("📅");
        Button btnCart = creerBoutonNavigation("🛒");
        Button btnUser = creerBoutonNavigation("👤");
        navBar.getChildren().addAll(btnHome, btnCalendar, btnCart, btnUser);

        // ===== Contenu central =====
        VBox contenu = new VBox(15, titreLabel, emailField, passwordField, loginButton, messageLabel);
        contenu.setAlignment(Pos.CENTER);
        contenu.setPadding(new Insets(10));

        VBox centerBox = new VBox(logoBox, contenu);
        VBox.setMargin(logoBox, new Insets(0, 0, 10, 0));
        centerBox.setAlignment(Pos.TOP_CENTER);

        // ===== Layout principal avec navigation en bas =====
        BorderPane root = new BorderPane();
        root.setCenter(centerBox);
        root.setBottom(navBar);
        root.setStyle("-fx-background-color: #d0f5c8;"); // fond vert clair Jurassic Park

        Scene scene = new Scene(root, 350, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void styliserChamp(TextField champ) {
        champ.setStyle(
                "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-padding: 8;" +
                        "-fx-border-color: #bdc3c7;" +
                        "-fx-border-width: 1px;" +
                        "-fx-background-color: #ff8f8f;" // rose inspiré de l'image JP
        );
        champ.setFont(Font.font("Arial", 14));
        champ.setMaxWidth(250);
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

    private void connecter(TextField emailField, PasswordField passwordField, Label messageLabel, Stage primaryStage) {
        String mail = emailField.getText();
        String motDePasse = passwordField.getText();

        AuthentificationService auth = new AuthentificationService();

        try (Connection conn = ConnexionBDD.getConnexion()) {
            Utilisateur utilisateur = auth.connecter(mail, motDePasse, conn);

            if (utilisateur != null) {
                /// CONDITION SUIVANTE POUR TESTS, à retirer
                if ("admin".equals(utilisateur.getRole())) {

                    //int id, String mail, String mdp, String nom, String prenom
                    Admin admin = new Admin(utilisateur.getId(),utilisateur.getEmail(),"",utilisateur.getNom(), utilisateur.getPrenom());

                    Scanner scanner = new Scanner(System.in);

                    AttractionDAO dao = new AttractionDAO(conn);
                    List<Attraction> attractions = dao.getAllAttractions();  // à adapter selon ton DAO réel

                    System.out.println("=== Liste des attractions ===");
                    for (Attraction a : attractions) {
                        System.out.println("ID: " + a.getIdAttraction() + " | Nom: " + a.getNom() + " | Prix: " + a.getPrix());
                    }

                    System.out.print("\nEntrez l’ID de l’attraction à modifier : ");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Nouveau nom : ");
                    String nom = scanner.nextLine();

                    System.out.print("Nouveau prix : ");
                    double prix = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Nouvelle image (chemin ou URL) : ");
                    String image = scanner.nextLine();

                    Attraction a = new Attraction(id, nom, prix, image);
                    admin.modifierAttraction(a);


                    System.out.println("admin !!!");
                }
                messageLabel.setText("Connexion réussie !");
                messageLabel.setTextFill(Color.GREEN);

                // Changer de scène vers le calendrier JavaFX
                VueCalendrier vueCalendrier = new VueCalendrier(utilisateur);
                vueCalendrier.afficher(primaryStage);

            } else {
                messageLabel.setText("Identifiants incorrects.");
                messageLabel.setTextFill(Color.RED);
            }
        } catch (Exception e) {
            messageLabel.setText("Erreur de connexion.");
            messageLabel.setTextFill(Color.RED);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        launch(args);
    }
}
