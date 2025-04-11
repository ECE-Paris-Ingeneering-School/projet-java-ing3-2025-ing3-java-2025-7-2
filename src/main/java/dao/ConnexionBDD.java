package dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnexionBDD {
    public static Connection getConnexion() throws SQLException, ClassNotFoundException, IOException {

        /// A MODIF ? dossier config.properties ou changement de port global ?
        Properties props = new Properties();
        props.load(new FileInputStream("config.properties"));
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String motDePasse = props.getProperty("db.password");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, motDePasse);
    }
}

