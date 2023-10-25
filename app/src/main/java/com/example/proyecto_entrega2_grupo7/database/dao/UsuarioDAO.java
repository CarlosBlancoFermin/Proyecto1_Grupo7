package com.example.proyecto_entrega2_grupo7.database.dao;

import androidx.annotation.NonNull;

import com.example.proyecto_entrega2_grupo7.database.FirebaseListCallback;
import com.example.proyecto_entrega2_grupo7.database.FirebaseManager;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
//        user.setId("");
//        user.setNombre(nombre);
//        user.setApellidos(apellidos);
//        user.setPuesto(puesto);
//        user.setPass(Encriptador.passEncriptada(pass));
//        user.setCorreo(correo);
        DB_COLECCION.add(user).addOnSuccessListener(documentReference -> {
            user.setId(documentReference.getId());
            DB_COLECCION.document(user.getId()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    System.out.println("usuario creado");
                }
            });
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

    public void actualizarUsuario(Usuario user){
        DB_COLECCION.document(user.getId()).set(user)
                .addOnSuccessListener(unused -> System.out.println("usuario actualizado"));
    }

    public void borrarUsuario(Usuario user){
        DB_COLECCION.document(user.getId()).delete()
                .addOnSuccessListener(unused -> System.out.println("usuario " + user.getNombre() + "eliminado"));
    }

}




