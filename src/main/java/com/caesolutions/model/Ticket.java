package com.caesolutions.model;

import java.util.Date;

public class Ticket {
    private int idTicket;
    private int idCliente;
    private String asunto;
    private String descripcion;
    private String estado;
    private Date fechaCreacion;
    private Date fechaResolucion;
    private String notasResolucion;

    // Virtual property for UI JOINs
    private String nombreCliente;

    public Ticket() {}

    public int getIdTicket() { return idTicket; }
    public void setIdTicket(int idTicket) { this.idTicket = idTicket; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Date getFechaResolucion() { return fechaResolucion; }
    public void setFechaResolucion(Date fechaResolucion) { this.fechaResolucion = fechaResolucion; }

    public String getNotasResolucion() { return notasResolucion; }
    public void setNotasResolucion(String notasResolucion) { this.notasResolucion = notasResolucion; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
}
