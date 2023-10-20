package com.example.proyecto_entrega2_grupo7;

public class UserData {
   String ID;
    String Nombre;
    String Apellidos;
    String Puesto;
    String correo;

    @Override
    public String toString() {
        return "UserData{" +
                "ID='" + ID + '\'' +
                ", Nombre='" + Nombre + '\'' +
                ", Apellidos='" + Apellidos + '\'' +
                ", Puesto='" + Puesto + '\'' +
                ", correo='" + correo + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }

    String pass;

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
    public String getId() {
        return ID;
    }

    public void setId(String id) {

        this.ID = id;
    }
}
