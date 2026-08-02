package com.caesolutions.dao;

import com.caesolutions.model.Licencia;
import com.caesolutions.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LicenciaDAO {
    
    public List<Licencia> getAllLicencias() {
        List<Licencia> licencias = new ArrayList<>();
        String sql = "SELECT l.*, c.nombre_comercial as nombreCliente, s.nombre as nombreSistema, p.nombre as nombrePlan " +
                     "FROM Licencias l " +
                     "JOIN Clientes c ON l.id_cliente = c.id_cliente " +
                     "JOIN Sistemas s ON l.id_sistema = s.id_sistema " +
                     "JOIN Planes p ON l.id_plan = p.id_plan " +
                     "ORDER BY l.fecha_proximo_pago ASC";
                     
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                Licencia l = new Licencia();
                l.setIdLicencia(rs.getInt("id_licencia"));
                l.setIdCliente(rs.getInt("id_cliente"));
                l.setIdSistema(rs.getInt("id_sistema"));
                l.setIdPlan(rs.getInt("id_plan"));
                l.setFechaAdquisicion(rs.getDate("fecha_adquisicion"));
                l.setFechaProximoPago(rs.getDate("fecha_proximo_pago"));
                l.setEstado(rs.getString("estado"));
                l.setPrecioAcordado(rs.getDouble("precio_acordado"));
                l.setDeudaMeses(rs.getInt("deuda_meses"));
                l.setRecargoAplicado(rs.getDouble("recargo_aplicado"));
                
                // Virtual properties
                l.setNombreCliente(rs.getString("nombreCliente"));
                l.setNombreSistema(rs.getString("nombreSistema"));
                l.setNombrePlan(rs.getString("nombrePlan"));
                
                licencias.add(l);
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo licencias: " + e.getMessage());
        }
        return licencias;
    }

    public boolean insertLicencia(Licencia l) {
        String sql = "INSERT INTO Licencias (id_cliente, id_sistema, id_plan, fecha_adquisicion, fecha_proximo_pago, estado, precio_acordado, deuda_meses, recargo_aplicado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, l.getIdCliente());
            stmt.setInt(2, l.getIdSistema());
            stmt.setInt(3, l.getIdPlan());
            stmt.setDate(4, new java.sql.Date(l.getFechaAdquisicion().getTime()));
            stmt.setDate(5, new java.sql.Date(l.getFechaProximoPago().getTime()));
            stmt.setString(6, l.getEstado());
            stmt.setDouble(7, l.getPrecioAcordado());
            stmt.setInt(8, l.getDeudaMeses());
            stmt.setDouble(9, l.getRecargoAplicado());
            
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // updateLicencia and deleteLicencia are left as an exercise to build upon for full CRUD.
    public boolean deleteLicencia(int id) {
        String sql = "DELETE FROM Licencias WHERE id_licencia=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
