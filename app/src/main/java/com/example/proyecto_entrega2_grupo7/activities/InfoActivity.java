package com.example.proyecto_entrega2_grupo7.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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


public class InfoActivity extends SuperActivityBase {

    //Text presentes tanto en visualizar como en modificar
    EditText etNombre;
    EditText etApellidos;
    EditText etEmail;
    EditText etPassword;
    Spinner spPuesto;
    Spinner spHorario;
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
    List<String> emailList;
        /* Se almacenan los emails de la base de datos,
        para evitar repeticiones en inserts/updates.
        En caso de modificacion, no se incluye el del user a modificar */

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
        if (actionType != MODO_CREAR)
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
        btAction = findViewById(R.id.btInfoAction);
    }

    private void cargarComponentes() {
        //Asignacion de textos segun modo
        String titulo = "";
        String textoBoton = "";
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
                textoBoton = getResources().getString(R.string.tag_nuevoEmpleado);
        }
        super.crearHomebar(titulo);
        btAction.setText(textoBoton);

        /* En modo CREAR/MODIFICAR:
        1. Se recupera la lista de emails para evitar repeticiones.
        2. Se asignan listeners al campo contraseña y al boton para mostrarla.
         */
        if (actionType != MODO_DETALLES) {
            recuperarEmails(user == null ? null : user.getCorreo());
            setBotonMostrarPass(//Metodo de la superclase
                    (ImageButton) findViewById(R.id.btInfoShowPass),
                    etPassword);
        }

        //Establecer campos de solo lectura o editables
        setEditables();

        //Asignar funcion del boton ACTION
        setBotonAction();

        //Cargar datos del usuario en EditText (solo DETALLES/MODIFICAR)
        if (actionType != MODO_CREAR)
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
        etNombre.setBackgroundColor(ContextCompat.getColor(this, color));
        etApellidos.setBackgroundColor(ContextCompat.getColor(this, color));
        etEmail.setBackgroundColor(ContextCompat.getColor(this, color));
        etPassword.setBackgroundColor(ContextCompat.getColor(this, color));
        int fondoSpinner = (editable ?
                R.drawable.spinner_selected :
                R.drawable.spinner_background);
        spPuesto.setBackground(getResources().getDrawable(fondoSpinner));
        spHorario.setBackground(getResources().getDrawable(fondoSpinner));

    }

    /**
     * Funcion auxiliar para permitir modificacion en los EditText
     * @param text     EditText a cambiar su estado
     * @param editable su valor vuelve el edittext modificable o no
     */
    private void textoEditable(EditText text, boolean editable) {
        if (editable)
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
        if (!usuarioCargado) {
            etNombre.setText(user.getNombre());
            etApellidos.setText(user.getApellidos());
            etEmail.setText(user.getCorreo());
            etPassword.setHint(R.string.passwordCensura);
            usuarioCargado = true;
        }
        if (actionType == MODO_MODIFICAR)//Coloca el cursor al final del primer campo
            etNombre.setSelection(etNombre.getText().toString().length());

    }

    /**
     * Recupera en una lista los correos de todos los usuarios,
     * salvo el del usuario a modificar,
     * para evitar repeticiones en CREAR o MODIFICAR
     * @param emailUser
     */
    private void recuperarEmails(String emailUser) {
        userDAO.obtenerTodosEmails(emailUser,
                correos -> {
            emailList = correos;
        });
    }

    /**
     * Segun el actionType
     * se asigna una funcion al boton principal
     */
    private void setBotonAction() {
        btAction.setOnClickListener(view -> {
            pulsarBotonAction();
        });
    }

    private void pulsarBotonAction(){
        if(!hayConexion())
            return;
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
    }

    /**
     * Cambia el ActionType
     * y vuelve a cargar los componentes
     */
    private void pulsarBotonModoDetalles() {
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
        if (!inputControl()) {
            return;
        }

        //Nueva instancia de usuario con datos actualizados
        Usuario actualizado = nuevoUsuario();
        actualizado.setId(user.getId());
        if (actionType == MODO_MODIFICAR && etPassword.getText().toString().isEmpty())
            actualizado.setPass(user.getPass());
        //Si no se ha introducido nueva contraseña, se setea la anterior sin encriptar

        //Comprobar si ha habido cambios
        if (user.sinCambios(actualizado)) {
            Toast.makeText(this,
                    getResources().getString(R.string.msj_sinCambios),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        //Actualizar la base de datos y volver
        userDAO.actualizarRegistro(actualizado);
        setResult(RESULT_OK);
        Toast.makeText(this,
                getResources().getString(R.string.msj_modificarOK),
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
        if (!inputControl()) {
            return;
        }

        //Nueva instancia de usuario con datos actualizados
        Usuario nuevo = nuevoUsuario();

        userDAO.insertarRegistro(nuevo);
        setResult(RESULT_OK);
        Toast.makeText(this,
                getResources().getString(R.string.msj_crearOK),
                Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Controla que no haya campos vacíos en el formulario
     * y que la nueva contraseña tenga al menos 6 caracteres.
     * En el MODO_MODIFICAR, la contraseña está vacía por defecto,
     * y si se deja asi se entiende que no se cambia.
     *
     * @return true/false
     */
    private boolean inputControl() {
        String nombreInput = etNombre.getText().toString();
        String apellidosInput = etApellidos.getText().toString();
        String emailInput = etEmail.getText().toString();
        String passInput = etPassword.getText().toString();

        //CAMPOS VACIOS
        boolean emptyPass = (passInput.isEmpty()
                && actionType == MODO_CREAR);
        if (nombreInput.isEmpty() ||
                apellidosInput.isEmpty() ||
                emailInput.isEmpty() ||
                emptyPass) {
            Toast.makeText(this,
                    getResources().getString(R.string.msj_rellenarCampos),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        //CONTROL DEL EMAIL
        if (!Pattern.compile(
                        "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                        Pattern.CASE_INSENSITIVE)
                .matcher(emailInput).matches()) {
            Toast.makeText(this, getResources().getString(R.string.msj_emailFail),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //CONTRASEÑA MINIMA
        boolean mismaPass = (actionType == MODO_MODIFICAR
                && passInput.isEmpty());
        if (passInput.length() < 6 && !mismaPass) {
            Toast.makeText(this,
                    getResources().getString(R.string.msj_passwordFail),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //EMAIL REPETIDO
        if(emailList.contains(emailInput)){
            Toast.makeText(this,
                    getResources().getString(R.string.msj_emailExiste),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * Devuelve un usuario con los datos del formulario
     *
     * @return nuevo usuario
     */
    private Usuario nuevoUsuario() {
        return new Usuario(
                etEmail.getText().toString(),
                etPassword.getText().toString(),
                etNombre.getText().toString(),
                etApellidos.getText().toString(),
                puestoList.get((int) spPuesto.getSelectedItemId()).getId(),
                horarioList.get((int) spHorario.getSelectedItemId()).getId());
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
        if (actionType == MODO_DETALLES) {
            //Solo se carga el puesto y horario del Usuario
            puestoDAO.obtenerRegistroPorId(user.getPuesto(),
                    element -> {
                        puestoList.add((Filtros) element);
                        llenarSpinner(PUESTO);
                    });
            horarioDAO.obtenerRegistroPorId(user.getHorario(),
                    element -> {
                        horarioList.add((Filtros) element);
                        llenarSpinner(HORARIO);
                    });
        } else {
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
                ((TextView) findViewById(R.id.tvInfoHoras)).setText(cadena.toString());
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
     *
     * @param tipo
     */
    private void llenarSpinner(int tipo) {
        switch (tipo) {
            case PUESTO:
                spPuesto.setAdapter(new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        listaNombres(puestoList)));
                if (actionType != MODO_CREAR) {
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
                if (actionType != MODO_CREAR) {
                    Horario h = new Horario();
                    h.setId(user.getHorario());
                    spHorario.setSelection(horarioList.indexOf(h));
                }
                break;
        }
    }

    private List<String> listaNombres(List<Filtros> listaObjects) {
        List listaNombres = new ArrayList();
        for (Filtros element : listaObjects)
            listaNombres.add(element.getNombre());
        return listaNombres;
    }
}
