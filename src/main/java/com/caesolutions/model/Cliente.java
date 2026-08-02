package com.caesolutions.model;

public class Cliente {
    private int idCliente;
    private String nombreComercial;
    private String contactoPrincipal;
    private String telefono;
    private String email;
    private String direccion;
    private String rtnDni;

    public Cliente() {}

    public Cliente(int idCliente, String nombreComercial, String contactoPrincipal, String telefono, String email, String direccion, String rtnDni) {
        this.idCliente = idCliente;
        this.nombreComercial = nombreComercial;
        this.contactoPrincipal = contactoPrincipal;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.rtnDni = rtnDni;
    }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombreComercial() { return nombreComercial; }
    public void setNombreComercial(String nombreComercial) { this.nombreComercial = nombreComercial; }

    public String getContactoPrincipal() { return contactoPrincipal; }
    public void setContactoPrincipal(String contactoPrincipal) { this.contactoPrincipal = contactoPrincipal; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getRtnDni() { return rtnDni; }
    public void setRtnDni(String rtnDni) { this.rtnDni = rtnDni; }

    @Override
    public String toString() {
        return nombreComercial; // Útil para ComboBoxes o representaciones de texto
    }
}
