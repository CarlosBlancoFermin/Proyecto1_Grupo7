package com.example.proyecto_entrega2_grupo7.database.dao;

import static com.example.proyecto_entrega2_grupo7.entities.Filtros.NUM_FILTROS;
import static com.example.proyecto_entrega2_grupo7.entities.Filtros.NOMBRES_FILTROS;

import com.example.proyecto_entrega2_grupo7.database.FirebaseCallback;
import com.example.proyecto_entrega2_grupo7.database.FirebaseListCallback;
import com.example.proyecto_entrega2_grupo7.entities.*;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * Funciones CRUD de la coleccion Usuario
 */
public class UsuarioDAO implements IServiceDAO {

    //Acceso a la colección usuarios de la BDD
    final CollectionReference DB_COLECCION;

    public UsuarioDAO() {
        DB_COLECCION = DB.getDatabase()
                .collection("usuarios");
    }

    @Override
    public void insertarRegistro(Object user) {
        DB_COLECCION.add(user).addOnSuccessListener(documentReference -> {
            ((Usuario) user).setId(documentReference.getId());
            DB_COLECCION.document(((Usuario)user).getId()).set(user).addOnSuccessListener(unused ->
                    System.out.println("usuario creado"));
        });
    }

    @Override
    public void obtenerRegistroPorId(String id, FirebaseCallback callback){
        DB_COLECCION.whereEqualTo("id", id).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Usuario usuario = new Usuario();
                for (QueryDocumentSnapshot doc : task.getResult()){
                    usuario = doc.toObject(Usuario.class);
                }
                callback.onCallback(usuario);
            }
        });
    }


    @Override
    public void obtenerTodos(FirebaseListCallback callback){
        DB_COLECCION.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<Usuario> usuarios = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()){
                    Usuario user = doc.toObject(Usuario.class);
                    usuarios.add(user);
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

    /**
     * Obtiene una lista con todos los emails,
     * salvo el pasado por parametro si no es null,
     * para evitar repeticiones de emails en inserts y updates
     * @param emailExcluido el email del usuario que se modifica, en su caso
     */
    public void obtenerTodosEmails(
            String emailExcluido,
           FirebaseListCallback callback){

        Query q = DB_COLECCION;
        if(emailExcluido != null){
            q = q.whereNotEqualTo("correo",emailExcluido);
        }

        q.get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null){
                List<String> correoList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()){
                    String correo = doc.toObject(Usuario.class).getCorreo();
                    correoList.add(correo);
                }
                callback.onCallback(correoList);
            }
        });

    }

    @Override
    public void actualizarRegistro(Object user){
        DB_COLECCION.document(((Usuario)user).getId()).set(user)
                .addOnSuccessListener(unused -> System.out.println("usuario actualizado"));
    }

    @Override
    public void borrarRegistro(Object user){
        DB_COLECCION.document(((Usuario)user).getId()).delete()
                .addOnSuccessListener(unused ->
                        System.out.println("usuario " + ((Usuario)user).getNombre() + "eliminado"));
    }

}




