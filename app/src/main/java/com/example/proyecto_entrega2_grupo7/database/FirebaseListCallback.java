package com.example.proyecto_entrega2_grupo7.database;

import java.util.List;

public interface FirebaseListCallback {
    void onCallback(List list);
    /*
     He modificado el parámetro a una lista genérica
     para que pueda utilizarse por todos los DAO.
     */
    }



