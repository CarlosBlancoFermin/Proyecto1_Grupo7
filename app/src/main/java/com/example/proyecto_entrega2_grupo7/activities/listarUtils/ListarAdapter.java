package com.example.proyecto_entrega2_grupo7.activities.listarUtils;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.activities.SuperLoggedActivity;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

import java.util.List;

/**
 * Adaptador personalizado para mostrar las filas del RecyclerView
 * que contiene la lista de usuarios.
 * Muestra el nombre de usuario y tres botones,
 * si bien sus eventos son implementados en ListarActivity.
 */
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
        holder.bindData(position);

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class ListarViewHolder extends RecyclerView.ViewHolder{

        View view;
        TextView etNombre;
        TextView etApellidos;
        ImageButton btDetalles;
        ImageButton btUpdate;
        ImageButton btDelete;

        public ListarViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            etNombre = view.findViewById(R.id.etListarNombre);
            etApellidos = view.findViewById(R.id.etListarApellidos);
            btDetalles = view.findViewById(R.id.btListarDetalles);
            btUpdate = view.findViewById(R.id.btListarModificar);
            btDelete = view.findViewById(R.id.btListarBorrar);
        }

        void bindData(int position){
            Usuario u = lista.get(position);
            etNombre.setText(u.getNombre());
            etApellidos.setText(u.getApellidos());

            //Resaltar nombre usuario logeado
            if(u.equals(SuperLoggedActivity.getUserLogged())){
                etNombre.setTextColor(ContextCompat.getColor(view.getContext(),R.color.dark));
                etNombre.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                etApellidos.setTextColor(ContextCompat.getColor(view.getContext(),R.color.dark));
                etApellidos.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.fila_sel));
            }
            else if(position % 2 != 0)//Distinto color de las filas pares
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.element));
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
