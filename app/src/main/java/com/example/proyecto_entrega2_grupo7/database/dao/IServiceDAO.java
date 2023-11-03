package com.example.proyecto_entrega2_grupo7.database.dao;

import com.example.proyecto_entrega2_grupo7.database.FirebaseListCallback;
import com.example.proyecto_entrega2_grupo7.database.FirebaseManager;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Interfaz con los métodos básicos a implementar
 * por las clases de acceso a datos (DAO)
 * de las diferentes colecciones
 */
public interface IServiceDAO {

    FirebaseFirestore DB = FirebaseManager.getDatabase();

    /**
     * Recibe un objeto y lo inserta en la coleccion
     * @param element
     */
    void insertarRegistro(Object element);

    /**
     * Permite obtener una lista con todos los objetos de la colección
     * @param callback
     */
    void obtenerAllRegistros(FirebaseListCallback callback);

    /**
     * Actualiza el documento de la coleccion
     * con el mismo id que el recibido por parametro
     * con los datos recibidos
     * @param element
     */
    void actualizarRegistro(Object element);

    /**
     * Elimina de la colección
     * el documento con el id
     * del objeto pasado por parametro
     * @param element
     */
    void borrarRegistro(Object element);

}
