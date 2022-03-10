package com.example.games;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

//Grid de 4 x 4
public class GameView2048 extends GridLayout{

    public static Card2048[][] cards = new Card2048[4][4]; //tabla 4x4
    //lista de las cartas vacias para añadir carta aleatorias
    private static List<Point> emptyPoints = new ArrayList<Point>();
    public int num[][] = new int[4][4]; // creo la lista de 4 x 4
    public int score;
    public boolean hasTouched = false;
    boolean movimiento2 = false;


    public GameView2048(Context context) {
        super(context);
        initGameView();
    }

    public GameView2048(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView2048(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    private void initGameView() {
        setRowCount(4); // filas
        setColumnCount(4); // columnas
        setOnTouchListener(new Listener());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        int cardWidth = (Math.min(w, h)-10)/4;
        addCards(cardWidth, cardWidth);
        startGame();

    }


    private void addCards(int cardWidth, int cardHeight) {
        //reset
        this.removeAllViews();
        Card2048 c;
        //recorremos tabla y añadimos cartas
        for(int y=0;y<4;++y) {
            for(int x = 0;x<4;++x) {
                //creamos cartas
                c = new Card2048(getContext());
                c.setNum(0);
                addView(c, cardWidth, cardHeight);
                cards[x][y] = c; //Ponemos todas las cartas en 0
            }
        }
    }

    private static void addRandomNum() {
        //reset
        emptyPoints.clear();
        //recorremos tabla
        for (int y = 0; y < 4; ++y) {
            for (int x = 0; x < 4; ++x) {
                if (cards[x][y].getNum() == 0) {
                    emptyPoints.add(new Point(x, y)); // si la carta es 0 añadimos carta a la lista de cartas vacias
                }
            }
        }
        //seleccioniamos aleatoria mente una carta vacia de la lista y le añadimos un random de 2 o 4 (preferentemente 2)
        Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
        cards[p.x][p.y].setNum(Math.random()>0.1?2:4);
    }

    public static void startGame() {
        //hacemos reset de la puntuacion
        //ponemos todas las cartas a 0
        //empezamos el juego con 2 cartas aleatorias
        game2048.getGame2048().clearScore();
        for(int y=0;y<4;++y) {
            for(int x=0;x<4;++x) {
                cards[x][y].setNum(0);
            }
        }
        addRandomNum();
        addRandomNum();
    }

    //Procedemos a realizar los movimientos, una vez saquemos el primero sera mas facil sacar los demas puesto que requiere la misma logica
    private void movimientoIzq() {
        //inicializamos en falso
        boolean movimiento = false;
        //recorremos el tablero, carta por carta
        for(int y=0;y<4;++y) {
            for(int x=0;x<3;++x) {
                for(int x1=x+1;x1<4;++x1) {
                    //comprobamos si la carta tiene un valor
                    if (cards[x1][y].getNum()>0) {
                        //miramos si la carta de la izq es 0
                        if (cards[x][y].getNum()==0) {
                            //si es asi le asignamos el valor de la carta que queremos mover a esta posicion
                            cards[x][y].setNum(cards[x1][y].getNum());
                            //entonces asignamos la posicion en la que estabamos a 0
                            cards[x1][y].setNum(0);
                            //al movernos a la izq debemos movernos de posicion x (retrocedemos)
                            --x;
                            //movimiento es true
                            movimiento = true;

                            //comprobamos ahora si la carta donde queremos desplazarnos tiene el mismo valor que la actual
                        } else if (cards[x][y].equals(cards[x1][y])) {
                            //si es asi las multiplicamos
                            cards[x][y].setNum(cards[x][y].getNum()*2);
                            //el valor de la posicion en la que estabamos pasa a 0
                            cards[x1][y].setNum(0);
                            //añadimos la puntuacion de la operacion actual
                            game2048.getGame2048().addScore(cards[x][y].getNum());
                            movimiento = true;
                        }
                        break;
                    }
                }
            }
        }
        //si hemos realizado el movimiento añadimos cartas y revisamos si podemos continuar jugando
        if (movimiento) {
            addRandomNum();
            checkGameOver();

        }
    }

    private void movimientoDer() {
        boolean movimiento = false;
        for(int y=0;y<4;++y) {
            for(int x=3;x>0;--x) {
                for(int x1=x-1;x1>=0;--x1) {
                    if (cards[x1][y].getNum()>0) {
                        if (cards[x][y].getNum()==0) {
                            cards[x][y].setNum(cards[x1][y].getNum());
                            cards[x1][y].setNum(0);
                            ++x;
                            movimiento = true;
                        } else if (cards[x][y].equals(cards[x1][y])) {
                            cards[x][y].setNum(cards[x][y].getNum()*2);
                            cards[x1][y].setNum(0);
                            game2048.getGame2048().addScore(cards[x][y].getNum());
                            movimiento = true;
                        }
                        break;
                    }
                }
            }
        }
        if (movimiento) {
            addRandomNum();
            checkGameOver();
        }
    }

    private void movimientoArr() {
        boolean movimiento = false;
        for(int x=0;x<4;++x) {
            for(int y=0;y<3;++y) {
                for(int y1=y+1;y1<4;++y1) {
                    if (cards[x][y1].getNum()>0) {
                        if (cards[x][y].getNum()==0) {
                            cards[x][y].setNum(cards[x][y1].getNum());
                            cards[x][y1].setNum(0);
                            --y;
                            movimiento = true;
                        } else if (cards[x][y].equals(cards[x][y1])) {
                            cards[x][y].setNum(cards[x][y].getNum()*2);
                            cards[x][y1].setNum(0);
                            game2048.getGame2048().addScore(cards[x][y].getNum());
                            movimiento = true;
                        }
                        break;
                    }
                }
            }
        }
        if (movimiento) {
            addRandomNum();
            checkGameOver();
        }
    }

    private void movimientoAb() {
        boolean movimiento = false;
        for(int x=0;x<4;++x) {
            for(int y=3;y>0;--y) {
                for(int y1=y-1;y1>=0;--y1) {
                    if (cards[x][y1].getNum()>0) {
                        if (cards[x][y].getNum()==0) {
                            cards[x][y].setNum(cards[x][y1].getNum());
                            cards[x][y1].setNum(0);
                            ++y;
                            movimiento = true;
                        } else if (cards[x][y].equals(cards[x][y1])) {
                            cards[x][y].setNum(cards[x][y].getNum()*2);
                            cards[x][y1].setNum(0);
                            game2048.getGame2048().addScore(cards[x][y].getNum());
                            movimiento = true;
                        }
                        break;
                    }
                }
            }
        }
        if (movimiento) {
            addRandomNum();
            checkGameOver();
        }
    }

    private void checkGameOver() {
        //inicializamos en true
        boolean isOver = true;

        //recorremos tablero
        for(int y=0;y<4;++y) {
            for(int x=0;x<4;++x) {
                //miramos hay alguna carta con valor 0
                if (cards[x][y].getNum()==0||
                        (x<3&&cards[x][y].getNum()==cards[x+1][y].getNum())||
                        (y<3&&cards[x][y].getNum()==cards[x][y+1].getNum())) {

                    isOver = false;

                }
            }
        }
        //si la anterior comprobacion ha no da false procedemos a hacer el game Over
        if (isOver) {

            new AlertDialog.Builder(getContext()).setTitle("Game over :(").setMessage("Your Score: "+game2048.score).setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startGame();
                }
            }).show();
        }
    }


    class Listener extends Activity implements View.OnTouchListener {

        private float startX, startY, offsetX, offsetY;
        MediaPlayer mediaPlayer;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click1);

        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (!hasTouched) {
                hasTouched = true;
            }
          //  mediaPlayer.start();
            score = game2048.score;

            for(int y=0;y<4;++y) {
                for(int x=0;x<4;++x) {
                    num[y][x] = cards[y][x].getNum();
                }
            }

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = motionEvent.getX();
                    startY = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    offsetX = motionEvent.getX()-startX;
                    offsetY = motionEvent.getY()-startY;

                    if (Math.abs(offsetX)>Math.abs(offsetY)) {
                        if (offsetX<-5) {
                            movimientoIzq();
                        } else if (offsetX>5) {
                            movimientoDer();
                        }
                    } else {
                        if (offsetY<-5) {
                            movimientoArr();
                        } else if (offsetY>5) {
                            movimientoAb();
                        }
                    }
            }
            return true;
        }

    }

}