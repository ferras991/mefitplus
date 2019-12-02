package com.example.exercicio3_imc;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class ShowUserImcActivity extends AppCompatActivity {

    private EditText imcTxt, pminTxt, pmaxTxt, igTxt, mgTxt, atTxt, mineralTxt, proteinTxt;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_imc);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imcTxt = findViewById(R.id.imcShowImc);
        pminTxt = findViewById(R.id.pminShowImc);
        pmaxTxt = findViewById(R.id.pmaxShowImc);
        igTxt = findViewById(R.id.igShowImc);
        mgTxt = findViewById(R.id.mgShowImc);
        atTxt = findViewById(R.id.atShowImc);
        mineralTxt = findViewById(R.id.mineralShowImc);
        proteinTxt = findViewById(R.id.proteinShowImc);

        Bundle extras = getIntent().getExtras();
        getInfo(extras.getString("time+imc"));
    }


    private void getInfo(String batatas) {
        batatas = batatas.replace("{", "");
        batatas = batatas.replace("}", "");

        for (String cenas3 : batatas.split(", ")) {
            String[] cenas1 = cenas3.split("=");

            if (cenas1[0].equals("imc")) imcTxt.setText(cenas1[1]);
            if (cenas1[0].equals("imcPmin")) pminTxt.setText(cenas1[1]);
            if (cenas1[0].equals("imcPmax")) pmaxTxt.setText(cenas1[1]);
            if (cenas1[0].equals("imcIg")) igTxt.setText(cenas1[1]);
            if (cenas1[0].equals("imcMg")) mgTxt.setText(cenas1[1]);
            if (cenas1[0].equals("imcAt")) atTxt.setText(cenas1[1]);
            if (cenas1[0].equals("imcMineral")) mineralTxt.setText(cenas1[1]);
            if (cenas1[0].equals("imcProtein")) proteinTxt.setText(cenas1[1]);
        }
    }
}
