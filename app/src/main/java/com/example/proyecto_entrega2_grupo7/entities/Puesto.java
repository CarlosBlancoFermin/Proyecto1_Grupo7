package com.example.proyecto_entrega2_grupo7.entities;

public class Puesto {
    private String id;
    private String nombre;
    private String salario;

    public Puesto(String nombre, String salario) {
        this.nombre = nombre;
        this.salario = salario;
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
