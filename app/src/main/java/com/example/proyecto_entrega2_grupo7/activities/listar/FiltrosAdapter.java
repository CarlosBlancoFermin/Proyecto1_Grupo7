package com.example.proyecto_entrega2_grupo7.activities.listar;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.entities.Filtros;

import java.util.List;

public class FiltrosAdapter extends RecyclerView.Adapter<FiltrosAdapter.FiltrosViewHolder> {
    private List<Filtros> lista;
    /* La lista recibida será de objetos de una clase que herede de Filtros.
       El tipo es identificado mediante instanceof */
    private SparseBooleanArray checks;
    /* Array recibido con las opciones marcadas y desmarcadas de la lista,
    según posicion */
    private ListarEventReceptor receptor;
    /*Actividad que recibirá los eventos asignados a los checkbox de cada ViewHolder
    En este caso, ListarActivity.
     */


    public FiltrosAdapter(List<Filtros> l, SparseBooleanArray array, ListarEventReceptor r) {
        this.lista = l;
        this.checks = array;
        this.receptor = r;
    }

    @NonNull
    @Override
    public FiltrosAdapter.FiltrosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filtros_item, parent,false);
        return new FiltrosAdapter.FiltrosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FiltrosAdapter.FiltrosViewHolder holder, int position) {
        holder.bindData(lista.get(position),position);

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class FiltrosViewHolder extends RecyclerView.ViewHolder{

        View view;
        CheckBox cbItem;
        Context context;


        public FiltrosViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            context = view.getContext();
            cbItem = view.findViewById(R.id.cbFiltrosItem);
            addListener(cbItem);
        }

        /**
         * Se vincula el nombre del objeto al texto del checkbox,
         * y se marca o desmarca según la información del SparseBooleanArray checks
         * @param item Objeto de la lista en la posición de la fila (ViewHolder)
         */
        void bindData(Filtros item, int position){
            cbItem.setText(item.getNombre());
            cbItem.setChecked(checks.get(position));
        }

        /**
         * Añade un evento al componente pasado por parámetro,
         * que se enlazará con la Actividad receptor
         * (en este caso, ListarActivity),
         * la cual implementa la función a ejecutar
         * (a través de su correspondiente función onCheckBoxClick).
         * @param view componente que desencadena el evento
         */
        void addListener(View view){
            view.setOnClickListener(v -> {
                if (receptor != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        receptor.onCheckboxClick(lista.get(position), position, (CheckBox)view);
                    }
                }
            });
        }
    }
}
