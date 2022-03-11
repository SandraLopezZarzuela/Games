package com.example.games;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;


public class game2048 extends Activity {

    public static com.example.games.game2048 game2048 = null;
    private TextView Score;
    public static int score = 0;
    private TextView maxScore;
    private GameView2048 gameView;
    int id;
    Usuario u;
    daoUsuario dao;
    Chronometer chronometer;
    long detenerse;

    public game2048() {
        game2048 = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048);

        chronometer = (Chronometer) findViewById(R.id.crono);
        startChrono();

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.cancion2);
        mediaPlayer.start();

        Bundle b = getIntent().getExtras();
        id = b.getInt("id");

        //Asignamos el nombre y apellido del usuario gracias a su id
        dao = new daoUsuario(this);
        u= dao.getUsuarioById(id);

        Score = (TextView) findViewById(R.id.Score);

        gameView = (GameView2048)findViewById(R.id.gameView);

        Button restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetChrono();
                GameView2048.startGame();
            }
        });

        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameView.hasTouched) {
                    score = gameView.score;
                    showScore();
                    for(int y=0;y<4;++y) {
                        for(int x=0;x<4;++x) {
                            gameView.cards[y][x].setNum(gameView.num[y][x]);
                        }
                    }
                }
            }
        });

        Button menu = (Button)findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                Intent intent = new Intent(game2048.this, Menu.class);
                intent.putExtra("id", u.getId());
                startActivity(intent);
                finish();
            }
        });
    }

    public static com.example.games.game2048 getGame2048() {

        return game2048;
    }

    public void clearScore() {
        score = 0;
        showScore();
    }


    public void addScore(int i) {
        score += i;
        showScore();
        updateScore();

    }

    public void updateScore(){
        if (score>u.getScore2048()){
            u.setScore2048(score);
            dao.updateScore2048(u);
        }
    }

    public void showScore() {
        Score.setText(score + "");
    }

    public void resetChrono(){
        chronometer.setBase(SystemClock.elapsedRealtime());
        detenerse =0;
    }

    public void startChrono(){
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
    }
}