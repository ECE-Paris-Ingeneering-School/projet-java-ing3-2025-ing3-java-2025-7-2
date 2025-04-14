package vue;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modele.Utilisateur;
import controlleur.AuthentificationService;
import modele.dao.ConnexionBDD;

import java.sql.Connection;

public class ConnexionApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Connexion");

        // Création des champs
        TextField emailField = new TextField();
        emailField.setPromptText("Adresse mail");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");

        Label messageLabel = new Label();

        Button loginButton = new Button("Se connecter");

        loginButton.setOnAction(event -> {
            String mail = emailField.getText();
            String motDePasse = passwordField.getText();

            AuthentificationService auth = new AuthentificationService();

            try (Connection conn = ConnexionBDD.getConnexion()) {
                Utilisateur utilisateur = auth.connecter(mail, motDePasse, conn);

                if (utilisateur != null) {
                    messageLabel.setText("Connexion réussie !");
                    VueUtilisateur.afficherInfos(utilisateur); // reste en console pour l'instant
                } else {
                    messageLabel.setText("Identifiants incorrects.");
                }
            } catch (Exception e) {
                messageLabel.setText("Erreur de connexion.");
                e.printStackTrace();
            }
        });

        // Mise en page verticale façon mobile
        VBox root = new VBox(10, emailField, passwordField, loginButton, messageLabel);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setPrefSize(350, 550);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode principale pour lancer l'application
    public static void main(String[] args) {
        launch(args);
    }
}
