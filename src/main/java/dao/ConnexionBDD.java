package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBDD {
    public static Connection getConnexion() throws SQLException, ClassNotFoundException {

        /// A MODIF ? dossier config.properties ou changement de port global ?

        String url = "jdbc:mysql://localhost:3308/attraction";
        String user = "root";
        String motDePasse = "";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, motDePasse);
    }
}

