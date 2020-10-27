package com.rukayat_oyefeso.police_tracking_information;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Register extends AppCompatActivity {

    Button regiBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regiBtn = findViewById(R.id.regBtn);

        regiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regBtn = new Intent(Register.this,login.class);
                startActivity(regBtn);
            }
        });
    }
}