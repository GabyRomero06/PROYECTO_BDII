package com.caesolutions.dao;

import com.caesolutions.model.Sistema;
import com.caesolutions.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SistemaDAO {
    public List<Sistema> getAllSistemas() {
        List<Sistema> sistemas = new ArrayList<>();
        String sql = "SELECT * FROM Sistemas ORDER BY nombre ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sistemas.add(new Sistema(
                    rs.getInt("id_sistema"),
                    rs.getString("nombre"),
                    rs.getString("descripcion")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sistemas;
    }
}
