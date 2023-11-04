package com.example.proyecto_entrega2_grupo7.activities;

import static com.example.proyecto_entrega2_grupo7.activities.MainActivity.UPDATE_CODE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.database.FirebaseCallback;
import com.example.proyecto_entrega2_grupo7.database.dao.UsuarioDAO;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;


public class EmpleadoInfoActivity extends AppCompatActivity {


    //Labels presentes tanto en visualizar como en modificar
    TextView nameLabelEmployee;
    TextView surnameLabelEmployee;
    TextView phoneLabelEmployee;
    TextView emailLabelEmployee;
    TextView jobLabelEmployee;


    //Text presentes tanto en visualizar como en modificar
    EditText nombreEmployee;
    EditText apellidoEmployee;
    EditText telefonoEmployee;
    EditText correoEmployee;
    EditText puestoEmployee;

    //Botones visualizados en visualizar
    Button botonIzquierda;
    Button botonDerecha;


    //Botones visualizados en modificar

    Button cancelarModificarEmpleado;
    Button aceptarModificarEmpleado;


    //
    //Variables auxiliares
    //String userId;
    UsuarioDAO userEmployeeDao = new UsuarioDAO();
    Intent intent;
    Usuario user;

    int defaultValue = 0;

    //Usuario gettinUser;

    //Botones , comparacion con enteros,
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empleado_informacion);
        uploadComponents();
        uploadInformationEmployee();

        intent = getIntent();
        if (intent != null) {
            //this.userId = intent.getStringExtra("id");
            this.user = intent.getParcelableExtra("usuario");
        }

        int actionType = getIntent().getIntExtra("ACTION_TYPE", defaultValue); //it return defaultValue if there's no key

        switch (actionType){
            case 1:
                //los campos son inmodificables
                readOnlyEditText(nombreEmployee, true);
                readOnlyEditText(apellidoEmployee, true);
                readOnlyEditText(correoEmployee, true);
                readOnlyEditText(puestoEmployee, true);

                //Volver

                botonIzquierda.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
                //Modificar

                botonDerecha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //carga los botones y funciones de modificar
                        uploadModifyLayout();
                    }
                });





                break;

            case 2:
                //los campos son modificables
                readOnlyEditText(nombreEmployee, false);
                readOnlyEditText(apellidoEmployee, false);
                readOnlyEditText(correoEmployee, false);
                readOnlyEditText(puestoEmployee, false);
                //carga los botones y funciones de modificar
                uploadModifyLayout();
                break;
            default:
                break;
        }



        //gettinUser = getIntent().getParcelableExtra("usuario");

        //userId = getIntent().getStringExtra("id");

        //Recuperar info de usuario desde la actividad Listar


        //POSIBILIDAD DE RECUPERAR EL USUARIO COMPLETO DESDE LA ACTIVIDAD
//        user = getIntent().getParcelableExtra("usuario");
//        nombreEmployee.setText(user.getNombre());
//        apellidoEmployee.setText(user.getApellidos());
//        correoEmployee.setText(user.getCorreo());
//        puestoEmployee.setText(user.getPuesto());


        /*

        if (actionType != null) {
            if (actionType.equals("DETALLES_EMPLEADO")) {
                refreshInfo();
                readOnlyEditText(nombreEmployee, true);
                readOnlyEditText(apellidoEmployee, true);
                readOnlyEditText(correoEmployee, true);
                readOnlyEditText(puestoEmployee, true);
            } else if (actionType.equals("MODIFICAR_EMPLEADO")) {
                refreshInfo();
                enableModify(true);
                setResult(Activity.RESULT_OK);
            }
        }

         */
    }


    private void uploadInformationEmployee(){
        user = getIntent().getParcelableExtra("usuario");
        nombreEmployee.setText(user.getNombre());
        apellidoEmployee.setText(user.getApellidos());
        correoEmployee.setText(user.getCorreo());
        puestoEmployee.setText(user.getPuesto());
    }

    private void uploadComponents() {
        nameLabelEmployee = findViewById(R.id.nameLabelEmployee);
        surnameLabelEmployee = findViewById(R.id.surnameLabelEmployee);
        phoneLabelEmployee = findViewById(R.id.phoneLabelEmployee);
        emailLabelEmployee = findViewById(R.id.emailLabelEmployee);
        jobLabelEmployee = findViewById(R.id.jobLabelEmployee);

        nombreEmployee = findViewById(R.id.recoverTextNameEmployee);
        apellidoEmployee = findViewById(R.id.recoverTextSurnameEmployee);
        telefonoEmployee = findViewById(R.id.recoverTextPhoneEmployee);
        correoEmployee = findViewById(R.id.recoverEmailTextEmployee);
        puestoEmployee = findViewById(R.id.recoverTextJobEmployee);

        botonIzquierda = findViewById(R.id.bt_botonIzquierda);
        botonDerecha = findViewById(R.id.bt_botonDerecha);

    }



    /**
     * Carga los componentes del layout
     */
    /*
    private void chargeContent(boolean detalleLayout) {
        nameLabelEmployee = findViewById(R.id.nameLabelEmployee);
        surnameLabelEmployee = findViewById(R.id.surnameLabelEmployee);
        phoneLabelEmployee = findViewById(R.id.phoneLabelEmployee);
        emailLabelEmployee = findViewById(R.id.emailLabelEmployee);
        jobLabelEmployee = findViewById(R.id.jobLabelEmployee);

        nombreEmployee = findViewById(R.id.recoverTextNameEmployee);
        apellidoEmployee = findViewById(R.id.recoverTextSurnameEmployee);
        telefonoEmployee = findViewById(R.id.recoverTextPhoneEmployee);
        correoEmployee = findViewById(R.id.recoverEmailTextEmployee);
        puestoEmployee = findViewById(R.id.recoverTextJobEmployee);

        botonIzquierda = findViewById(R.id.bt_botonIzquierda);
        botonDerecha = findViewById(R.id.bt_botonDerecha);

        if(detalleLayout){
            botonIzquierda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enableModify(false);
                    finish();
                }
            });

            botonDerecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enableModify(true);
                }
            });
        }

        if(!detalleLayout){
            botonIzquierda.setText(R.string.bt_cancelarModifyEmployee);
            botonDerecha.setText(R.string.bt_aceptarModifyEmployee);
        }



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

    /**
     * Transforma el layout de vista de empleado en modificar empleado
     * @param allowUpdates su valor habilita los componentes escondidos de modificacion
     */
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

    /**
     * Acepta la información actualizada escrita del empleado
     * @param acceptedModify su valor modifica la base de datos con la informacion actualizada
     */
    private void toAcceptEmployeeModify(boolean acceptedModify) {
        if (!acceptedModify) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        } else {
            user.setNombre(nombreEmployee.getText().toString());
            user.setApellidos(apellidoEmployee.getText().toString());
            user.setCorreo(correoEmployee.getText().toString());
            user.setPuesto(puestoEmployee.getText().toString());
            userEmployeeDao.actualizarRegistro(user);
            setResult(Activity.RESULT_OK);


            /*

            userEmployeeDao.(userId, new FirebaseCallback() {
                @Override
                public void onCallback(Usuario usuario) {
                    user = usuario;


                    setResult(Activity.RESULT_OK);//Obliga a Listar a actualizarse

                }

                @Override
                public void onFailedCallback() {
                    Toast.makeText(EmpleadoInfoActivity.this, "No se puede conseguir la " +
                            "informacion del usuario", Toast.LENGTH_SHORT).show();
                }
            });
            */

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

    private void uploadModifyLayout(){

        botonIzquierda.setText(R.string.bt_cancelarModifyEmployee);
        botonDerecha.setText(R.string.bt_aceptarModifyEmployee);
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

}
