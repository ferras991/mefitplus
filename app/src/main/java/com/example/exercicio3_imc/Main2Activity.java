package com.example.exercicio3_imc;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.exercicio3_imc.Class.User;

import java.text.DecimalFormat;

public class Main2Activity extends AppCompatActivity {

    private EditText txtImc, txtPmin, txtPmax, txtIg;
    private EditText txtMg, txtAt, txtMineral, txtProteina;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //set button to go to the main page
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get views by id
        txtImc = findViewById(R.id.txtImc);
        txtPmin = findViewById(R.id.txtPmin);
        txtPmax = findViewById(R.id.txtPmax);
        txtIg = findViewById(R.id.txtIg);
        txtMg = findViewById(R.id.txtMg);
        txtAt = findViewById(R.id.txtAt);
        txtMineral = findViewById(R.id.txtMineral);
        txtProteina = findViewById(R.id.txtProteina);

        //get user object and calculate the info
        getUserInfo();
    }

    private void getUserInfo() {
        //get user object
        User user = (User)getIntent().getSerializableExtra("user");

        //get user name and place it on the action bar
        this.getSupportActionBar().setTitle("IMC - " + user.getName());

        //calculate and save the info
        double imc = user.calculateImc(user.getWeight(), user.getHeight());
        double pmin = user.calculatePMin(user.getHeight());
        double pmax = user.calculatePMax(user.getHeight());
//        double ig = user.calculateIg(user.getAge(), user.getGender(), imc);
//        double mg = user.calculateMg(user.getWeight(), ig);
//        double at = user.calculateAt(user.getGender(), user.getAge(), user.getHeight(), user.getWeight());
//        double mineral = user.calculateMineral(user.getWeight(), mg, at);
//        double proteina = user.calculateProteina(user.getWeight(), mg, at);

        //show user info
//        setUserInfo(imc, pmin, pmax, ig, mg, at, mineral, proteina);
    }

    private void setUserInfo(double imc, double pmin, double pmax, double ig, double mg, double at, double mineral, double proteina) {
        DecimalFormat fd = new DecimalFormat("#.##");

        txtImc.setText(fd.format(imc));
        txtPmin.setText(fd.format(pmin));
        txtPmax.setText(fd.format(pmax));
        txtIg.setText(fd.format(ig));
        txtMg.setText(fd.format(mg));
        txtAt.setText(fd.format(at));
        txtMineral.setText(fd.format(mineral));
        txtProteina.setText(fd.format(proteina));
    }

}
