package com.rukayat_oyefeso.police_tracking_information;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    ImageView imageView, loginImg, PassImg;
    Animation smalltobig, btta, btta2;
    TextView textView, subtitle_header, regText, fp;
    Button button;
    EditText mEmailEdit, mPassEdit;
    private DatabaseReference mRef, mUserRef, mPoliceRef;
    private FirebaseAuth mAuth;

    private ProgressDialog mProgDialog;
    private static final  int REQUEST_CODE_PERMISSION =2;
    String mPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mProgDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        try{
            if (ActivityCompat.checkSelfPermission(this,mPermission)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{mPermission},REQUEST_CODE_PERMISSION);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // load the login animation
        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        btta = AnimationUtils.loadAnimation(this, R.anim.btta);
        btta2 = AnimationUtils.loadAnimation(this, R.anim.btta2);

        mAuth = FirebaseAuth.getInstance();
        imageView = findViewById(R.id.imageView);
        loginImg = findViewById(R.id.imageView2);
        PassImg = findViewById(R.id.imageView3);

        regText = findViewById(R.id.regTextView);
        textView = findViewById(R.id.textView);
        subtitle_header = findViewById(R.id.subtitle_header);

        button = findViewById(R.id.loginBtn);

        mEmailEdit = findViewById(R.id.editText);
        mPassEdit = findViewById(R.id.editText2);
        fp = findViewById(R.id.forgetPass);

        // passing animation and start it
        imageView.startAnimation(smalltobig);

        textView.startAnimation(btta);
        subtitle_header.startAnimation(btta);

        button.startAnimation(btta2);

        mEmailEdit.startAnimation(btta2);
        mPassEdit.startAnimation(btta2);
        loginImg.startAnimation(btta2);
        PassImg.startAnimation(btta2);

        regText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(login.this,Register.class);
                startActivity(regIntent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String em = mEmailEdit.getText().toString();
//                String pass = mPassEdit.getText().toString();
//                SessionManager sessionManager = new SessionManager(login.this);
//                sessionManager.createLoginSession(mEmailEdit, mPassEdit);
                loginProcess();
//                mAuth.signInWithEmailAndPassword(em,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Intent regBtn = new Intent(login.this, MainActivity.class);
//                            startActivity(regBtn);
//                        }
//                        else {
//                            Toast.makeText(login.this, "Invalid details", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

            }
        });

        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forText = new Intent(login.this,forgetPassword.class);
                startActivity(forText);
            }
        });
    }

    public void loginProcess() {
        mProgDialog.setTitle("Login Process");
        mProgDialog.setMessage("Please wait for a moment");
        mProgDialog.setCanceledOnTouchOutside(false);
        mProgDialog.show();

        final String eml = mEmailEdit.getText().toString();
        final String pass = mPassEdit.getText().toString();

        if (eml.isEmpty()) {
            mProgDialog.hide();
            mEmailEdit.setError("Please enter email");
        }

        else if (pass.isEmpty()) {
            mProgDialog.hide();
            mPassEdit.setError("Please enter password");
        }
        else {

            mAuth.signInWithEmailAndPassword(eml,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()) {
                        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String userId = mCurrentUser.getUid();

                        mUserRef = mRef.child(userId);

                        mUserRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String accountType = dataSnapshot.child("accountType").getValue().toString();
                                Log.i("accountType", accountType);
                                String fillForm = dataSnapshot.child("fillForm").getValue().toString();
                                Log.i("fillForm", fillForm);

                                if (accountType.equals("Vehicle Owner"))
                                {
                                    if(fillForm.equals("false")) {
                                        if(mAuth.getCurrentUser().isEmailVerified())
                                        {
                                            mProgDialog.dismiss();
                                            Intent vehicleOwnerFormIntent = new Intent(login.this, VehicleOwnerForm.class);
                                            startActivity(vehicleOwnerFormIntent);
                                            finish();
                                        }
                                        else{
                                            mProgDialog.hide();
                                            Toast.makeText(login.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    else
                                    {
                                        mProgDialog.dismiss();
                                        Intent vehicleIntent = new Intent(login.this, VehicleOwnerMainActivity.class);
                                        startActivity(vehicleIntent);
                                        finish();
                                    }
                                }
                                else if(accountType.equals("Police")) {
                                    if(fillForm.equals("false")) {
                                        if(mAuth.getCurrentUser().isEmailVerified()) {
                                            mProgDialog.dismiss();
                                            Intent policeFormIntent = new Intent(login.this, policeForm.class);
                                            startActivity(policeFormIntent);
                                            finish();
                                        }
                                        else{
                                            mProgDialog.hide();
                                            Toast.makeText(login.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    else{
                                        Intent policeIntent = new Intent(login.this, MainActivity.class);
                                        startActivity(policeIntent);
                                        finish();
                                    }
                                }
                                else {
                                    mProgDialog.dismiss();
                                    Intent mainIntent = new Intent(login.this, login.class);
                                    startActivity(mainIntent);
                                    finish();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else {
                        mProgDialog.dismiss();
                        Toast.makeText(login.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    public void onClick(View view)
    {
        Intent intent = new Intent(login.this, Register.class);
        startActivity(intent);

    }
}