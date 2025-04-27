package vue;

import controleur.ControleurReporting;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modele.Attraction;
import modele.Utilisateur;

import java.util.List;

/**
 * VueReporting permet d'afficher un graphique à barres
 * représentant les statistiques des attractions et leur nombre de réservations.
 */
public class VueReporting {

    private ControleurReporting controleurReporting;

    /**
     * Constructeur de VueReporting.
     *
     * @param controleurReporting Le contrôleur qui fournit les données de reporting
     */
    public VueReporting(ControleurReporting controleurReporting) {
        this.controleurReporting = controleurReporting;
    }

    /**
     * Affiche la fenêtre des statistiques des attractions.
     *
     * @param stage La fenêtre JavaFX
     * @param utilisateur L'utilisateur connecté
     */
    public void afficher(Stage stage, Utilisateur utilisateur) {
        stage.setTitle("Statistiques des Attractions");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Button btnChargerStats = new Button("Charger les Statistiques");
        btnChargerStats.setOnAction(e -> chargerStats(root));

        root.setTop(btnChargerStats);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Charge les données de réservations et affiche le graphique à barres.
     *
     * @param root Le conteneur principal où placer le graphique
     */
    private void chargerStats(BorderPane root) {
        List<Attraction> attractions = controleurReporting.getAttractionsAvecReservations();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Attraction");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre de Réservations");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Réservations");

        for (Attraction attraction : attractions) {
            dataSeries.getData().add(new XYChart.Data<>(attraction.getNom(), attraction.getNombreReservations()));
        }

        barChart.getData().add(dataSeries);

        root.setCenter(barChart);
    }
}
