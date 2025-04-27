package modele;

import modele.Utilisateur;
import java.sql.*;

/**
 * Service d'authentification permettant de connecter un utilisateur dans l'app
 * Identifie s'il s'agit d'un admin ou d'un client
 */
public class AuthentificationService {

    /**
     * Tente de connecter un utilisateur en vérifiant son adresse email et son mot de passe
     * Cette méthode vérifie d'abord les identifiants dans la table des administrateurs, puis dans la table clients
     *
     * @param mail L'email de l'utilisateur
     * @param motDePasse Le mot de passe de l'utilisateur
     * @param conn La connexion à la base de données
     * @return Un objet {@link Utilisateur} si les identifiants sont valides, sinon null
     */
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

    /**
     * Vérifie les identifiants d'un utilisateur dans une table spécifique (admin ou client).
     * Si les identifiants sont valides, un objet {@link Utilisateur} est retourné.
     *
     * @param conn La connexion à la base de données
     * @param mail L'adresse email de l'utilisateur
     * @param motDePasse Le mot de passe de l'utilisateur
     * @param table Le nom de la table dans laquelle vérifier les identifiants (admin ou client)
     * @param idField Le nom du champ représentant l'ID dans la table (idAdmin ou idClient)
     * @param role Le rôle de l'utilisateur (admin ou client)
     * @return Un objet {@link Utilisateur} si les identifiants sont valides, sinon null
     * @throws SQLException Si une erreur survient lors de la requête SQL
     */
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
