package controleur;

import modele.Utilisateur;
import java.sql.*;
                                                                    /// A SUPPR
public class AuthentificationService {

    ///  A ETE DEPLACE VERS LE MODELE (ou la fct est sensee etre)

/**
    public Utilisateur connecter(String mail, String motDePasse, Connection conn) {
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
    }**/
}
