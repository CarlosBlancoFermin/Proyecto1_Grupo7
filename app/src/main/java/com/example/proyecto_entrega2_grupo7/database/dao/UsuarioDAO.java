package com.example.proyecto_entrega2_grupo7.database.dao;

import static com.example.proyecto_entrega2_grupo7.entities.Filtros.NUM_FILTROS;
import static com.example.proyecto_entrega2_grupo7.entities.Filtros.NOMBRES_FILTROS;

import android.widget.TextView;

import com.example.proyecto_entrega2_grupo7.database.FirebaseCallback;
import com.example.proyecto_entrega2_grupo7.database.FirebaseListCallback;
import com.example.proyecto_entrega2_grupo7.database.FirebaseManager;
import com.example.proyecto_entrega2_grupo7.entities.*;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * Funciones CRUD de la coleccion Usuario
 */
public class UsuarioDAO {

    //Cuando se instancia este servicio se conecta a la base de datos
    final CollectionReference DB_COLECCION =
                FirebaseManager.getDatabase()
                    .collection("usuarios");


    public void registrarUsuario(String correo, String pass, String nombre, String apellidos, String puesto, String horario) {
        Usuario user = new Usuario(correo,pass,nombre,apellidos,puesto,horario);
        DB_COLECCION.add(user).addOnSuccessListener(documentReference -> {
            user.setId(documentReference.getId());
            DB_COLECCION.document(user.getId()).set(user).addOnSuccessListener(unused ->
                    System.out.println("usuario creado"));
        });

    }

    public void obtenerUsuario(String userId, FirebaseCallback callback){
        DB_COLECCION.whereEqualTo("id", userId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Usuario usuario = new Usuario();
                for (QueryDocumentSnapshot doc : task.getResult()){
                    usuario = doc.toObject(Usuario.class);;
                }
                callback.onCallback(usuario);
            }
        });
    }

    public void obtenerUsuarios(FirebaseListCallback callback){
        DB_COLECCION.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<Usuario> usuarios = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()){
                    Usuario user = doc.toObject(Usuario.class);
                    usuarios.add(user);
                    System.out.println(user.getNombre());
                }
                callback.onCallback(usuarios);
            }
        });
    }

    /**
     * Obtiene una lista de Usuarios
     * cuyos atributos coincidan con alguno de los elementos
     * de las respectivas listas pasadas por parámetro en un array.
     * (Actualmente Puesto y Horario)
     * Si alguna de la lista es null,
     * significa que ese filtro no se aplica (es decir, vale cualquier valor)
     * @param filtrosMarcados array con una lista por cada filtro con los elementos marcados
     * @param callback callback de Firebase
     */
    public void obtenerUsuariosFiltro(
            List[] filtrosMarcados,
            FirebaseListCallback callback){

        Query q = DB_COLECCION;
        for(int i = 0; i < NUM_FILTROS; i++)
            if(filtrosMarcados[i] != null)
                q = q.whereIn(NOMBRES_FILTROS[i],filtrosMarcados[i]);

        q.get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null){
                List<Usuario> usuarios = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()){
                    Usuario user = doc.toObject(Usuario.class);
                    usuarios.add(user);
                }
                callback.onCallback(usuarios);
            }
        });
    }

    public void actualizarUsuario(Usuario user){
        DB_COLECCION.document(user.getId()).set(user)
                .addOnSuccessListener(unused -> System.out.println("usuario actualizado"));
    }

    public void borrarUsuario(Usuario user){
        DB_COLECCION.document(user.getId()).delete()
                .addOnSuccessListener(unused -> System.out.println("usuario " + user.getNombre() + "eliminado"));
    }

    //El único parámetro que puede recibir esta función es un objeto Usuario o su id
    public void detallesUsuario(TextView nombreTextView, TextView apellidoTextView,
                                TextView correoTextView, TextView puestoTextView, String userId){

        DB_COLECCION.whereEqualTo("id", userId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){

                //Esto no puede ir aqui, la consulta tiene que devolver un objeto Usuario.
                //El contenido de los editText los tiene que gestionar la Activity
                QuerySnapshot document = task.getResult();
                String nombreEmployee = null;
                String apellidoEmployee = null;
                String correoEmployee = null;
                //telefono = snapdocument.getString("telefono");
                String puestoEmployee = null;
                for (QueryDocumentSnapshot snapdocument: document){
                    nombreEmployee = snapdocument.getString("nombre");
                    apellidoEmployee  = snapdocument.getString("apellidos");
                    correoEmployee = snapdocument.getString("correo");
                    //telefono = snapdocument.getString("telefono");
                    puestoEmployee = snapdocument.getString("puesto");
                }
                nombreTextView.setText(nombreEmployee);
                apellidoTextView.setText(apellidoEmployee);
                correoTextView.setText(correoEmployee);
                puestoTextView.setText(puestoEmployee);
            }
        });
    }

}




