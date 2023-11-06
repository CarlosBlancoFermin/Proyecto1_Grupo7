package com.example.proyecto_entrega2_grupo7.entities;

import java.util.Objects;

/**
 * Superclase abstracta de la que deben heredar
 * todas las clases que representen entidades/tablas
 * de la base de datos y puedan actuar como filtro.
 * Todos los Filtros disponen de un atributo id (String)
 * y un nombre (String).
 * (Actualmente heredan Puesto y Horario)
 */
public abstract class Filtros {
    public static String [] NOMBRES_FILTROS = new String [] {"puesto","horario"};
    //nombre de los atributos de usuario que sirven de filtro
    public static int NUM_FILTROS = 2;

    protected String id;
    protected String nombre;

    public Filtros() {}

    public Filtros(String nombre) {
        this.nombre = nombre;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filtros filtros = (Filtros) o;
        return Objects.equals(id, filtros.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
