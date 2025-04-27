package vue;

import controleur.ControleurAdminAttractions;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import modele.Attraction;
import modele.Utilisateur;

import java.util.List;

public class VueListeAttractions {

    public static void afficher(Stage stage, Utilisateur utilisateur) {
        stage.setTitle("Attractions du Parc");

        ControleurAdminAttractions controleur;
        try {
            controleur = new ControleurAdminAttractions();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Affiche une alerte ou un message d'erreur graphique :
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors du chargement des attractions.");
            alert.showAndWait();
            return; // On arrête la méthode si le contrôleur ne peut pas être créé
        }

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.setStyle("-fx-background-color: #d0f5c8;");

        Label titre = new Label("Toutes les attractions");
        titre.setFont(Font.font("Arial", 22));
        titre.setStyle("-fx-text-fill: #2c3e50;");
        titre.setAlignment(Pos.CENTER);
        titre.setMaxWidth(Double.MAX_VALUE);

        VBox listAttractions = new VBox(14);
        listAttractions.setPadding(new Insets(10));

        // Charger la liste actuelle
        rafraichirListeAttractions(listAttractions, controleur);

        ScrollPane scrollPane = new ScrollPane(listAttractions);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #d0f5c8;");

        content.getChildren().addAll(titre, scrollPane);

        // Barre de navigation en bas
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

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #d0f5c8;");
        root.setCenter(content);
        root.setBottom(navBar);

        Scene scene = new Scene(root, 350, 600);
        stage.setScene(scene);
        stage.show();
    }

    private static void rafraichirListeAttractions(VBox container, ControleurAdminAttractions controleur) {
        container.getChildren().clear();
        List<Attraction> attractions = controleur.getToutesAttractions();

        for (Attraction a : attractions) {
            HBox ligne = new HBox(10);
            ligne.setAlignment(Pos.CENTER_LEFT);

            // Affiche l'image si dispo
            ImageView imageView = null;
            try {
                if (a.getImage() != null && !a.getImage().isEmpty()) {
                    Image image = new Image("/images/" + a.getImage(), 48, 48, true, true);
                    imageView = new ImageView(image);
                }
            } catch (Exception e) {
                imageView = new ImageView(); // pas d'image si erreur
            }
            if (imageView == null) imageView = new ImageView();

            Label nom = new Label(a.getNom());
            nom.setFont(Font.font("Arial", 14));
            nom.setStyle("-fx-font-weight: bold; -fx-text-fill: #154360;");
            Label prix = new Label(a.getPrix() + " €");
            prix.setFont(Font.font("Arial", 13));
            prix.setStyle("-fx-text-fill: #2c3e50;");

            VBox desc = new VBox(nom, prix);

            ligne.getChildren().addAll(imageView, desc);
            ligne.setStyle("-fx-background-color: #fff; -fx-background-radius: 12; -fx-padding: 10;");
            container.getChildren().add(ligne);
        }
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
