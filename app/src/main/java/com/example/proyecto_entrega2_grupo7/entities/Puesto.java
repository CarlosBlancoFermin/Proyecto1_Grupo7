package com.example.proyecto_entrega2_grupo7.entities;

public class Puesto {
    String id;
    String nombre;
    String salario;

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
