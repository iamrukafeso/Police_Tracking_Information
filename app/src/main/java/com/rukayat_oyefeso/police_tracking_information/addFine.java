package com.rukayat_oyefeso.police_tracking_information;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class addFine extends AppCompatActivity {
    private ImageView mcreateBtn, mcancelBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fine);

        mcreateBtn = findViewById(R.id.createFine);
        mcancelBtn = findViewById(R.id.cancelFine);

        mcreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createBtn = new Intent(addFine.this, ticketCreated.class);
                startActivity(createBtn);
            }
        });

        mcancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelBtn = new Intent(addFine.this, DisplayUserInfo.class);
                startActivity(cancelBtn);
            }
        });

//        Date currentTime = Calendar.getInstance().getTime();

    }
}