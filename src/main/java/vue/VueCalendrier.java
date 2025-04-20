package vue;

import controleur.Calendrier;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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

        // Navigation mois
        HBox topPanel = new HBox(10);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setPadding(new Insets(10));

        Button prevButton = new Button("<");
        Button nextButton = new Button(">");

        monthLabel = new Label();
        monthLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        prevButton.setOnAction(e -> navigateMonth(-1));
        nextButton.setOnAction(e -> navigateMonth(1));

        topPanel.getChildren().addAll(prevButton, monthLabel, nextButton);

        // Grille du calendrier
        calendarGrid = new GridPane();
        calendarGrid.setHgap(5);
        calendarGrid.setVgap(5);
        calendarGrid.setPadding(new Insets(10));

        ScrollPane calendarScroll = new ScrollPane(calendarGrid);
        calendarScroll.setFitToWidth(true);

        // Panel réservation
        reservationPanel = new VBox(10);
        reservationPanel.setPadding(new Insets(10));
        reservationPanel.setStyle("-fx-border-color: gray; -fx-border-width: 1;");

        // Bas de page : bouton
        Button voirReservationsBtn = new Button("Voir mes réservations");
        voirReservationsBtn.setOnAction(e -> {
            VueReservations vueRes = new VueReservations(utilisateurConnecte);
            vueRes.afficher(new Stage());
        });

        HBox bottomPanel = new HBox(voirReservationsBtn);
        bottomPanel.setPadding(new Insets(10));
        bottomPanel.setAlignment(Pos.CENTER_RIGHT);

        root.setTop(topPanel);
        root.setCenter(calendarScroll);
        root.setRight(reservationPanel);
        root.setBottom(bottomPanel);

        controller.initializeCalendar();

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Calendrier - Connecté : " + utilisateurConnecte.getPrenom());
        stage.setScene(scene);
        stage.show();
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

    public YearMonth getCurrentYearMonth() {
        return currentYearMonth;
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

            reserveBtn.setOnAction(e -> {
                if ("admin".equalsIgnoreCase(utilisateurConnecte.getRole())) {
                    showAlert(Alert.AlertType.WARNING, "Accès refusé", "Les administrateurs ne peuvent pas réserver.");
                    return;
                }

                try {
                    String selected = attractionCombo.getValue();
                    int idAttr = new AttractionDAO(ConnexionBDD.getConnexion()).getAttractionIdByName(selected);
                    new ReservationDAO(ConnexionBDD.getConnexion()).ajouterReservation(utilisateurConnecte.getId(), idAttr, Date.valueOf(date));
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation ajoutée pour " + selected);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la réservation.");
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
}
