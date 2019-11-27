package com.example.exercicio3_imc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        btn = findViewById(R.id.passwordRecuperationBtn);
        emailField = findViewById(R.id.emailFieldPasswordRecuperation);
    }

    public void onClick(View view) {
        try {
            btn.setEnabled(false);
            if (emailField.getText().toString().isEmpty()) {
                emailField.setError(getResources().getString(R.string.fields_empty_error));
                btn.setEnabled(true);
            } else {
                final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(emailField.getText().toString(), "123456789")
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.delete();
                                }

                                Toast.makeText(PasswordRecuperation.this, "Email Does Not Exist", Toast.LENGTH_LONG).show();
                                btn.setEnabled(true);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (e.getMessage().equals("The email address is already in use by another account.")) {
                                    mAuth.sendPasswordResetEmail(emailField.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(PasswordRecuperation.this, "Email sent", Toast.LENGTH_LONG).show();
                                                    finish();
                                                }
                                            });
                                } else {
                                    Toast.makeText(PasswordRecuperation.this, "Email Does Not Exist", Toast.LENGTH_LONG).show();
                                    btn.setEnabled(true);
                                }
                            }
                        });
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
