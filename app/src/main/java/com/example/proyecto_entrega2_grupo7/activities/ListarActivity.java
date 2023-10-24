package com.example.proyecto_entrega2_grupo7.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.database.FirebaseListCallback;
import com.example.proyecto_entrega2_grupo7.database.dao.UsuarioDAO;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

import java.util.Collections;
import java.util.List;

public class ListarActivity extends AppCompatActivity implements OnClickAvisador {

    UsuarioDAO userService = new UsuarioDAO();
    List<Usuario> users;
    RecyclerView rvLista;
    Button btOrder;
    boolean ordenDesc;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        btOrder = findViewById(R.id.btOrdenar);
        ordenDesc = true;
        setArrowOrdenar();

        rvLista = findViewById(R.id.rv_lista);
        initRecyclerView();

    }

    /**
     * Crea/refresca la lista de usuarios
     */
    private void initRecyclerView() {
        OnClickAvisador callback = this;
        userService.obtenerUsuarios(new FirebaseListCallback() {
            @Override
            public void onCallback(List<Usuario> list) {
                users = list;
                Collections.sort(users,
                        ordenDesc ? null : Collections.reverseOrder());
                rvLista.setAdapter(new ListarAdapter(users,callback));
            }
        });
    }

    /**
     * Gestiona las acciones de los botones de cada fila
     * @param usuario usuario seleccionado
     * @param idButton boton pulsado
     */
    @Override
    public void onUserClick(Usuario usuario, int idButton) {

        final Context context = this;

        if(idButton == R.id.btDetalles ){
            Toast.makeText(this,"Detalles de ".concat(usuario.getNombre()),
                    Toast.LENGTH_SHORT).show();
            intent = new Intent(context, EmpleadoInfoActivity.class);
            startActivity(intent);

        }else if(idButton == R.id.btModificar){
            Toast.makeText(this,"Modificar ".concat(usuario.getNombre()),
                    Toast.LENGTH_SHORT).show();

        }else if(idButton == R.id.btBorrar){
            userService.borrarUsuario(usuario);
            initRecyclerView();
            Toast.makeText(this,"Usuario borrado",Toast.LENGTH_LONG).show();
        }
    }

    private void setArrowOrdenar(){
        int arrow = ordenDesc ?
                android.R.drawable.arrow_down_float :
                android.R.drawable.arrow_up_float;
        Drawable newImg = ContextCompat.getDrawable(this, arrow);
        btOrder.setCompoundDrawablesWithIntrinsicBounds(null, null, newImg, null);
    }

    public void pulsarAddEmpleado(View view){
        userService.registrarusuario("son goku", "gonzalez", "programador","123456","test@test.test");
        initRecyclerView();
        Toast.makeText(this,"Nuevo usuario registrado",Toast.LENGTH_LONG).show();
    }



    public void pulsarOrdenar(View view){
        ordenDesc = !ordenDesc;
        setArrowOrdenar();
        initRecyclerView();
    }

    public void pulsarFiltros(View view){

    }
}