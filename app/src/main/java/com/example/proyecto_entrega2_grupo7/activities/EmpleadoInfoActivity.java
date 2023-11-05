package com.example.proyecto_entrega2_grupo7.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.database.FirebaseCallback;
import com.example.proyecto_entrega2_grupo7.database.dao.HorarioDAO;
import com.example.proyecto_entrega2_grupo7.database.dao.PuestoDAO;
import com.example.proyecto_entrega2_grupo7.database.dao.UsuarioDAO;
import com.example.proyecto_entrega2_grupo7.database.utils.UtilsCheckNetwork;
import com.example.proyecto_entrega2_grupo7.entities.Horario;
import com.example.proyecto_entrega2_grupo7.entities.Puesto;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//FALTA PONER CONTRASEÑA AL CREAR USUARIO (TANTO EN LAYOUT COMO EN LA CLASE)

public class EmpleadoInfoActivity extends SuperLoggedActivity {

    //Text presentes tanto en visualizar como en modificar
    EditText nombreEmployee;
    EditText apellidoEmployee;
    EditText correoEmployee;
    Spinner puestoEmployee;
    Spinner horarioEmployeeEntrada;

    Spinner horarioEmployeeSalida;

    Button botonIzquierda;//SI PONEMOS EL BOTON VOLVER EN LA BARRA SUPERIOR NOS PODEMOS CARGAR ESTE
    Button botonDerecha;

    //Configuracion de Spinners
    PuestoDAO puestoDAO = new PuestoDAO();
    HorarioDAO horarioDAO = new HorarioDAO();
    List<String> puestoList;
    List<String> horarioList;

    //
    //Variables auxiliares
    //String userId;
    UsuarioDAO userEmployeeDao = new UsuarioDAO();
    Intent intent;
    Usuario user;

    UtilsCheckNetwork con = new UtilsCheckNetwork();
    int actionType;
        /* Variable que determina el modo de la ventana (CREAR, DETALLES O MODIFICAR)
        Las constantes correspondientes están en la Superclase SuperLoggedActivity.
        La he sacado para fuera para que sea accesible a todas las funciones de esta Activity
         */




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //Carga de componentes
        uploadComponents();

        //Establecer el modo de la actividad
        setActionType();


    }

    private void uploadComponents() {

        nombreEmployee = findViewById(R.id.etInfoNombre);
        apellidoEmployee = findViewById(R.id.etInfoApellidos);
        correoEmployee = findViewById(R.id.etInfoEmail);
        puestoEmployee = findViewById(R.id.spInfoPuesto);
        horarioEmployeeEntrada = findViewById(R.id.spInfoHorario);
        horarioEmployeeSalida = findViewById(R.id.spInfoHorario2);

        botonIzquierda = findViewById(R.id.bt_botonIzquierda);
        botonDerecha = findViewById(R.id.btInfoAction);

    }

    private void uploadInformationEmployee(){

        intent = getIntent();
        if (intent != null) {
            user = intent.getParcelableExtra("usuario");
            nombreEmployee.setText(user.getNombre());
            apellidoEmployee.setText(user.getApellidos());
            correoEmployee.setText(user.getCorreo());
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
                    horarioEmployeeEntrada.setAdapter(adapter);

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



    private void setActionType() {

        //Si no se pasa valor: ventana de crear usuario,
        // que tampoco requiere que se le pase ningun usuario

        //Titulo de la barra superior Y nombres botones
        String titulo = "";
        String textoBotonDerecha = "";
        actionType = getIntent().getIntExtra("ACTION_TYPE", MODO_CREAR);

        switch (actionType) {

            case MODO_DETALLES:
                titulo = "Detalles de empleado";
                textoBotonDerecha = getResources().getString(R.string.bt_modificar);
                //Carga de la informacion
                uploadInformationEmployee();
                //Funcion para cambiar a modo modificar si se clica a modificar
                botonDerecha.setOnClickListener(new View.OnClickListener() {
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
                botonDerecha.setOnClickListener(new View.OnClickListener() {
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
                botonDerecha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toAcceptEmployeeModify(true, MODO_CREAR);
                    }
                });

                break;

        }
        super.crearHomebar(titulo);
        botonIzquierda.setText(R.string.bt_cancelar);//PUEDE BORRARSE
        botonDerecha.setText(textoBotonDerecha);

        //Campos de texto readOnly o editables
        boolean soloLectura = (actionType == MODO_DETALLES);
        //SI ES MODIFICAR O CREAR, es false

        readOnlyEditText(nombreEmployee, soloLectura);
        readOnlyEditText(apellidoEmployee, soloLectura);
        readOnlyEditText(correoEmployee, soloLectura);
        puestoEmployee.setEnabled(!soloLectura);
        puestoEmployee.setClickable(!soloLectura);
        horarioEmployeeEntrada.setEnabled(!soloLectura);
        horarioEmployeeEntrada.setClickable(!soloLectura);
        horarioEmployeeSalida.setEnabled(!soloLectura);
        horarioEmployeeSalida.setClickable(!soloLectura);
    }

    /**
     * Acepta la información actualizada escrita del empleado
     * @param acceptedModify su valor modifica la base de datos con la informacion actualizada
     */
    private void toAcceptEmployeeModify(boolean acceptedModify, int type) {
        if (!acceptedModify) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        } else {

            //inicializando el objeto usuario ya se guarda en la base de datos pero si se hace
            //aqui afuera, ya no actualiza la base de datos




            /*
            Puesto puestoSeleccionado = puestoList.get(puestoEmployee.getSelectedItemPosition());
            user.setPuesto(puestoSeleccionado.getId());
            Horario horarioSeleccionado = horarioList.get(horarioEmployee.getSelectedItemPosition());
            user.setPuesto(horarioSeleccionado.getId());

             */

            //NO GUARDA EL USUARIO EN LA BASE DE DATOS
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

    /**
     * Funcion auxiliar para permitir modificacion en los EditText
     * @param text EditText a cambiar su estado
     * @param readOnly su valor vuelve el edittext modificable o no
     */
    private void readOnlyEditText(EditText text, boolean readOnly){
        if(readOnly){
            text.setKeyListener(null);
            text.setFocusable(false);
            text.setCursorVisible(false);
            text.setFocusableInTouchMode(false);
        } else {
            text.setKeyListener(new EditText(this).getKeyListener());
            text.setFocusable(true);
            text.setCursorVisible(true);
            text.setFocusableInTouchMode(true);
        }

    }



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
