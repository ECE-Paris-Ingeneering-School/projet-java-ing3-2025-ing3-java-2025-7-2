package vue;

import controleur.ControleurAdminClients;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import modele.Client;
import modele.Utilisateur;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class VueAdminClients {

    public static void afficher(Stage stage, Utilisateur utilisateur) {
        stage.setTitle("Gestion des Clients");

        ControleurAdminClients controleur = new ControleurAdminClients();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #d0f5c8;");

        VBox contenu = new VBox(10);
        contenu.setPadding(new Insets(10));
        Label titre = new Label("Liste des Clients");
        titre.setFont(Font.font("Arial", 22));
        titre.setStyle("-fx-text-fill: #2c3e50;");
        contenu.getChildren().add(titre);

        VBox listClients = new VBox(10);
        listClients.setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane(listClients);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #d0f5c8;");
        contenu.getChildren().add(scrollPane);

        Button btnAjouter = new Button("➕ Ajouter un client");
        btnAjouter.setStyle(
                "-fx-background-color: #2ecc71;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 8 20 8 20;"
        );
        btnAjouter.setOnAction(e -> afficherPopupAjout(controleur, () -> rafraichirListeClients(listClients, controleur)));
        contenu.getChildren().add(btnAjouter);

        rafraichirListeClients(listClients, controleur);

        root.setCenter(contenu);

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
        root.setBottom(navBar);

        Scene scene = new Scene(root, 350, 550); // largeur type mobile
        stage.setScene(scene);
        stage.show();
    }

    private static void rafraichirListeClients(VBox container, ControleurAdminClients controleur) {
        container.getChildren().clear();
        List<Client> clients = controleur.getTousLesClients();

        for (Client c : clients) {
            HBox ligne = new HBox(10);
            ligne.setPadding(new Insets(5));
            ligne.setAlignment(Pos.CENTER_LEFT);
            ligne.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 5;");

            Label label = new Label(
                    c.getNom() + " " + c.getPrenom() + " - " + c.getMail() + " | Âge : " + c.getAge()
            );
            label.setFont(Font.font("Arial", 12));
            label.setTextFill(javafx.scene.paint.Color.BLACK);
            label.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(label, Priority.ALWAYS);

            Button btnModifier = new Button("🔧");
            btnModifier.setStyle("-fx-font-size: 18px; -fx-background-color: transparent;");
            btnModifier.setOnAction(e -> afficherPopupModification(controleur, c, () -> rafraichirListeClients(container, controleur)));

            Button btnSupprimer = new Button("❌");
            btnSupprimer.setStyle("-fx-font-size: 18px; -fx-background-color: transparent;");
            btnSupprimer.setOnAction(e -> afficherPopupSuppression(controleur, c, () -> rafraichirListeClients(container, controleur)));

            ligne.getChildren().addAll(label, btnModifier, btnSupprimer);
            container.getChildren().add(ligne);
        }
    }

    private static void afficherPopupAjout(ControleurAdminClients controleur, Runnable onSuccess) {
        try {
            Optional<String> mail = champ("Email :");
            Optional<String> mdp = champ("Mot de passe :");
            Optional<String> nom = champ("Nom :");
            Optional<String> prenom = champ("Prénom :");
            Optional<String> naissance = champ("Date de naissance (yyyy-MM-dd)");

            if (mail.isPresent() && mdp.isPresent() && nom.isPresent() && prenom.isPresent() && naissance.isPresent()) {
                Date date = Date.valueOf(naissance.get());
                controleur.ajouterClient(mail.get(), mdp.get(), nom.get(), prenom.get(), date);
                onSuccess.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
            alerte("Erreur lors de l'ajout du client.");
        }
    }

    private static void afficherPopupModification(ControleurAdminClients controleur, Client client, Runnable onSuccess) {
        try {
            Optional<String> mail = champ("Modifier email :", client.getMail());
            Optional<String> mdp = champ("Modifier mot de passe :", client.getMdp());
            Optional<String> nom = champ("Modifier nom :", client.getNom());
            Optional<String> prenom = champ("Modifier prénom :", client.getPrenom());
            Optional<String> naissance = champ("Modifier date de naissance (yyyy-MM-dd) :", client.getDatedeNaissance().toString());

            if (mail.isPresent() && mdp.isPresent() && nom.isPresent() && prenom.isPresent() && naissance.isPresent()) {
                Date date = Date.valueOf(naissance.get());
                controleur.modifierClient(client, mail.get(), mdp.get(), nom.get(), prenom.get(), date);
                onSuccess.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
            alerte("Erreur lors de la modification.");
        }
    }

    private static void afficherPopupSuppression(ControleurAdminClients controleur, Client client, Runnable onSuccess) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Supprimer un client");
        confirmation.setHeaderText("Confirmer la suppression de ce client ?");
        confirmation.setContentText(client.getNom() + " " + client.getPrenom() + " - " + client.getMail());

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                controleur.supprimerClient(client);
                onSuccess.run();
            } catch (Exception e) {
                e.printStackTrace();
                alerte("Erreur lors de la suppression.");
            }
        }
    }

    private static Optional<String> champ(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(message);
        return dialog.showAndWait();
    }

    private static Optional<String> champ(String message, String valeurInitiale) {
        TextInputDialog dialog = new TextInputDialog(valeurInitiale);
        dialog.setHeaderText(message);
        return dialog.showAndWait();
    }

    private static void alerte(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
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
