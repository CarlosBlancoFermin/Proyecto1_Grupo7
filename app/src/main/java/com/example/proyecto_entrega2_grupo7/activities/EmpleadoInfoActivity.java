package com.example.proyecto_entrega2_grupo7.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.content.ContextCompat;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.database.dao.HorarioDAO;
import com.example.proyecto_entrega2_grupo7.database.dao.PuestoDAO;
import com.example.proyecto_entrega2_grupo7.database.dao.UsuarioDAO;
import com.example.proyecto_entrega2_grupo7.database.utils.UtilsCheckNetwork;
import com.example.proyecto_entrega2_grupo7.entities.Filtros;
import com.example.proyecto_entrega2_grupo7.entities.Horario;
import com.example.proyecto_entrega2_grupo7.entities.Puesto;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class EmpleadoInfoActivity extends SuperLoggedActivity {

    //Text presentes tanto en visualizar como en modificar
    EditText etNombre;
    EditText etApellidos;
    EditText etEmail;
    EditText etPassword;
    Spinner spPuesto;
    Spinner spHorario;
    TextView tvHorasHorario;
    Button btAction;

    //Configuracion de Spinners
    PuestoDAO puestoDAO = new PuestoDAO();
    HorarioDAO horarioDAO = new HorarioDAO();
    List<Filtros> puestoList = new ArrayList();
    List<Filtros> horarioList = new ArrayList();
    private static final int PUESTO = 0;
    private static final int HORARIO = 1;

    //
    //Variables auxiliares
    UsuarioDAO userDAO = new UsuarioDAO();
    Usuario user;
    boolean usuarioCargado = false;

    UtilsCheckNetwork con = new UtilsCheckNetwork();
    int actionType;
        /* Variable que determina el modo de la ventana (CREAR, DETALLES O MODIFICAR)
        Las constantes correspondientes están en la Superclase SuperLoggedActivity. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //Establecer el modo de la actividad
        actionType = getIntent().getIntExtra(ACTION_TYPE, MODO_CREAR);

        //Cargar objeto usuario (solo DETALLES/MODIFICAR)
        if(actionType != MODO_CREAR)
            user = getIntent().getParcelableExtra("usuario");

        //Asignar variables a componentes
        dataBinding();

        //Cargar componentes segun ACTION_TYPE
        cargarComponentes();
    }

    private void dataBinding() {
        etNombre = findViewById(R.id.etInfoNombre);
        etApellidos = findViewById(R.id.etInfoApellidos);
        etEmail = findViewById(R.id.etInfoEmail);
        etPassword = findViewById(R.id.etInfoPass);
        spPuesto = findViewById(R.id.spInfoPuesto);
        spHorario = findViewById(R.id.spInfoHorario);
        tvHorasHorario = findViewById(R.id.tvInfoHoras);
        btAction = findViewById(R.id.btInfoAction);
    }

    private void cargarComponentes() {
        //Asignacion de textos segun modo
        String titulo ="";
        String textoBoton ="";
        switch (actionType) {
            case MODO_DETALLES:
                titulo = (user.equals(userLogged) ?
                        getResources().getString(R.string.tag_miperfil) :
                        getResources().getString(R.string.tag_detalles));
                textoBoton = getResources().getString(R.string.bt_modificar);
                break;
            case MODO_MODIFICAR:
                titulo = (user.equals(userLogged) ?
                        getResources().getString(R.string.tag_miperfil) :
                        getResources().getString(R.string.tag_modificar));
                textoBoton = getResources().getString(R.string.bt_aceptar);
                break;
            default://MODO_CREAR
                titulo = "Nuevo empleado";
                textoBoton = getResources().getString(R.string.bt_crearusuario);
        }
        super.crearHomebar(titulo);
        btAction.setText(textoBoton);

        //Establecer campos de solo lectura o editables
        setEditables();

        //Asignar funcion del boton ACTION
        setBotonAction();

        //Cargar datos del usuario en EditText (solo DETALLES/MODIFICAR)
        if(actionType != MODO_CREAR)
            cargarDatosUsuario();

        //Cargar elementos de los spinners
        cargarSpinners();
    }

    /**
     * Hace que los campos de texto sean editables o de solo lectura
     * segun el modo de la actividad
     * y cambia el color si son editables
     */
    private void setEditables() {
        boolean editable = (actionType != MODO_DETALLES);

        textoEditable(etNombre, editable);
        textoEditable(etApellidos, editable);
        textoEditable(etEmail, editable);
        textoEditable(etPassword, editable);
        spPuesto.setEnabled(editable);
        spPuesto.setClickable(editable);
        spHorario.setEnabled(editable);
        spHorario.setClickable(editable);

        int color = (editable ?
                R.color.element :
                R.color.transparente);
        etNombre.setBackgroundColor(ContextCompat.getColor(this,color));
        etApellidos.setBackgroundColor(ContextCompat.getColor(this,color));
        etEmail.setBackgroundColor(ContextCompat.getColor(this,color));
        etPassword.setBackgroundColor(ContextCompat.getColor(this,color));
        int fondoSpinner = (editable ?
                R.drawable.spinner_selected :
                R.drawable.spinner_background);
        spPuesto.setBackground(getResources().getDrawable(fondoSpinner));
        spHorario.setBackground(getResources().getDrawable(fondoSpinner));

    }

    /**
     * Funcion auxiliar para permitir modificacion en los EditText
     * @param text EditText a cambiar su estado
     * @param editable su valor vuelve el edittext modificable o no
     */
    private void textoEditable(EditText text, boolean editable){
        if(editable)
            text.setKeyListener(new EditText(this).getKeyListener());
        else
            text.setKeyListener(null);

        text.setFocusable(editable);
        text.setCursorVisible(editable);
        text.setFocusableInTouchMode(editable);
    }

    /**
     * Se cargan los datos del usuario
     * en los editText del formulario.
     * (Los datos de los spinners se cargan de forma asíncrona
     * después de realizar las consultas.)
     */
    private void cargarDatosUsuario() {
        if(!usuarioCargado){
            etNombre.setText(user.getNombre());
            etApellidos.setText(user.getApellidos());
            etEmail.setText(user.getCorreo());
            etPassword.setText(user.getPass());
            usuarioCargado = true;
        }
        if(actionType == MODO_MODIFICAR)//Coloca el cursor al final del primer campo
            etNombre.setSelection(etNombre.getText().toString().length());
    }

    /**
     * Segun el actionType
     * se asigna una funcion al boton principal
     */
    private void setBotonAction() {
        btAction.setOnClickListener(view -> {
            switch (actionType) {
                case MODO_DETALLES:
                    pulsarBotonModoDetalles();
                    break;
                case MODO_MODIFICAR:
                    pulsarBotonModoModificar();
                    break;
                default://MODO_CREAR
                    pulsarBotonModoCrear();
            }
        });
    }

    /**
     * Cambia el ActionType
     * y vuelve a cargar los componentes
     */
    private void pulsarBotonModoDetalles() {
//        Intent intent = new Intent(EmpleadoInfoActivity.this, EmpleadoInfoActivity.class);
//        intent.putExtra("usuario", user);
//        intent.putExtra("ACTION_TYPE", MODO_MODIFICAR);
//        startActivityForResult(intent, ACTUALIZABLE);
//        finish();
        actionType = MODO_MODIFICAR;
        cargarComponentes();
    }

    /**
     * Comprueba si ha habido cambios,
     * actualiza el usuario en base de datos
     * y vuelve a la actividad anterior.
     */
    private void pulsarBotonModoModificar() {
        //Comprobar input
        if(!inputControl()) {
            return;
        }

        //Nueva instancia de usuario con datos actualizados
        Usuario actualizado = nuevoUsuario();
        actualizado.setId(user.getId());

        //Comprobar si ha habido cambios
        if(user.sinCambios(actualizado)) {
            Toast.makeText(this, "No se ha hecho ningún cambio", Toast.LENGTH_SHORT);
            return;
        }
        //Actualizar la base de datos y volver
        userDAO.actualizarRegistro(actualizado);
        setResult(RESULT_OK);
        Toast.makeText(this,"Empleado modificado",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Comprueba si hay campos vacíos y el formato de email y contraseña
     * inserta el usuario en la base de datos
     * y vuelve a la actividad anterior
     */
    private void pulsarBotonModoCrear() {
        //Comprobar input
        if(!inputControl()) {
            return;
        }

        //Nueva instancia de usuario con datos actualizados
        Usuario nuevo = nuevoUsuario();

        userDAO.insertarRegistro(nuevo);
        setResult(RESULT_OK);
        Toast.makeText(this,"Empleado creado con éxito",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Controla que no haya campos vacíos en el formulario
     * y que la contraseña tenga al menos 6 caracteres.
     * @return true/false
     */
    private boolean inputControl() {
        //CAMPOS VACIOS
        if(etEmail.getText().toString().equals("") ||
                etPassword.getText().toString().equals("") ||
                etNombre.getText().toString().equals("") ||
                etApellidos.getText().toString().equals("")) {
            Toast.makeText(this,"Debe rellenar todos los campos",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        //CONTROL DEL EMAIL ???
        if(!Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE)
                .matcher(etEmail.getText().toString()).matches()){
            Toast.makeText(this,"El email no tiene un formato correcto",
                    Toast.LENGTH_SHORT).show();
            return false;
        }


        //CONTRASEÑA MINIMA
        if(etPassword.getText().toString().length() < 6) {
            Toast.makeText(this,"La contraseña ha de tener al menos 6 caracteres",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Devuelve un usuario con los datos del formulario
     * @return nuevo usuario
     */
    private Usuario nuevoUsuario() {
        return new Usuario(
                etEmail.getText().toString(),
                etPassword.getText().toString(),
                etNombre.getText().toString(),
                etApellidos.getText().toString(),
                puestoList.get((int)spPuesto.getSelectedItemId()).getId(),
                horarioList.get((int)spHorario.getSelectedItemId()).getId());
                    /*De la posicion seleccionada en el spinner
                      obtiene el puesto/horario de la lista
                      y de él coge su id */
    }

    /**
     * Realiza consultas a las colecciones Puestos y Horarios
     * para cargar los elementos en los spinners,
     * y añade un listener para que muestre la hora del horario seleccionado
     */
    private void cargarSpinners() {
        setListenersSpinnerHorario();
        if(actionType == MODO_DETALLES){
            //Solo se carga el puesto y horario del Usuario
            puestoDAO.obtenerRegistroPorId(user.getPuesto(),
                    element -> {
                        puestoList.add((Filtros)element);
                        llenarSpinner(PUESTO);
                    });
            horarioDAO.obtenerRegistroPorId(user.getHorario(),
                    element -> {
                        horarioList.add((Filtros)element);
                        llenarSpinner(HORARIO);
                    });
        }else{
            //Se cargan todos los horarios y puestos
            puestoDAO.obtenerTodos(
                    puestos -> {
                        puestoList = puestos;
                        llenarSpinner(PUESTO);
                    });
            horarioDAO.obtenerTodos(
                    horarios -> {
                        horarioList = horarios;
                        llenarSpinner(HORARIO);
                    });
        }
    }

    private void setListenersSpinnerHorario() {
        spHorario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Horario sel = (Horario) horarioList.get(position);
                StringBuilder cadena = new StringBuilder()
                        .append("De ")
                        .append(sel.getHoraEntrada())
                        .append(" a ")
                        .append(sel.getHoraSalida());
                tvHorasHorario.setText(cadena.toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    /**
     * Mete los elementos de las listas de Puesto y Horario
     * en los spinners a través de un Adapter
     * y selecciona el elemento correspondiente al usuario
     * @param tipo
     */
    private void llenarSpinner(int tipo) {
        switch(tipo){
            case PUESTO:
                spPuesto.setAdapter(new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        listaNombres(puestoList)));
                if(actionType != MODO_CREAR){
                    Puesto p = new Puesto();
                    p.setId(user.getPuesto());
                    spPuesto.setSelection(puestoList.indexOf(p));
                }
                break;
            case HORARIO:
                spHorario.setAdapter(new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        listaNombres(horarioList)));
                if(actionType != MODO_CREAR) {
                    Horario h = new Horario();
                    h.setId(user.getHorario());
                    spHorario.setSelection(horarioList.indexOf(h));
                }
                break;
        }
    }

    private List<String> listaNombres(List<Filtros> listaObjects) {
        List listaNombres = new ArrayList();
        for(Filtros element : listaObjects)
            listaNombres.add(element.getNombre());
        return listaNombres;
    }
/*


        switch (actionType) {
            case MODO_DETALLES:
                titulo = "Detalles de empleado";
                textoBotonDerecha = getResources().getString(R.string.bt_modificar);
                //Carga de la informacion
                uploadInformationEmployee();
                //Funcion para cambiar a modo modificar si se clica a modificar
                btAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(EmpleadoInfoActivity.this, EmpleadoInfoActivity.class);
                        intent.putExtra("usuario", user);
                        intent.putExtra("ACTION_TYPE", MODO_MODIFICAR);
                        startActivityForResult(intent, ACTUALIZABLE);
                        finish();
                    }
                });


                break;
            //MODO MODIFICAR FUNCIONA
            case MODO_MODIFICAR:
                titulo = "Modificar empleado";
                textoBotonDerecha = getResources().getString(R.string.bt_aceptar);
                uploadInformationEmployee();
                //que se pueda modificar y guardar
                btAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println(user);
                        toAcceptEmployeeModify(true, MODO_MODIFICAR);
                        System.out.println(user);
                        finish();
                    }
                });


                break;
            case MODO_CREAR:
                titulo = "Nuevo empleado";
                textoBotonDerecha = getResources().getString(R.string.bt_crearusuario);
                btAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toAcceptEmployeeModify(true, MODO_CREAR);
                    }
                });

                break;

        }


        //Campos de texto readOnly o editables

    }

    private void uploadInformationEmployee(){

        intent = getIntent();
        if (intent != null) {
            user = intent.getParcelableExtra("usuario");

            puestoDAO.obtenerRegistroPorId(user.getPuesto(), new FirebaseCallback() {
                @Override
                public void onCallback(Object element) {
                    Puesto puesto = (Puesto) element;
                    puestoList = new ArrayList<>();
                    puestoList.add(puesto.getNombre());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EmpleadoInfoActivity.this,
                            android.R.layout.simple_spinner_item, puestoList);
                    puestoEmployee.setAdapter(adapter);
                }

                @Override
                public void onFailedCallback() {
                    System.out.println("Callback failed.");
                }
            });

            horarioDAO.obtenerRegistroPorId(user.getHorario(), new FirebaseCallback() {
                @Override
                public void onCallback(Object element) {
                    Horario horario = (Horario) element;
                    horarioList = new ArrayList<>();
                    horarioList.add(horario.getHoraEntrada());
                    horarioList.add(horario.getHoraSalida());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EmpleadoInfoActivity.this,
                            android.R.layout.simple_spinner_item, Collections.singletonList(horarioList.get(0)));
                    horarioEmployee.setAdapter(adapter);

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(EmpleadoInfoActivity.this,
                            android.R.layout.simple_spinner_item, Collections.singletonList(horarioList.get(1)));
                    horarioEmployeeSalida.setAdapter(adapter2);
                }

                @Override
                public void onFailedCallback() {
                    System.out.println("Callback failed.");
                }
            });

        }
    }






    private void toAcceptEmployeeModify(boolean acceptedModify, int type) {
        if (!acceptedModify) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        } else {

            //inicializando el objeto usuario ya se guarda en la base de datos pero si se hace
            //aqui afuera, ya no actualiza la base de datos




            Puesto puestoSeleccionado = puestoList.get(puestoEmployee.getSelectedItemPosition());
            user.setPuesto(puestoSeleccionado.getId());
            Horario horarioSeleccionado = horarioList.get(horarioEmployee.getSelectedItemPosition());
            user.setPuesto(horarioSeleccionado.getId());


            NO GUARDA EL USUARIO EN LA BASE DE DATOS
            if(type == MODO_MODIFICAR){
                user.setNombre(nombreEmployee.getText().toString());
                user.setApellidos(apellidoEmployee.getText().toString());
                user.setCorreo(correoEmployee.getText().toString());
                userEmployeeDao.actualizarRegistro(user);
                setResult(Activity.RESULT_OK);
            } else if (type == MODO_CREAR){
                user = new Usuario();
                user.setNombre(nombreEmployee.getText().toString());
                user.setApellidos(apellidoEmployee.getText().toString());
                user.setCorreo(correoEmployee.getText().toString());
                userEmployeeDao.insertarRegistro(user);
                setResult(Activity.RESULT_OK);
                //consultaInicialUsuarios();
                Toast.makeText(this,"Nuevo usuario registrado", Toast.LENGTH_LONG).show();
            }
        }
    }
*/




}












/*




            botonIzquierda.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        if(con.isOnline(EmpleadoInfoActivity.this)){
        switch(actionType) {
        case MODO_DETALLES:
        //FUNCION BOTON IZQUIERDA - DETALLES
        setResult(Activity.RESULT_OK);
        finish();
        break;
        case MODO_MODIFICAR:
        //FUNCION BOTON IZQUIERDA - MODIFICAR
        setResult(Activity.RESULT_OK);
        finish();
        break;
default://MODO_CREAR
        //FUNCION BOTON IZQUIERDA - CREAR
        }
        }
        else {
        Toast.makeText(EmpleadoInfoActivity.this, "No hay conexion a internet", Toast.LENGTH_SHORT).show();
        }

        }
        });

        botonDerecha.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        if(con.isOnline(EmpleadoInfoActivity.this)){
        switch(actionType) {
        case MODO_DETALLES:
        //FUNCION BOTON DERECHA - DETALLES
        setResult(Activity.RESULT_OK);
        finish();
        break;
        case MODO_MODIFICAR:
        //FUNCION BOTON DERECHA - MODIFICAR
        uploadModifyLayout();
        break;
default://MODO_CREAR
        //FUNCION BOTON DERECHA - CREAR
        }
        }
        else {
        Toast.makeText(EmpleadoInfoActivity.this, "No hay conexion a internet", Toast.LENGTH_SHORT).show();
        }

        }
        });
        }

*/

