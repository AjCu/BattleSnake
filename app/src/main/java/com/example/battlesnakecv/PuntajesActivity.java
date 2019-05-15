package com.example.battlesnakecv;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PuntajesActivity extends AppCompatActivity {

    private TextView tv, tv2, tv3, tv4, tv5;
    String ptj_1, ptj_2, ptj_3, ptj_4, ptj_5;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntajes);
        Context context = this.getApplicationContext();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String data[] = {sharedPref.getString((getString(R.string.campo1)), ""), sharedPref.getString((getString(R.string.campo2)), ""), sharedPref.getString((getString(R.string.campo3)), ""), sharedPref.getString((getString(R.string.campo4)), ""), sharedPref.getString((getString(R.string.campo5)), "")};

        int k, t;
        String temp;

        for (int i = 0; i < 5 ; i++) {
            for (int j = 0; j < 5 ; j++) {

                if(data[i].equals("")==false && data[j].equals("")==false){
                    String parts_data_i[]=data[i].split(" ");
                    String parts_data_j[]=data[j].split(" ");

                    k=Integer.parseInt(parts_data_i[1]);
                    t=Integer.parseInt(parts_data_j[1]);

                    if (k<t) {
                        temp = data[i];
                        data[i] = data[j];
                        data[j] = temp;
                    }
                }
            }
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.campo1), data[0]);
        editor.commit();
        editor.putString(getString(R.string.campo2), data[1]);
        editor.commit();
        editor.putString(getString(R.string.campo3), data[2]);
        editor.commit();
        editor.putString(getString(R.string.campo4), data[3]);
        editor.commit();
        editor.putString(getString(R.string.campo5), data[4]);
        editor.commit();


        tv=(TextView)findViewById(R.id.tv_puntaje1);
        tv2=(TextView)findViewById(R.id.tv_puntaje2);
        tv3=(TextView)findViewById(R.id.tv_puntaje3);
        tv4=(TextView)findViewById(R.id.tv_puntaje4);
        tv5=(TextView)findViewById(R.id.tv_puntaje5);
        ptj_1=sharedPref.getString((getString(R.string.campo1)), "");
        ptj_2=sharedPref.getString((getString(R.string.campo2)), "");
        ptj_3=sharedPref.getString((getString(R.string.campo3)),"");
        ptj_4=sharedPref.getString((getString(R.string.campo4)),"");
        ptj_5=sharedPref.getString((getString(R.string.campo5)),"");
        tv.setText(ptj_1);
        tv2.setText(ptj_2);
        tv3.setText(ptj_3);
        tv4.setText(ptj_4);
        tv5.setText(ptj_5);
    }
}
