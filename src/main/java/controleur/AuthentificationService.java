package controlleur;

import modele.Utilisateur;

import java.sql.*;

public class AuthentificationService {

    public Utilisateur connecter(String mail, String motDePasse, Connection conn) {
        try {
            // Vérification du mail dans la base de données pour admin
            Utilisateur utilisateur = verifier(conn, mail, motDePasse, "admin", "idAdmin", "Admin");
            if (utilisateur != null) {
                return utilisateur; // Connexion réussie
            }

            // Vérification du mail dans la base de données pour client
            utilisateur = verifier(conn, mail, motDePasse, "client", "idClient", "Client");
            return utilisateur; // Vérification dans la table client

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Si aucune connexion réussie, on retourne null
    }

    private Utilisateur verifier(Connection conn, String mail, String motDePasse, String table, String idField, String role) throws SQLException {
        String requete = "SELECT nom, prenom, mail, motDePasse FROM " + table + " WHERE mail = ?";
        try (PreparedStatement stmt = conn.prepareStatement(requete)) {
            stmt.setString(1, mail);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Si le mail est trouvé, vérifier le mot de passe
                if (rs.getString("motDePasse").equals(motDePasse)) {
                    // Connexion réussie, création de l'utilisateur avec le rôle approprié
                    return new Utilisateur(
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("mail"),
                            role // Assignation du rôle selon la table
                    );
                } else {
                    System.out.println("❌ Mot de passe incorrect.");
                    return null;
                }
            } else {
                return null;
            }
        }
    }
}       
