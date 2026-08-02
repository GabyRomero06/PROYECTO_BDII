package com.caesolutions.model;

import java.util.Date;

public class Pago {
    private int idPago;
    private int idLicencia;
    private Date fechaPago;
    private double monto;
    private String comprobante;
    private String notas;

    // Extra virtual fields for UI viewing
    private String infoLicencia; // e.g. "Cliente A - Sistema X"
    
    public Pago() {}

    public int getIdPago() { return idPago; }
    public void setIdPago(int idPago) { this.idPago = idPago; }

    public int getIdLicencia() { return idLicencia; }
    public void setIdLicencia(int idLicencia) { this.idLicencia = idLicencia; }

    public Date getFechaPago() { return fechaPago; }
    public void setFechaPago(Date fechaPago) { this.fechaPago = fechaPago; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getComprobante() { return comprobante; }
    public void setComprobante(String comprobante) { this.comprobante = comprobante; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getInfoLicencia() { return infoLicencia; }
    public void setInfoLicencia(String infoLicencia) { this.infoLicencia = infoLicencia; }
}
