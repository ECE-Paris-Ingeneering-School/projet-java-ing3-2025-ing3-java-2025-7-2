package modele.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttractionDAO {
    private Connection conn;

    public AttractionDAO(Connection conn) {
        this.conn = conn;
    }

    public List<String> getAllAttractionNames() throws SQLException {
        List<String> attractions = new ArrayList<>();
        String query = "SELECT nom FROM attraction";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                attractions.add(rs.getString("nom"));
            }
        }
        return attractions;
    }

    public int getAttractionIdByName(String name) throws SQLException {
        String query = "SELECT idAttraction FROM attraction WHERE nom = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idAttraction");
                }
            }
        }
        return -1;
    }
}

