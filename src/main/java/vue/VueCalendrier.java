package vue;

import controleur.Calendrier;
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
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class VueCalendrier {

    private Calendrier controller;
    private YearMonth currentYearMonth;
    private GridPane calendarGrid;
    private Label monthLabel;
    private VBox reservationPanel;
    private Utilisateur utilisateurConnecte;
    private Stage stage;

    public VueCalendrier(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        this.controller = new Calendrier(this);
    }

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

        prevButton.setOnAction(e -> navigateMonth(-1));
        nextButton.setOnAction(e -> navigateMonth(1));

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

        // ===== Placement dans BorderPane =====
        root.setCenter(centerContent);
        root.setBottom(navBar);

        controller.initializeCalendar();

        Scene scene = new Scene(root, 350, 600);
        stage.setTitle("Calendrier - " + utilisateurConnecte.getPrenom());
        stage.setScene(scene);
        stage.show();
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

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            Button dayBtn = new Button(String.valueOf(day));
            dayBtn.setMaxWidth(Double.MAX_VALUE);
            dayBtn.setOnAction(e -> showDayAttractions(date));
            calendarGrid.add(dayBtn, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private void navigateMonth(int delta) {
        displayMonth(currentYearMonth.plusMonths(delta));
    }

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

                boolean ok = controller.reserverAttraction(selected, date, utilisateurConnecte);
                if (ok) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation ajoutée pour " + selected);
                } else {
                    showAlert(Alert.AlertType.WARNING, "Erreur", "Réservation impossible (rôle ou erreur BDD).");
                }
            });


            reservationPanel.getChildren().addAll(
                    new Label("Attractions disponibles le " + date + " :"),
                    attractionCombo,
                    reserveBtn
            );
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public YearMonth getCurrentYearMonth() {
        return currentYearMonth;
    }
}
