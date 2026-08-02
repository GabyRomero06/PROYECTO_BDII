package com.caesolutions.model;

import java.util.Date;

public class Licencia {
    private int idLicencia;
    private int idCliente;
    private int idSistema;
    private int idPlan;
    private Date fechaAdquisicion;
    private Date fechaProximoPago;
    private String estado;
    private double precioAcordado;
    private int deudaMeses;
    private double recargoAplicado;

    // Campos extra para la vista de tabla (provenientes de los JOINs)
    private String nombreCliente;
    private String nombreSistema;
    private String nombrePlan;

    public Licencia() {}

    public int getIdLicencia() { return idLicencia; }
    public void setIdLicencia(int idLicencia) { this.idLicencia = idLicencia; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdSistema() { return idSistema; }
    public void setIdSistema(int idSistema) { this.idSistema = idSistema; }

    public int getIdPlan() { return idPlan; }
    public void setIdPlan(int idPlan) { this.idPlan = idPlan; }

    public Date getFechaAdquisicion() { return fechaAdquisicion; }
    public void setFechaAdquisicion(Date fechaAdquisicion) { this.fechaAdquisicion = fechaAdquisicion; }

    public Date getFechaProximoPago() { return fechaProximoPago; }
    public void setFechaProximoPago(Date fechaProximoPago) { this.fechaProximoPago = fechaProximoPago; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getPrecioAcordado() { return precioAcordado; }
    public void setPrecioAcordado(double precioAcordado) { this.precioAcordado = precioAcordado; }

    public int getDeudaMeses() { return deudaMeses; }
    public void setDeudaMeses(int deudaMeses) { this.deudaMeses = deudaMeses; }

    public double getRecargoAplicado() { return recargoAplicado; }
    public void setRecargoAplicado(double recargoAplicado) { this.recargoAplicado = recargoAplicado; }

    // Getters y Setters para las columnas virtuales (JOIN)
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getNombreSistema() { return nombreSistema; }
    public void setNombreSistema(String nombreSistema) { this.nombreSistema = nombreSistema; }

    public String getNombrePlan() { return nombrePlan; }
    public void setNombrePlan(String nombrePlan) { this.nombrePlan = nombrePlan; }
}
