package com.caesolutions.dao;

import com.caesolutions.model.Ticket;
import com.caesolutions.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT t.*, c.nombre_comercial as nombreCliente " +
                     "FROM Tickets_Soporte t " +
                     "JOIN Clientes c ON t.id_cliente = c.id_cliente " +
                     "ORDER BY t.fecha_creacion DESC";
                     
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                Ticket t = new Ticket();
                t.setIdTicket(rs.getInt("id_ticket"));
                t.setIdCliente(rs.getInt("id_cliente"));
                t.setAsunto(rs.getString("asunto"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setEstado(rs.getString("estado"));
                t.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                t.setFechaResolucion(rs.getTimestamp("fecha_resolucion"));
                t.setNotasResolucion(rs.getString("notas_resolucion"));
                
                t.setNombreCliente(rs.getString("nombreCliente"));
                
                tickets.add(t);
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo tickets: " + e.getMessage());
        }
        return tickets;
    }

    public boolean insertTicket(Ticket t) {
        String sql = "INSERT INTO Tickets_Soporte (id_cliente, asunto, descripcion, estado, fecha_creacion) VALUES (?, ?, ?, ?, GETDATE())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, t.getIdCliente());
            stmt.setString(2, t.getAsunto());
            stmt.setString(3, t.getDescripcion());
            stmt.setString(4, "Abierto"); // Default state
            
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTicketStatus(int idTicket, String estado, String notasResolucion) {
        String sql = "UPDATE Tickets_Soporte SET estado=?, notas_resolucion=?, fecha_resolucion=GETDATE() WHERE id_ticket=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, estado);
            stmt.setString(2, notasResolucion);
            stmt.setInt(3, idTicket);
            
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
