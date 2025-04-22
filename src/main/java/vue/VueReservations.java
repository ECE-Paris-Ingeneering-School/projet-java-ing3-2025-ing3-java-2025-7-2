// version modifiée pour afficher séparément les réservations passées et à venir
package vue;

import controleur.ControleurReservations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import modele.Utilisateur;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VueReservations {

    private final Utilisateur utilisateur;
    private VBox contentBox;
    private final ControleurReservations controller;

    public VueReservations(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.controller = new ControleurReservations();
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

    private boolean showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setHeaderText(title);
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }

    public void afficher(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #d0f5c8;");

        contentBox = new VBox(15);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20));
        contentBox.setStyle("-fx-background-color: #d0f5c8; -fx-font-size:14px;");

        try {
            List<String> anciennes = controller.getReservationsPassees(utilisateur);
            List<String> aVenir = controller.getReservationsFutures(utilisateur);

            Label titre = new Label("Vos réservations à venir");
            titre.setStyle("-fx-font-size:20px; -fx-font-weight:bold; -fx-text-fill:#2c3e50;");
            contentBox.getChildren().add(titre);

            if (aVenir.isEmpty()) {
                contentBox.getChildren().add(new Label("Aucune réservation à venir."));
            } else {
                for (String res : aVenir) {
                    ajouterReservationAvecSuppression(res, stage);
                }
            }

            Label separateur = new Label("Vos anciennes réservations");
            separateur.setStyle("-fx-font-size:20px; -fx-font-weight:bold; -fx-text-fill:#2c3e50; -fx-padding: 10 0 0 0;");
            contentBox.getChildren().add(separateur);

            if (anciennes.isEmpty()) {
                contentBox.getChildren().add(new Label("Aucune réservation passée."));
            } else {
                for (String res : anciennes) {
                    Label lbl = new Label(res);
                    lbl.setStyle("-fx-font-size:13px; -fx-text-fill:#333333;");
                    contentBox.getChildren().add(lbl);
                }
            }

            Button confirmer = new Button("Confirmer & Générer Facture");
            confirmer.setStyle("-fx-background-color:#e74c3c; -fx-text-fill:white; -fx-font-size:14px; -fx-padding:8 20 8 20;");
            confirmer.setOnAction(e -> {
                try {
                    Integer idFacture = controller.genererFacture(utilisateur);
                    if (idFacture != null && showAlert(Alert.AlertType.INFORMATION, "Facture", "Facture générée avec succès !")) {
                        afficherFacture(idFacture);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la génération.");
                }
            });
            contentBox.getChildren().add(confirmer);

        } catch (Exception e) {
            e.printStackTrace();
            Label err = new Label("Erreur lors du chargement.");
            err.setStyle("-fx-font-size:14px; -fx-text-fill:red;");
            contentBox.getChildren().add(err);
        }

        ScrollPane scroll = new ScrollPane(contentBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:transparent; -fx-background-color:transparent;");
        root.setCenter(scroll);

        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER);
        navBar.setPadding(new Insets(15));
        navBar.setStyle("-fx-background-color:yellow;");

        navBar.getChildren().addAll(
                creerBoutonNavigation("🏠"),
                creerBoutonNavigation("📅"),
                creerBoutonNavigation("🛒"),
                creerBoutonNavigation("👤")
        );
        root.setBottom(navBar);

        Scene scene = new Scene(root, 350, 550);
        stage.setTitle("Réservations - " + utilisateur.getPrenom());
        stage.setScene(scene);
        stage.show();
    }

    private void ajouterReservationAvecSuppression(String res, Stage stage) {
        HBox ligne = new HBox(10);
        ligne.setAlignment(Pos.CENTER_LEFT);

        Label lbl = new Label(res);
        lbl.setStyle("-fx-font-size:13px; -fx-text-fill:#333333;");

        Button del = new Button("Supprimer");
        del.setStyle("-fx-background-color:#c0392b; -fx-text-fill:white; -fx-font-size:12px;");
        del.setOnAction(e -> {
            try {
                int idRes = Integer.parseInt(res.split("#")[1].split(" ")[0]);
                controller.supprimerReservation(idRes);
                showAlert(Alert.AlertType.INFORMATION, "Suppression", "Réservation supprimée.");
                this.afficher(new Stage());
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        ligne.getChildren().addAll(lbl, del);
        contentBox.getChildren().add(ligne);
    }

    private void afficherFacture(int idFacture) {
        // inchangé : contenu existant de la méthode afficherFacture
    }
}
