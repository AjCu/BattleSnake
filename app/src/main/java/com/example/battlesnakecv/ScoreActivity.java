package com.example.battlesnakecv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ScoreActivity extends AppCompatActivity {

    Bundle score_obtenido;
    String score_reg, nombre;
    private EditText et1;
    private TextView tv1;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        score_obtenido=getIntent().getExtras();
        et1=(EditText)findViewById(R.id.edittext123);

        tv1 = (TextView)findViewById(R.id.textscore);
        score_reg=score_obtenido.getString("score");
        tv1.setText("Puntaje: "+score_reg);

        Context context = this.getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    private void EditarRegistro(){

        SharedPreferences.Editor editor = sharedPref.edit();
        String data[] = {sharedPref.getString((getString(R.string.campo1)), ""), sharedPref.getString((getString(R.string.campo2)), ""), sharedPref.getString((getString(R.string.campo3)), ""), sharedPref.getString((getString(R.string.campo4)), ""), sharedPref.getString((getString(R.string.campo5)), "")};
        nombre=et1.getText().toString();

        for(int i=0;i<5;i++){

            if(data[i].equals("")==true){

                data[i]=nombre+" "+score_reg;

                switch (i){
                    case 0:{
                        editor.putString(getString(R.string.campo1), nombre+" "+score_reg);
                        editor.commit();
                        break;
                    }
                    case 1:{
                        editor.putString(getString(R.string.campo2), nombre+" "+score_reg);
                        editor.commit();
                        break;
                    }
                    case 2:{
                        editor.putString(getString(R.string.campo3), nombre+" "+score_reg);
                        editor.commit();
                        break;
                    }
                    case 3:{
                        editor.putString(getString(R.string.campo4), nombre+" "+score_reg);
                        editor.commit();
                        break;
                    }
                    case 4:{
                        editor.putString(getString(R.string.campo5), nombre+" "+score_reg);
                        editor.commit();
                        break;
                    }
                }

                break;
            }
        }

        if(data[0].equals("")==false){

            String spt[]=data[0].split(" ");
            int score_n=Integer.parseInt(spt[1]);
            int score_comp=Integer.parseInt(score_reg);
            if(score_comp>score_n){
                data[0]=nombre+""+score_reg;
                editor.putString(getString(R.string.campo1), nombre+" "+score_reg);
                editor.commit();
            }
        }
    }

    public void Devolverse(View view) throws FileNotFoundException {

        EditarRegistro();
        Intent siguiente = new Intent(this, MainActivity.class);
        startActivity(siguiente);
    }
}