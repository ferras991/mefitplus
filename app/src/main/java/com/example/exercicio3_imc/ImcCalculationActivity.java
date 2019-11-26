package com.example.exercicio3_imc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.exercicio3_imc.Class.User;
import com.example.exercicio3_imc.Globals.Globals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ImcCalculationActivity extends AppCompatActivity {

    private EditText weightField, weightDate;
    private Button saveBtn;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc_calculation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        weightField = findViewById(R.id.weightImcCalculation);
        weightDate = findViewById(R.id.weightDateImcCalculation);
        saveBtn = findViewById(R.id.keepweightBtn);

        Date date = new Date();
        String modifiedDate= new SimpleDateFormat("dd-MM-yyyy").format(date);
        weightDate.setText(modifiedDate);
    }

    public void onImcCalculationClick(View view) {
        switch (view.getId()) {
            case R.id.calculateImcBtn:
                calculateImc();
                break;

            case R.id.keepweightBtn:
                keepWeightBtn();
                break;
        }
    }

    private void calculateImc() {
        if (weightField.getText().toString().isEmpty()) {
            weightField.setError(getResources().getString(R.string.emptyField));
        } else {
            double weight = Double.parseDouble(weightField.getText().toString());

            User user = new User();
            double imc = user.calculateImc(weight, Globals.height);
            double pmin = user.calculatePMin(Globals.height);
            double pmax = user.calculatePMax(Globals.height);
            double ig = user.calculateIg(calculateAge(), Globals.gender, imc);
            double mg = user.calculateMg(weight, ig);
            double at = user.calculateAt(Globals.gender, calculateAge(), Globals.height, weight);
            double mineral = user.calculateMineral(weight, mg, at);
            double proteina = user.calculateProteina(weight, mg, at);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("User IMC");
            builder.setMessage(
                    "IMC: " + imc + "" +
                    "\nPmin: " + pmin +
                    "\nPmax: " + pmax +
                    "\nIg: " + ig +
                    "\nMg: " + mg +
                    "\nAt: " + at +
                    "\nMineral: " + mineral +
                    "\nProtein: " + proteina);

            builder.show();

            saveBtn.setEnabled(true);
        }
    }

    private int calculateAge() {
        Calendar now = Calendar.getInstance();
        String date = Globals.birthDate;

        String[] paths = date.split("/");
        int userDay=Integer.parseInt(paths[0]);
        int userMonth=Integer.parseInt(paths[1]);
        int userYear=Integer.parseInt(paths[2]);

        int year = now.get(Calendar.YEAR); //get current year
        int age = year - userYear;
        int month1 = (now.get(Calendar.MONTH) + 1); //get current month
//
        if ((userMonth) > month1) {
            age--;
        } else if (month1 == (userMonth)) {
            int day1 = now.get(Calendar.DAY_OF_MONTH); //get current day
            if (userDay > day1) {
                age--;
            }
        }

        return age;
    }

    private void keepWeightBtn() {
        Toast.makeText(this, "Cenas", Toast.LENGTH_SHORT).show();
    }

}
