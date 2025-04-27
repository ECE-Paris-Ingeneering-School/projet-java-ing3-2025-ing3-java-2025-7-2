package vue;

import controleur.ControleurInscription;
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

import java.sql.Date;

/**
 * VueInscription est la vue permettant aux utilisateurs
 * de créer un nouveau compte client dans l'application.
 */
public class VueInscription {

    private final ControleurInscription controleur = new ControleurInscription();

    /**
     * Affiche la fenêtre d'inscription.
     *
     * @param stage La scène principale JavaFX
     */
    public void afficher(Stage stage) {

        Image logoImage = new Image(getClass().getResource("/images/logo_JP.png").toExternalForm());
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(60);
        logoView.setPreserveRatio(true);
        HBox logoBox = new HBox(logoView);
        logoBox.setAlignment(Pos.TOP_LEFT);
        logoBox.setPadding(new Insets(10, 0, 0, 10));

        Label titre = new Label("Créer un compte");
        titre.setFont(Font.font("Arial", 22));
        titre.setTextFill(Color.web("#2c3e50"));
        titre.setPadding(new Insets(0, 0, 12, 0));

        double FIELD_MAX_WIDTH = 180;

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        styliserChamp(nomField, FIELD_MAX_WIDTH);

        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");
        styliserChamp(prenomField, FIELD_MAX_WIDTH);

        TextField emailField = new TextField();
        emailField.setPromptText("Adresse email");
        styliserChamp(emailField, FIELD_MAX_WIDTH);

        PasswordField mdpField = new PasswordField();
        mdpField.setPromptText("Mot de passe");
        styliserChamp(mdpField, FIELD_MAX_WIDTH);

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Date de naissance");
        datePicker.setStyle(
                "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-padding: 8;" +
                        "-fx-border-color: #bdc3c7;" +
                        "-fx-border-width: 1px;" +
                        "-fx-background-color: #ff8f8f;"
        );
        datePicker.setMaxWidth(FIELD_MAX_WIDTH);

        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", 13));
        messageLabel.setStyle("-fx-text-fill: red;");

        Button creerCompteButton = new Button("Créer le compte");
        creerCompteButton.setStyle(
                "-fx-background-color: #2ecc71;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-font-size: 15px;" +
                        "-fx-padding: 8 20 8 20;"
        );

        creerCompteButton.setOnAction(event -> {
            if (nomField.getText().isEmpty() || prenomField.getText().isEmpty()
                    || emailField.getText().isEmpty() || mdpField.getText().isEmpty()
                    || datePicker.getValue() == null) {
                messageLabel.setText("Veuillez remplir tous les champs.");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            Date dateNaissance = Date.valueOf(datePicker.getValue());

            boolean success = controleur.inscrire(
                    nomField.getText(),
                    prenomField.getText(),
                    emailField.getText(),
                    mdpField.getText(),
                    dateNaissance
            );

            if (success) {
                messageLabel.setText("Inscription réussie !");
                messageLabel.setStyle("-fx-text-fill: green;");
            } else {
                messageLabel.setText("Erreur lors de l'inscription.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        Button retourButton = new Button("Retour");
        retourButton.setStyle(
                "-fx-background-color: #e74c3c;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 6 18 6 18;"
        );
        retourButton.setOnAction(event -> {
            vueConnexion vueConnexion = new vueConnexion();
            vueConnexion.start(stage);
        });

        VBox contenu = new VBox(9,
                titre, nomField, prenomField, emailField, mdpField, datePicker, creerCompteButton, messageLabel, retourButton
        );
        contenu.setAlignment(Pos.CENTER);
        contenu.setPadding(new Insets(10, 0, 10, 0));
        contenu.setMaxHeight(410);

        VBox centerBox = new VBox(logoBox, contenu);
        VBox.setMargin(logoBox, new Insets(0, 0, 4, 0));
        centerBox.setAlignment(Pos.TOP_CENTER);

        //Barre de navigation
        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER);
        navBar.setPadding(new Insets(15));
        navBar.setStyle("-fx-background-color: yellow;");
        navBar.getChildren().addAll(
                creerBoutonNavigation("🏠"),
                creerBoutonNavigation("📅"),
                creerBoutonNavigation("🛒"),
                creerBoutonNavigation("👤")
        );

        BorderPane root = new BorderPane();
        root.setCenter(centerBox);
        root.setBottom(navBar);
        root.setStyle("-fx-background-color: #d0f5c8;");

        Scene scene = new Scene(root, 350, 550);
        stage.setScene(scene);
        stage.setTitle("Inscription");
        stage.show();
    }

    /**
     * Applique un style personnalisé à un champ de texte.
     *
     * @param champ Le champ à styliser
     * @param width La largeur maximale
     */
    private void styliserChamp(TextField champ, double width) {
        champ.setStyle(
                "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-padding: 7;" +
                        "-fx-border-color: #bdc3c7;" +
                        "-fx-border-width: 1px;" +
                        "-fx-background-color: #ff8f8f;"
        );
        champ.setFont(Font.font("Arial", 13));
        champ.setMaxWidth(width);
    }

    /**
     * Crée un bouton de navigation avec un emoji.
     *
     * @param emoji Le symbole emoji du bouton
     * @return Le bouton créé
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
}
