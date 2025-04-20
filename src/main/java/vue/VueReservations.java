package vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import modele.Utilisateur;
import modele.dao.ConnexionBDD;
import modele.dao.ReservationDAO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class VueReservations {

    private final Utilisateur utilisateur;
    private VBox contentBox;

    public VueReservations(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    // Crée un bouton de nav identique à vueConnexion
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

    private boolean showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setHeaderText(title);
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }

    private void afficherFacture(int idFacture) {
        contentBox.getChildren().clear();

        try {
            ReservationDAO dao = new ReservationDAO(ConnexionBDD.getConnexion());
            Map<String, Object> info = dao.getFactureDetailsAvecReservations(idFacture);

            Label titre = new Label("🧾 Facture #" + idFacture);
            titre.setStyle("-fx-font-size:16px; -fx-font-weight:bold; -fx-text-fill:#2c3e50;");
            contentBox.getChildren().add(titre);

            contentBox.getChildren().add(new Label("Date : " + info.get("date")));
            contentBox.getChildren().add(new Label("Nb réservations : " + info.get("nb")));
            contentBox.getChildren().add(new Label("Total : " + info.get("total") + " €"));
            contentBox.getChildren().add(new Label(""));

            @SuppressWarnings("unchecked")
            List<String> lignes = (List<String>) info.get("reservations");
            for (String ligne : lignes) {
                contentBox.getChildren().add(new Label("• " + ligne));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            contentBox.getChildren().add(new Label("Erreur lors de l'affichage de la facture."));
        }
    }

    public void afficher(Stage stage) {
        // Racine
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #d0f5c8;"); // Fond vert clair Jurassic Park

        // ===== Contenu central vertical =====
        contentBox = new VBox(15);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20));

        // Titre
        Date today = Date.valueOf(LocalDate.now());
        Label titre = new Label("Vos réservations du " + today);
        titre.setStyle("-fx-font-size:18px; -fx-font-weight:bold;");

        contentBox.getChildren().add(titre);

        // Chargement des réservations
        try {
            ReservationDAO dao = new ReservationDAO(ConnexionBDD.getConnexion());
            List<String> resList = dao.getReservationsDetailsParClientEtDate(utilisateur.getId(), today);

            if (resList.isEmpty()) {
                contentBox.getChildren().add(new Label("Aucune réservation trouvée."));
            } else {
                for (String res : resList) {
                    HBox ligne = new HBox(10);
                    ligne.setAlignment(Pos.CENTER_LEFT);

                    Label lbl = new Label(res);
                    Button del = new Button("Supprimer");
                    del.setOnAction(e -> {
                        try {
                            int idRes = Integer.parseInt(res.split("#")[1].split(" ")[0]);
                            dao.supprimerReservation(idRes);
                            showAlert(Alert.AlertType.INFORMATION, "Suppression", "Réservation supprimée.");
                            // rafraîchir la page
                            this.afficher(new Stage());
                            stage.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    ligne.getChildren().addAll(lbl, del);
                    contentBox.getChildren().add(ligne);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            contentBox.getChildren().add(new Label("Erreur lors du chargement."));
        }

        // Bouton confirmer & générer facture
        Button confirmer = new Button("Confirmer & Générer Facture");
        confirmer.setStyle(
                "-fx-background-color: #e67e22;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 8 20 8 20;"
        );
        confirmer.setOnAction(e -> {
            try {
                ReservationDAO dao = new ReservationDAO(ConnexionBDD.getConnexion());
                int idFacture = dao.creerFacture(utilisateur.getId());
                boolean ok = showAlert(Alert.AlertType.INFORMATION, "Facture", "Facture générée avec succès !");
                if (ok) afficherFacture(idFacture);
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la génération.");
            }
        });
        contentBox.getChildren().add(confirmer);

        // Place le contenu au centre
        ScrollPane scroll = new ScrollPane(contentBox);
        scroll.setFitToWidth(true);
        root.setCenter(scroll);

        // ===== Barre de navigation en bas =====
        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER);
        navBar.setPadding(new Insets(15));
        navBar.setStyle("-fx-background-color: yellow;"); // Fond jaune

        Button btnHome = creerBoutonNavigation("🏠");
        Button btnCalendar = creerBoutonNavigation("📅");
        Button btnCart = creerBoutonNavigation("🛒");
        Button btnUser = creerBoutonNavigation("👤");

        navBar.getChildren().addAll(btnHome, btnCalendar, btnCart, btnUser);
        root.setBottom(navBar);

        // Scene
        Scene scene = new Scene(root, 350, 550);
        stage.setTitle("Réservations - " + utilisateur.getPrenom());
        stage.setScene(scene);
        stage.show();
    }
}
