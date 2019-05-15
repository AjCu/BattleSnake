package com.example.battlesnakecv;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class CreditosActivity extends AppCompatActivity {

    private TextView mt1, mt2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);

        mt1=(TextView)findViewById(R.id.CreditosCris);
        mt2=(TextView)findViewById(R.id.CreditosVic);
    }
}
