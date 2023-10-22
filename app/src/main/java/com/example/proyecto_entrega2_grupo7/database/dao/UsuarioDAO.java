package com.example.proyecto_entrega2_grupo7.database.dao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_entrega2_grupo7.database.utils.Encriptador;
import com.example.proyecto_entrega2_grupo7.database.FirebaseListCallback;
import com.example.proyecto_entrega2_grupo7.database.FirebaseManager;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * Funciones CRUD de la coleccion Usuario
 */
public class UsuarioDAO extends AppCompatActivity {

    //Cuando se instancia este servicio se conecta a la base de datos
    FirebaseFirestore db = FirebaseManager.getDatabase();

    public void registrarusuario(String nombre, String apellidos, String puesto, String pass, String correo) {


        Usuario user = new Usuario();
        user.setId("");
        user.setNombre(nombre);
        user.setApellidos(apellidos);
        user.setPuesto(puesto);
        user.setPass(Encriptador.passEncriptada(pass));
        user.setCorreo(correo);
        db.collection("usuarios").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                user.setId(documentReference.getId());
                db.collection("usuarios").document(user.getId()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("usuario creado");
                    }
                });
            }
        });

    }

    public void actualizarusuario(Usuario user){
        db.collection("usuarios").document(user.getId()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("usuario actualizado");
            }
        });
    }

    public void obtenerusuarios(FirebaseListCallback callback){
        List<Usuario> listausuarios = new ArrayList<>();

        db.collection("usuarios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for (QueryDocumentSnapshot doc : task.getResult()){
                        Usuario user = doc.toObject(Usuario.class);
                        listausuarios.add(user);
                        System.out.println(user.getNombre());
                    }
                    callback.onCallback(listausuarios);
                }
            }
        });
    }
}




