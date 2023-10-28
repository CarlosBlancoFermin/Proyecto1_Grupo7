package com.example.proyecto_entrega2_grupo7.entities;

/**
 * Superclase abstracta de la que deben heredar
 * todas las clases que representen entidades/tablas
 * de la base de datos y puedan actuar como filtro.
 * Todos los Filtros disponen de un atributo id (String)
 * y un nombre (String).
 */
public abstract class Filtros {
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
}
