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

import controleur.ControleurConnexion;
import modele.Utilisateur;

/**
 * vueConnexion est la fenêtre d'accueil permettant
 * à l'utilisateur de se connecter ou de s'inscrire.
 */
public class vueConnexion extends Application {

    private ControleurConnexion controleur;

    /**
     * Point d'entrée de l'application : affiche l'écran de connexion.
     *
     * @param primaryStage La fenêtre principale JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Connexion");

        controleur = new ControleurConnexion();

        
        Image logoImage = new Image(getClass().getResource("/images/logo_JP.png").toExternalForm());
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(80);
        logoView.setPreserveRatio(true);
        HBox logoBox = new HBox(logoView);
        logoBox.setAlignment(Pos.TOP_LEFT);
        logoBox.setPadding(new Insets(10, 0, 0, 10));

       
        Label titreLabel = new Label("Bienvenue !");
        titreLabel.setFont(Font.font("Arial", 26));
        titreLabel.setTextFill(Color.web("#2c3e50"));

      
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
        Button signupButton = new Button("S'inscrire");
        signupButton.setStyle(
                "-fx-background-color: #2ecc71;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 8 20 8 20;"
        );

        loginButton.setOnAction(event -> gererConnexion(emailField.getText(), passwordField.getText(), primaryStage, messageLabel));
        emailField.setOnAction(event -> gererConnexion(emailField.getText(), passwordField.getText(), primaryStage, messageLabel));
        passwordField.setOnAction(event -> gererConnexion(emailField.getText(), passwordField.getText(), primaryStage, messageLabel));
        signupButton.setOnAction(event -> afficherInscription(primaryStage));

        
        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER);
        navBar.setPadding(new Insets(15));
        navBar.setStyle("-fx-background-color: yellow;");

        Button btnHome = creerBoutonNavigation("🏠");
        Button btnCalendar = creerBoutonNavigation("📅");
        Button btnCart = creerBoutonNavigation("🛒");
        Button btnUser = creerBoutonNavigation("👤");
        navBar.getChildren().addAll(btnHome, btnCalendar, btnCart, btnUser);

        
        VBox contenu = new VBox(15, titreLabel, emailField, passwordField, loginButton, signupButton, messageLabel);
        contenu.setAlignment(Pos.CENTER);
        contenu.setPadding(new Insets(10));

        VBox centerBox = new VBox(logoBox, contenu);
        VBox.setMargin(logoBox, new Insets(0, 0, 10, 0));
        centerBox.setAlignment(Pos.TOP_CENTER);

        
        BorderPane root = new BorderPane();
        root.setCenter(centerBox);
        root.setBottom(navBar);
        root.setStyle("-fx-background-color: #d0f5c8;");

        Scene scene = new Scene(root, 350, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Applique un style prédéfini à un champ de texte.
     *
     * @param champ Le champ de texte à styliser
     */
    private void styliserChamp(TextField champ) {
        champ.setStyle(
                "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-padding: 8;" +
                        "-fx-border-color: #bdc3c7;" +
                        "-fx-border-width: 1px;" +
                        "-fx-background-color: #ff8f8f;"
        );
        champ.setFont(Font.font("Arial", 14));
        champ.setMaxWidth(250);
    }

    /**
     * Crée un bouton de navigation avec un emoji.
     *
     * @param emoji Emoji à afficher
     * @return Bouton stylisé
     */
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

    /**
     * Méthode principale pour lancer l'application JavaFX.
     *
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Gère la tentative de connexion d'un utilisateur.
     *
     * @param email Email entré
     * @param mdp Mot de passe entré
     * @param stage Stage principal
     * @param messageLabel Label où afficher les erreurs
     */
    private void gererConnexion(String email, String mdp, Stage stage, Label messageLabel) {
        Utilisateur utilisateur = controleur.connecter(email, mdp);

        if (utilisateur != null) {
            messageLabel.setText("Connexion réussie !");
            messageLabel.setStyle("-fx-text-fill: green;");
            VueCalendrier vueCalendrier = new VueCalendrier(utilisateur);
            vueCalendrier.afficher(stage);
        } else {
            messageLabel.setText("Identifiants incorrects");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    /**
     * Ouvre la fenêtre d'inscription pour créer un compte.
     *
     * @param primaryStage Fenêtre principale
     */
    private void afficherInscription(Stage primaryStage) {
        VueInscription vueInscription = new VueInscription();
        vueInscription.afficher(primaryStage);
    }
}
