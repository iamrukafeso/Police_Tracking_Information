package com.rukayat_oyefeso.police_tracking_information;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class viewFine extends AppCompatActivity {

    private ImageView mBackBTN;
    private TextView mBackBTN2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fine);
        mBackBTN = findViewById(R.id.backBTN);
        mBackBTN2 = findViewById(R.id.backBTN2);

        mBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bc = new Intent(viewFine.this, VehicleOwnerMainActivity.class);
                startActivity(bc);
            }
        });

        mBackBTN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bc2 = new Intent(viewFine.this, VehicleOwnerMainActivity.class);
                startActivity(bc2);
            }
        });
    }
}