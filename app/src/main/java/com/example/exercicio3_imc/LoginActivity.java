package com.example.exercicio3_imc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.example.exercicio3_imc.Class.User;
import com.example.exercicio3_imc.Globals.Globals;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginBtn;
    private TextView registerTxt;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getSupportActionBar().setTitle(getResources().getString(R.string.app_name) + " - Login");

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        registerTxt = findViewById(R.id.registerTxt);
        loginBtn = findViewById(R.id.loginBtn);
    }

    public void onClickLogin(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                login();
                break;
            case R.id.registerTxt:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.passwordResetTxt:
                startActivity(new Intent(LoginActivity.this, PasswordRecuperation.class));
                break;
            default:
                break;
        }
    }

    private void login() {
        dialog = ProgressDialog.show(LoginActivity.this, "",
                getResources().getString(R.string.LoadingTxt), true);
        dialog.setCancelable(false);

//        loginBtn.setEnabled(false);
//        registerTxt.setEnabled(false);
//
//        if (emailField.getText().toString().isEmpty() || passwordField.getText().toString().isEmpty()) {
//            checkAllFields();
//        } else {
//            getInfo();
//        }

        getInfo();
    }

    private void checkAllFields() {
        if (emailField.getText().toString().isEmpty()) {
            emailField.setError(getResources().getString(R.string.fields_empty_error));
            loginBtn.setEnabled(true);
            registerTxt.setEnabled(true);
        }

        if (passwordField.getText().toString().isEmpty()) {
            passwordField.setError(getResources().getString(R.string.fields_empty_error));
            loginBtn.setEnabled(true);
            registerTxt.setEnabled(true);
        }

        dialog.dismiss();
    }

    private void getInfo() {
//        String email = emailField.getText().toString();
//        String password = passwordField.getText().toString();

        String email = "ferrasdocas@gmail.com";
        String password = "123456789";



        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userID = mAuth.getUid();
                            sendToMain(userID);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("Login Failed");
                        builder.setMessage(e.getMessage());
                        builder.show();

                        loginBtn.setEnabled(true);
                        registerTxt.setEnabled(true);

                        dialog.dismiss();
                    }
                });
    }

    private void sendToMain(final String userId) {
        mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                try {
                    User newUser = dataSnapshot.getValue(User.class);

                    if (userId.equals(newUser.getId())) {
                        Globals.id = userId;
                        Globals.email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        Globals.name = newUser.getName();
                        Globals.birthDate = newUser.getbirthDate();
                        Globals.gender = newUser.getGender();
                        Globals.height = newUser.getHeight();
                        Globals.weight = newUser.getWeight();
                    }
                }catch (Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

