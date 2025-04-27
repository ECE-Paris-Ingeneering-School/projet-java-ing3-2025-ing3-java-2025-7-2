package vue;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import modele.Evenement;
import modele.Utilisateur;
import modele.dao.ConnexionBDD;
import modele.dao.EvenementDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class VueAdminEvenements {

    private static EvenementDAO evenementDAO;

    public static void afficher(Stage stage, Utilisateur utilisateur) throws SQLException, IOException, ClassNotFoundException {
        stage.setTitle("Gestion des Événements");

        Connection conn = ConnexionBDD.getConnexion();
        evenementDAO = new EvenementDAO(conn);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label titre = new Label("Liste des Événements");
        titre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        root.getChildren().add(titre);

        VBox listEvenements = new VBox(10);
        listEvenements.setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane(listEvenements);
        scrollPane.setFitToWidth(true);

        // Chargement initial des événements
        rafraichirListeEvenements(listEvenements);

        Button boutonAjouter = new Button("\u2795 Ajouter un événement");
        boutonAjouter.setOnAction(e -> afficherPopupAjout(() -> rafraichirListeEvenements(listEvenements)));

        root.getChildren().addAll(scrollPane, boutonAjouter);

        Scene scene = new Scene(root, 450, 600);
        stage.setScene(scene);
        stage.show();
    }

    private static void rafraichirListeEvenements(VBox container) {
        container.getChildren().clear();
        try {
            List<Evenement> evenements = evenementDAO.getAllEvenements();

            for (Evenement e : evenements) {
                HBox ligne = new HBox(10);
                ligne.setPadding(new Insets(5));
                ligne.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 5;");

                Label label = new Label(e.getNom() + " - Du " + e.getDateDebut() + " au " + e.getDateFin() + " (Supplément : " + e.getSupplement() + "€)");
                label.setPrefWidth(300);

                Button boutonModifier = new Button("\uD83D\uDD27");
                boutonModifier.setOnAction(ev -> afficherPopupModification(e, () -> rafraichirListeEvenements(container)));

                Button boutonSupprimer = new Button("\u274C");
                boutonSupprimer.setOnAction(ev -> afficherPopupSuppression(e, () -> rafraichirListeEvenements(container)));

                ligne.getChildren().addAll(label, boutonModifier, boutonSupprimer);
                container.getChildren().add(ligne);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void afficherPopupAjout(Runnable onSuccess) {
        TextInputDialog dialogNom = new TextInputDialog();
        dialogNom.setHeaderText("Nom de l'événement");
        Optional<String> nom = dialogNom.showAndWait();

        TextInputDialog dialogSupplement = new TextInputDialog();
        dialogSupplement.setHeaderText("Supplément (€)");
        Optional<String> supplement = dialogSupplement.showAndWait();

        TextInputDialog dialogDateDebut = new TextInputDialog();
        dialogDateDebut.setHeaderText("Date de début (AAAA-MM-JJ)");
        Optional<String> dateDebut = dialogDateDebut.showAndWait();

        TextInputDialog dialogDateFin = new TextInputDialog();
        dialogDateFin.setHeaderText("Date de fin (AAAA-MM-JJ)");
        Optional<String> dateFin = dialogDateFin.showAndWait();

        TextInputDialog dialogImage = new TextInputDialog();
        dialogImage.setHeaderText("Nom du fichier image");
        Optional<String> image = dialogImage.showAndWait();

        if (nom.isPresent() && supplement.isPresent() && dateDebut.isPresent() && dateFin.isPresent() && image.isPresent()) {
            try {
                double suppl = Double.parseDouble(supplement.get());
                LocalDate debut = LocalDate.parse(dateDebut.get());
                LocalDate fin = LocalDate.parse(dateFin.get());
                evenementDAO.ajouterEvenement(nom.get(), suppl, java.sql.Date.valueOf(debut), java.sql.Date.valueOf(fin), image.get());
                onSuccess.run();
            } catch (Exception e) {
                e.printStackTrace();
                alerte("Erreur lors de l'ajout de l'événement.");
            }
        }
    }

    private static void afficherPopupModification(Evenement evenement, Runnable onSuccess) {
        TextInputDialog dialogNom = new TextInputDialog(evenement.getNom());
        dialogNom.setHeaderText("Modifier le nom");
        Optional<String> nom = dialogNom.showAndWait();

        TextInputDialog dialogSupplement = new TextInputDialog(String.valueOf(evenement.getSupplement()));
        dialogSupplement.setHeaderText("Modifier le supplément (€)");
        Optional<String> supplement = dialogSupplement.showAndWait();

        TextInputDialog dialogDateDebut = new TextInputDialog(evenement.getDateDebut().toString());
        dialogDateDebut.setHeaderText("Modifier la date de début (AAAA-MM-JJ)");
        Optional<String> dateDebut = dialogDateDebut.showAndWait();

        TextInputDialog dialogDateFin = new TextInputDialog(evenement.getDateFin().toString());
        dialogDateFin.setHeaderText("Modifier la date de fin (AAAA-MM-JJ)");
        Optional<String> dateFin = dialogDateFin.showAndWait();

        TextInputDialog dialogImage = new TextInputDialog(evenement.getImage());
        dialogImage.setHeaderText("Modifier le fichier image");
        Optional<String> image = dialogImage.showAndWait();

        if (nom.isPresent() && supplement.isPresent() && dateDebut.isPresent() && dateFin.isPresent() && image.isPresent()) {
            try {
                evenement.setNom(nom.get());
                evenement.setSupplement(Double.parseDouble(supplement.get()));
                evenement.setDateDebut(java.sql.Date.valueOf(LocalDate.parse(dateDebut.get())));
                evenement.setDateFin(java.sql.Date.valueOf(LocalDate.parse(dateFin.get())));
                evenement.setImage(image.get());
                evenementDAO.modifierEvenement(evenement);
                onSuccess.run();
            } catch (Exception e) {
                e.printStackTrace();
                alerte("Erreur lors de la modification de l'événement.");
            }
        }
    }

    private static void afficherPopupSuppression(Evenement evenement, Runnable onSuccess) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Suppression");
        confirmation.setHeaderText("Supprimer l'événement : " + evenement.getNom() + " ?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                evenementDAO.supprimerEvenement(evenement.getIdEvenement());
                onSuccess.run();
            } catch (Exception e) {
                e.printStackTrace();
                alerte("Erreur lors de la suppression.");
            }
        }
    }

    private static void alerte(String message) {
        Alert alerte = new Alert(Alert.AlertType.ERROR, message);
        alerte.showAndWait();
    }
}
