package com.example.exercicio3_imc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserInfo extends AppCompatActivity {

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
    }

    public void onClickUserInfo(View view) {
        switch (view.getId()) {
            case R.id.changePasswordUserInfo:
                startActivity(new Intent(UserInfo.this, ChangePasswordActivity.class));
                break;
        }
    }
}
