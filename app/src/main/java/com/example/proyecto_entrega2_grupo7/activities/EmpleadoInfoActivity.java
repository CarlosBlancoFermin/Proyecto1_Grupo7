package com.example.proyecto_entrega2_grupo7.activities;

import static com.example.proyecto_entrega2_grupo7.activities.MainActivity.UPDATE_CODE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.database.dao.HorarioDAO;
import com.example.proyecto_entrega2_grupo7.database.dao.PuestoDAO;
import com.example.proyecto_entrega2_grupo7.database.dao.UsuarioDAO;


public class EmpleadoInfoActivity extends AppCompatActivity {

    UsuarioDAO userEmployeeDao = new UsuarioDAO();
    TextView nameLabelEmployee;
    TextView surnameLabelEmployee;
    TextView phoneLabelEmployee;
    TextView emailLabelEmployee;
    TextView jobLabelEmployee;

    TextView nombreEmployee;
    TextView apellidoEmployee;
    TextView telefonoEmployee;
    TextView correoEmployee;
    TextView puestoEmployee;
    Button volverBotonEmployee;
    Button modificarBotonEmployee;
    String userId;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empleado_informacion);
        volverBotonEmployee = findViewById(R.id.bt_ReturnToListarActivity);
        modificarBotonEmployee = findViewById(R.id.bt_ModificarInfoEmpleado);
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

        volverBotonEmployee = findViewById(R.id.bt_ReturnToListarActivity);
        volverBotonEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toWatchEmployeeInfo(true);
            }
        });

        modificarBotonEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toWatchEmployeeInfo(false);
            }
        });



        Intent intentAux = getIntent();
        if (intentAux != null){
            this.userId = intentAux.getStringExtra("id");
        }

        refreshInfo();



    }

    /**
     * He metido en una función aparte la carga de los datos del usuario,
     * para que se pueda volver a ejecutar aisladamente
     * cuando se vuelva de la pantalla de Modificar
     */
    private void refreshInfo() {
        userEmployeeDao.detallesUsuario(nombreEmployee,
                apellidoEmployee, correoEmployee, puestoEmployee, this.userId);
        //La función detallesUsuario debería cargar los datos del Usuario en una variable Usuario de esta clase
        //La logica de colocar los textos en los EditText debería implementarse en esta clase,
        //no en el DAO
    }

    /**
     * Esta funcion se ejecuta cuando finaliza una Actividad llamada desde esta
     * a través de la función starActivityForResult con el requestCode correspondiente
     * (en este caso, UPDATE_CODE).
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPDATE_CODE && resultCode == Activity.RESULT_OK){
            refreshInfo();
        }
    }



    private void toWatchEmployeeInfo(boolean unableToModify) {
        /*Aqui el nombre del parametro es confuso.
        * De hecho, cuando se pulsa el boton volver,
        * no solo no es unableToModify,
        * sino que tiene que mandar el mensaje a la Actividad Listar
        * de que actualice la lista por si se ha modificado el Usuario
         */
        if (unableToModify) {
            setResult(Activity.RESULT_OK);//Obliga a Listar a actualizarse
            finish();
        } else {
            intent = new Intent(this, EmpleadoModifyActivity.class);

            intent.putExtra("id", this.userId);
            startActivityForResult(intent, UPDATE_CODE);
            //Al utilizar esta funcion con el UPDATE_CODE (definido en MainActivity)

            Log.d("asas", "asas");
        }
    }
}

//Toda esta clase funciona pero no recupera los datos al darle a modificar
