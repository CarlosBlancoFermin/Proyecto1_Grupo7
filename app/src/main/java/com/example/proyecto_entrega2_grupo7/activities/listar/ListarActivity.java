package com.example.proyecto_entrega2_grupo7.activities.listar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.activities.EmpleadoInfoActivity;
import com.example.proyecto_entrega2_grupo7.database.dao.HorarioDAO;
import com.example.proyecto_entrega2_grupo7.database.dao.PuestoDAO;
import com.example.proyecto_entrega2_grupo7.database.dao.UsuarioDAO;
import com.example.proyecto_entrega2_grupo7.entities.Filtros;
import com.example.proyecto_entrega2_grupo7.entities.Horario;
import com.example.proyecto_entrega2_grupo7.entities.Puesto;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListarActivity extends AppCompatActivity implements ListarEventReceptor {

    UsuarioDAO userService = new UsuarioDAO();
    PuestoDAO puestoService = new PuestoDAO();
    HorarioDAO horarioService = new HorarioDAO();
    List<Usuario> usuarioList;
    List<Filtros> puestoList;
    List horarioList;
    SparseBooleanArray puestosChecked = new SparseBooleanArray();
    SparseBooleanArray horariosChecked = new SparseBooleanArray();
    RecyclerView rvLista;
    Button btOrder;
    boolean ordenDesc;
    boolean filtroCambiado = false;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        btOrder = findViewById(R.id.btListarOrdenar);
        ordenDesc = true;
        setArrowOrdenar();

        rvLista = findViewById(R.id.rvListarUsuarios);
        consultaInicialUsuarios();
        cargarListasFiltros();
    }

    /**
     * Carga la lista completa de usuarios
     * y la muestra en el RecyclerView.
     */
    private void consultaInicialUsuarios() {
        userService.obtenerUsuarios(list -> {
            usuarioList = list;
            llenarRecyclerView();
        });
    }

    /**
     * Ordena la lista según el boolean ordenDesc, y
     * crea/actualiza el contenido de la lista
     * del RecyclerView
     */
    private void llenarRecyclerView(){
        Collections.sort(usuarioList,
                ordenDesc ? null : Collections.reverseOrder());
        rvLista.setAdapter(new ListarAdapter(usuarioList,this));
    }

    /**
     * Recupera de la base de datos los elementos
     * para mostrar en el DialogFiltros
     * (Actualmente Puestos y Horarios)
     */
    private void cargarListasFiltros(){
        puestoService.obtenerPuestos(puestoList -> {
            checkAllFilter(puestoList.size(),puestosChecked);
            this.puestoList = puestoList;
        });
        horarioService.obtenerHorarios(horarioList -> {
            checkAllFilter(horarioList.size(),horariosChecked);
            this.horarioList = horarioList;
        });
    }

    /**
     * Marca el estado de todos los checkbox de cada filtro como TRUE
     * (Actualmente Puestos y Horarios)
     * @param size tamaño de la lista
     * @param array vector que almacena las marcas de checkbox
     */
    private void checkAllFilter(int size, SparseBooleanArray array){
        for(int position = 0; position < size; position++){
            array.put(position,true);
        }
    }

    /**
     * Gestiona los eventos asignados a botones de un ViewHolder
     * en un RecyclerView vinculado a esta Acitivity
     * @param usuario usuario seleccionado
     * @param idButton id del botón pulsado
     */
    @Override
    public void onButtonClick(Usuario usuario, int idButton) {
            //El evento es alguno de los botones de la lista de Usuarios

        if(idButton == R.id.btListarDetalles){
            Toast.makeText(this,"Detalles de ".concat(usuario.getNombre()),
                    Toast.LENGTH_SHORT).show();
            intent = new Intent(this, EmpleadoInfoActivity.class);
            intent.putExtra("id", usuario.getId());
            startActivity(intent);

        }else if(idButton == R.id.btListarModificar){
            Toast.makeText(this,"Modificar ".concat(usuario.getNombre()),
                    Toast.LENGTH_SHORT).show();

        }else if(idButton == R.id.btListarBorrar){
            userService.borrarUsuario(usuario);
            consultaInicialUsuarios();
            Toast.makeText(this,"Usuario borrado",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Gestiona los eventos asignados a checkbox de un ViewHolder
     * en un RecyclerView vinculado a esta Acitivity
     * @param item elemento de filtro seleccionado
     *             (Actualmente Puesto u Horario)
     * @param position posición en la lista
     * @param cb checkbox pulsado
     */
    @Override
    public void onCheckboxClick(Filtros item, int position, CheckBox cb) {
        if(item instanceof Puesto){
            position = puestoList.indexOf(item);
            if(position != -1)
                puestosChecked.put(position,cb.isChecked());
        }else if(item instanceof Horario){
            position = horarioList.indexOf(item);
            if(position != -1)
                horariosChecked.put(position,cb.isChecked());
        }
        if(!filtroCambiado)
            filtroCambiado = true;
    }

    /**
     * Gestiona el evento asignado al botón APLICAR
     * del cuadro de diálogo de filtros (FiltrosDialog)
     */
    @Override
    public void onDialogAccept() {
        aplicarFiltros();
    }

    /**
     * Si ha cambiado el filtro aplicado,
     * almacena en tantas listas como filtros haya
     * los ID de los elementos marcados en las checkbox.
     *** Si algún filtro no tiene ningún elemento marcado,
     * se muestra un mensaje y vuelve a llamarse al FiltrosDialog.
     *** Si algún filtro tiene TODOS los elementos marcados,
     * la lista correspondiente se pasa como null.
     */
    private void aplicarFiltros() {
        if(filtroCambiado) {
            List<String> puestoFiltros = crearFiltro(puestoList, puestosChecked);
            List<String> horarioFiltros = crearFiltro(horarioList, horariosChecked);

            //CONTROL PROVISONAL FILTROS VACIOS
            if(puestoFiltros.isEmpty() || horarioFiltros.isEmpty()){
                Toast.makeText(this,
                        "Debe seleccionar al menos un elemento en cada filtro",
                        Toast.LENGTH_SHORT).show();
                crearDialog();
                return;
            }
            filtroCambiado = false;

            //CONTROL FILTRO COMPLETO
            /* (si no se ha desmarcado ningún elemento,
            se manda la lista como null
            para que no se aplique el condicional */

            if(puestoFiltros.size() == puestoList.size())
                puestoFiltros = null;
            if(horarioFiltros.size() == horarioList.size())
                horarioFiltros = null;

            //EJECUCIÓN DE LA CONSULTA
            userService.obtenerUsuariosFiltro(puestoFiltros, horarioFiltros,
                    list -> {
                        usuarioList = list;
                        llenarRecyclerView();
                    });
        }
    }

    /**
     * Selecciona los items de la lista
     * que aparecen marcados (true) en el Array de checks
     * y los guarda en una nueva lista con sus ID.
     * Si la nueva lista es igual a la lista completa,
     * devuelve null
     * @param items lista completa de elementos del filtro
     *              (Actualmente Puesto u Horario)
     * @param checks array ordenado de los checkbox marcados
     * @return lista filtrada / null
     */
    private List<String> crearFiltro (List<Filtros> items, SparseBooleanArray checks){
        List<String> listaFiltrada = new ArrayList<String>();
        for(int position = 0; position < items.size(); position++){
            if(checks.get(position))
                listaFiltrada.add(items.get(position).getId());
        }
        return listaFiltrada;
    }


    private void pulsarBotonDetalles(){

    }

    private void pulsarBotonModificar(){

    }

    private void pulsarBotonEliminar(){

    }

    private void setArrowOrdenar(){
        int arrow = ordenDesc ?
                android.R.drawable.arrow_down_float :
                android.R.drawable.arrow_up_float;
        Drawable newImg = ContextCompat.getDrawable(this, arrow);
        btOrder.setCompoundDrawablesWithIntrinsicBounds(null, null, newImg, null);
    }

    public void pulsarAddEmpleado(View view){
        userService.registrarUsuario(
                "genkidama@test.test", "123456",
                "Son Goku","Gonzalez",
                "aAPLZJ5S3Kz2ANlKR6jk",
                "kM1QHbf6GFoinUC76aec");
        consultaInicialUsuarios();
        Toast.makeText(this,"Nuevo usuario registrado",Toast.LENGTH_LONG).show();
    }

    public void pulsarOrdenar(View view){
        ordenDesc = !ordenDesc;
        llenarRecyclerView();
        setArrowOrdenar();
    }

    public void pulsarFiltros(View view){
        if(puestoList == null || horarioList == null){
            Toast.makeText(this,"Cargando datos. Espere.",Toast.LENGTH_SHORT).show();
        }else {
            crearDialog();
        }
    }


    private void crearDialog(){
        DialogFragment dialog = new FiltrosDialog(
                new FiltrosAdapter[]{
                    new FiltrosAdapter(puestoList, puestosChecked, this),
                    new FiltrosAdapter(horarioList, horariosChecked, this)});
        dialog.show(getSupportFragmentManager(), "filtros");
    }
}