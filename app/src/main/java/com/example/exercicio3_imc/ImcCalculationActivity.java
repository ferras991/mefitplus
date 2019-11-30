package com.example.exercicio3_imc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.exercicio3_imc.Class.Imc;
import com.example.exercicio3_imc.Class.User;
import com.example.exercicio3_imc.Globals.Globals;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ImcCalculationActivity extends AppCompatActivity {

    private EditText weightField, weightDate;
    private Button saveBtn;

    double imc = 0.0;
    double pmin = 0.0;
    double pmax = 0.0;
    double ig = 0.0;
    double mg = 0.0;
    double at = 0.0;
    double mineral = 0.0;
    double protein = 0.0;

    private Calendar now = Calendar.getInstance();

    private DatabaseReference mDatabase;

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

        mDatabase = FirebaseDatabase.getInstance().getReference("imcs");

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
        try{
            if (weightField.getText().toString().isEmpty()) {
                weightField.setError(getResources().getString(R.string.emptyField));
            } else {
                double weight = Double.parseDouble(weightField.getText().toString());

                User user = new User();
                imc = user.calculateImc(weight, Globals.height);
                pmin = user.calculatePMin(Globals.height);
                pmax = user.calculatePMax(Globals.height);
                ig = user.calculateIg(calculateAge(), Globals.gender, imc);
                mg = user.calculateMg(weight, ig);
                at = user.calculateAt(Globals.gender, calculateAge(), Globals.height, weight);
                mineral = user.calculateMineral(weight, mg, at);
                protein = user.calculateProteina(weight, mg, at);


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("User IMC");
                builder.setMessage(
                        "IMC: " + imc +
                                "\nPmin: " + pmin +
                                "\nPmax: " + pmax +
                                "\nIg: " + ig +
                                "\nMg: " + mg +
                                "\nAt: " + at +
                                "\nMineral: " + mineral +
                                "\nProtein: " + protein);

                builder.show();

                saveBtn.setEnabled(true);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private int calculateAge() {
        String date = Globals.birthDate;

        String[] paths = date.split("/");
        int userDay=Integer.parseInt(paths[0]);
        int userMonth=Integer.parseInt(paths[1]);
        int userYear=Integer.parseInt(paths[2]);

        int year = now.get(Calendar.YEAR); //get current year
        int age = year - userYear;
        int month1 = (now.get(Calendar.MONTH) + 1); //get current month

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
        try{
            DateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
            Date time = new Date();

            String date = (now.get(Calendar.DAY_OF_MONTH)) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);

            Imc imcClass = new Imc(timeFormat.format(time), date, imc, pmax, pmin, ig, mg, at, mineral, protein);

            final ProgressDialog dialog = ProgressDialog.show(ImcCalculationActivity.this, "",
                    "Loading. Please wait...", true);
            dialog.setCancelable(false);

            mDatabase.child(Globals.id).child(date).child(timeFormat.format(time)).setValue(imcClass)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(ImcCalculationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

}
