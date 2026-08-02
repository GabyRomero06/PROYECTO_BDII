package com.caesolutions.model;

public class Empresa {
    private int idEmpresa;
    private String nombreEmpresa;
    private String telefono;
    private String correo;
    private String direccion;
    private String logoEmpresaRuta;

    public Empresa() {}

    public int getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(int idEmpresa) { this.idEmpresa = idEmpresa; }

    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getLogoEmpresaRuta() { return logoEmpresaRuta; }
    public void setLogoEmpresaRuta(String logoEmpresaRuta) { this.logoEmpresaRuta = logoEmpresaRuta; }
}
