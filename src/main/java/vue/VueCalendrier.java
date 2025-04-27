package vue;

import controleur.Calendrier;
import controleur.ControleurEvenement;
import controleur.ControleurFactures;
import vue.VueAccueil;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import modele.Utilisateur;
import modele.dao.AttractionDAO;
import modele.dao.ConnexionBDD;
import modele.dao.ReservationDAO;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

/**
 * VueCalendrier et la vue permettant d'afficher un calendrier interactif,
 * avec navigation par Mois et affichage des attractions disponibles pour un utilisateur.
 */
public class VueCalendrier {

    private Calendrier controller;
    private YearMonth currentYearMonth;
    private GridPane calendarGrid;
    private Label monthLabel;
    private VBox reservationPanel;
    private Utilisateur utilisateurConnecte;
    private Stage stage;
    private ControleurEvenement controleurEvenement;

    /**
     * Constructeur de VueCalendrier.
     *
     * @param utilisateur L'utilisateur actuellement connecté
     */
    public VueCalendrier(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        this.controller = new Calendrier(this);
        this.controleurEvenement = new ControleurEvenement();
    }

    /**
     * Affiche la fenêtre du calendrier.
     *
     * @param stage La fenêtre JavaFX à utiliser
     */
    public void afficher(Stage stage) {
        this.stage = stage;

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #d0f5c8;"); // fond vert clair Jurassic Park

        // ===== Titre du mois et navigation =====
        HBox topPanel = new HBox(10);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setPadding(new Insets(10));

        Button prevButton = new Button("<");
        Button nextButton = new Button(">");

        monthLabel = new Label();
        monthLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        prevButton.setOnAction(e -> navigationparMois(-1));
        nextButton.setOnAction(e -> navigationparMois(1));

        topPanel.getChildren().addAll(prevButton, monthLabel, nextButton);

        // ===== Grille calendrier =====
        calendarGrid = new GridPane();
        calendarGrid.setHgap(5);
        calendarGrid.setVgap(5);
        calendarGrid.setPadding(new Insets(10));

        ScrollPane calendarScroll = new ScrollPane(calendarGrid);
        calendarScroll.setFitToWidth(true);
        calendarScroll.setStyle("-fx-background-color: transparent;");

        // ===== Panel réservation =====
        reservationPanel = new VBox(10);
        reservationPanel.setPadding(new Insets(10));
        reservationPanel.setStyle("-fx-border-color: gray; -fx-border-width: 1;");

        // ===== Bouton voir réservations =====
        Button voirReservationsBtn = new Button("Voir mes réservations");
        voirReservationsBtn.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 8 20 8 20;"
        );
        voirReservationsBtn.setOnAction(e -> {
            try {
                VueReservations vueRes = new VueReservations(utilisateurConnecte, ConnexionBDD.getConnexion());
                vueRes.afficher(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d’ouvrir les réservations.");
            }
        });


        VBox centerContent = new VBox(10, topPanel, calendarScroll, reservationPanel, voirReservationsBtn);
        centerContent.setAlignment(Pos.TOP_CENTER);
        centerContent.setPadding(new Insets(10));

        // ===== Barre de navigation mobile en bas =====
        HBox navBar = new HBox(15);
        navBar.setAlignment(Pos.CENTER);
        navBar.setPadding(new Insets(15));
        navBar.setStyle("-fx-background-color: yellow;");

        Button btnHome = creerBoutonNavigation("🏠");
        Button btnCalendar = creerBoutonNavigation("📅");
        Button btnCart = creerBoutonNavigation("🛒");
        Button btnUser = creerBoutonNavigation("👤");

        navBar.getChildren().addAll(btnHome, btnCalendar, btnCart, btnUser);

        ///  RAFRAICHISSEMENT APGE
        btnCalendar.setOnAction(e -> {
            this.afficher(new Stage());  // relance la page actuelle
            stage.close();
        });
        //parcours icone du bas
        btnUser.setOnAction(e -> {
            try {
                if ("client".equalsIgnoreCase(utilisateurConnecte.getRole())) {
                    VueClient.afficher(new Stage(), utilisateurConnecte);
                }
                else if ("admin".equalsIgnoreCase(utilisateurConnecte.getRole())) {
                    VueAdmin.afficher(new Stage(), utilisateurConnecte);
                }
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        btnCart.setOnAction(e ->{
            try {
                ControleurFactures controleurFactures = new ControleurFactures(ConnexionBDD.getConnexion()); // adapte si c’est déjà instancié ailleurs
                new VueFactures(controleurFactures, utilisateurConnecte); // ouvre la vue des factures
               /// stage.close()
                /// ==> Fenêtre sans barre de navigation donc on la laisse en popup jusqu'à implémentation
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });

        btnHome.setOnAction(e -> {
            VueAccueil vueAccueil = new VueAccueil (utilisateurConnecte); // Pas besoin de passer d’utilisateur
            vueAccueil.afficher(new Stage());         // Affiche dans une nouvelle fenêtre
            // Optionnel : stage.close(); // Si tu veux fermer la page actuelle
        });

        // ===== Placement dans BorderPane =====
        root.setCenter(centerContent);
        root.setBottom(navBar);

        controller.initializeCalendar();

        Scene scene = new Scene(root, 350, 600);
        stage.setTitle("Calendrier - " + utilisateurConnecte.getPrenom());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Crée un bouton de navigation avec un emoji.
     *
     * @param emoji Le symbole à afficher
     * @return Le bouton configuré
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


    public void displayMonth(YearMonth yearMonth) {
        this.currentYearMonth = yearMonth;
        monthLabel.setText(yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH) + " " + yearMonth.getYear());

        calendarGrid.getChildren().clear();

        String[] days = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
        for (int i = 0; i < days.length; i++) {
            Label label = new Label(days[i]);
            label.setStyle("-fx-font-weight: bold;");
            calendarGrid.add(label, i, 0);
        }

        LocalDate firstDay = yearMonth.atDay(1);
        int startDayOfWeek = firstDay.getDayOfWeek().getValue(); // Lundi = 1

        int row = 1;
        int col = startDayOfWeek - 1;

        List<LocalDate> joursEvenements = controleurEvenement.getDatesAvecEvenements(yearMonth);

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            Button dayBtn = new Button(String.valueOf(day));
            dayBtn.setMaxWidth(Double.MAX_VALUE);

            if (joursEvenements.contains(date)) {
                //System.out.println(date);
                if(controleurEvenement.getIDEvenementParDate(date) == 1){
                    dayBtn.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white;");
                }
                else if(controleurEvenement.getIDEvenementParDate(date) == 2){
                    dayBtn.setStyle("-fx-background-color: #11ff00; -fx-text-fill: white;");
                }

                else if(controleurEvenement.getIDEvenementParDate(date) == 3){
                    dayBtn.setStyle("-fx-background-color:  #fd7200; -fx-text-fill: white;");
                }

                else if(controleurEvenement.getIDEvenementParDate(date) == 4){
                    dayBtn.setStyle("-fx-background-color: #00ffd9; -fx-text-fill: white;");
                }

                else if(controleurEvenement.getIDEvenementParDate(date) == 5){
                    dayBtn.setStyle("-fx-background-color: #ff00dd; -fx-text-fill: white;");
                }

                else if(controleurEvenement.getIDEvenementParDate(date) == 6){
                    dayBtn.setStyle("-fx-background-color: #002aff; -fx-text-fill: white;");
                }
                else{
                    dayBtn.setStyle("-fx-background-color: orange; -fx-text-fill: white;");

                }

            }

            if (date.isBefore(LocalDate.now())) {
                dayBtn.setDisable(true);
                dayBtn.setStyle("-fx-background-color: lightgray; -fx-text-fill: darkgray;");
                if (joursEvenements.contains(date)) {
                    //System.out.println(date);
                    if(controleurEvenement.getIDEvenementParDate(date) == 1){
                        dayBtn.setStyle("-fx-background-color: lightgray; -fx-text-fill: #ff0000;");
                    }
                    else if(controleurEvenement.getIDEvenementParDate(date) == 2){
                        dayBtn.setStyle("-fx-background-color: lightgray; -fx-text-fill: #11ff00;");
                    }

                    else if(controleurEvenement.getIDEvenementParDate(date) == 3){
                        dayBtn.setStyle("-fx-background-color: lightgray; -fx-text-fill: #fd7200;");
                    }

                    else if(controleurEvenement.getIDEvenementParDate(date) == 4){
                        dayBtn.setStyle("-fx-background-color: lightgray; -fx-text-fill: #00ffd9;");
                    }

                    else if(controleurEvenement.getIDEvenementParDate(date) == 5){
                        dayBtn.setStyle("-fx-background-color: lightgray; -fx-text-fill: #ff00dd;");
                    }

                    else if(controleurEvenement.getIDEvenementParDate(date) == 6){
                        dayBtn.setStyle("-fx-background-color: lightgray; -fx-text-fill: #002aff;");
                    }
                    else{
                        dayBtn.setStyle("-fx-background-color: lightgray; -fx-text-fill: orange;");

                    }

                }
            } else {
                dayBtn.setOnAction(e -> showDayAttractions(date));
            }

            calendarGrid.add(dayBtn, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }


    private void navigationparMois(int delta) {
        displayMonth(currentYearMonth.plusMonths(delta));
    }

    /**
     * Affiche les attractions disponibles pour un jour donné.
     *
     * @param date La date sélectionnée
     */
    private void showDayAttractions(LocalDate date) {
        reservationPanel.getChildren().clear();

        List<String> attractions = controller.getAttractionsForDay(date);

        if (attractions.isEmpty()) {
            reservationPanel.getChildren().add(new Label("Aucune attraction disponible"));
        } else {
            ComboBox<String> attractionCombo = new ComboBox<>();
            attractionCombo.getItems().addAll(attractions);
            attractionCombo.getSelectionModel().selectFirst();

            Button reserveBtn = new Button("Réserver");
            reserveBtn.setStyle(
                    "-fx-background-color: #e74c3c;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 15;" +
                            "-fx-padding: 6 15 6 15;"
            );

            reserveBtn.setOnAction(e -> {
                String selected = attractionCombo.getValue();

                if(date.isBefore(LocalDate.now())) {
                    showAlert(Alert.AlertType.WARNING,"Erreur","Cette date est dépassée");
                }
                else{
                    boolean ok = controller.reserverAttraction(selected, date, utilisateurConnecte);
                    if (ok) {
                        showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation ajoutée pour " + selected);
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Erreur", "Réservation impossible (rôle ou erreur BDD).");
                    }
                }

            });


            reservationPanel.getChildren().addAll(
                    new Label("Attractions disponibles le " + date + " :"),
                    attractionCombo,
                    reserveBtn
            );
        }
    }

    /**
     * Affiche une alerte.
     *
     * @param type Type de l'alerte
     * @param title Titre de l'alerte
     * @param message Message à afficher
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Retourne le mois et l'année actuellement affichés.
     *
     * @return Le YearMonth courant
     */
    public YearMonth getCurrentYearMonth() {
        return currentYearMonth;
    }
}
