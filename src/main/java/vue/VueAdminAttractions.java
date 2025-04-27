package vue;

import controleur.ControleurAdminAttractions;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import modele.Attraction;
import modele.Utilisateur;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class VueAdminAttractions {

    public static void afficher(Stage stage, Utilisateur utilisateur) throws SQLException, IOException, ClassNotFoundException {
        stage.setTitle("Gestion des Attractions");

        ControleurAdminAttractions controleur = new ControleurAdminAttractions();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #d0f5c8;");

        VBox contenu = new VBox(10);
        contenu.setPadding(new Insets(10));
        Label titre = new Label("Liste des Attractions");
        titre.setFont(Font.font("Arial", 22));
        titre.setStyle("-fx-text-fill: #2c3e50;");
        contenu.getChildren().add(titre);

        VBox listAttractions = new VBox(10);
        listAttractions.setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane(listAttractions);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #d0f5c8;");
        contenu.getChildren().add(scrollPane);

        // Chargement initial des attractions
        rafraichirListeAttractions(listAttractions, controleur);

        Button boutonAjouter = new Button("➕ Ajouter une attraction");
        boutonAjouter.setStyle(
                "-fx-background-color: #2ecc71;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 8 20 8 20;"
        );
        boutonAjouter.setOnAction(e -> afficherPopupAjout(controleur, () -> rafraichirListeAttractions(listAttractions, controleur)));
        contenu.getChildren().add(boutonAjouter);

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

        Scene scene = new Scene(root, 350, 550);
        stage.setScene(scene);
        stage.show();
    }

    private static void rafraichirListeAttractions(VBox container, ControleurAdminAttractions controleur) {
        container.getChildren().clear();
        List<Attraction> attractions = controleur.getToutesAttractions();

        for (Attraction a : attractions) {
            HBox ligne = new HBox(10);
            ligne.setPadding(new Insets(5));
            ligne.setAlignment(Pos.CENTER_LEFT);
            ligne.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 5;");

            Label label = new Label(a.getNom() + " - " + a.getPrix() + " €");
            label.setFont(Font.font("Arial", 12));
            label.setTextFill(javafx.scene.paint.Color.BLACK);
            label.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(label, Priority.ALWAYS);

            Button boutonModifier = new Button("🔧");
            boutonModifier.setStyle("-fx-font-size: 18px; -fx-background-color: transparent;");
            boutonModifier.setOnAction(e -> afficherPopupModification(controleur, a, () -> rafraichirListeAttractions(container, controleur)));

            Button boutonSupprimer = new Button("❌");
            boutonSupprimer.setStyle("-fx-font-size: 18px; -fx-background-color: transparent;");
            boutonSupprimer.setOnAction(e -> afficherPopupSuppression(controleur, a, () -> rafraichirListeAttractions(container, controleur)));

            ligne.getChildren().addAll(label, boutonModifier, boutonSupprimer);
            container.getChildren().add(ligne);
        }
    }

    private static void afficherPopupAjout(ControleurAdminAttractions controleur, Runnable onSuccess) {
        TextInputDialog dialogNom = new TextInputDialog();
        dialogNom.setHeaderText("Nom de l'attraction");
        Optional<String> nom = dialogNom.showAndWait();

        TextInputDialog dialogPrix = new TextInputDialog();
        dialogPrix.setHeaderText("Prix de l'attraction (€)");
        Optional<String> prix = dialogPrix.showAndWait();

        TextInputDialog dialogImg = new TextInputDialog();
        dialogImg.setHeaderText("Nom du fichier image");
        Optional<String> image = dialogImg.showAndWait();

        if (nom.isPresent() && prix.isPresent()) {
            try {
                double prixDouble = Double.parseDouble(prix.get());
                controleur.ajouterAttraction(nom.get(), prixDouble, image.get());
                onSuccess.run();
            } catch (Exception e) {
                e.printStackTrace();
                alerte("Erreur lors de l'ajout de l'attraction.");
            }
        }
    }

    private static void afficherPopupModification(ControleurAdminAttractions controleur, Attraction attraction, Runnable onSuccess) {
        TextInputDialog dialogNom = new TextInputDialog(attraction.getNom());
        dialogNom.setHeaderText("Modifier le nom");
        Optional<String> nom = dialogNom.showAndWait();

        TextInputDialog dialogPrix = new TextInputDialog(String.valueOf(attraction.getPrix()));
        dialogPrix.setHeaderText("Modifier le prix (€)");
        Optional<String> prix = dialogPrix.showAndWait();

        TextInputDialog dialogImg = new TextInputDialog(attraction.getImage());
        dialogImg.setHeaderText("Modifier le nom de l'image");
        Optional<String> image = dialogImg.showAndWait();

        if (nom.isPresent() && prix.isPresent() && image.isPresent()) {
            try {
                double prixDouble = Double.parseDouble(prix.get());
                controleur.modifierAttraction(attraction, nom.get(), prixDouble, image.get());
                onSuccess.run();
            } catch (Exception e) {
                e.printStackTrace();
                alerte("Erreur lors de la modification de l'attraction.");
            }
        }
    }

    private static void afficherPopupSuppression(ControleurAdminAttractions controleur, Attraction attraction, Runnable onSuccess) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Suppression");
        confirmation.setHeaderText("Supprimer l'attraction : " + attraction.getNom() + " ?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                controleur.supprimerAttraction(attraction);
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
