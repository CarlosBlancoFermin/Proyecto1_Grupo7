package com.example.proyecto_entrega2_grupo7.activities.listar;

import static com.example.proyecto_entrega2_grupo7.entities.Filtros.NUM_FILTROS;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.activities.EmpleadoInfoActivity;
import com.example.proyecto_entrega2_grupo7.activities.SuperLoggedActivity;
import com.example.proyecto_entrega2_grupo7.database.dao.HorarioDAO;
import com.example.proyecto_entrega2_grupo7.database.dao.IServiceDAO;
import com.example.proyecto_entrega2_grupo7.database.dao.PuestoDAO;
import com.example.proyecto_entrega2_grupo7.database.dao.UsuarioDAO;
import com.example.proyecto_entrega2_grupo7.entities.Filtros;
import com.example.proyecto_entrega2_grupo7.entities.Horario;
import com.example.proyecto_entrega2_grupo7.entities.Puesto;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Actividad que se encarga de mostrar la lista de usuarios en un RecyclerView,
 * con un botón para ORDENAR alfabeticamente,
 * otro botón para FILTRAR, a través de un cuadro de dialogo,
 * botones en cada fila para ver detalles, modificar o eliminar un usuario,
 * y un boton para crear un nuevo usuario.
 */
public class ListarActivity extends SuperLoggedActivity implements ListarEventReceptor {
    //Variables de la lista de usuarios
    UsuarioDAO userService = new UsuarioDAO();//Objeto de acceso a BD
    List<Usuario> usuarioList;//Lista de objetos de la coleccion
    RecyclerView rvLista;//Componente visual

    //Variables de filtros
    /* Los filtros han de crearse en el mismo orden
     * que en la variable NOMBRES_FILTROS de la clase Filtro. */
    Class<?> [] filtrosClass = {Puesto.class,Horario.class};
        //Clases de los filtros utilizados
    IServiceDAO[] filtrosDAO = {new PuestoDAO(),new HorarioDAO()};
        //Clases de acceso a datos de las colecciones de los filtros
    List<Filtros>[] filtrosList = new List[NUM_FILTROS];
        //Listas de los objetos de cada colección de filtros
    SparseBooleanArray[] filtrosChecked = new SparseBooleanArray[NUM_FILTROS];
        //Array para guardar los checkbox marcados de cada filtro

    //Flags
    boolean ordenDesc = true;
    boolean filtroCambiado = false;


    //region CARGA INICIAL DE LA ACTIVIDAD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        super.crearHomebar("Lista de empleados");

        initArrays();

        //Dibuja la flecha del boton Ordenar
        setArrowOrdenar();

        //Asigna el RecyclerView a su variable
        rvLista = findViewById(R.id.rvListarUsuarios);

        //Consulta de usuarios y carga del RecyclerView
        consultaInicialUsuarios();

        //Consulta de las tablas de filtros (Puestos y Horarios)
        cargarListasFiltros();
    }

    private void setArrowOrdenar(){
        int arrow = ordenDesc ?
                android.R.drawable.arrow_down_float :
                android.R.drawable.arrow_up_float;
        Drawable newImg = ContextCompat.getDrawable(this, arrow);

        Button btOrdenar = findViewById(R.id.btListarOrdenar);
        btOrdenar.setCompoundDrawablesWithIntrinsicBounds(null, null, newImg, null);
    }

    /**
     * Inicializa los elementos de los arrays de filtros
     * (lista de objetos y array de checks)
     */
    private void initArrays() {
        for(int i = 0; i < NUM_FILTROS; i++){
            filtrosList[i] = new ArrayList<>();
            filtrosChecked[i] = new SparseBooleanArray();
        }
    }

    /**
     * Carga la lista completa de usuarios
     * y la muestra en el RecyclerView.
     */
    private void consultaInicialUsuarios() {
        userService.obtenerTodos(list -> {
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
     * Por cada filtro, se conecta a la colección de la BD
     * recupera todos los registros y los carga en una lista,
     * y marca todas las casillas de los checkbox
     * para mostrar en el DialogFiltros
     * (Actualmente Puestos y Horarios)
     */
    private void cargarListasFiltros(){
        for(int i = 0; i < NUM_FILTROS; i++){
            final int index = i;
            filtrosDAO[index].obtenerTodos(list -> {
                checkAllFilter(list.size(),filtrosChecked[index]);
                this.filtrosList[index] = list;
            });
        }
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
    //endregion

    //region ACTUALIZACION DE ACTIVIDAD (RESULT)
    /**
     * Funcion que actualiza el RecyclerView
     * cuando otra actividad llamada con el codigo UPDATE_CODE
     * finaliza y ha modificado la tabla Usuarios (envia RESULT_OK)
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTUALIZABLE && resultCode == Activity.RESULT_OK)
            actualizarLista();
    }

    /**
     * Vuelve a hacer consulta a base de datos
     * con los filtros seleccionados
     */
    private void actualizarLista(){
        filtroCambiado = true;
        aplicarFiltros();
    }

    //endregion

    //region GESTION DE EVENTOS

        //region BOTON ORDENAR
        /**
         * Cambia la flag ordenDesc,
         * reordena la lista y la muestra en el RecyclerView
         * y cambia la imagen de la flecha
         * @param view
         */
        public void pulsarOrdenar(View view){
            ordenDesc = !ordenDesc;
            llenarRecyclerView();
            setArrowOrdenar();
        }
        //endregion

        //region BOTONES DE LISTA DE USUARIOS
        /**
         * Gestiona los eventos asignados a botones de un ViewHolder (LISTAR)
         * en un RecyclerView vinculado a esta Acitivity.
         * Tres botones: DETALLES, MODIFICAR y ELIMINAR
         * @param usuario usuario seleccionado
         * @param idButton id del botón pulsado
         */
        @Override
        public void onButtonClick(Usuario usuario, int idButton) {
            if(idButton == R.id.btListarDetalles)
                pulsarBotonDetalles(usuario);
            else if(idButton == R.id.btListarModificar)
                pulsarBotonModificar(usuario);
            else if(idButton == R.id.btListarBorrar)
                pulsarBotonEliminar(usuario);
        }

            /**
             * Llama a la Activity que muestra la info del usuario
             * @param user objeto usuario
             */
            private void pulsarBotonDetalles(Usuario user){
                Intent intent = new Intent(this, EmpleadoInfoActivity.class);
                //intent.putExtra("id", user.getId());
                intent.putExtra("usuario",user); //MEJOR MANDAR EL USUARIO COMPLETO
                intent.putExtra("ACTION_TYPE", MODO_DETALLES);
                startActivityForResult(intent, ACTUALIZABLE);
                //Esta función aparece como deprecated,
                // pero la alternativa que da chatGPT es mucho mas farragosa
            }

            /**
             * Llama a la Activity que modifica la info del usuario
             * @param user objeto usuario
             */
            private void pulsarBotonModificar(Usuario user){
                Intent intent = new Intent(this, EmpleadoInfoActivity.class);
                intent.putExtra("usuario",user); //MEJOR MANDAR EL USUARIO COMPLETO
                intent.putExtra("ACTION_TYPE", MODO_MODIFICAR);
                startActivityForResult(intent, ACTUALIZABLE);
            }

            /**
             * Muestra un Dialog de confirmación.
             * En caso positivo, elimina al usuario de la base de datos
             * y actualiza la lista.
             * En caso negativo, vuelve a mostrar la lista.
             * @param u usuario que debe ser eliminado
             */
            private void pulsarBotonEliminar(Usuario u){
                new AlertDialog.Builder(this)
                        .setTitle("Eliminar usuario")
                        .setMessage("¿Deseas eliminar permanentemente a este usuario?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userService.borrarRegistro(u);
                                actualizarLista();
                                Toast.makeText(ListarActivity.this,"Usuario borrado",Toast.LENGTH_LONG).show();

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Código para manejar el clic en "Cancelar"
                            }
                        })
                        .show();
            }
            //endregion

        //region FILTROS
        /**
         * Si se han cargado todos los filtros lanza el Dialog de Filtros;
         * en caso contrario, muestra un mensaje al usuario
         * @param view
         */
        public void pulsarFiltros(View view){
            if(filtrosCargados())
                crearDialog();
            else
                Toast.makeText(this,"Cargando datos. Espere.",Toast.LENGTH_SHORT).show();
        }

        /**
         * Comprueba si se han realizado todas las consultas
         * en las tablas de filtros y se han asignado valores
         * a las respectivas listas
         * @return true/false
         */
        private boolean filtrosCargados(){
            for(int i = 0; i < NUM_FILTROS; i++)
                if(filtrosList[i] == null)
                    return false;
            return true;
        }

        /**
         * Crea el cuadro de diálogo de filtros
         */
        private void crearDialog(){
            DialogFragment dialog = new FiltrosDialog(
                    arrayFiltrosAdapter());
            dialog.show(getSupportFragmentManager(), "filtros");
        }

        /**
         * Crea los Adapters personalizados para los RecyclerView
         * del cuadro de diálogo de filtros
         * @return
         */
        private FiltrosAdapter[] arrayFiltrosAdapter(){
            FiltrosAdapter[] array = new FiltrosAdapter[NUM_FILTROS];
            for(int i = 0; i < NUM_FILTROS; i++)
                array[i] = new FiltrosAdapter(filtrosList[i],filtrosChecked[i],this);
            return array;
        }

        /**
         * Gestiona los eventos asignados a checkbox de un ViewHolder (FILTROS)
         * en un RecyclerView vinculado a esta Acitivity
         * @param item elemento de filtro seleccionado
         *             (Actualmente Puesto u Horario)
         * @param position posición en la lista
         * @param cb checkbox pulsado
         */
        @Override
        public void onCheckboxClick(Filtros item, int position, CheckBox cb) {
            for(int i = 0; i < NUM_FILTROS; i++) {
                if (filtrosClass[i].isInstance(item)) {
                    position = filtrosList[i].indexOf(item);
                    if (position != -1)
                        filtrosChecked[i].put(position, cb.isChecked());
                }
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
                //Crear listas para cada filtro con el nombre de los elementos marcados en el checkbox
                List<String> [] filtrosMarcados = new List[NUM_FILTROS];
                for(int i = 0; i < NUM_FILTROS; i++)
                    filtrosMarcados[i] = crearFiltro(filtrosList[i],filtrosChecked[i]);

                //CONTROL PROVISONAL FILTROS VACIOS
                //En cada filtro debe hacer al menos un elemento marcado
                if(filtrosVacios(filtrosMarcados)){
                    Toast.makeText(this,
                            "Debe seleccionar al menos un elemento en cada filtro",
                            Toast.LENGTH_SHORT).show();
                    crearDialog();//Se vuelve a crear
                    return;
                }
                filtroCambiado = false;

                //CONTROL FILTRO COMPLETO
                /* Si no se ha desmarcado ningún elemento,
                se manda la lista como null
                para que no se aplique el condicional */
                for(int i = 0; i < NUM_FILTROS; i++)
                    if(filtrosMarcados[i].size() == filtrosList[i].size())
                        filtrosMarcados[i] = null;

                //EJECUCIÓN DE LA CONSULTA
                userService.obtenerUsuariosFiltro(filtrosMarcados,
                        list -> {
                            usuarioList = list;
                            llenarRecyclerView();
                        });
            }
        }

        /**
         * Comprueba que ninguna de las listas de los nombres de filtros
         * esté vacía.
         * @param nombresFiltros array de listas con nombres seleccionados en cada filtro
         * @return true/false
         */
        private boolean filtrosVacios(List[] nombresFiltros){
            for(int i = 0; i < NUM_FILTROS; i++)
                if(nombresFiltros[i].isEmpty())
                    return true;
            return false;
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
        //endregion

        //region BOTON CREAR EMPLEADO

        /**
         * Llama a la Actividad de crear usuario
         * @param view
         */
        public void pulsarAddEmpleado(View view){
            //Implementacion provisional
            userService.insertarRegistro(new Usuario("genkidama@test.test", "123456",
                            "Son Goku","Gonzalez",
                            "aAPLZJ5S3Kz2ANlKR6jk",
                            "kM1QHbf6GFoinUC76aec")
                    );
            consultaInicialUsuarios();
            Toast.makeText(this,"Nuevo usuario registrado",Toast.LENGTH_LONG).show();
        }
        //endregion
    }