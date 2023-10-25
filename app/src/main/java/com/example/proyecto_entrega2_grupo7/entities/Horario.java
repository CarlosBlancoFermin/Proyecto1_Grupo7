package com.example.proyecto_entrega2_grupo7.entities;

public class Horario {
    private String id;
    private String nombre;
    private String horaEntrada;
    private String horaSalida;


    public Horario(String descripcion, String horaEntrada, String horaSalida) {
        this.nombre = descripcion;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    @Override
    public String toString() {
        return "Horario{" +
                "id='" + id + '\'' +
                ", descripcion='" + nombre + '\'' +
                ", horaEntrada='" + horaEntrada + '\'' +
                ", horaSalida='" + horaSalida + '\'' +
                '}';
    }
}
