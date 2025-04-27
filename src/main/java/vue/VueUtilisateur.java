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

public class VueUtilisateur {

    public static void afficher(Stage stage, ConnexionBDD conn, Utilisateur utilisateur) throws SQLException, IOException, ClassNotFoundException {
        stage.setTitle("Mon Profil");

        // ===== Logo en haut à gauche =====
        Image logoImage = new Image(VueUtilisateur.class.getResource("/images/logo_JP.png").toExternalForm());
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(80);
        logoView.setPreserveRatio(true);
        HBox logoBox = new HBox(logoView);
        logoBox.setAlignment(Pos.TOP_LEFT);
        logoBox.setPadding(new Insets(10, 0, 0, 10));

        // ===== Conteneur principal =====
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPadding(new Insets(10));

        // ===== Informations de l'utilisateur =====
        VBox vboxUserInfo = new VBox(15);
        vboxUserInfo.setAlignment(Pos.CENTER);
        vboxUserInfo.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 20; -fx-border-radius: 10;");
        vboxUserInfo.setMaxWidth(400);

        // Afficher toutes les infos de l'utilisateur
        Label utilisateurInfo = new Label("Nom: " + utilisateur.getNom() +
                "\nEmail: " + utilisateur.getEmail() +
                "\nRôle: " + utilisateur.getRole() );
        utilisateurInfo.setFont(Font.font("Arial", 14));
        utilisateurInfo.setTextFill(Color.web("#2c3e50"));
        utilisateurInfo.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 10; -fx-padding: 10;");
        vboxUserInfo.getChildren().add(utilisateurInfo);

        // ===== Gestion des attractions (pour l'administrateur) =====
        if (utilisateur.getRole().equals("admin")) {
            // Titre de gestion des attractions
            Label gestionAttractionsLabel = new Label("Gestion des Attractions");
            gestionAttractionsLabel.setFont(Font.font("Arial", 16));
            gestionAttractionsLabel.setTextFill(Color.web("#2c3e50"));

            // Récupérer les attractions
            AttractionDAO attractionDAO = new AttractionDAO(conn.getConnexion());
            List<Attraction> attractions = attractionDAO.getAllAttractions();
            ListView<String> attractionsListView = new ListView<>();
            for (Attraction attraction : attractions) {
                attractionsListView.getItems().add(attraction.getNom() + " - Prix: " + attraction.getPrix());
            }

            // Bouton pour ajouter une attraction
            Button btnAjouterAttraction = new Button("Ajouter une Attraction");
            btnAjouterAttraction.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 10;");

            // Bouton pour modifier une attraction
            Button btnModifierAttraction = new Button("Modifier une Attraction");
            btnModifierAttraction.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-background-radius: 10;");

            // Bouton pour supprimer une attraction
            Button btnSupprimerAttraction = new Button("Supprimer une Attraction");
            btnSupprimerAttraction.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 10;");

            // Ajouter les éléments dans la vue
            vboxUserInfo.getChildren().addAll(gestionAttractionsLabel, attractionsListView, btnAjouterAttraction, btnModifierAttraction, btnSupprimerAttraction);

            // Action pour ajouter une attraction
            btnAjouterAttraction.setOnAction(e -> {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Ajouter une Attraction");
                dialog.setHeaderText("Nom de l'Attraction");
                dialog.setContentText("Entrez le nom de l'attraction :");
                dialog.showAndWait().ifPresent(nom -> {
                    // Vous pouvez ajouter plus de champs ici, comme le prix et l'image
                    try {
                        attractionDAO.ajouterAttraction(nom, 10.0, "image_path");
                        attractionsListView.getItems().add(nom + " - Prix: 10.0");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });
            });

            // Action pour modifier une attraction
            btnModifierAttraction.setOnAction(e -> {
                String selectedAttraction = attractionsListView.getSelectionModel().getSelectedItem();
                if (selectedAttraction != null) {
                    String attractionName = selectedAttraction.split(" - ")[0];
                    TextInputDialog dialog = new TextInputDialog(attractionName);
                    dialog.setTitle("Modifier l'Attraction");
                    dialog.setHeaderText("Modifier le nom de l'attraction");
                    dialog.setContentText("Entrez le nouveau nom de l'attraction :");
                    dialog.showAndWait().ifPresent(newNom -> {
                        try {
                            int attractionId = attractionDAO.getIdAttractionParNom(attractionName);
                            attractionDAO.modifierAttraction(attractionId, newNom, 10.0, "new_image_path");
                            attractionsListView.getItems().set(attractionsListView.getSelectionModel().getSelectedIndex(), newNom + " - Prix: 10.0");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    });
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Avertissement");
                    alert.setHeaderText(null);
                    alert.setContentText("Veuillez sélectionner une attraction à modifier.");
                    alert.showAndWait();
                }
            });

            // Action pour supprimer une attraction
            btnSupprimerAttraction.setOnAction(e -> {
                String selectedAttraction = attractionsListView.getSelectionModel().getSelectedItem();
                if (selectedAttraction != null) {
                    String attractionName = selectedAttraction.split(" - ")[0];
                    try {
                        int attractionId = attractionDAO.getIdAttractionParNom(attractionName);
                        attractionDAO.supprimerAttraction(attractionId);
                        attractionsListView.getItems().remove(selectedAttraction);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Avertissement");
                    alert.setHeaderText(null);
                    alert.setContentText("Veuillez sélectionner une attraction à supprimer.");
                    alert.showAndWait();
                }
            });
        }

        // ===== Gestion des événements (pour l'administrateur) =====
        if (utilisateur.getRole().equals("admin")) {
            // Titre de gestion des événements
            Label gestionEvenementsLabel = new Label("Gestion des Événements");
            gestionEvenementsLabel.setFont(Font.font("Arial", 16));
            gestionEvenementsLabel.setTextFill(Color.web("#2c3e50"));

            // Récupérer les événements
            EvenementDAO evenementDAO = new EvenementDAO(conn.getConnexion());
            List<Evenement> evenements = evenementDAO.getAllEvenements();
            ListView<String> evenementsListView = new ListView<>();
            for (Evenement evenement : evenements) {
                evenementsListView.getItems().add(evenement.getNom() + " - Du " + evenement.getDateDebut() + " au " + evenement.getDateFin());
            }

            // Bouton pour ajouter un événement
            Button btnAjouterEvenement = new Button("Ajouter un Événement");
            btnAjouterEvenement.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 10;");

            // Bouton pour modifier un événement
            Button btnModifierEvenement = new Button("Modifier un Événement");
            btnModifierEvenement.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-background-radius: 10;");

            // Bouton pour supprimer un événement
            Button btnSupprimerEvenement = new Button("Supprimer un Événement");
            btnSupprimerEvenement.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 10;");

            // Ajouter les éléments dans la vue
            vboxUserInfo.getChildren().addAll(gestionEvenementsLabel, evenementsListView, btnAjouterEvenement, btnModifierEvenement, btnSupprimerEvenement);

            // Action pour ajouter un événement
            btnAjouterEvenement.setOnAction(e -> {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Ajouter un Événement");
                dialog.setHeaderText("Nom de l'Événement");
                dialog.setContentText("Entrez le nom de l'événement :");
                dialog.showAndWait().ifPresent(nom -> {
                    // Vous pouvez ajouter plus de champs ici, comme les dates et le supplément
                    try {
                        evenementDAO.ajouterEvenement(nom, 5.0, java.sql.Date.valueOf("2025-01-01"), java.sql.Date.valueOf("2025-01-05"));
                        evenementsListView.getItems().add(nom + " - Du 2025-01-01 au 2025-01-05");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });
            });

            // Action pour modifier un événement
            btnModifierEvenement.setOnAction(e -> {
                String selectedEvenement = evenementsListView.getSelectionModel().getSelectedItem();
                if (selectedEvenement != null) {
                    String evenementName = selectedEvenement.split(" - ")[0];
                    TextInputDialog dialog = new TextInputDialog(evenementName);
                    dialog.setTitle("Modifier l'Événement");
                    dialog.setHeaderText("Modifier le nom de l'événement");
                    dialog.setContentText("Entrez le nouveau nom de l'événement :");
                    dialog.showAndWait().ifPresent(newNom -> {
                        try {
                            Evenement event = evenementDAO.getEvenementParId(Integer.parseInt(evenementName));
                            evenementDAO.modifierEvenement(event.getIdEvenement(), newNom, 5.0, java.sql.Date.valueOf("2025-01-01"), java.sql.Date.valueOf("2025-01-05"));
                            evenementsListView.getItems().set(evenementsListView.getSelectionModel().getSelectedIndex(), newNom + " - Du 2025-01-01 au 2025-01-05");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    });
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Avertissement");
                    alert.setHeaderText(null);
                    alert.setContentText("Veuillez sélectionner un événement à modifier.");
                    alert.showAndWait();
                }
            });

            // Action pour supprimer un événement
            btnSupprimerEvenement.setOnAction(e -> {
                String selectedEvenement = evenementsListView.getSelectionModel().getSelectedItem();
                if (selectedEvenement != null) {
                    String evenementName = selectedEvenement.split(" - ")[0];
                    try {
                        Evenement event = evenementDAO.getEvenementParId(Integer.parseInt(evenementName));
                        evenementDAO.supprimerEvenement(event.getIdEvenement());
                        evenementsListView.getItems().remove(selectedEvenement);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Avertissement");
                    alert.setHeaderText(null);
                    alert.setContentText("Veuillez sélectionner un événement à supprimer.");
                    alert.showAndWait();
                }
            });
        }

        // ===== Conteneur final =====
        ScrollPane scrollPane = new ScrollPane(vboxUserInfo);
        scrollPane.setFitToWidth(true); // Ajuste la largeur du contenu au ScrollPane
        scrollPane.setStyle("-fx-background-color: #ecf0f1;");

        // Créer la scène et l'afficher
        Scene scene = new Scene(scrollPane, 600, 700);
        stage.setScene(scene);
        stage.show();
    }
}
*/