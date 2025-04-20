package vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import modele.Utilisateur;
import modele.dao.ConnexionBDD;
import modele.dao.ReservationDAO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// ... imports inchangés

public class VueReservations {

    private Utilisateur utilisateur;
    private VBox contentBox;

    private void afficherFacture(int idFacture) {
        contentBox.getChildren().clear();

        try {
            ReservationDAO dao = new ReservationDAO(ConnexionBDD.getConnexion());
            Map<String, Object> factureInfo = dao.getFactureDetailsAvecReservations(idFacture);

            Label titre = new Label("🧾 Facture #" + idFacture);
            titre.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            titre.setTextFill(Color.web("#2c3e50"));
            titre.setAlignment(Pos.CENTER);
            titre.setPadding(new Insets(10));
            titre.setMaxWidth(Double.MAX_VALUE);
            VBox.setMargin(titre, new Insets(10, 0, 10, 0));
            contentBox.getChildren().add(titre);

            Label dateLabel = new Label("Date de facture : " + factureInfo.get("date"));
            Label nbLabel = new Label("Nombre de réservations : " + factureInfo.get("nb"));
            Label totalLabel = new Label("Prix total : " + factureInfo.get("total") + " €");

            List<Label> details = List.of(dateLabel, nbLabel, totalLabel);
            for (Label lbl : details) {
                lbl.setFont(Font.font("Arial", 14));
                lbl.setTextFill(Color.BLACK);
                contentBox.getChildren().add(lbl);
            }

            contentBox.getChildren().add(new Label(""));

            Label sousTitre = new Label("Détail des réservations :");
            sousTitre.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            sousTitre.setTextFill(Color.DARKGREEN);
            contentBox.getChildren().add(sousTitre);

            @SuppressWarnings("unchecked")
            List<String> lignes = (List<String>) factureInfo.get("reservations");

            for (String ligne : lignes) {
                Label ligneLabel = new Label("• " + ligne);
                ligneLabel.setFont(Font.font("Arial", 13));
                ligneLabel.setWrapText(true);
                contentBox.getChildren().add(ligneLabel);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Label erreur = new Label("Erreur lors de l'affichage de la facture.");
            erreur.setTextFill(Color.RED);
            contentBox.getChildren().add(erreur);
        }
    }

    public VueReservations(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void afficher(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        contentBox = new VBox(10);
        root.getChildren().add(contentBox);

        try {
            ReservationDAO dao = new ReservationDAO(ConnexionBDD.getConnexion());
            Date today = Date.valueOf(LocalDate.now());
            List<String> reservations = dao.getReservationsDetailsParClientEtDate(utilisateur.getId(), today);

            Label titre = new Label("Vos réservations pour aujourd'hui (" + today + ") :");
            titre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            contentBox.getChildren().add(titre);

            if (reservations.isEmpty()) {
                contentBox.getChildren().add(new Label("Aucune réservation trouvée."));
            } else {
                for (String res : reservations) {
                    HBox ligne = new HBox(10);
                    ligne.setAlignment(Pos.CENTER_LEFT);

                    Label label = new Label(res);
                    Button deleteBtn = new Button("Supprimer");

                    deleteBtn.setOnAction(e -> {
                        try {
                            int idRes = Integer.parseInt(res.split("#")[1].split(" ")[0]);
                            dao.supprimerReservation(idRes);
                            showAlert(Alert.AlertType.INFORMATION, "Suppression", "Réservation supprimée.");
                            stage.close();
                            this.afficher(new Stage());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    ligne.getChildren().addAll(label, deleteBtn);
                    contentBox.getChildren().add(ligne);
                }

                Button confirmer = new Button("Confirmer & Générer Facture");
                confirmer.setOnAction(e -> {
                    try {
                        int idFacture = dao.creerFacture(utilisateur.getId());
                        boolean ok = showAlert(Alert.AlertType.INFORMATION, "Facture", "Facture générée avec succès !");
                        if (ok) {
                            afficherFacture(idFacture); // ✅ Affichage automatique après confirmation
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la génération.");
                    }
                });

                contentBox.getChildren().add(confirmer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(new ScrollPane(root), 600, 400);
        stage.setTitle("Réservations - " + utilisateur.getPrenom());
        stage.setScene(scene);
        stage.show();
    }

    private boolean showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }
}

