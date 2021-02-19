package com.rukayat_oyefeso.police_tracking_information;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {
    //5200 Equals 5.2 seconds.
    private static int SPLASH_SCREEN = 5200;

    LottieAnimationView lottieAnimationView;

    SharedPreferences boardingScreen;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef, mUserDatabase;
    private ProgressDialog mProgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        lottieAnimationView = findViewById(R.id.lottieAnimationView);

        lottieAnimationView.animate().translationX(1).setDuration(500).setStartDelay(500);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        mUserDatabase = mRef.child("Users");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendToMainActivities();
            }
        },SPLASH_SCREEN);

    }

    private void sendToMainActivities() {
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();


        if(mCurrentUser == null) {
            Intent intent = new Intent(SplashScreen.this, walkThroughScreen.class);
            startActivity(intent);
            finish();
        }
        else if (mCurrentUser != null) {
            String userId = mCurrentUser.getUid();

            mUserDatabase.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String accType = dataSnapshot.child("accountType").getValue().toString();
                        String fillForm = dataSnapshot.child("fillForm").getValue().toString();

                        if (accType.equals("Vehicle Owner") && fillForm.equals("true")) {
                            Intent vehicleIntent = new Intent(SplashScreen.this, FingerPrintAuthentication.class);
                            startActivity(vehicleIntent);
                            finish();
                        } else if (accType.equals("Police") && fillForm.equals("true")) {
                            Intent policeIntent = new Intent(SplashScreen.this, FingerPrintAuthentication.class);
                            startActivity(policeIntent);
                            finish();
                        }
                        else{
                            Intent loginIntent = new Intent(SplashScreen.this, login.class);
                            startActivity(loginIntent);
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
}
