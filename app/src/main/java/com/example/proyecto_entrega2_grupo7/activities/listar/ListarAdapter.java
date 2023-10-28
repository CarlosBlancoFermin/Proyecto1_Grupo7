package com.example.proyecto_entrega2_grupo7.activities.listar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

import java.util.List;

public class ListarAdapter extends RecyclerView.Adapter<ListarAdapter.ListarViewHolder> {
    private List<Usuario> lista;
    private ListarEventReceptor receptor;
    /*Actividad que recibirá los eventos asignados a los checkbox de cada ViewHolder
    En este caso, ListarActivity.
     */


    public ListarAdapter(List<Usuario> l, ListarEventReceptor r) {
        this.lista = l;
        this.receptor = r;
    }

    @NonNull
    @Override
    public ListarAdapter.ListarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listar_item, parent,false);
        return new ListarAdapter.ListarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListarAdapter.ListarViewHolder holder, int position) {
        holder.bindData(lista.get(position));

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class ListarViewHolder extends RecyclerView.ViewHolder{

        View view;
        EditText etNombre;
        ImageButton btDetalles;
        ImageButton btUpdate;
        ImageButton btDelete;

        public ListarViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            etNombre = view.findViewById(R.id.etListarNombre);
            btDetalles = view.findViewById(R.id.btListarDetalles);
            btUpdate = view.findViewById(R.id.btListarModificar);
            btDelete = view.findViewById(R.id.btListarBorrar);
        }

        void bindData(Usuario u){
            etNombre.setText(u.getNombre().concat("\n").concat(u.getApellidos()));
            addListener(btDetalles);
            addListener(btUpdate);
            addListener(btDelete);
        }

        /**
         * Añade un evento al componente pasado por parámetro,
         * que se enlazará con la Actividad receptor
         * (en este caso, ListarActivity),
         * la cual implementa la función a ejecutar
         * (a través de su correspondiente función onButtonClick).
         * @param view componente que desencadena el evento
         */
        void addListener(View view){
            view.setOnClickListener(v -> {
                if (receptor != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        receptor.onButtonClick(lista.get(position), view.getId());
                    }
                }
            });
        }
    }
}
