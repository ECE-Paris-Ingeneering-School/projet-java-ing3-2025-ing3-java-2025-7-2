package modele;

import modele.Utilisateur;
import java.sql.*;

public class AuthentificationService {

    public Utilisateur connecter(String mail, String motDePasse, Connection conn) {
        System.out.println("je suis au bon endroit ! (test)");
        try {
            // Vérification dans la table admin
            Utilisateur utilisateur = verifier(conn, mail, motDePasse, "admin", "idAdmin", "admin");
            if (utilisateur != null) {
                return utilisateur;
            }

            // Vérification dans la table client
            utilisateur = verifier(conn, mail, motDePasse, "client", "idClient", "client");
            return utilisateur;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Utilisateur verifier(Connection conn, String mail, String motDePasse, String table, String idField, String role) throws SQLException {
        System.out.println("je suis au bon endroit ! (test 2)");
        String requete = "SELECT " + idField + ", nom, prenom, mail, motDePasse FROM " + table + " WHERE mail = ?";
        try (PreparedStatement stmt = conn.prepareStatement(requete)) {
            stmt.setString(1, mail);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (rs.getString("motDePasse").equals(motDePasse)) {
                    int id = rs.getInt(idField);
                    return new Utilisateur(
                            id,
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("mail"),
                            role
                    );
                } else {
                    System.out.println("❌ Mot de passe incorrect.");
                }
            }
        }

        return null;
    }
}
