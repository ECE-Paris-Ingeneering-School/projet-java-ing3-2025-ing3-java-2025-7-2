/*package vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import modele.Utilisateur;
import modele.Attraction;
import modele.Evenement;
import modele.dao.AttractionDAO;
import modele.dao.EvenementDAO;
import modele.dao.ConnexionBDD;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * VueUtilisateur est la vue permettant à un utilisateur de consulter son profil.
 * Pour un administrateur, elle propose aussi la gestion des attractions et des événements.
 */
//public class VueUtilisateur {

    /**
     * Affiche l'interface de profil utilisateur.
     * Si l'utilisateur est administrateur, il peut gérer attractions et événements.
     *
     * @param stage Fenêtre JavaFX
     * @param conn Connexion à la base de données
     * @param utilisateur Utilisateur connecté
     * @throws SQLException Si une erreur SQL survient
     * @throws IOException Si une erreur d'entrée/sortie survient
     * @throws ClassNotFoundException Si une classe est manquante
     */
   /* public static void afficher(Stage stage, ConnexionBDD conn, Utilisateur utilisateur) throws SQLException, IOException, ClassNotFoundException {
        stage.setTitle("Mon Profil");

        
        Image logoImage = new Image(VueUtilisateur.class.getResource("/images/logo_JP.png").toExternalForm());
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(80);
        logoView.setPreserveRatio(true);
        HBox logoBox = new HBox(logoView);
        logoBox.setAlignment(Pos.TOP_LEFT);
        logoBox.setPadding(new Insets(10, 0, 0, 10));

        
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPadding(new Insets(10));

       
        VBox vboxUserInfo = new VBox(15);
        vboxUserInfo.setAlignment(Pos.CENTER);
        vboxUserInfo.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 20; -fx-border-radius: 10;");
        vboxUserInfo.setMaxWidth(400);

        Label utilisateurInfo = new Label("Nom: " + utilisateur.getNom() +
                "\nEmail: " + utilisateur.getEmail() +
                "\nRôle: " + utilisateur.getRole());
        utilisateurInfo.setFont(Font.font("Arial", 14));
        utilisateurInfo.setTextFill(Color.web("#2c3e50"));
        utilisateurInfo.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 10; -fx-padding: 10;");
        vboxUserInfo.getChildren().add(utilisateurInfo);

       
        if (utilisateur.getRole().equals("admin")) {
            afficherGestionAttractions(conn, vboxUserInfo);
            afficherGestionEvenements(conn, vboxUserInfo);
        }

      
        ScrollPane scrollPane = new ScrollPane(vboxUserInfo);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #ecf0f1;");

        Scene scene = new Scene(scrollPane, 600, 700);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Affiche la section de gestion des attractions dans la vue utilisateur.
     *
     * @param conn Connexion à la base de données
     * @param vboxUserInfo Conteneur d'affichage
     * @throws SQLException Si une erreur SQL survient
     */
    /*private static void afficherGestionAttractions(ConnexionBDD conn, VBox vboxUserInfo) throws SQLException {
        Label gestionAttractionsLabel = new Label("Gestion des Attractions");
        gestionAttractionsLabel.setFont(Font.font("Arial", 16));
        gestionAttractionsLabel.setTextFill(Color.web("#2c3e50"));

        AttractionDAO attractionDAO = new AttractionDAO(conn.getConnexion());
        List<Attraction> attractions = attractionDAO.getAllAttractions();
        ListView<String> attractionsListView = new ListView<>();
        for (Attraction attraction : attractions) {
            attractionsListView.getItems().add(attraction.getNom() + " - Prix: " + attraction.getPrix());
        }

        Button btnAjouterAttraction = new Button("Ajouter une Attraction");
        Button btnModifierAttraction = new Button("Modifier une Attraction");
        Button btnSupprimerAttraction = new Button("Supprimer une Attraction");

        styliserBoutonAdmin(btnAjouterAttraction, "#3498db");
        styliserBoutonAdmin(btnModifierAttraction, "#f39c12");
        styliserBoutonAdmin(btnSupprimerAttraction, "#e74c3c");

        vboxUserInfo.getChildren().addAll(gestionAttractionsLabel, attractionsListView, btnAjouterAttraction, btnModifierAttraction, btnSupprimerAttraction);

        btnAjouterAttraction.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Ajouter une Attraction");
            dialog.setHeaderText("Nom de l'Attraction");
            dialog.setContentText("Entrez le nom de l'attraction :");
            dialog.showAndWait().ifPresent(nom -> {
                try {
                    attractionDAO.ajouterAttraction(nom, 10.0, "image_path");
                    attractionsListView.getItems().add(nom + " - Prix: 10.0");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
        });

        btnModifierAttraction.setOnAction(e -> {
            String selected = attractionsListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String nom = selected.split(" - ")[0];
                TextInputDialog dialog = new TextInputDialog(nom);
                dialog.setTitle("Modifier l'Attraction");
                dialog.setHeaderText("Modifier le nom de l'attraction");
                dialog.showAndWait().ifPresent(newNom -> {
                    try {
                        int id = attractionDAO.getIdAttractionParNom(nom);
                        attractionDAO.modifierAttraction(id, newNom, 10.0, "new_image_path");
                        attractionsListView.getItems().set(attractionsListView.getSelectionModel().getSelectedIndex(), newNom + " - Prix: 10.0");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });
            } else {
                afficherAlerte("Veuillez sélectionner une attraction à modifier.");
            }
        });

        btnSupprimerAttraction.setOnAction(e -> {
            String selected = attractionsListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String nom = selected.split(" - ")[0];
                try {
                    int id = attractionDAO.getIdAttractionParNom(nom);
                    attractionDAO.supprimerAttraction(id);
                    attractionsListView.getItems().remove(selected);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                afficherAlerte("Veuillez sélectionner une attraction à supprimer.");
            }
        });
    }

    /**
     * Affiche la section de gestion des événements dans la vue utilisateur.
     *
     * @param conn Connexion à la base de données
     * @param vboxUserInfo Conteneur d'affichage
     * @throws SQLException Si une erreur SQL survient
     */
  /*  private static void afficherGestionEvenements(ConnexionBDD conn, VBox vboxUserInfo) throws SQLException {
        Label gestionEvenementsLabel = new Label("Gestion des Événements");
        gestionEvenementsLabel.setFont(Font.font("Arial", 16));
        gestionEvenementsLabel.setTextFill(Color.web("#2c3e50"));

        EvenementDAO evenementDAO = new EvenementDAO(conn.getConnexion());
        List<Evenement> evenements = evenementDAO.getAllEvenements();
        ListView<String> evenementsListView = new ListView<>();
        for (Evenement evenement : evenements) {
            evenementsListView.getItems().add(evenement.getNom() + " - Du " + evenement.getDateDebut() + " au " + evenement.getDateFin());
        }

        Button btnAjouterEvenement = new Button("Ajouter un Événement");
        Button btnModifierEvenement = new Button("Modifier un Événement");
        Button btnSupprimerEvenement = new Button("Supprimer un Événement");

        styliserBoutonAdmin(btnAjouterEvenement, "#3498db");
        styliserBoutonAdmin(btnModifierEvenement, "#f39c12");
        styliserBoutonAdmin(btnSupprimerEvenement, "#e74c3c");

        vboxUserInfo.getChildren().addAll(gestionEvenementsLabel, evenementsListView, btnAjouterEvenement, btnModifierEvenement, btnSupprimerEvenement);

       
    }

    private static void styliserBoutonAdmin(Button btn, String couleur) {
        btn.setStyle("-fx-background-color: " + couleur + "; -fx-text-fill: white; -fx-background-radius: 10;");
    }

    private static void afficherAlerte(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Avertissement");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
*/