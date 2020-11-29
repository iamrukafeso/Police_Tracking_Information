package com.rukayat_oyefeso.police_tracking_information;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VehicleOwnerForm extends AppCompatActivity {

    Button compBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_owner_form);

        compBtn =findViewById(R.id.button2);

        compBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehicleOwnerForm.this,login.class);
                startActivity(intent);
            }
        });


    }
}