package com.example.exercicio3_imc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ShowUserImcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_imc);

        Bundle extras = getIntent().getExtras();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(extras.getString("time+imc"));
        builder.show();
    }
}
