package com.example.proyecto_entrega2_grupo7.activities.listar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_entrega2_grupo7.R;

import java.util.Objects;

public class FiltrosDialog extends DialogFragment {

    final int NUM_FILTROS = 2;
    FiltrosAdapter [] adapters;
    Button btDesplegableFiltro [] = new Button[NUM_FILTROS];
    RecyclerView rvFiltro[] = new RecyclerView[NUM_FILTROS];

    /*Si hubiese más filtros, se pasarían más FiltrosAdapter por parámetro,
    y se crearían un botón desplegable y un RecyclerView más.
     */


    public FiltrosDialog(FiltrosAdapter [] fa) {
        this.adapters = fa.clone();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Inflador del layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        //Variable de la vista inflada y asignación de elementos
        View view = inflater.inflate(R.layout.dialog_filtros, null);
        rvFiltro[0] = view.findViewById(R.id.rvListarFiltro1);
        rvFiltro[1] = view.findViewById(R.id.rvListarFiltro2);
        btDesplegableFiltro[0] = view.findViewById(R.id.btListarFiltro1);
        btDesplegableFiltro[1] = view.findViewById(R.id.btListarFiltro2);
        addEventClick(btDesplegableFiltro);

        builder.setView(view)
                .setTitle(R.string.bt_filtros)
                .setPositiveButton(R.string.bt_aplicarFiltro, (dialog, id) -> {
                    ListarEventReceptor receptor = (ListarEventReceptor) getActivity();
                    if (receptor != null)
                        receptor.onDialogAccept();
                })
                .setNegativeButton("Cancelar", (dialog, id) ->
                        Objects.requireNonNull(FiltrosDialog.this.getDialog()).cancel());

        initRecyclerView();

        return builder.create();
    }

    /**
     * Asigna cada adaptador recibido por constructor
     * al RecyclerView correspondiente.
     */
    private void initRecyclerView() {
        for(int i = 0; i < NUM_FILTROS; i++){
            rvFiltro[i].setAdapter(adapters[i]);
        }
    }

    /**
     * Añade la función de desplegar el respectivo RecyclerView
     * a la cabecera de cada filtro, que actúa como botón
     * @param buttons array de todos los botones desplegables
     */
    private void addEventClick(Button[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            int position = i;
            buttons[i].setOnClickListener(v -> {
                RecyclerView desplegable = rvFiltro[position];
                //Funciones de visibilidad/ocultacion
                if (desplegable.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition((ViewGroup) desplegable.getParent());
                    desplegable.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition((ViewGroup) desplegable.getParent());
                    desplegable.setVisibility(View.GONE);
                }
            });
        }
    }
}

