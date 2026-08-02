package com.caesolutions.dao;

import com.caesolutions.model.Cliente;
import com.caesolutions.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public List<Cliente> getAllClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes ORDER BY nombre_comercial ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                clientes.add(mapResultSetToCliente(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo clientes: " + e.getMessage());
        }
        return clientes;
    }

    public boolean insertCliente(Cliente c) {
        String sql = "INSERT INTO Clientes (nombre_comercial, contacto_principal, telefono, email, direccion, rtn_dni) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, c.getNombreComercial());
            stmt.setString(2, c.getContactoPrincipal());
            stmt.setString(3, c.getTelefono());
            stmt.setString(4, c.getEmail());
            stmt.setString(5, c.getDireccion());
            stmt.setString(6, c.getRtnDni());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error insertando cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCliente(Cliente c) {
        String sql = "UPDATE Clientes SET nombre_comercial=?, contacto_principal=?, telefono=?, email=?, direccion=?, rtn_dni=? WHERE id_cliente=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, c.getNombreComercial());
            stmt.setString(2, c.getContactoPrincipal());
            stmt.setString(3, c.getTelefono());
            stmt.setString(4, c.getEmail());
            stmt.setString(5, c.getDireccion());
            stmt.setString(6, c.getRtnDni());
            stmt.setInt(7, c.getIdCliente());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCliente(int idCliente) {
        String sql = "DELETE FROM Clientes WHERE id_cliente=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminando cliente: " + e.getMessage());
            return false;
        }
    }

    private Cliente mapResultSetToCliente(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("id_cliente"),
            rs.getString("nombre_comercial"),
            rs.getString("contacto_principal"),
            rs.getString("telefono"),
            rs.getString("email"),
            rs.getString("direccion"),
            rs.getString("rtn_dni")
        );
    }
}
