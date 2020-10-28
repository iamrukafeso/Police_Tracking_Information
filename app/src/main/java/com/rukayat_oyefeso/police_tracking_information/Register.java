package com.rukayat_oyefeso.police_tracking_information;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Register extends AppCompatActivity {

    EditText name, user, email, pass, confirmPass;
    Spinner accountType;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.regEditText);
        user = findViewById(R.id.regEditText2);
        email = findViewById(R.id.regEditText3);
        pass = findViewById(R.id.regEditText4);
        confirmPass = findViewById(R.id.regEditText5);
        accountType = findViewById(R.id.accTypeSpinner);
        nextBtn = findViewById(R.id.regNextBtn);

        //move to different activity when you select either Police or user
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.userTypes, R.layout.support_simple_spinner_dropdown_item);
        accountType.setAdapter(adapter);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = accountType.getSelectedItem().toString();
                if (item.equals("Vehicle Owner")){
                    Intent intent = new Intent(Register.this, VehicleOwnerForm.class);
                    startActivity(intent);
                }
                else {
                    Intent intent2 = new Intent(Register.this, policeForm.class);
                    startActivity(intent2);
                }
            }
        });


    }
}