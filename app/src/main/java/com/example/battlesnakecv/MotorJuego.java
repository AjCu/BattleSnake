package com.example.battlesnakecv;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class MotorJuego extends SurfaceView implements Runnable {
    private Thread thread = null;

    private Context context;
    //Pixeles de pantalla
    private int screenX;
    private int screenY;

    private int LargoSnake;
    //Coordenadas bob
    private int [] EnemieX;
    private int [] EnemieY;
    //Vidas
    private int vidas;
    //Coordenadas Disparo
    private int[] shotX;
    private int[] shotY;
//Imagenes
    Bitmap cabeza,cuerpo, cuerpoenemie2, cuerpoenemie,cuerpotiro,cuerpocorazon;
    // Control del siguiente frame
    private long nextFrameTime;
    // Numero de Frames por segundo
    private final long FPS = 20;

    private final long MILLIS_PER_SECOND = 1000;

    private int score,nivel;
    //Coordenadas Serpiente y auxiliares
    private int[] snakeXs;
    private int[] snakeYs;
    private int xaux,yaux;
    private int shotxaux;
    private int shotyaux;

    private double theta;

    private volatile boolean Estajugando;

    private String nombre;

    private Canvas canvas;

    private SurfaceHolder surfaceHolder;

    private Paint paint;


    public MotorJuego(Context context, Point size)
    {
        super(context);
    //Obteniendo el contexto para los intents
        context = context.getApplicationContext();

        screenX = size.x;
        screenY = size.y;
//Cargando bitmaps
        cabeza = BitmapFactory.decodeResource(getResources(),R.drawable.cabezaf);
        cuerpo = BitmapFactory.decodeResource(getResources(),R.drawable.cuerpof);
        cuerpotiro = BitmapFactory.decodeResource(getResources(),R.drawable.drop);
        cuerpoenemie = BitmapFactory.decodeResource(getResources(),R.drawable.enemie2);
        cuerpoenemie2 = BitmapFactory.decodeResource(getResources(),R.drawable.enemie3);
        cuerpocorazon = BitmapFactory.decodeResource(getResources(),R.drawable.corazon);

        surfaceHolder = getHolder();
        paint = new Paint();

        snakeXs = new int[200];
        snakeYs = new int[200];
        shotX = new int[10];
        shotY = new int[10];
        EnemieX = new int[6];
        EnemieY = new int[6];
        xaux=0;
        yaux=0;
        for(int i=0;i<3;i++)
        {
            shotX[i]=screenX;
            shotY[i]=screenY;
        }

        theta=90;
        // Inicio del juego
        JuegoNuevo();

    }
    @Override
    public void run() {

        while (Estajugando) {

            if(updateRequired()) {
                update();
                draw();
            }
        }
    }

    public void pause()
    {
        Estajugando = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }
    public void resume()
    {
        Estajugando = true;
        thread = new Thread(this);
        thread.start();
    }
    public void update()
    {
       int bandera;
        //Colisiones tiro enemigo
        for(int j=0;j<nivel+1;j++) {
            for (int i = 0; i < 3; i++) {
                bandera=0;
                if ((shotX[i] >= EnemieX[j]) && (shotX[i] <= (EnemieX[j] + cuerpoenemie.getWidth()))) {
                    if ((shotY[i] >= EnemieY[j]) && (shotY[i] <= (EnemieY[j] + cuerpoenemie.getHeight()))) {
                        bandera=1;
                        EnemieX[j]=screenX+2000;
                        EnemieY[j]=screenX+2000;
                        //eatBob();
                    }
                }
                if ((shotX[i]+cuerpotiro.getWidth() >= EnemieX[j]) && (shotX[i]+cuerpotiro.getWidth() <= (EnemieX[j] + cuerpoenemie.getWidth()))) {
                    if ((shotY[i]+cuerpotiro.getHeight() >= EnemieY[j]) && (shotY[i]+cuerpotiro.getHeight() <= (EnemieY[j] + cuerpoenemie.getWidth()))) {
                        bandera=1;
                        EnemieX[j]=screenX+2000;
                        EnemieY[j]=screenX+2000;
                        //eatBob();
                    }
                }
               if(bandera==1)
               {
                   score=score+1;
                   checkcambiarnivel();
                   break;
               }
            }
        //Colisiones cabeza de serpiente con enemigo
            if ((snakeXs[0] >= EnemieX[j]) && (snakeXs[0] <= (EnemieX[j] + cuerpoenemie.getWidth()))) {
                if ((snakeYs[0] >= EnemieY[j]) && (snakeYs[0] <= (EnemieY[j] + cuerpoenemie.getHeight()))) {
                    if (vidas < 1) {
                        registrarpuntaje();
                        JuegoNuevo();
                    }
                   vidas--;
                }
            }
        }
    //Movimientos Serpiente y Enemigos
        moverSerpiente();
        moverEnemigo();

        if (detectDeath()) {

            vidas--;
            snakeXs[0] = screenX/2;
            snakeYs[0] = screenY/2;
            theta=90;

            if(vidas<1)
            {
                registrarpuntaje();
                JuegoNuevo();
            }
        }
    }
    public void JuegoNuevo()
    {
        LargoSnake = 1;
        vidas=6;
        snakeXs[0] = screenX/2;
        snakeYs[0] = screenY/2;
        theta=90;

        RelocalizarEnemies();

        score = 0;
        nivel=0;

        nextFrameTime = System.currentTimeMillis();
    }
    public void RelocalizarEnemies()
    {

        for(int i=0;i<nivel+1;i++)
        {
            Random random = new Random();
            EnemieX[i] = random.nextInt(screenX - 100) + 20;
            EnemieY[i] = random.nextInt(screenY - 100) + 20;
        }

    }
    public void checkcambiarnivel()
    {
            if(score==1)
            {
                if(EnemieX[0]==screenX+2000)
                {
                    eatBob();
                }
            }
            if(score>=1 && score <5)
            {
                if(EnemieX[0]==screenX+2000 && EnemieX[1]==screenX+2000)
                {
                    eatBob();
                }
            }
            if(score>=5 && score <7)
            {
                if(EnemieX[0]==screenX+2000 && EnemieX[1]==screenX+2000 && EnemieX[2]==screenX+2000)
                {
                    eatBob();
                }
            }
            if(score>=6 && score <11)
            {
                if(EnemieX[0]==screenX+2000 && EnemieX[1]==screenX+2000 && EnemieX[2]==screenX+2000 && EnemieX[2]==screenX+2000)
                {
                    eatBob();
                }
            }
            if(score>=11 && score <16)
            {
                if(EnemieX[0]==screenX+2000 && EnemieX[1]==screenX+2000 && EnemieX[2]==screenX+2000 && EnemieX[2]==screenX+2000 && EnemieX[3]==screenX+2000)
                {
                        registrarpuntaje();
                }
            }
    }
    public boolean shoot(int sx ,int sy)
    {
        boolean check=true;

        if (sx <= 0) check=false;
        if (sx >= screenX) check=false;
        if (sy <= 0) check=false;
        if (sy >= screenY) check=false;

        return check;
    }
    public void registrarpuntaje()
    {
        Intent i=new Intent().setClass(getContext(),ScoreActivity.class);
        String score_s=Integer.toString(score);
        i.putExtra("score",score_s);
        (getContext()).startActivity(i);
    }
    public void eatBob()
    {
        LargoSnake++;
        nivel=nivel+1;

        RelocalizarEnemies();
    }
    public void moverSerpiente()
    {
        float a = 10;
        float b = 10;
        float r = 20;

        for (int i = LargoSnake; i > 0; i--) {

            snakeXs[i] = snakeXs[i - 1]-xaux;
            snakeYs[i] = snakeYs[i - 1]-yaux;

        }
        xaux =  (int) (a +r*Math.cos(theta));

        yaux = (int) (b +r*Math.sin(theta));

        if(xaux>=14)
        {
            xaux=14;
        }
        if(xaux>=-14 && xaux<=-4)
        {
            xaux=-14;
        }
        if(yaux>=14)
        {
           yaux=14;
        }
        if(yaux>=-14 && yaux<=-4)
        {
            yaux=-14;
        }
        snakeYs[0]=snakeYs[0]+yaux;
        snakeXs[0]=snakeXs[0]+xaux;

        if(!shoot(shotX[0], shotY[0]))
        {
            shotxaux=xaux;
            shotyaux=yaux;
            for(int i=1;i<4;i++)
            {
                shotX[i-1]=snakeXs[0]+xaux*(3);
                shotY[i-1]=snakeYs[0]+yaux*(3);
            }

        }
        else
        {
            for(int i=1;i<4;i++) {
                shotX[i-1] = shotX[i-1] + (shotxaux * (3*i));
                shotY[i-1] = shotY[i-1] + (shotyaux * (3*i));
            }
        }


    }

    public void moverEnemigo(){
        for(int i=0;i<nivel+1;i++)
        {
            if (EnemieX[i] < screenX)
            {
                if (EnemieX[i] < snakeXs[0] && EnemieY[i] < snakeYs[0]) {
                    EnemieX[i] += 6;
                    EnemieY[i] += 6;
                } else {

                    if (EnemieX[i] > snakeXs[0] && EnemieY[i] > snakeYs[0]) {
                        EnemieX[i] -= +6;
                        EnemieY[i] -= +6;
                    } else {
                        if (EnemieX[i] > snakeXs[0] && EnemieY[i] < snakeYs[0]) {
                            EnemieX[i] -= 6;
                            EnemieY[i] += 6;

                        } else {
                            if (EnemieX[i] < snakeXs[0] && EnemieY[i] > snakeYs[0]) {
                                EnemieX[i] += 6;
                                EnemieY[i] -= 6;
                            }
                        }
                    }
                }
             }
        }
    }

    private boolean detectDeath()
    {
        boolean dead = false;

        if (snakeXs[0] <= 0) dead = true;
        if (snakeXs[0] >= screenX ) dead = true;
        if (snakeYs[0] <= 0 ) dead = true;
        if (snakeYs[0] >= screenY ) dead = true;

        for (int i = LargoSnake - 1; i > 0; i--) {
            if ((i > 4) && (snakeXs[0] == snakeXs[i]) && (snakeYs[0] == snakeYs[i])) {
                dead = true;
            }
        }

        return dead;
    }

    public void draw()
    {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.argb(255, 163, 115, 209));

            paint.setColor(Color.argb(255, 255, 255, 255));

            paint.setTextSize(90);

            canvas.drawText("Puntaje: " +score+"                   Nivel: "+nivel, 10, 70, paint);

            for (int i = 1; i < LargoSnake; i++) {
                    canvas.drawBitmap(cuerpo,snakeXs[i], snakeYs[i],null);
            }
            canvas.drawBitmap(cabeza,snakeXs[0],snakeYs[0],null);

            for (int i = 0; i < 3; i++) {
                canvas.drawBitmap(cuerpotiro,shotX[i], shotY[i],null);
            }

            for (int i = 0; i < vidas; i++) {
                canvas.drawBitmap(cuerpocorazon,50+(i*100), 80,null);
            }

            for (int i = 0; i < nivel+1; i++) {
                if(i%2==0)
                {
                    canvas.drawBitmap(cuerpoenemie, EnemieX[i], EnemieY[i], null);
                }
                else
                {
                    canvas.drawBitmap(cuerpoenemie2, EnemieX[i], EnemieY[i], null);
                }

            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
    public boolean  updateRequired()
    {
        if(nextFrameTime <= System.currentTimeMillis()){

            nextFrameTime =System.currentTimeMillis() + MILLIS_PER_SECOND / FPS;

            return true;
        }

        return false;
    }
    public boolean onTouchEvent(MotionEvent motionEvent) {



        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_MOVE:
                if (motionEvent.getX() >= screenX / 2)
                {
                    theta = theta + Math.toRadians(5);
                }
                else
                {
                    theta = theta - Math.toRadians(5);
                }
        }
        return true;
    }
}