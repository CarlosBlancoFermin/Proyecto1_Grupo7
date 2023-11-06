package com.example.proyecto_entrega2_grupo7.database.dao;

import com.example.proyecto_entrega2_grupo7.database.FirebaseCallback;
import com.example.proyecto_entrega2_grupo7.database.FirebaseListCallback;
import com.example.proyecto_entrega2_grupo7.database.FirebaseManager;

/**
 * Interfaz con los métodos básicos a implementar
 * por las clases de acceso a datos (DAO)
 * de las diferentes colecciones
 */
public interface IServiceDAO {

    FirebaseManager DB = new FirebaseManager();

    /**
     * Recibe un objeto y lo inserta en la coleccion
     * @param element
     */
    void insertarRegistro(Object element);

    /**
     * Permite obtener un registro por su id
     * @param id
     * @param callback
     */
    void obtenerRegistroPorId(String id, FirebaseCallback callback);

    /**
     * Permite obtener una lista con todos los objetos de la colección
     * @param callback
     */
    void obtenerTodos(FirebaseListCallback callback);

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
