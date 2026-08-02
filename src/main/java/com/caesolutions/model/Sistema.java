package com.caesolutions.model;

public class Sistema {
    private int idSistema;
    private String nombre;
    private String descripcion;

    public Sistema() {}

    public Sistema(int idSistema, String nombre, String descripcion) {
        this.idSistema = idSistema;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdSistema() { return idSistema; }
    public void setIdSistema(int idSistema) { this.idSistema = idSistema; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return nombre;
    }
}
