package com.example.exercicio3_imc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;

public class ImcCalculationActivity extends AppCompatActivity {

    private Calendar currentDate = Calendar.getInstance();
    private int day = currentDate.get(Calendar.DAY_OF_MONTH);
    private int month = currentDate.get(Calendar.MONTH);
    private int year = currentDate.get(Calendar.YEAR);

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
            case R.id.weightDateImcCalculation:
                getDate();
                break;
        }
    }

    private void getDate() {
        //open a datepickerdialog to save the user birthday date
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int selectedMonth, int selectedDay) {
                        year = selectedYear;
                        month = (selectedMonth + 1);
                        day = selectedDay;

                        String day2 = day + "";
                        String month2 = month + "";
                        day2 = (day2.length() == 1) ? "0" + day+"" : day2+""; // add a 0 if day is between 1 and 9
                        month2 = (month2.length() == 1) ? "0" + month : month2+"";// add a 0 if month is between 1 and 9

                        //render the birthDate
                        weightDate.setText(day2 + "-" + month2 + "-" + year);
                        weightDate.setError(null);
                    }
                }, day, month , year);
        datePickerDialog.updateDate(year, month, day); // set today's date
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()); // set today's day as max value
        datePickerDialog.show();
    }


    private void calculateImc() {
        try{
            if (!checkError()) {
                double weight = Double.parseDouble(weightField.getText().toString()); // get weight

                User user = new User();
                imc = user.calculateImc(weight, Globals.height); // calculate imc
                pmin = user.calculatePMin(Globals.height); // calculate pmin
                pmax = user.calculatePMax(Globals.height); // calculate pmax
                ig = user.calculateIg(calculateAge(), Globals.gender, imc); // calculate ig
                mg = user.calculateMg(weight, ig); // calculate mg
                at = user.calculateAt(Globals.gender, calculateAge(), Globals.height, weight); // calculate at
                mineral = user.calculateMineral(weight, mg, at); // calculate mineral
                protein = user.calculateProteina(weight, mg, at); // calculate protein

                // show user imc and the rest
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
                                "\nProtein: " + protein
                );

                builder.show();

                saveBtn.setEnabled(true);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private boolean checkError() {
        boolean error = false;

        if (weightField.getText().toString().isEmpty()) {
            weightField.setError(getResources().getString(R.string.emptyField)); //render error
            error = true;
        }

        return error;
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
        final ProgressDialog dialog = ProgressDialog.show(ImcCalculationActivity.this, "",
                "Loading. Please wait...", true);
        dialog.setCancelable(false);

        try{
            DateFormat timeFormat = new SimpleDateFormat("HH-mm-ss"); //set date format
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

            Date time = new Date();
            String date = weightDate.getText().toString();


            Date dt = simpleDateFormat.parse(date);
            long millis = dt.getTime();

            Imc imcClass = new Imc(timeFormat.format(time), date, imc, pmax, pmin, ig, mg, at, mineral, protein);

            long cenas = 9999999999999L;

            mDatabase.child(Globals.id).child((cenas - millis) + "").child(timeFormat.format(time)).setValue(imcClass)
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(ImcCalculationActivity.this);
                            builder.setMessage(e.getMessage());
                            builder.show();
                        }
                    });
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ImcCalculationActivity.this);
            builder.setMessage(e.getMessage());
            builder.show();

            dialog.dismiss();
        }

    }

}
