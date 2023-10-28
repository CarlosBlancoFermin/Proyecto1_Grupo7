package com.example.proyecto_entrega2_grupo7.entities;

public class Horario extends Filtros {
    private String horaEntrada;
    private String horaSalida;

    public Horario() {}

    public Horario(String nombre, String horaEntrada, String horaSalida) {
        super(nombre);
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
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
