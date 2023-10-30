package com.example.proyecto_entrega2_grupo7.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        userEmployeeDao.detallesUsuario(nombreEmployee,
                apellidoEmployee, correoEmployee, puestoEmployee, this.userId);

    }

    private void toWatchEmployeeInfo(boolean unableToModify) {

        if (unableToModify) {
            finish();
        } else {
            intent = new Intent(this, EmpleadoModifyActivity.class);

            intent.putExtra("id", this.userId);
            startActivity(intent);
            Log.d("asas", "asas");
        }
    }
}

//Toda esta clase funciona pero no recupera los datos al darle a modificar
