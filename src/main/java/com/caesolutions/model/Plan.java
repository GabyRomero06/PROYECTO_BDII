package com.caesolutions.model;

public class Plan {
    private int idPlan;
    private String nombre;
    private String tipo;

    public Plan() {}

    public Plan(int idPlan, String nombre, String tipo) {
        this.idPlan = idPlan;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public int getIdPlan() { return idPlan; }
    public void setIdPlan(int idPlan) { this.idPlan = idPlan; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ")";
    }
}
