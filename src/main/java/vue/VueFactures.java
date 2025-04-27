package vue;

import controleur.ControleurFactures;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import modele.Utilisateur;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class VueFactures {

    private final ControleurFactures controleurFactures;
    private final Utilisateur utilisateur;

    public VueFactures(ControleurFactures controleurFactures, Utilisateur utilisateur) throws SQLException, IOException, ClassNotFoundException {
        this.controleurFactures = controleurFactures;
        this.utilisateur = utilisateur;
        afficherFactures();
    }

    public void afficherFactures() throws SQLException, IOException, ClassNotFoundException {
        Stage stage = new Stage();
        stage.setTitle("Mes factures");

        TableView<FactureFX> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(340);

        TableColumn<FactureFX, Integer> idCol = new TableColumn<>("ID Facture");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idFacture"));
        idCol.setStyle("-fx-font-weight: bold; -fx-alignment: CENTER;");
        idCol.setPrefWidth(80);

        TableColumn<FactureFX, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateFacture"));
        dateCol.setStyle("-fx-alignment: CENTER;");
        dateCol.setPrefWidth(110);

        TableColumn<FactureFX, Double> prixCol = new TableColumn<>("Prix (€)");
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
        prixCol.setStyle("-fx-alignment: CENTER;");
        prixCol.setPrefWidth(80);

        table.getColumns().addAll(idCol, dateCol, prixCol);

        List<Object[]> factures = controleurFactures.getFacturesDuClient(utilisateur);
        ObservableList<FactureFX> donnees = FXCollections.observableArrayList();

        for (Object[] f : factures) {
            int id = (int) f[0];
            Date date = (Date) f[1];
            double prix = (double) f[2];
            donnees.add(new FactureFX(id, date, prix));
        }
        table.setItems(donnees);

        Label titre = new Label("Mes factures");
        titre.setFont(Font.font("Arial", 22));
        titre.setStyle("-fx-text-fill: #2c3e50;");
        titre.setAlignment(Pos.CENTER);
        titre.setMaxWidth(Double.MAX_VALUE);

        VBox contenu = new VBox(18, titre, table);
        contenu.setAlignment(Pos.TOP_CENTER);
        contenu.setPadding(new Insets(15, 10, 15, 10));


        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #d0f5c8;");
        root.setCenter(contenu);

        Scene scene = new Scene(root, 400, 400); // format mobile
        stage.setScene(scene);
        stage.show();
    }


    // Classe interne pour lier les données à la vue
    public static class FactureFX {
        private final int idFacture;
        private final Date dateFacture;
        private final double prix;

        public FactureFX(int idFacture, Date dateFacture, double prix) {
            this.idFacture = idFacture;
            this.dateFacture = dateFacture;
            this.prix = prix;
        }

        public int getIdFacture() {
            return idFacture;
        }

        public Date getDateFacture() {
            return dateFacture;
        }

        public double getPrix() {
            return prix;
        }
    }
}
