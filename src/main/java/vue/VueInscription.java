package vue;

import controleur.ControleurInscription;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Date;

public class VueInscription {

    private final ControleurInscription controleur = new ControleurInscription();

    public void afficher(Stage stage) {
        Label titre = new Label("Créer un compte");
        titre.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50;");

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");

        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");

        TextField emailField = new TextField();
        emailField.setPromptText("Adresse email");

        PasswordField mdpField = new PasswordField();
        mdpField.setPromptText("Mot de passe");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Date de naissance");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        Button creerCompteButton = new Button("Créer le compte");
        creerCompteButton.setStyle(
                "-fx-background-color: #2ecc71;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 8 20 8 20;"
        );

        creerCompteButton.setOnAction(event -> {
            // Vérifications simples
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
        retourButton.setOnAction(event -> {
            vueConnexion vueConnexion = new vueConnexion();
            vueConnexion.start(stage);
        });

        VBox root = new VBox(12, titre, nomField, prenomField, emailField, mdpField, datePicker, creerCompteButton, messageLabel, retourButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        Scene scene = new Scene(root, 350, 550);
        stage.setScene(scene);
        stage.setTitle("Inscription");
        stage.show();
    }
}
