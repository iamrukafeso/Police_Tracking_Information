package com.rukayat_oyefeso.police_tracking_information;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ticketCreated extends AppCompatActivity {
    private TextView mAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_created);

        mAddBtn = findViewById(R.id.addTicketBtn);

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aBtn = new Intent(ticketCreated.this, fineAddedSuccessful.class);
                startActivity(aBtn);
            }
        });
    }
}