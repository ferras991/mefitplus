package com.example.exercicio3_imc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exercicio3_imc.Globals.Globals;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeEmailPasswordActivity extends AppCompatActivity {
    private FirebaseUser mAuth;

    private EditText currentEmailPasswordField, newPasswordField, confirmPasswordField;
    private TextView currentEmailPassTxt, newPasswordTxt, confirmPasswordTxt;
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
        setContentView(R.layout.activity_change_email_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();

        //TextViews
        currentEmailPassTxt = findViewById(R.id.currentEmailPassTxtChange);
        newPasswordTxt = findViewById(R.id.newPasswordTxtChange);
        confirmPasswordTxt = findViewById(R.id.confirmPasswordTxtChange);

        //EditTexts
        currentEmailPasswordField = findViewById(R.id.currentEmailPasswordFieldChange);
        newPasswordField = findViewById(R.id.newPasswordFieldChange);
        confirmPasswordField = findViewById(R.id.confirmPasswordFieldChange);

        //Buttons
        saveBtn = findViewById(R.id.saveBtnChange);

        Bundle extras = getIntent().getExtras();
        if (extras.getString("change").equals("email")) { emailSettings(); }
    }


    private void emailSettings() {
        currentEmailPassTxt.setText(getResources().getString(R.string.newEmail));
        currentEmailPasswordField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        //put the other TextViews invisible
        newPasswordTxt.setVisibility(View.INVISIBLE);
        confirmPasswordTxt.setVisibility(View.INVISIBLE);

        //put the other EditTexts invisible
        newPasswordField.setVisibility(View.INVISIBLE);
        confirmPasswordField.setVisibility(View.INVISIBLE);
    }

    public void onClickChangePassword(View view) {
        saveBtn.setEnabled(false);
        dialog = ProgressDialog.show(ChangeEmailPasswordActivity.this, "",
                getResources().getString(R.string.LoadingTxt), true);
        dialog.setCancelable(false);
        dialog.show();

        Bundle extras = getIntent().getExtras();
        if (extras.getString("change").equals("email")) {
            changeEmail();
        } else {
            changePassword();
        }


    }

    private void changeEmail() {
        if (!checkFields("email")) {
            final String email = currentEmailPasswordField.getText().toString();
            mAuth.updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChangeEmailPasswordActivity.this, "Email Changed Successfully!", Toast.LENGTH_SHORT).show();
                                saveEmail(email);
                                finish();
                            } else {
                                Toast.makeText(ChangeEmailPasswordActivity.this, "Email Change Failed!", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChangeEmailPasswordActivity.this, "Email Change Failed!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            saveBtn.setEnabled(true);
                        }
                    });
        }
    }

    private void changePassword() {
        if (!checkFields("")) {
            String email = mAuth.getEmail();
            String password = currentEmailPasswordField.getText().toString();
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);

            mAuth.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mAuth.updatePassword(newPasswordField.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ChangeEmailPasswordActivity.this, getResources().getString(R.string.passwordChange), Toast.LENGTH_LONG).show();
                                            finish();
                                        } else {
                                            Toast.makeText(ChangeEmailPasswordActivity.this, getResources().getString(R.string.passwordNotChange), Toast.LENGTH_LONG).show();
                                        }
                                        dialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ChangeEmailPasswordActivity.this, getResources().getString(R.string.passwordNotChange), Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                        saveBtn.setEnabled(true);
                                    }
                                });
                    }
                }
            });
        }
    }

    private boolean checkFields(String save) {
        boolean error = false;

        if (currentEmailPasswordField.getText().toString().isEmpty()) {
            currentEmailPasswordField.setError(getResources().getString(R.string.fields_empty_error));
            saveBtn.setEnabled(true);
            dialog.dismiss();

            if (save.equals("email")) { return true; } else { error = true; }
        }


        //if save.equals("") then check fields for password

        if (save.equals("")) {
            if (newPasswordField.getText().toString().isEmpty()) {
                newPasswordField.setError(getResources().getString(R.string.fields_empty_error));
                error = true;

                dialog.dismiss();
            }

            if (confirmPasswordField.getText().toString().isEmpty()) {
                confirmPasswordField.setError(getResources().getString(R.string.fields_empty_error));
                error = true;

                dialog.dismiss();
            }

            if (!newPasswordField.getText().toString().equals(confirmPasswordField.getText().toString())
                    && !confirmPasswordField.getText().toString().isEmpty()
                    && !newPasswordField.getText().toString().isEmpty()) {
                newPasswordField.setError(getResources().getString(R.string.passwords_match));
                confirmPasswordField.setError(getResources().getString(R.string.passwords_match));
                error = true;

                dialog.dismiss();
            }
        }
        saveBtn.setEnabled(true);

        return error;
    }

    private void saveEmail(String email) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(Globals.id);

        reference.push().setValue("email", email);
    }
}
