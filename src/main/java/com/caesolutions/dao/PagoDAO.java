package com.caesolutions.dao;

import com.caesolutions.model.Pago;
import com.caesolutions.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {

    public List<Pago> getAllPagos() {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre_comercial + ' - ' + s.nombre as infoLicencia " +
                     "FROM Pagos p " +
                     "JOIN Licencias l ON p.id_licencia = l.id_licencia " +
                     "JOIN Clientes c ON l.id_cliente = c.id_cliente " +
                     "JOIN Sistemas s ON l.id_sistema = s.id_sistema " +
                     "ORDER BY p.fecha_pago DESC";
                     
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                Pago p = new Pago();
                p.setIdPago(rs.getInt("id_pago"));
                p.setIdLicencia(rs.getInt("id_licencia"));
                p.setFechaPago(rs.getDate("fecha_pago"));
                p.setMonto(rs.getDouble("monto"));
                // p.setComprobante(rs.getString("comprobante"));
                p.setNotas(rs.getString("notas"));
                
                // Virtual
                p.setInfoLicencia(rs.getString("infoLicencia"));
                
                pagos.add(p);
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo pagos: " + e.getMessage());
        }
        return pagos;
    }

    public boolean insertPago(Pago p) {
        String sql = "INSERT INTO Pagos (id_licencia, fecha_pago, monto, notas) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, p.getIdLicencia());
            stmt.setDate(2, new java.sql.Date(p.getFechaPago().getTime()));
            stmt.setDouble(3, p.getMonto());
            // stmt.setString(4, p.getComprobante());
            stmt.setString(4, p.getNotas());
            
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
