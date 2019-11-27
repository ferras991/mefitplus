package com.example.exercicio3_imc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.exercicio3_imc.Class.User;
import com.example.exercicio3_imc.Globals.Globals;
import com.google.firebase.auth.FirebaseAuth;

public class UserInfo extends AppCompatActivity {

    private EditText name, birthDate, gender, height, email;
    private Button changeBtn;

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

        name = findViewById(R.id.nameFieldUserInfo);
        birthDate = findViewById(R.id.birthDateFieldUserInfo);
        gender = findViewById(R.id.genderFieldUserInfo);
        height = findViewById(R.id.heightFieldUserInfo);
        email = findViewById(R.id.emailFieldUserInfo);
        changeBtn = findViewById(R.id.changePasswordUserInfo);

//        User user = new User();
        name.setText(Globals.name);
        birthDate.setText(Globals.birthDate);
        if (Globals.gender == 0) gender.setText(getResources().getString(R.string.genderFemale)); else gender.setText(getResources().getString(R.string.genderMale));
        height.setText(Globals.height + "");
        email.setText(Globals.email);

    }

    public void onClickUserInfo(View view) {
        switch (view.getId()) {
            case R.id.changePasswordUserInfo:
                startActivity(new Intent(UserInfo.this, ChangePasswordActivity.class));
                break;
        }
    }
}
