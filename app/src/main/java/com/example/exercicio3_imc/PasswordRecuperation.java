package com.example.exercicio3_imc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordRecuperation extends AppCompatActivity {
    private Button btn;
    private EditText emailField;

    private ProgressDialog dialog;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recuperation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Buttons
        btn = findViewById(R.id.passwordRecuperationBtn);

        //EditTexts
        emailField = findViewById(R.id.emailFieldPasswordRecuperation);
    }

    public void onClick(View view) {
        try {
            btn.setEnabled(false);
            dialog = ProgressDialog.show(PasswordRecuperation.this, "",
                    getResources().getString(R.string.LoadingTxt), true);
            dialog.setCancelable(false);

            if (!checkError()) {emailExists();}
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkError() {
        if (emailField.getText().toString().isEmpty()) {
            emailField.setError(getResources().getString(R.string.fields_empty_error));
            btn.setEnabled(true);
            return true;
        }

        return false;
    }

    private void emailExists() {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(emailField.getText().toString(), "123456789")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.delete();

                            Toast.makeText(PasswordRecuperation.this, getResources().getString(R.string.emailNotExist), Toast.LENGTH_LONG).show();
                            btn.setEnabled(true);
                            dialog.dismiss();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e.getMessage().equals("The email address is already in use by another account.")) {
                            sendEmail(mAuth, emailField.getText().toString());
                            btn.setEnabled(true);
                        } else {
                            Toast.makeText(PasswordRecuperation.this, getResources().getString(R.string.emailNotExist), Toast.LENGTH_LONG).show();
                            btn.setEnabled(true);
                            dialog.dismiss();
                        }
                    }
                });
    }

    private void sendEmail(FirebaseAuth mAuth, String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Toast.makeText(PasswordRecuperation.this, getResources().getString(R.string.emailSent), Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(PasswordRecuperation.this, getResources().getString(R.string.emailNotSent), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(PasswordRecuperation.this, getResources().getString(R.string.emailNotSent), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
