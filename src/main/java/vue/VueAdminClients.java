package vue;

import controleur.ControleurAdminClients;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label titre = new Label("Liste des Clients");
        titre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        root.getChildren().add(titre);

        VBox listClients = new VBox(10);
        ScrollPane scrollPane = new ScrollPane(listClients);
        scrollPane.setFitToWidth(true);

        Button btnAjouter = new Button("➕ Ajouter un client");
        btnAjouter.setOnAction(e -> afficherPopupAjout(controleur, () -> rafraichirListeClients(listClients, controleur)));

        rafraichirListeClients(listClients, controleur);

        root.getChildren().addAll(scrollPane, btnAjouter);

        Scene scene = new Scene(root, 500, 600);
        stage.setScene(scene);
        stage.show();
    }

    private static void rafraichirListeClients(VBox container, ControleurAdminClients controleur) {
        container.getChildren().clear();
        List<Client> clients = controleur.getTousLesClients();

        for (Client c : clients) {
            HBox ligne = new HBox(10);
            ligne.setPadding(new Insets(5));
            ligne.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 5;");

            Label label = new Label(c.toString() + " | Âge : " + c.getAge());
            label.setPrefWidth(300);

            Button btnModifier = new Button("\uD83D\uDD27");
            btnModifier.setOnAction(e -> afficherPopupModification(controleur, c, () -> rafraichirListeClients(container, controleur)));

            Button btnSupprimer = new Button("❌");
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
        confirmation.setContentText(client.toString());

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
}
