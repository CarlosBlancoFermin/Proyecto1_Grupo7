package com.example.proyecto_entrega2_grupo7.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.proyecto_entrega2_grupo7.database.utils.UtilsEncriptador;

import java.util.Objects;

public class Usuario implements Comparable<Usuario>, Parcelable {
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
        this.pass = UtilsEncriptador.passEncriptada(pass);
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.puesto = puesto;
        this.horario = horario;
    }

    //Constructor Parcelable
    protected Usuario(Parcel in) {
        id = in.readString();
        correo = in.readString();
        pass = in.readString();
        nombre = in.readString();
        apellidos = in.readString();
        puesto = in.readString();
        horario = in.readString();
    }

    //Getters y setters
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
                ", correo='" + correo + '\'' +
                ", pass='" + pass + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", puesto='" + puesto + '\'' +
                ", horario='" + horario + '\'' +
                '}';
    }

    public boolean sinCambios(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) && Objects.equals(correo, usuario.correo) && Objects.equals(pass, usuario.pass) && Objects.equals(nombre, usuario.nombre) && Objects.equals(apellidos, usuario.apellidos) && Objects.equals(puesto, usuario.puesto) && Objects.equals(horario, usuario.horario);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Metodo para ordenar los usuarios
     * por apellido
     * @param u
     * @return
     */
    @Override
    public int compareTo(Usuario u) {
        return this.getApellidos().toUpperCase().compareTo(u.getApellidos().toUpperCase());
    }

    //Metodos Parcelable
    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(correo);
        parcel.writeString(pass);
        parcel.writeString(nombre);
        parcel.writeString(apellidos);
        parcel.writeString(puesto);
        parcel.writeString(horario);
    }
}


