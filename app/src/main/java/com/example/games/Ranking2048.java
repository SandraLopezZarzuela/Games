package com.example.games;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class Ranking2048 extends AppCompatActivity {

    ListView lista;
    daoUsuario dao;
    int id;
    Usuario u;
    Button menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        menu = (Button) findViewById(R.id.rMenu);


        Bundle b = getIntent().getExtras();
        id = b.getInt("id");

        //Asignamos el nombre y apellido del usuario gracias a su id
        dao = new daoUsuario(this);
        u= dao.getUsuarioById(id);

        lista = (ListView) findViewById(R.id.lista);
        listar();
        volver();

    }

    public void listar (){
        //cogemos los users de la bbdd
        ArrayList<Usuario> l = dao.selectUsuario();

        //asignamos los users como strings a traves del for
        ArrayList<String> l2 = new ArrayList<>();

        Collections.sort(l, u.bestScore);

        for (Usuario u : l) {
            l2.add(String.valueOf(u.getScore2048())+"                                                              "+ u.getUsuario());

        }
        Collections.reverse(l2);
        //la lista de cadena la pasamos al adapter y el adapter al listView
        ArrayAdapter<String> l3 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, l2);
        lista.setAdapter(l3);

    }


    public void volver(){
        menu = (Button) findViewById(R.id.rMenu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Ranking2048.this, Menu.class);
                i.putExtra("id", u.getId());
                startActivity(i);
                finish();
            }
        });
    }
}

