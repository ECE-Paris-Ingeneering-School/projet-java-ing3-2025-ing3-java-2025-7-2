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

public class VueReservations {

    private Utilisateur utilisateur;

    public VueReservations(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void afficher(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        try {
            ReservationDAO dao = new ReservationDAO(ConnexionBDD.getConnexion());
            Date today = Date.valueOf(LocalDate.now());
            List<String> reservations = dao.getReservationsDetailsParClientEtDate(utilisateur.getId(), today);

            Label titre = new Label("Vos réservations pour aujourd'hui (" + today + ") :");
            titre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            root.getChildren().add(titre);

            if (reservations.isEmpty()) {
                root.getChildren().add(new Label("Aucune réservation trouvée."));
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
                    root.getChildren().add(ligne);
                }

                Button confirmer = new Button("Confirmer & Générer Facture");
                confirmer.setOnAction(e -> {
                    try {
                        dao.creerFacture(utilisateur.getId());
                        showAlert(Alert.AlertType.INFORMATION, "Facture", "Facture générée avec succès !");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la génération.");
                    }
                });

                root.getChildren().add(confirmer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(new ScrollPane(root), 600, 400);
        stage.setTitle("Réservations - " + utilisateur.getPrenom());
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
