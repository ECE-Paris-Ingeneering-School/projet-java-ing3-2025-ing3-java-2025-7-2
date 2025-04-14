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

public class ConnexionApp extends Application {

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

        loginButton.setOnAction(event -> {
            String mail = emailField.getText();
            String motDePasse = passwordField.getText();

            AuthentificationService auth = new AuthentificationService();

            try (Connection conn = ConnexionBDD.getConnexion()) {
                Utilisateur utilisateur = auth.connecter(mail, motDePasse, conn);

                if (utilisateur != null) {
                    messageLabel.setText("Connexion réussie !");
                    messageLabel.setTextFill(Color.GREEN);
                    VueUtilisateur.afficherInfos(utilisateur);
                } else {
                    messageLabel.setText("Identifiants incorrects.");
                    messageLabel.setTextFill(Color.RED);
                }
            } catch (Exception e) {
                messageLabel.setText("Erreur de connexion.");
                messageLabel.setTextFill(Color.RED);
                e.printStackTrace();
            }
        });

        // ===== Layout principal =====
        VBox root = new VBox(15, logoBox, titreLabel, emailField, passwordField, loginButton, messageLabel);
        root.setAlignment(Pos.TOP_CENTER); // pour que tout sauf logo soit centré
        root.setPadding(new Insets(30));
        root.setPrefSize(350, 550);
        root.setStyle("-fx-background-color: #ecf0f1;");

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void styliserChamp(TextField champ) {
        champ.setStyle(
                "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-padding: 8;" +
                        "-fx-border-color: #bdc3c7;" +
                        "-fx-border-width: 1px;"
        );
        champ.setFont(Font.font("Arial", 14));
        champ.setMaxWidth(250);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
