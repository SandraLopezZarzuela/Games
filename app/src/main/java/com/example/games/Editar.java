package com.example.games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Editar extends AppCompatActivity implements View.OnClickListener{

    EditText editUser, editPass, editNombre, editApellido;
    Button actualizar, cancelar;
    int id = 0;
    Usuario u;
    daoUsuario dao;
    Intent x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        editUser = (EditText) findViewById(R.id.EditUser);
        editPass= (EditText) findViewById(R.id.EditPass);
        editNombre = (EditText) findViewById(R.id.EditNombre);
        editApellido = (EditText) findViewById(R.id.EditApellido);

        actualizar = (Button) findViewById(R.id.EditActualizar);
        cancelar = (Button) findViewById(R.id.Editcancelar);

        actualizar.setOnClickListener(this);
        cancelar.setOnClickListener(this);

        //Recuperamos id del user
        Bundle b = getIntent().getExtras();
        id = b.getInt("id");

        //Asignamos el nombre y apellido del usuario gracias a su id
        dao = new daoUsuario(this);
        u= dao.getUsuarioById(id);
        editUser.setText(u.getUsuario());
        editPass.setText(u.getPassword());
        editNombre.setText(u.getNombre());
        editApellido.setText(u.getApellidos());


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.EditActualizar:
                u.setUsuario(editUser.getText().toString());
                u.setPassword(editPass.getText().toString());
                u.setNombre(editNombre.getText().toString());
                u.setApellidos(editApellido.getText().toString());
                if (!u.isNull()) {
                    Toast.makeText(this, "ERROR: CAMPOS VACIOS", Toast.LENGTH_LONG).show();
                } else if (dao.updateUsuario(u)) {
                    Toast.makeText(this, "Actualizacion correcta", Toast.LENGTH_LONG).show();
                    Intent i2 = new Intent(Editar.this, Menu.class);
                    i2.putExtra("id", u.getId());
                    startActivity(i2);
                    finish();
                } else {
                    Toast.makeText(this, "No se puede actualizar", Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.Editcancelar:
                Intent i = new Intent(Editar.this, Menu.class);
                i.putExtra("id", u.getId());
                startActivity(i);
                finish();
                break;

        }

    }
}
