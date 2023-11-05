package com.example.proyecto_entrega2_grupo7.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.database.dao.HorarioDAO;
import com.example.proyecto_entrega2_grupo7.database.dao.PuestoDAO;
import com.example.proyecto_entrega2_grupo7.database.dao.UsuarioDAO;
import com.example.proyecto_entrega2_grupo7.entities.Horario;
import com.example.proyecto_entrega2_grupo7.entities.Puesto;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

import java.util.List;


public class EmpleadoInfoActivity extends SuperLoggedActivity {

    //Text presentes tanto en visualizar como en modificar
    EditText nombreEmployee;
    EditText apellidoEmployee;
    EditText correoEmployee;
    Spinner puestoEmployee;
    Spinner horarioEmployee;

    Button botonIzquierda;//SI PONEMOS EL BOTON VOLVER EN LA BARRA SUPERIOR NOS PODEMOS CARGAR ESTE
    Button botonDerecha;


    //Configuracion de Spinners
    PuestoDAO puestoDAO = new PuestoDAO();
    HorarioDAO horarioDAO = new HorarioDAO();
    List<Puesto> puestoList;
    List<Horario> horarioList;

    //
    //Variables auxiliares
    //String userId;
    UsuarioDAO userEmployeeDao = new UsuarioDAO();
    Intent intent;
    Usuario user;

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










        //Recuperar info de usuario desde la actividad Listar


        //POSIBILIDAD DE RECUPERAR EL USUARIO COMPLETO DESDE LA ACTIVIDAD
//        user = getIntent().getParcelableExtra("usuario");
//        nombreEmployee.setText(user.getNombre());
//        apellidoEmployee.setText(user.getApellidos());
//        correoEmployee.setText(user.getCorreo());
//        puestoEmployee.setText(user.getPuesto());


    }

    private void uploadComponents() {

        nombreEmployee = findViewById(R.id.etInfoNombre);
        apellidoEmployee = findViewById(R.id.etInfoApellidos);
        correoEmployee = findViewById(R.id.etInfoEmail);
        puestoEmployee = findViewById(R.id.spInfoPuesto);
        horarioEmployee = findViewById(R.id.spInfoHorario);

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
                //que se pueda modiificar y guardar
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
                toAcceptEmployeeModify(true, MODO_CREAR);
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
        horarioEmployee.setEnabled(!soloLectura);
        horarioEmployee.setClickable(!soloLectura);


        //Asignacion de funciones a los botones
        //!!!SI PONES EL SWITCH DENTRO TE AHORRAS UNAS CUANTAS LINEAS

        /*
            botonIzquierda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
            });
*/
        /*
            botonDerecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch(actionType) {
                        case MODO_DETALLES:
                            intent = new Intent(EmpleadoInfoActivity.this, EmpleadoInfoActivity.class);
                            intent.putExtra("ACTION_TYPE", MODO_MODIFICAR);
                            startActivityForResult(intent, ACTUALIZABLE);
                            //El boton tiene que llevar al lyot de modificar

                            //toAcceptEmployeeModify(true);
                            //FUNCION BOTON DERECHA - DETALLES
                            setResult(Activity.RESULT_OK);
                            finish();
                            break;
                        case MODO_MODIFICAR:
                            //El boton tiene que aceptar la modificacion
                        case MODO_CREAR://MODO_CREAR
                            //FUNCION BOTON DERECHA - CREAR
                            //FUNCION BOTON DERECHA - MODIFICAR
                            uploadModifyLayout();
                            break;
                    }
                }
            });

         */
    }
/*
    private void uploadModifyLayout(){

        botonIzquierda.setText(R.string.bt_cancelar);
        botonDerecha.setText(R.string.bt_aceptar);
        botonDerecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cancelar


                botonIzquierda.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toAcceptEmployeeModify(false);
                        finish();
                    }
                });
                //Aceptar

                botonDerecha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toAcceptEmployeeModify(true);
                        finish();
                    }
                });
            }
        });

    }

*/




    /**
     * carga los datos del usuario, y se ejecuta aisladamente
     * cuando vuelve de la pantalla de Modificar
     */
    /*
    private void refreshInfo() {


        userEmployeeDao.obtenerUsuarioPorId(this.userId, new FirebaseCallback() {
            @Override
            public void onCallback(Usuario usuario) {
                nombreEmployee.setText(usuario.getNombre());
                apellidoEmployee.setText(usuario.getApellidos());
                correoEmployee.setText(usuario.getCorreo());
                puestoEmployee.setText(usuario.getPuesto());

                setResult(Activity.RESULT_OK);//Obliga a Listar a actualizarse (???)
                //finish();
            }

            @Override
            public void onFailedCallback() {
                Toast.makeText(EmpleadoInfoActivity.this, "No se puede conseguir la " +
                        "informacion del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Esta funcion se ejecuta cuando finaliza una Actividad llamada desde esta
     * a través de la función starActivityForResult con el requestCode correspondiente
     * (en este caso, UPDATE_CODE).
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_CODE && resultCode == Activity.RESULT_OK) {
            refreshInfo();
        }
    }

     */

    /* !!!ESTA FUNCION QUEDA SUSTITUIDA POR setActionType() linea 127+
     * Transforma el layout de vista de empleado en modificar empleado
     * @param allowUpdates su valor habilita los componentes escondidos de modificacion

    private void enableModify(boolean allowUpdates) {

        if (!allowUpdates) {
            setResult(Activity.RESULT_OK);//Obliga a Listar a actualizarse
            //finish();
        } else {
            botonIzquierda.setVisibility(View.INVISIBLE);
            botonDerecha.setVisibility(View.INVISIBLE);

            readOnlyEditText(nombreEmployee, false);
            readOnlyEditText(apellidoEmployee, false);
            readOnlyEditText(correoEmployee, false);
            readOnlyEditText(puestoEmployee, false);

            cancelarModificarEmpleado.setVisibility(View.VISIBLE);
            aceptarModificarEmpleado.setVisibility(View.VISIBLE);

            cancelarModificarEmpleado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toAcceptEmployeeModify(false);
                    finish();
                }
            });

            aceptarModificarEmpleado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toAcceptEmployeeModify(true);
                    finish();
                }
            });
        }
    }
    */

    /**
     * Acepta la información actualizada escrita del empleado
     * @param acceptedModify su valor modifica la base de datos con la informacion actualizada
     */
    private void toAcceptEmployeeModify(boolean acceptedModify, int type) {
        if (!acceptedModify) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        } else {
            user.setNombre(nombreEmployee.getText().toString());
            user.setApellidos(apellidoEmployee.getText().toString());
            user.setCorreo(correoEmployee.getText().toString());

            /*
            Puesto puestoSeleccionado = puestoList.get(puestoEmployee.getSelectedItemPosition());
            user.setPuesto(puestoSeleccionado.getId());
            Horario horarioSeleccionado = horarioList.get(horarioEmployee.getSelectedItemPosition());
            user.setPuesto(horarioSeleccionado.getId());

             */

            if(type == MODO_MODIFICAR){
                userEmployeeDao.actualizarRegistro(user);
                setResult(Activity.RESULT_OK);
            } else if (type == MODO_CREAR){
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
