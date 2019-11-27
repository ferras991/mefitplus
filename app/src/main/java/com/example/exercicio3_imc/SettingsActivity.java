package com.example.exercicio3_imc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void onClickSettings(View view) {
        switch (view.getId()) {
            case R.id.calculateIMCSettings:
                startActivity(new Intent(SettingsActivity.this, ImcCalculationActivity.class));
                break;
            case R.id.userSettings:
                startActivity(new Intent(SettingsActivity.this, UserInfo.class));
        }
    }
}
