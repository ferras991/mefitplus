package com.example.exercicio3_imc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.exercicio3_imc.Class.User;
import com.example.exercicio3_imc.Globals.Globals;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText emailField, passwordField, confirmPasswordField;
    private Button registerBtn;
    private EditText txtName, txtWeight, txtHeight, birthField;
    private RadioButton genderM, genderF;

    private Calendar currentDate = Calendar.getInstance();
    private int day = currentDate.get(Calendar.DAY_OF_MONTH);
    private int month = currentDate.get(Calendar.MONTH);
    private int year = currentDate.get(Calendar.YEAR);

    private ProgressDialog dialog;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.register));

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        emailField = findViewById(R.id.emailFieldRegister);
        passwordField = findViewById(R.id.passwordFieldRegister);
        confirmPasswordField = findViewById(R.id.confirmPasswordFieldRegister);
        registerBtn = findViewById(R.id.registerBtn);

        txtName = findViewById(R.id.nameRegister); // name
        txtWeight = findViewById(R.id.weightRegister); // weight
        txtHeight = findViewById(R.id.heightRegister); // height
        birthField = findViewById(R.id.birthDateRegister); // age
        genderM = findViewById(R.id.genderMRegister); // gender
        genderF = findViewById(R.id.genderFRegister); // gende
    }

    public void onClickRegister(View view) {
        switch (view.getId()) {
            case R.id.birthDateRegister:
                getDate();
                break;
            case R.id.genderMRegister:
                getGender("M");
                break;
            case R.id.genderFRegister:
                getGender("");
                break;
            case R.id.registerBtn:
                register();
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

                        //render the birthDate
                        birthField.setText(day + "-" + month + "-" + year);
                        birthField.setError(null);
                    }
                }, day, month , year);
        datePickerDialog.updateDate(year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void getGender(String gender) {
        //check which gender radiobutton is checked
        if (gender.equals("M")) {
            genderM.setChecked(true);
            genderF.setChecked(false);
        } else {
            genderF.setChecked(true);
            genderM.setChecked(false);
        }
    }

    private void register() {
        dialog = ProgressDialog.show(RegisterActivity.this, "",
                getResources().getString(R.string.LoadingTxt), true);
        dialog.setCancelable(false);

        registerBtn.setEnabled(false);

        try {

            if (!checkAllFields()) {
                final String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                final String name = txtName.getText().toString();
                final double weight = Double.parseDouble(txtWeight.getText().toString());
                final double height = Double.parseDouble(txtHeight.getText().toString());
                final String birthDate = birthField.getText().toString();
                final int gender = (genderM.isChecked()) ? 1 : 0 ;

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.userSuccess), Toast.LENGTH_LONG).show();

                                final String uid = mAuth.getUid();

                                User user = new User(uid, name, weight, height, birthDate, gender/*, email*/);

                                mDatabase.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            sendToMain(uid, name, weight, height, birthDate,gender);
                                        }
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setTitle(getResources().getString(R.string.userFail));
                                        builder.setMessage(e.getMessage());
                                        builder.show();

                                        dialog.dismiss();
                                        registerBtn.setEnabled(true);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setTitle(getResources().getString(R.string.userFail));
                                builder.setMessage(e.getMessage());
                                builder.show();

                                dialog.dismiss();
                                registerBtn.setEnabled(true);
                            }
                        });
            }
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(e.getMessage());
            builder.show();
        }

    }

    private boolean checkAllFields(){
        boolean error = false;

        if (emailField.getText().toString().isEmpty()) {
            emailField.setError(getResources().getString(R.string.fields_empty_error));
            dialog.dismiss();
            error = true;
        }

        if (passwordField.getText().toString().isEmpty()) {
            passwordField.setError(getResources().getString(R.string.fields_empty_error));
            dialog.dismiss();
            error = true;
        }

        if (confirmPasswordField.getText().toString().isEmpty()) {
            confirmPasswordField.setError(getResources().getString(R.string.fields_empty_error));
            dialog.dismiss();
            error = true;
        }

        if (!passwordField.getText().toString().equals(confirmPasswordField.getText().toString())
            && !passwordField.getText().toString().isEmpty()
            && !confirmPasswordField.getText().toString().isEmpty()) {
            passwordField.setError(getResources().getString(R.string.passwords_match));
            confirmPasswordField.setError(getResources().getString(R.string.passwords_match));
            dialog.dismiss();
            error = true;
        }

        if (txtName.getText().toString().isEmpty()) {
            txtName.setError(getResources().getString(R.string.fields_empty_error));
            dialog.dismiss();
            error = true;
        }

        if (txtWeight.getText().toString().isEmpty()) {
            txtWeight.setError(getResources().getString(R.string.fields_empty_error));
            dialog.dismiss();
            error = true;
        }

        if (txtHeight.getText().toString().isEmpty()) {
            txtHeight.setError(getResources().getString(R.string.fields_empty_error));
            dialog.dismiss();
            error = true;
        }

        if (birthField.getText().toString().isEmpty()) {
            birthField.setError(getResources().getString(R.string.fields_empty_error));
            dialog.dismiss();
            error = true;
        }

        registerBtn.setEnabled(true);
        return error;
    }

    private void sendToMain(final String userId, String name, double weight, double height, String birthDate, int gender) {
        Globals.id = userId;
        Globals.email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Globals.name = name;
        Globals.birthDate = birthDate;
        Globals.gender = gender;
        Globals.height = height;
        Globals.weight = weight;

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Main thread Interrupted");
        }

        dialog.dismiss();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
