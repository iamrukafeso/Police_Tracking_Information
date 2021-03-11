package com.rukayat_oyefeso.police_tracking_information;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class fineAddedSuccessful extends AppCompatActivity {
    private ImageView addSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_added_successful);

        addSuccess = findViewById(R.id.addSuccessfully);

        addSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aBtn = new Intent(fineAddedSuccessful.this, MainActivity.class);
                startActivity(aBtn);
            }
        });


    }
}