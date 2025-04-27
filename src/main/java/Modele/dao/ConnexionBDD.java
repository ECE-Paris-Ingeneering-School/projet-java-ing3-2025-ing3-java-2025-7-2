package modele.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe qui établie les connexions à la BDD
 */
public class ConnexionBDD {

    /**
     * Établit et retourne une connexion à la base de données en utilisant les informations du fichier de configuration.
     *
     * @return une instance de {@link Connection} pour interagir avec la base de données
     * @throws SQLException si une erreur SQL survient lors de la connexion
     * @throws ClassNotFoundException si le driver JDBC n'est pas trouvé
     * @throws IOException si une erreur d'entrée/sortie survient lors de la lecture du fichier de configuration
     */
    public static Connection getConnexion() throws SQLException, ClassNotFoundException, IOException {


        Properties props = new Properties();
        props.load(new FileInputStream("config.properties"));
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String motDePasse = props.getProperty("db.password");

        //Chargement du driver JDBC
        Class.forName("com.mysql.cj.jdbc.Driver");

        //Retourne la connexion à la BDD
        return DriverManager.getConnection(url, user, motDePasse);
    }

}

