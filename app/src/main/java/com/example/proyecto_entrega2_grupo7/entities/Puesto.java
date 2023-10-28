package com.example.proyecto_entrega2_grupo7.entities;

public class Puesto extends Filtros {
    private String salario;

    public Puesto() {}

    public Puesto(String nombre, String salario) {
        super(nombre);
        this.salario = salario;
    }

    public String getSalario() {
        return salario;
    }

    public void setSalario(String salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return "Puesto{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", salario='" + salario + '\'' +
                '}';
    }
}
