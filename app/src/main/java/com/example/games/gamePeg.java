package com.example.games;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.SystemClock;
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
                Game();
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
    //recorremos toda la matriz
        for (int i = 0; i < matrixImageView.length; i++) {
            for (int j = 0; j < matrixImageView[0].length; j++) {
                int x = i;
                int y = j;
                //activamos el listener
                matrixImageView[x][y].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //comprobamos si la ficha que seleccionamos primero esta rellena
                        if (firstClick && matrixImageView[x][y].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())
                                || firstClick && matrixImageView[x][y].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())) {
                            //si la condicion se cumple pasamos a comprobar si la siguiente tambien esta rellena
                            if (matrixImageView[iV_selectedI][iV_selectedJ].getBackground().getConstantState().equals(
                                    getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())) {
                                matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.ficharellena);
                            }
                            matrixImageView[x][y].setBackgroundResource(R.drawable.fichaseleccionada);
                            iV_selectedI = x;
                            iV_selectedJ = y;
                            firstClick = false;
                            //si la segunda ficha esta vacia pasamos a realizar los movimientos
                        } else if (!firstClick && matrixImageView[x][y].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.vacia, null).getConstantState())) {
                            Toast.makeText(gamePeg.this, "Primer ElseIF", Toast.LENGTH_SHORT).show();
                            //matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.ficha_shape_noclicked);
                            realizarMovimiento(x, y);
                            //A medida que hagamos movimientos restamos una score
                            //Hacemos la comprobacion si nuestra score actual es mejor que la anterior
                            //Si es asi, hacemos update en bbdd e insertamos al ranking la nueva
                            contarFichasRellenas();
                            score--;
                            updateScore(score);
                            //Pasamos a comprobar si se puede seguir jugando o no
                            if (checkGameOver()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(gamePeg.this);
                                    builder.setTitle("Loose");
                                    builder.setMessage("Has Perdido");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(getApplicationContext(), Menu.class);
                                            gamePeg.this.finish();
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }

                        } else if (!firstClick && matrixImageView[x][y].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())) {
                            Toast.makeText(gamePeg.this, "Segundo ElseIF", Toast.LENGTH_SHORT).show();
                            matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.ficharellena);
                            matrixImageView[x][y].setBackgroundResource(R.drawable.fichaseleccionada);
                            iV_selectedI = x;
                            iV_selectedJ = y;
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
        } else {
            u.setScorePeg(u.getScorePeg());
            dao.updateScorePeg(u);
        }

    }

    public void realizarMovimiento(int finalJ, int finalI) {

        // Abajo-Arriba
        if (iV_selectedJ == finalJ) {
            if (finalI - iV_selectedI == -2) {
                //Primera Bola
                matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.vacia);
                // Bola Medio
                matrixImageView[iV_selectedI - 1][iV_selectedJ].setBackgroundResource(R.drawable.vacia);
                //Ultima Bola
                matrixImageView[iV_selectedI - 2][iV_selectedJ].setBackgroundResource(R.drawable.ficharellena);
                firstClick = true;
            }
            //Arriba - Abajo
            else if (finalI - iV_selectedI == 2) {
                //Primera Bola
                matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.vacia);
                // Bola Medio
                matrixImageView[iV_selectedI + 1][iV_selectedJ].setBackgroundResource(R.drawable.vacia);
                //Ultima Bola
                matrixImageView[iV_selectedI + 2][iV_selectedJ].setBackgroundResource(R.drawable.ficharellena);
                firstClick = true;
            }
        } else if (iV_selectedI == finalI) {
            //Izquierda- Derecha
            if (finalJ - iV_selectedJ == 2) {
                //Primera Bola
                matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.vacia);
                // Bola Medio
                matrixImageView[iV_selectedI][iV_selectedJ + 1].setBackgroundResource(R.drawable.vacia);
                //Ultima Bola
                matrixImageView[iV_selectedI][iV_selectedJ + 2].setBackgroundResource(R.drawable.ficharellena);
                firstClick = true;

            }
            //Derecha-Izquierda
            else if (finalJ - iV_selectedJ == -2) {
                //Primera Bola
                matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.vacia);
                // Bola Medio
                matrixImageView[iV_selectedI][iV_selectedJ - 1].setBackgroundResource(R.drawable.vacia);
                //Ultima Bola
                matrixImageView[iV_selectedI][iV_selectedJ - 2].setBackgroundResource(R.drawable.ficharellena);
                firstClick = true;
            }
        } else {
            if (matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                    getResources().getDrawable(R.drawable.fichaseleccionada, null).getConstantState()) &&
                    matrixImageView[iV_selectedI][iV_selectedJ].getBackground().getConstantState().equals(
                            getResources().getDrawable(R.drawable.vacia, null).getConstantState())) {
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
        //si es == 1 game Over (gana)
        if (numeroCasillasRellenas == 1) {
            Intent intent = new Intent(gamePeg.this, SplashGameOver.class);
            intent.putExtra("id", id);
            startActivity(intent);
            finish();
        }
        return numeroCasillasRellenas;

}


    public boolean checkGameOver() {
        //Recorremos la matriz y detectamos las fichas rellenas
        //Miramos si las fichas pueden realizar un movimiento de izquierda-derecha o de arriba-abajo
        //simplemente miramos si las casillas de los lados tienen drawable
        //si ninguna tiene terminas, sino seguimos
        for (int i = 0; i < matrixImageView.length; i++) {
            for (int j = 0; j < matrixImageView[0].length - 2; j++) {
                //Izquierda-Derecha (Derecha)
                if (matrixImageView[i][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())
                        && matrixImageView[i][j + 1].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())
                        && matrixImageView[i][j + 2].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.vacia, null).getConstantState())) {
                    return false;
                }//Izquierda-Derecha (Izquierda)
                if (matrixImageView[i][j + 1].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())
                        && matrixImageView[i][j + 2].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())
                        && matrixImageView[i][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.vacia, null).getConstantState())) {
                    return false;
                }
            }
        }
        for (int i = 0; i < matrixImageView.length - 2; i++) {
            for (int j = 0; j < matrixImageView[0].length; j++) {
                //Arriba-abajo (Abajo)
                if (matrixImageView[i][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())
                        && matrixImageView[i + 1][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())
                        && matrixImageView[i + 2][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.vacia, null).getConstantState())) {
                    return false;
                }//Arriba-abajo (Arriba)
                if (matrixImageView[i + 1][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())
                        && matrixImageView[i + 2][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficharellena, null).getConstantState())
                        && matrixImageView[i][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.vacia, null).getConstantState())) {
                    return false;
                }
            }
        }
        return true;
    }
}