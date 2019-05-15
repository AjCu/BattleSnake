package com.example.battlesnakecv;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = MediaPlayer.create(this, R.raw.bnw);

            mp.start();

    }

    public void Siguiente(View view){

        Intent siguiente = new Intent(this, JuegoActivity.class);
        startActivity(siguiente);
    }

    public void Puntaje (View view){
        Intent siguiente = new Intent(this, PuntajesActivity.class);
        startActivity(siguiente);
    }

    public void Creditos (View view){
        Intent siguiente = new Intent (this, CreditosActivity.class);
        startActivity(siguiente);
    }
}
