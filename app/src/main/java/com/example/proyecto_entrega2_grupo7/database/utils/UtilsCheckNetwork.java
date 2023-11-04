package com.example.proyecto_entrega2_grupo7.database.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UtilsCheckNetwork {
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null) {
                // Hay una conexión a Internet disponible
                return true;
            }
        }
        // No hay una conexión a Internet disponible
        return false;
    }

}