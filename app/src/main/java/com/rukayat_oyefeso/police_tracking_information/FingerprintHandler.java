package com.rukayat_oyefeso.police_tracking_information;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private FirebaseAuth mAuth;
    private DatabaseReference mRef, mUserDatabase;

    private Context context;
    private Activity activity;


    public FingerprintHandler(Context context, Activity activity){
        this.activity = activity;
        this.context = context;

    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {

        this.update("There was an Auth Error. " + errString, false);

    }

    @Override
    public void onAuthenticationFailed() {

        this.update("Auth Failed. ", false);

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

        this.update("Error: " + helpString, false);

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        this.update("You can now access the app.", true);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendToMainActivities();
            }
        }, 800);
        //go to police/User main activity

    }

    @SuppressLint("ResourceType")
    private void update(String s, boolean bool) {

        TextView paraLabel = (TextView) ((Activity)context).findViewById(R.id.paraLabel);
        LottieAnimationView imageView = (LottieAnimationView) ((Activity)context).findViewById(R.id.fingerprintImage);

        paraLabel.setText(s);

        if(bool == false){

            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

        } else {

            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.green));

            imageView.animate().setDuration(5000).setStartDelay(100);
            imageView.setAnimation(R.raw.done);
        }

    }


    private void sendToMainActivities() {

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        mUserDatabase = mRef.child("Users");
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();


        if(mCurrentUser == null) {
            Intent intent = new Intent(context.getApplicationContext(), walkThroughScreen.class);
            context.startActivity(intent);
            activity.finish();
            //finish();
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
                            Intent vehicleIntent = new Intent(context.getApplicationContext(), VehicleOwnerMainActivity.class);
                            context.startActivity(vehicleIntent);
                            //finish();
                            activity.finish();
                        } else if (accType.equals("Police") && fillForm.equals("true")) {
                            Intent policeIntent = new Intent(context.getApplicationContext(), MainActivity.class);
                            context.startActivity(policeIntent);
                            //finish();
                            activity.finish();
                        }
                        else{
                            Intent loginIntent = new Intent(context.getApplicationContext(), login.class);
                            context.startActivity(loginIntent);
                            activity.finish();
                            //finish();
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
