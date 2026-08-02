package com.caesolutions.dao;

import com.caesolutions.model.Plan;
import com.caesolutions.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlanDAO {
    public List<Plan> getAllPlanes() {
        List<Plan> planes = new ArrayList<>();
        String sql = "SELECT * FROM Planes ORDER BY nombre ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                planes.add(new Plan(
                    rs.getInt("id_plan"),
                    rs.getString("nombre"),
                    rs.getString("tipo")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return planes;
    }
}
