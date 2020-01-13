package com.example.exercicio3_imc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.exercicio3_imc.Globals.Globals;
import com.google.firebase.auth.FirebaseAuth;

public class UserInfo extends AppCompatActivity {

    private EditText name, birthDate, gender, height, email;
    private Button changeUserInfoBtn;

    private FirebaseAuth mAuth;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        //EditTexts
        name = findViewById(R.id.nameFieldUserInfo);
        birthDate = findViewById(R.id.birthDateFieldUserInfo);
        gender = findViewById(R.id.genderFieldUserInfo);
        height = findViewById(R.id.heightFieldUserInfo);
        email = findViewById(R.id.emailFieldUserInfo);

        //Buttons
        changeUserInfoBtn = findViewById(R.id.changeUserInfo);

        name.setText(Globals.name);
        birthDate.setText(Globals.birthDate);
        if (Globals.gender == 0) gender.setText(getResources().getString(R.string.genderFemale)); else gender.setText(getResources().getString(R.string.genderMale));
        height.setText(Globals.height + "");
        email.setText(Globals.email);

    }

    public void onClickUserInfo(View view) {
        switch (view.getId()) {
            case R.id.changePasswordUserInfo:
                startActivity(new Intent(
                        UserInfo.this,
                        ChangeEmailPasswordActivity.class)
                        .putExtra("change", "password"));
                break;
            case R.id.changeUserEmailInfo:
                startActivity(new Intent(
                        UserInfo.this,
                        ChangeEmailPasswordActivity.class)
                        .putExtra("change", "email"));
                break;
            case R.id.changeUserInfo:
                if (changeUserInfoBtn.getText().toString().equals("Edit Fields")) {
                    enableFields(true);
                } else {
                    enableFields(false);
                }
                break;
        }
    }

    private void enableFields(boolean enable) {
        if (enable) {
            name.setEnabled(true);
            birthDate.setEnabled(true);
            gender.setEnabled(true);
            height.setEnabled(true);

            changeUserInfoBtn.setText("Save Fields");
        } else {
            name.setEnabled(false);
            birthDate.setEnabled(false);
            gender.setEnabled(false);
            height.setEnabled(false);

            changeUserInfoBtn.setText("Edit Fields");
        }
    }
}
