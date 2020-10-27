package com.rukayat_oyefeso.police_tracking_information;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class login extends AppCompatActivity {

    ImageView imageView, loginImg, PassImg;
    Animation smalltobig, btta, btta2;
    TextView textView, subtitle_header;
    Button button;
    EditText editText, editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // load the login animation
        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        btta = AnimationUtils.loadAnimation(this, R.anim.btta);
        btta2 = AnimationUtils.loadAnimation(this, R.anim.btta2);

        imageView = findViewById(R.id.imageView);
        loginImg = findViewById(R.id.imageView2);
        PassImg = findViewById(R.id.imageView3);

        textView = findViewById(R.id.textView);
        subtitle_header = findViewById(R.id.subtitle_header);

        button = findViewById(R.id.loginBtn);

        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);



        // passing animation and start it
        imageView.startAnimation(smalltobig);

        textView.startAnimation(btta);
        subtitle_header.startAnimation(btta);

        button.startAnimation(btta2);

        editText.startAnimation(btta2);
        editText2.startAnimation(btta2);
        loginImg.startAnimation(btta2);
        PassImg.startAnimation(btta2);
    }
}