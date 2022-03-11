package com.example.games;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Menu extends AppCompatActivity {

    TextView nombre;
    int id = 0;
    Toolbar toolbar;
    Usuario u;
    daoUsuario dao;
    MediaPlayer click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        click = MediaPlayer.create(this, R.raw.click2);

        nombre = (TextView) findViewById(R.id.nombreUsuario);
        //cogemos el id
        Bundle b = getIntent().getExtras();
        id = b.getInt("id");



        //Asignamos el nombre y apellido del usuario gracias a su id
        dao = new daoUsuario(this);
        u = dao.getUsuarioById(id);
        nombre.setText(u.getNombre() + " " + u.getApellidos());


        opcionesMenu();
        

    }


    private void opcionesMenu() {

        CardView card = (CardView) findViewById(R.id.jugar2048);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.start();
                Intent intent = new Intent(Menu.this, game2048.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });

       CardView card2 = (CardView) findViewById(R.id.jugarPeg);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.start();
                Intent intent = new Intent(Menu.this, gamePeg.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });

        CardView card3 = (CardView) findViewById(R.id.ranking2048);
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.start();
                Intent intent = new Intent(Menu.this, Ranking2048.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });

        CardView card4 = (CardView) findViewById(R.id.rankingPeg);
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.start();
                Intent intent = new Intent(Menu.this, RankingPeg.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });

        CardView eliminar = (CardView) findViewById(R.id.InitEliminar);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.start();
                AlertDialog.Builder b = new AlertDialog.Builder(Menu.this);
                b.setMessage("Estas seguro de eliminar tu cuenta?");
                b.setCancelable(false);
                b.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        click.start();
                        if (dao.deleteUsuario(id)) {
                            Toast.makeText(Menu.this, "Usuario eliminado", Toast.LENGTH_LONG).show();
                            Intent i2 = new Intent(Menu.this, Login.class);
                            startActivity(i2);
                            finish();
                        } else {
                            Toast.makeText(Menu.this, "ERROR: No se elimino el usuario", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        click.start();
                        dialogInterface.cancel();
                    }
                });
                b.show();
            }
        });

        CardView editar = (CardView) findViewById(R.id.InitEditar);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.start();
                Intent i = new Intent(Menu.this, Editar.class);
                i.putExtra("id", id);
                startActivity(i);
                finish();
            }
        });


        Button salir = (Button) findViewById(R.id.InitSalir);
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.start();
                AlertDialog.Builder b = new AlertDialog.Builder(Menu.this);
                b.setMessage("Seguro que quieres cerrar sesion?");
                b.setCancelable(false);
                b.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        click.start();
                            Toast.makeText(Menu.this, "Sesion cerrada", Toast.LENGTH_LONG).show();
                            Intent i2 = new Intent(Menu.this, Login.class);
                            startActivity(i2);
                            finish();
                    }
                });
                b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        click.start();
                        dialogInterface.cancel();
                    }
                });
                b.show();
            }
        });
    }
}




