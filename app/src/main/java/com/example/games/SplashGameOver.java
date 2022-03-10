package com.example.games;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class SplashGameOver extends AppCompatActivity {
    private final int duracion = 3000;
    int id;
    Usuario u;
    daoUsuario dao;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_game_over);
        Bundle b = getIntent().getExtras();
        id = b.getInt("id");

        //Asignamos el nombre y apellido del usuario gracias a su id
        dao = new daoUsuario(this);
        u = dao.getUsuarioById(id);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent (SplashGameOver.this, Menu.class);
                intent.putExtra("id", u.getId());
                startActivity(intent);
                finish();
            }
        }, duracion);
    }
}
