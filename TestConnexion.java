import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestConnexion {
    public static void main(String[] args) {
        try (Connection conn = ConnexionBDD.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Connexion réussie à la base de données !");

                // Test : on affiche les clients
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM attraction");

                System.out.println("Liste des attractions :");
                while (rs.next()) {
                    String nom = rs.getString("nom");
                    double prix = rs.getDouble("prix");
                    System.out.println("- " + nom + " (" + prix + " €)");
                }


                rs.close();
                stmt.close();
            } else {
                System.out.println("❌ Connexion échouée.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
