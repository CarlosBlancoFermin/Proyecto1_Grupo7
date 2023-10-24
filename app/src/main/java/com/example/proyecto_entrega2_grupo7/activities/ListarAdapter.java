package com.example.proyecto_entrega2_grupo7.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

import java.util.List;

public class ListarAdapter extends RecyclerView.Adapter<ListarAdapter.ListarViewHolder> {
    private List<Usuario> lista;
    private OnClickCallback callback;


    public ListarAdapter(List<Usuario> l, OnClickCallback cb) {
        this.lista = l;
        this.callback = cb;

    }

    @NonNull
    @Override
    public ListarAdapter.ListarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
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
            etNombre = view.findViewById(R.id.etNombre);
            btDetalles = view.findViewById(R.id.btDetalles);
            btUpdate = view.findViewById(R.id.btModificar);
            btDelete = view.findViewById(R.id.btBorrar);
        }

        void bindData(Usuario u){
            etNombre.setText(u.getNombre());
            addListener(btDetalles);
            addListener(btUpdate);
            addListener(btDelete);
        }

        void addListener(View view){
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (callback != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            callback.onUserClick(lista.get(position), view.getId());
                        }
                    }
                }
            });
        }
    }
}