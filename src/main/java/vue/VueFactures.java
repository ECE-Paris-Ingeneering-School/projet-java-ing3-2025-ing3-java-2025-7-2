package vue;

import controleur.ControleurFactures;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
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

        TableColumn<FactureFX, Integer> idCol = new TableColumn<>("ID Facture");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idFacture"));

        TableColumn<FactureFX, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateFacture"));

        TableColumn<FactureFX, Double> prixCol = new TableColumn<>("Prix (€)");
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));

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

        VBox root = new VBox(table);
        root.setPadding(new Insets(10));
        Scene scene = new Scene(root, 500, 400);

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
