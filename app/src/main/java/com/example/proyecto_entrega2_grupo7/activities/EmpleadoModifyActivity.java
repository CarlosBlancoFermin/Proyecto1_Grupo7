package com.example.proyecto_entrega2_grupo7.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.database.dao.UsuarioDAO;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

public class EmpleadoModifyActivity extends AppCompatActivity {

    UsuarioDAO modifyEmployeeDao = new UsuarioDAO();
    Usuario user;
    TextView nameLabelEmployee;
    TextView surnameLabelEmployee;
    TextView phoneLabelEmployee;
    TextView emailLabelEmployee;
    TextView jobLabelEmployee;

    EditText nombreEmployee;
    EditText apellidoEmployee;
    EditText telefonoEmployee;
    EditText correoEmployee;
    EditText puestoEmployee;
    Button cancelarModificarEmpleado;
    Button aceptarModificarEmpleado;
    String userId;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado_modify);

        cancelarModificarEmpleado = findViewById(R.id.bt_cancelarModificarEmpleado);
        aceptarModificarEmpleado = findViewById(R.id.bt_ModificarEmpleado);
        nameLabelEmployee = findViewById(R.id.nameLabelEmployee);
        surnameLabelEmployee = findViewById(R.id.surnameLabelEmployee);
        phoneLabelEmployee = findViewById(R.id.phoneLabelEmployee);
        emailLabelEmployee = findViewById(R.id.emailLabelEmployee);
        jobLabelEmployee = findViewById(R.id.jobLabelEmployee);

        nombreEmployee = findViewById(R.id.editTextNameEmployee);
        apellidoEmployee = findViewById(R.id.editTextSurnameEmployee);
        telefonoEmployee = findViewById(R.id.editTextPhoneEmployee);
        correoEmployee = findViewById(R.id.editEmailTextEmployee);
        puestoEmployee = findViewById(R.id.editTextJobEmployee);


        intent = getIntent();
        if (intent != null){
            this.userId = intent.getStringExtra("id");
        }
        modifyEmployeeDao.detallesUsuario(nombreEmployee, apellidoEmployee, correoEmployee, puestoEmployee, userId);
        cancelarModificarEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAcceptEmployeeModify(false);
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

    private void toAcceptEmployeeModify(boolean acceptedModify) {

        if (!acceptedModify) {
            finish();
        } else {
            UsuarioDAO auxUsuario = new UsuarioDAO();
            auxUsuario.obtenerUsuario(userId, usuario -> {
                user = usuario;

                user.setNombre(nombreEmployee.getText().toString());
                user.setApellidos(apellidoEmployee.getText().toString());
                user.setCorreo(correoEmployee.getText().toString());
                user.setPuesto(puestoEmployee.getText().toString());

                modifyEmployeeDao.actualizarUsuario(user);

            });
        }
    }
}