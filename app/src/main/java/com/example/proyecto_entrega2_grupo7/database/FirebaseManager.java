package com.example.proyecto_entrega2_grupo7.database;

import com.google.firebase.firestore.FirebaseFirestore;


/**
 * Clase que gestiona la conexion con la base de datos
 * en una unica instancia siguiendo el patron Singleton.
 * La primera vez que es instanciada crea la conexion con la base de datos;
 * cuando se instancie por segunda vez, obtendrá la conexión ya establecida.
 */
public class FirebaseManager {
    private static FirebaseFirestore mDatabase;

    public static FirebaseFirestore getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseFirestore.getInstance();
        }
        return mDatabase;
    }
}
