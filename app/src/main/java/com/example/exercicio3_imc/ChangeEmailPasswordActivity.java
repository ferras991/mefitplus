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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    private FirebaseUser mAuth;

    private EditText currentPassword, newPassword, confirmPassword;
    private Button saveBtn;

    private ProgressDialog dialog;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();

        currentPassword = findViewById(R.id.passwordFieldChangePassword);
        newPassword = findViewById(R.id.newPasswordFieldChangePassword);
        confirmPassword = findViewById(R.id.confirmPasswordFieldChangePassword);
        saveBtn = findViewById(R.id.saveBtnChangePassword);
    }

    public void onClickChangePassword(View view) {
        saveBtn.setEnabled(false);
        dialog = ProgressDialog.show(ChangePasswordActivity.this, "",
                getResources().getString(R.string.LoadingTxt), true);
        dialog.setCancelable(false);

        if (currentPassword.getText().toString().isEmpty() || newPassword.getText().toString().isEmpty()
                || confirmPassword.getText().toString().isEmpty()) {
            checkFields();
        } else {
            String email = mAuth.getEmail();
            String password = currentPassword.getText().toString();
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);

            mAuth.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mAuth.updatePassword(newPassword.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.passwordChange), Toast.LENGTH_LONG).show();
                                            finish();
                                        } else {
                                            Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.passwordNotChange), Toast.LENGTH_LONG).show();
                                        }
                                        dialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.passwordNotChange), Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                        saveBtn.setEnabled(true);
                                    }
                                });
                    }
                }
            });
        }
    }

    private void checkFields() {
        if (currentPassword.getText().toString().isEmpty()) {
            currentPassword.setError(getResources().getString(R.string.fields_empty_error));

        }

        if (newPassword.getText().toString().isEmpty()) {
            newPassword.setError(getResources().getString(R.string.fields_empty_error));
        }

        if (confirmPassword.getText().toString().isEmpty()) {
            confirmPassword.setError(getResources().getString(R.string.fields_empty_error));
        }

        if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())
                && !confirmPassword.getText().toString().isEmpty()
                && !newPassword.getText().toString().isEmpty()) {
            newPassword.setError(getResources().getString(R.string.passwords_match));
            confirmPassword.setError(getResources().getString(R.string.passwords_match));
        }

        saveBtn.setEnabled(true);
        dialog.dismiss();
    }
}
