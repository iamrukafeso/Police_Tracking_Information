package com.rukayat_oyefeso.police_tracking_information;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class policeForm extends AppCompatActivity {

    Button policeBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_form);

        policeBtn = findViewById(R.id.policeBtnXml);
        mAuth = FirebaseAuth.getInstance();

        policeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent policeIntent = new Intent(policeForm.this, login.class);
                startActivity(policeIntent);
            }
        });
    }
}