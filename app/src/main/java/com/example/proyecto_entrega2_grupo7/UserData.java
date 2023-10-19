package com.example.proyecto_entrega2_grupo7;

public class UserData {
   String ID;

    public String getId() {
        return ID;
    }

    public void setId(String id) {

        this.ID = id;
    }

    String Nombre;
    String Apellidos;

    String Puesto;
    public String getApellidos() {
        return Apellidos;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getPuesto() {
        return Puesto;
    }

    public void setPuesto(String puesto) {
        Puesto = puesto;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    UserData(){
        ID="";
        Nombre = "";
        Apellidos = "";
        Puesto = "";
    }
    UserData(String id,String nombre, String apellidos,String puesto){
        this.ID = id;
        this.Nombre = nombre;
        this.Apellidos = apellidos;
        this.Puesto = puesto;
    }
}
