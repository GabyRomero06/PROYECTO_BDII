package com.caesolutions.dao;

import com.caesolutions.model.Empresa;
import com.caesolutions.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class EmpresaDAO {

    public Empresa getEmpresa() {
        String sql = "SELECT TOP 1 * FROM Empresa";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            if (rs.next()) {
                Empresa e = new Empresa();
                e.setIdEmpresa(rs.getInt("id_empresa"));
                e.setNombreEmpresa(rs.getString("nombre_empresa"));
                e.setTelefono(rs.getString("telefono"));
                e.setCorreo(rs.getString("correo"));
                e.setDireccion(rs.getString("direccion"));
                e.setLogoEmpresaRuta(rs.getString("logo_empresa_ruta"));
                return e;
            }
        } catch (Exception ex) {
            System.err.println("Error obteniendo datos de empresa: " + ex.getMessage());
        }
        return null;
    }

    public boolean updateEmpresa(Empresa e) {
        String sql = "UPDATE Empresa SET nombre_empresa=?, telefono=?, correo=?, direccion=? WHERE id_empresa=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, e.getNombreEmpresa());
            stmt.setString(2, e.getTelefono());
            stmt.setString(3, e.getCorreo());
            stmt.setString(4, e.getDireccion());
            stmt.setInt(5, e.getIdEmpresa());
            
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
