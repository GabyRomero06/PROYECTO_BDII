package com.caesolutions.model;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String passwordHash;
    private int estadoUsuario;

    public Usuario() {}

    public Usuario(int idUsuario, String nombreUsuario, String passwordHash, int estadoUsuario) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.passwordHash = passwordHash;
        this.estadoUsuario = estadoUsuario;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public int getEstadoUsuario() { return estadoUsuario; }
    public void setEstadoUsuario(int estadoUsuario) { this.estadoUsuario = estadoUsuario; }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", estadoUsuario=" + estadoUsuario +
                '}';
    }
}
