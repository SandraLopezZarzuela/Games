package com.example.games;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Bundle;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class gamePeg extends AppCompatActivity {
    public ImageView[][] matrixImageView;
    int[][] BOARD;
    boolean firstClick = true;
    int iV_selectedI;
    int iV_selectedJ;
    TextView puntuacion;
    Chronometer chronometer;
    int id;
    private static final String TAG = "MyActivity";
    daoUsuario dao;
    Usuario u;
    private Animation inicio;
    long detenerse;
    int score = 32;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_peg);
        puntuacion = (TextView) findViewById(R.id.puntuacion);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.cancion);
        mediaPlayer.start();

        chronometer = (Chronometer) findViewById(R.id.cronoPeg);
        startChrono();

        Bundle b = getIntent().getExtras();
        id = b.getInt("id");

        //Asignamos el nombre y apellido del usuario gracias a su id
        dao = new daoUsuario(this);
        u = dao.getUsuarioById(id);

        Game();

        Button menu = (Button) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                Intent intent = new Intent(gamePeg.this, Menu.class);
                intent.putExtra("id", u.getId());
                startActivity(intent);
                finish();
            }
        });

        Button restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                Intent intent = new Intent(gamePeg.this, gamePeg.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });
        Game();
    }

    public void Game() {
        matrix();
        seleccionarCasilla();
    }

    public void startChrono() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    public void resetChrono() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        detenerse = 0;
    }

    //creamos la matriz 7x7 cogiendo imageView del layout
    public void matrix() {
        matrixImageView = new ImageView[][]{
                {findViewById(R.id.cell00), findViewById(R.id.cell01), findViewById(R.id.cell02), findViewById(R.id.cell03), findViewById(R.id.cell04), findViewById(R.id.cell05), findViewById(R.id.cell06)},
                {findViewById(R.id.cell10), findViewById(R.id.cell11), findViewById(R.id.cell12), findViewById(R.id.cell13), findViewById(R.id.cell14), findViewById(R.id.cell15), findViewById(R.id.cell16)},
                {findViewById(R.id.cell20), findViewById(R.id.cell21), findViewById(R.id.cell22), findViewById(R.id.cell23), findViewById(R.id.cell24), findViewById(R.id.cell25), findViewById(R.id.cell26)},
                {findViewById(R.id.cell30), findViewById(R.id.cell31), findViewById(R.id.cell32), findViewById(R.id.cell33), findViewById(R.id.cell34), findViewById(R.id.cell35), findViewById(R.id.cell36)},
                {findViewById(R.id.cell40), findViewById(R.id.cell41), findViewById(R.id.cell42), findViewById(R.id.cell43), findViewById(R.id.cell44), findViewById(R.id.cell45), findViewById(R.id.cell46)},
                {findViewById(R.id.cell50), findViewById(R.id.cell51), findViewById(R.id.cell52), findViewById(R.id.cell53), findViewById(R.id.cell54), findViewById(R.id.cell55), findViewById(R.id.cell56)},
                {findViewById(R.id.cell60), findViewById(R.id.cell61), findViewById(R.id.cell62), findViewById(R.id.cell63), findViewById(R.id.cell64), findViewById(R.id.cell65), findViewById(R.id.cell66)},
        };
    }

    public void seleccionarCasilla() {
        //ponemos el firstClick en true
        //recorremos el tablero
        for (int i = 0; i < matrixImageView.length; i++) {
            for (int j = 0; j < matrixImageView[0].length; j++) {
                int finalI = i;
                int finalJ = j;
                //instaciamos el listener
                matrixImageView[finalI][finalJ].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //el primer click tiene que ser en ficha rellena o ficha seleccionada
                        if (firstClick && matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())
                                || firstClick && matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.fichaseleccionada, null).getConstantState())) {
                            matrixImageView[finalI][finalJ].setBackgroundResource(R.drawable.fichaseleccionada);
                            iV_selectedI = finalI;
                            iV_selectedJ = finalJ;
                            firstClick = false;

                            //si el segundo click es una ficha rellena no podemos ejecutar el movimiento
                       }else if (!firstClick && matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                            getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())) {
                        matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.ficharellena);
                        matrixImageView[finalI][finalJ].setBackgroundResource(R.drawable.fichaseleccionada);
                        iV_selectedI = finalI;
                        iV_selectedJ = finalJ;
                        firstClick = false;
                        //Procedemos a comprobar si es posible hacer el movimiento
                            // debemos saber si el segundo click la fixha esta vacia, si la anterior esta rellena y si la (doble) anterior es la seleciconada
                            //para esto lo comprobamos en las 4 direcciones
                            // abajo - arriba
                        }else if (!firstClick && matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.vacia, null).getConstantState())&& matrixImageView[finalI+1][finalJ].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())&&matrixImageView[finalI+2][finalJ].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.fichaseleccionada, null).getConstantState())){
                            realizarMovimiento(finalJ, finalI);
                            contarFichasRellenas();
                      //      checkGameOver();

                            //arriba - abajo
                        } else if (!firstClick && matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                            getResources().getDrawable(R.drawable.vacia, null).getConstantState())&& matrixImageView[finalI-1][finalJ].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())&& matrixImageView[finalI-2][finalJ].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.fichaseleccionada, null).getConstantState())) {
                            realizarMovimiento(finalJ, finalI);
                            contarFichasRellenas();
                       //     checkGameOver();
                            //izq- derecha
                        }else if (!firstClick && matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                            getResources().getDrawable(R.drawable.vacia, null).getConstantState())&& matrixImageView[finalI][finalJ-1].getBackground().getConstantState().equals(
                            getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())&&matrixImageView[finalI][finalJ-2].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.fichaseleccionada, null).getConstantState())){
                        realizarMovimiento(finalJ, finalI);
                        contarFichasRellenas();
                    //    checkGameOver();
                            //derecha-izq
                        } else if (!firstClick && matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                            getResources().getDrawable(R.drawable.vacia, null).getConstantState())&& matrixImageView[finalI][finalJ+1].getBackground().getConstantState().equals(
                            getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())&&matrixImageView[finalI][finalJ+2].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.fichaseleccionada, null).getConstantState())) {
                            realizarMovimiento(finalJ, finalI);
                            contarFichasRellenas();
                     //       checkGameOver();

                       } else if (!firstClick && matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.vacia, null).getConstantState())&& matrixImageView[finalI][finalJ+1].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.vacia, null).getConstantState())&&matrixImageView[finalI][finalJ+2].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.fichaseleccionada, null).getConstantState())) {
                            Toast.makeText(getApplicationContext(),"Movimiento invalido",Toast.LENGTH_SHORT).show();
                            iV_selectedI = finalI;
                            iV_selectedJ = finalJ;
                            firstClick = false;
                        } else if (!firstClick && matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.vacia, null).getConstantState())&& matrixImageView[finalI][finalJ+1].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.vacia, null).getConstantState())&&matrixImageView[finalI][finalJ+2].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.fichaseleccionada, null).getConstantState())) {
                            Toast.makeText(getApplicationContext(),"Movimiento invalido",Toast.LENGTH_SHORT).show();
                            iV_selectedI = finalI;
                            iV_selectedJ = finalJ;
                            firstClick = false;
                        } else if (!firstClick && matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.vacia, null).getConstantState())&& matrixImageView[finalI][finalJ+1].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.vacia, null).getConstantState())&&matrixImageView[finalI][finalJ+2].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.fichaseleccionada, null).getConstantState())) {
                            Toast.makeText(getApplicationContext(),"Movimiento invalido",Toast.LENGTH_SHORT).show();
                            iV_selectedI = finalI;
                            iV_selectedJ = finalJ;
                            firstClick = false;
                        } else if (!firstClick && matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.vacia, null).getConstantState())&& matrixImageView[finalI][finalJ+1].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.vacia, null).getConstantState())&&matrixImageView[finalI][finalJ+2].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.fichaseleccionada, null).getConstantState())) {
                            Toast.makeText(getApplicationContext(),"Movimiento invalido",Toast.LENGTH_SHORT).show();
                            iV_selectedI = finalI;
                            iV_selectedJ = finalJ;
                            firstClick = false;
                        }else if (!firstClick && matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.vacia, null).getConstantState())){
                            matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.vacia);
                            iV_selectedI = finalI;
                            iV_selectedJ = finalJ;
                        } else{
                            Toast.makeText(getApplicationContext(),"Movimiento invalido",Toast.LENGTH_SHORT).show();
                            matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.vacia);
                            iV_selectedI = finalI;
                            iV_selectedJ = finalJ;
                            firstClick = false;
                        }
                    }

                });
            }
        }
    }


    //metodo para comprobar si la puntuacion es menor a la de nuestra bbdd y hacer el update
    public void updateScore(int score) {
        if (score < u.getScorePeg()) {
            u.setScorePeg(score);
            dao.updateScorePeg(u);
        }
    }

    public void realizarMovimiento(int finalJ, int finalI) {
        // Abajo-Arriba
        if (iV_selectedJ == finalJ) {
            if (finalI - iV_selectedI == -2) {
                //la del primer click pasa a ser vacia
                matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.vacia);
                // la de enmedio pasa a ser vacia
                matrixImageView[iV_selectedI - 1][iV_selectedJ].setBackgroundResource(R.drawable.vacia);
                //la del segundo click pasa a ser rellena
                matrixImageView[iV_selectedI - 2][iV_selectedJ].setBackgroundResource(R.drawable.ficharellena);
                firstClick = true;
                //Al realizar el movmiento restamos uno en la puntuacion
                score--;
            }
            //Arriba - Abajo
            else if (finalI - iV_selectedI == 2) {
                //la del primer click pasa a ser vacia
                matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.vacia);
                // la de enmedio pasa a ser vacia
                matrixImageView[iV_selectedI + 1][iV_selectedJ].setBackgroundResource(R.drawable.vacia);
                //la del segundo click pasa a ser rellena
                matrixImageView[iV_selectedI + 2][iV_selectedJ].setBackgroundResource(R.drawable.ficharellena);
                firstClick = true;
                //Al realizar el movmiento restamos uno en la puntuacion
                score--;
            }
        } else if (iV_selectedI == finalI) {
            //Izquierda- Derecha
            if (finalJ - iV_selectedJ == 2) {
                //la del primer click pasa a ser vacia
                matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.vacia);
                // la de enmedio pasa a ser vacia
                matrixImageView[iV_selectedI][iV_selectedJ + 1].setBackgroundResource(R.drawable.vacia);
                //la del segundo click pasa a ser rellena
                matrixImageView[iV_selectedI][iV_selectedJ + 2].setBackgroundResource(R.drawable.ficharellena);
                firstClick = true;
                //Al realizar el movmiento restamos uno en la puntuacion
                score--;
            }
            //Derecha-Izquierda
            else if (finalJ - iV_selectedJ == -2) {
                //la del primer click pasa a ser vacia
                matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.vacia);
                // la de enmedio pasa a ser vacia
                matrixImageView[iV_selectedI][iV_selectedJ - 1].setBackgroundResource(R.drawable.vacia);
                //la del segundo click pasa a ser rellena
                matrixImageView[iV_selectedI][iV_selectedJ - 2].setBackgroundResource(R.drawable.ficharellena);
                firstClick = true;
                //Al realizar el movmiento restamos uno en la puntuacion
                score--;
            }
        /*} else {
            if (matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                    getResources().getDrawable(R.drawable.fichaseleccionada, null).getConstantState()) &&
                    matrixImageView[iV_selectedI][iV_selectedJ].getBackground().getConstantState().equals(
                            getResources().getDrawable(R.drawable.vacia, null).getConstantState())) {
            }*/
        }
        updateScore(score);
       // checkGameOver();
    }

    public void checkGameOver() {

        int casillasDisponibles = 0;

        for (int i = 0; i < matrixImageView.length; i++) {
            for (int j = 0; j < matrixImageView[0].length; j++) {
                // buscamos casilla rellena
                if (matrixImageView[i][j].getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())) {
                    //miramos si la ficha rellena tiene alguna ficha rellena a su alrededor y luego una vacia
                    if (matrixImageView[i + 1][j].getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())&&
                            matrixImageView[i + 2][j].getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.vacia, null).getConstantState()) ){
                        casillasDisponibles++;
                    }else if(matrixImageView[i - 1][j].getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())
                            && matrixImageView[i + 2][j].getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.vacia, null).getConstantState())){
                        casillasDisponibles++;
                    }else if(matrixImageView[i][j+1].getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())
                            &&matrixImageView[i][j+2].getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.vacia, null).getConstantState())){
                        casillasDisponibles++;
                    }else if(matrixImageView[i][j-1].getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())
                            &&matrixImageView[i][j-2].getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.vacia, null).getConstantState())) {
                        casillasDisponibles++;
                            }
                }else{
                    //casillas = 0
                    //game Over
                    Intent intent = new Intent(gamePeg.this, SplashGameOver.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    finish();

                }
            }
        }


    }

    public int contarFichasRellenas() {
        //restamos las esquinas
        int numeroCasillasRellenas = -16;
        for (int i = 0; i < matrixImageView.length; i++) {
            for (int j = 0; j < matrixImageView[0].length; j++) {
                if (matrixImageView[i][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())) {
                    numeroCasillasRellenas++;
                    //puntuacion por pantalla
                    puntuacion.setText(Integer.toString(numeroCasillasRellenas));

                }
            }
        }

        //si es == 1  gana
        if (numeroCasillasRellenas == 1) {
            Intent intent = new Intent(gamePeg.this, SplashGameOver.class);
            intent.putExtra("id", id);
            startActivity(intent);
            finish();
        }
        return numeroCasillasRellenas;
    }
}