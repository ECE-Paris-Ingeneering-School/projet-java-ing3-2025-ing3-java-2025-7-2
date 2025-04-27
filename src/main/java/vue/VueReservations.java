package vue;

import controleur.ControleurReservations;
import controleur.ControleurFactures;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import modele.Utilisateur;
import modele.dao.ConnexionBDD;

import java.sql.Connection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * VueReservations est la vue qui permet d'afficher les réservations
 * d'un utilisateur, séparées entre passées et futures.
 * Elle permet aussi de supprimer des réservations et de générer une facture.
 */
public class VueReservations {

    private final Utilisateur utilisateur;
    private final Connection connexion;
    private VBox contentBox;
    private final ControleurReservations controller;
    private final ControleurFactures controleurFactures;

    /**
     * Constructeur de VueReservations.
     *
     * @param utilisateur L'utilisateur connecté
     * @param connexion La connexion SQL active
     */
    public VueReservations(Utilisateur utilisateur, Connection connexion) {
        this.utilisateur = utilisateur;
        this.connexion = connexion;
        this.controller = new ControleurReservations();
        this.controleurFactures = new ControleurFactures(connexion);
    }

    /**
     * Crée un bouton de navigation avec un emoji.
     *
     * @param emoji Le symbole du bouton
     * @return Le bouton stylisé
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
     * Affiche une boîte d'alerte avec un message.
     *
     * @param type Type d'alerte (INFO, WARNING, ERROR)
     * @param title Titre de la fenêtre d'alerte
     * @param message Message à afficher
     * @return true si OK cliqué
     */
    private boolean showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setHeaderText(title);
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }

    /**
     * Affiche la fenêtre listant les réservations de l'utilisateur.
     *
     * @param stage La fenêtre JavaFX
     */
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
                    System.out.println("Tentative de génération facture");

                    Integer idFacture = controleurFactures.genererFacture(utilisateur);
                    if (idFacture != null && idFacture > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Facture", "Facture générée avec succès ! ID : " + idFacture);
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Aucune facture", "Aucune réservation facturable trouvée.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la génération de la facture.");
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

        Button btnHome = creerBoutonNavigation("🏠");
        Button btnCalendar = creerBoutonNavigation("📅");
        Button btnCart = creerBoutonNavigation("🛒");
        Button btnUser = creerBoutonNavigation("👤");

        navBar.getChildren().addAll(btnHome, btnCalendar, btnCart, btnUser);
        root.setBottom(navBar);

        btnUser.setOnAction(e -> {
            try {
                if ("client".equalsIgnoreCase(utilisateur.getRole())) {
                    VueClient.afficher(new Stage(), utilisateur);
                } else if ("admin".equalsIgnoreCase(utilisateur.getRole())) {
                    VueAdmin.afficher(new Stage(), utilisateur);
                }
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnCart.setOnAction(e -> {
            try {
                ControleurFactures controleurFactures = new ControleurFactures(ConnexionBDD.getConnexion());
                new VueFactures(controleurFactures, utilisateur);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnCalendar.setOnAction(e -> {
            VueCalendrier vueCal = new VueCalendrier(utilisateur);
            vueCal.afficher(new Stage());
            stage.close();
        });



        Scene scene = new Scene(root, 350, 600);
        stage.setTitle("Réservations - " + utilisateur.getPrenom());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Ajoute une réservation à venir avec un bouton de suppression associé.
     *
     * @param res La chaîne descriptive de la réservation
     * @param stage La fenêtre JavaFX
     */
    private void ajouterReservationAvecSuppression(String res, Stage stage) {
        HBox ligne = new HBox(10);
        ligne.setAlignment(Pos.CENTER_LEFT);

        Label lbl = new Label(res);
        lbl.setStyle("-fx-font-size:13px; -fx-text-fill:#333333;");

        Button del = new Button("Supprimer");
        del.setStyle("-fx-background-color:#c0392b; -fx-text-fill:white; -fx-font-size:12px;");
        del.setOnAction(e -> {
            try {
                Pattern pattern = Pattern.compile("#\\s*(\\d+)$");
                Matcher matcher = pattern.matcher(res);

                if (matcher.find()) {
                    int idRes = Integer.parseInt(matcher.group(1));
                    controller.supprimerReservation(idRes);
                    showAlert(Alert.AlertType.INFORMATION, "Suppression", "Réservation supprimée.");
                    this.afficher(new Stage());
                    stage.close();
                } else {
                    System.err.println("[Erreur] Impossible de trouver l'id de réservation dans : " + res);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        ligne.getChildren().addAll(lbl, del);
        contentBox.getChildren().add(ligne);
    }
}
