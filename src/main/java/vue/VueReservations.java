package vue;

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
import modele.dao.ConnexionBDD;
import modele.dao.ReservationDAO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.util.Map;

public class VueReservations {

    private final Utilisateur utilisateur;
    private VBox contentBox;

    public VueReservations(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
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

    // Classe pour les lignes de facture
    public static class FactureLine {
        private final String attraction, date, prix, supplement;
        public FactureLine(String a, String d, String p, String s) {
            this.attraction = a; this.date = d; this.prix = p; this.supplement = s;
        }
        public String getAttraction() { return attraction; }
        public String getDate()       { return date; }
        public String getPrix()       { return prix; }
        public String getSupplement() { return supplement; }
    }

    private void afficherFacture(int idFacture) {
        contentBox.getChildren().clear();

        try {
            // Récupération des infos de facture
            ReservationDAO dao = new ReservationDAO(ConnexionBDD.getConnexion());
            Map<String, Object> info = dao.getFactureDetailsAvecReservations(idFacture);

            // Titre & résumé (inchangés)
            Label titre = new Label(" Facture #" + idFacture);
            titre.setStyle("-fx-font-size:16px; -fx-font-weight:bold; -fx-text-fill:#2c3e50;");
            titre.setMaxWidth(Double.MAX_VALUE);
            titre.setAlignment(Pos.CENTER);
            contentBox.getChildren().add(titre);

            HBox résumé = new HBox(10,
                    new Label("Date : "  + info.get("date")),
                    new Label("Nombre : " + info.get("nb")),
                    new Label("Total : "  + info.get("total") + " €")
            );
            résumé.setAlignment(Pos.CENTER);
            contentBox.getChildren().add(résumé);

            // Création du tableau
            TableView<FactureLine> table = new TableView<>();
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            table.setPrefHeight(200);

            TableColumn<FactureLine, String> colA = new TableColumn<>("Attraction");
            TableColumn<FactureLine, String> colD = new TableColumn<>("Date");
            TableColumn<FactureLine, String> colP = new TableColumn<>("Prix (€)");
            TableColumn<FactureLine, String> colS = new TableColumn<>("Supplément");

            colA.setCellValueFactory(new PropertyValueFactory<>("attraction"));
            colD.setCellValueFactory(new PropertyValueFactory<>("date"));
            colP.setCellValueFactory(new PropertyValueFactory<>("prix"));
            colS.setCellValueFactory(new PropertyValueFactory<>("supplement"));

            // largeur relative
            colA.prefWidthProperty().bind(table.widthProperty().multiply(0.4));
            colD.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
            colP.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
            colS.prefWidthProperty().bind(table.widthProperty().multiply(0.2));

            table.getColumns().addAll(colA, colD, colP, colS);

            // Extraction des lignes
            @SuppressWarnings("unchecked")
            List<String> lignes = (List<String>) info.get("reservations");
            ObservableList<FactureLine> data = FXCollections.observableArrayList();

            // Regex pour la date entre parenthèses dans l'attraction
            Pattern pat = Pattern.compile("^(.*) \\((\\d{4}-\\d{2}-\\d{2})\\)$");

            for (String ligne : lignes) {
                // 1) On sépare Attraction (+ date) vs Prix+Supplément
                String[] parts = ligne.split(" - Prix : ", 2);
                String rawAttraction = parts[0].trim();        // ex: "Parcours Jurassic Guidé (2025-04-19)"
                String pricePart    = parts.length>1 ? parts[1] : "";

                // 2) On extrait le nom et la date
                String attraction = rawAttraction;
                String dateStr    = "";
                Matcher m = pat.matcher(rawAttraction);
                if (m.matches()) {
                    attraction = m.group(1).trim();   // "Parcours Jurassic Guidé"
                    dateStr    = m.group(2);          // "2025-04-19"
                }

                // 3) On découpe le prix et le supplément
                String prixStr = "";
                String supStr  = "0";  // défaut si pas de supplément
                if (pricePart.contains("+ Supplément :")) {
                    String[] ps = pricePart.split("\\+ Supplément :");
                    prixStr = ps[0].trim();           // "15.75€"
                    supStr  = ps[1].trim();           // "1.75€"
                } else {
                    prixStr = pricePart.trim();       // si pas de supplément
                }

                data.add(new FactureLine(attraction, dateStr, prixStr, supStr));
            }

            table.setItems(data);
            contentBox.getChildren().add(table);

        } catch (Exception ex) {
            ex.printStackTrace();
            contentBox.getChildren().add(new Label("Erreur lors de l'affichage de la facture."));
        }
    }

    public void afficher(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #d0f5c8;");

        // Contenu vertical
        contentBox = new VBox(15);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20));


        // Chargement des réservations
        try {
            ReservationDAO dao = new ReservationDAO(ConnexionBDD.getConnexion());
            Date today = Date.valueOf(LocalDate.now());
            List<String> resList = dao.getReservationsDetailsParClientEtDate(utilisateur.getId(), today);

            Label titre = new Label("Vos réservations du " + today);
            titre.setStyle("-fx-font-size:18px; -fx-font-weight:bold;");
            contentBox.getChildren().add(titre);

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
                            this.afficher(new Stage());
                            stage.close();
                        } catch (Exception ex) { ex.printStackTrace(); }
                    });
                    ligne.getChildren().addAll(lbl, del);
                    contentBox.getChildren().add(ligne);
                }
            }

            // Bouton générer facture
            Button confirmer = new Button("Confirmer & Générer Facture");
            confirmer.setStyle(
                    "-fx-background-color: #e74c3c; -fx-text-fill: white;" +
                            "-fx-background-radius: 20; -fx-font-size:14px; -fx-padding:8 20 8 20;"
            );
            confirmer.setOnAction(e -> {
                try {
                    int idFacture = dao.creerFacture(utilisateur.getId());
                    if (showAlert(Alert.AlertType.INFORMATION, "Facture", "Facture générée avec succès !")) {
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
            contentBox.getChildren().add(new Label("Erreur lors du chargement."));
        }

        // Scroll si nécessaire
        ScrollPane scroll = new ScrollPane(contentBox);
        scroll.setFitToWidth(true);
        root.setCenter(scroll);

        // Barre de navigation mobile en bas
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

        Scene scene = new Scene(root, 350, 550);
        stage.setTitle("Réservations - " + utilisateur.getPrenom());
        stage.setScene(scene);
        stage.show();
    }
}
