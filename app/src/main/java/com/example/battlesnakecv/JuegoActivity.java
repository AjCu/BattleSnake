package com.example.battlesnakecv;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class JuegoActivity extends AppCompatActivity {

    MotorJuego juegazo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        // Get the pixel dimensions of the screen
        Display display = getWindowManager().getDefaultDisplay();

        // Initialize the result into a Point object
        Point size = new Point();
        display.getSize(size);

        // Create a new instance of the SnakeEngine class
        juegazo = new MotorJuego(this, size);

        // Make snakeEngine the view of the Activity
        setContentView(juegazo);
    }
    @Override
    protected void onResume() {
        super.onResume();
        juegazo.resume();
    }

    // Stop the thread in snakeEngine
    @Override
    protected void onPause() {
        super.onPause();
        juegazo.pause();
    }
}