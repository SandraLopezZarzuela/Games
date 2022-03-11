package com.example.games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText user, pass;
    Button entrar, registrar;
    daoUsuario dao;
    MediaPlayer click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        click = MediaPlayer.create(this,R.raw.click2);
        user = (EditText) findViewById(R.id.User);
        pass = (EditText) findViewById(R.id.Pass);

        entrar = (Button) findViewById(R.id.entrar);
        registrar = (Button) findViewById(R.id.registrar);

        entrar.setOnClickListener(this);
        registrar.setOnClickListener(this);
        dao = new daoUsuario(this);

    }

    @Override
    public void onClick(View view) {
        click.start();
        switch (view.getId()){
            case R.id.entrar:
                String u = user.getText().toString();
                String p = pass.getText().toString();
                if (u.equals("")&&p.equals("")){
                    Toast.makeText(this,"ERROR: CAMPOS VACIOS",Toast.LENGTH_SHORT).show();
                }else if (dao.login(u,p)==1){
                    Usuario ux = dao.getUsuario(u,p);
                    Toast.makeText(this,"Datos correctos",Toast.LENGTH_SHORT).show();
                    Intent i2 = new Intent(Login.this, Menu.class);
                    i2.putExtra("id", ux.getId());
                    startActivity(i2);
                    finish();
                }else{
                    Toast.makeText(this,"Usario y/o password incorrecto",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.registrar:
                Intent i = new Intent (Login.this,Registro.class);
                startActivity(i);
                break;
        }
    }
}