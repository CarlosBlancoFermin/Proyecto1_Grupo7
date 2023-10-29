package com.example.proyecto_entrega2_grupo7.entities;

import com.example.proyecto_entrega2_grupo7.database.utils.Encriptador;

import java.io.Serializable;

public class Usuario implements Comparable<Usuario>, Serializable {
    private String id;
    private String correo;
    private String pass;
    private String nombre;
    private String apellidos;
    private String puesto;
    private String horario;


    public Usuario() {
    }

    public Usuario(String correo, String pass, String nombre, String apellidos, String puesto, String horario) {
        this.correo = correo;
        this.pass = Encriptador.passEncriptada(pass);
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.puesto = puesto;
        this.horario = horario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "ID='" + id + '\'' +
                ", correo='" + correo + '\'' +
                ", pass='" + pass + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", puesto='" + puesto + '\'' +
                ", horario='" + horario + '\'' +
                '}';
    }

    @Override
    public int compareTo(Usuario u) {
        return this.getApellidos().toUpperCase().compareTo(u.getApellidos().toUpperCase());
    }
}
