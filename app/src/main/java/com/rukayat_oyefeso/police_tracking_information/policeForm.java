package com.rukayat_oyefeso.police_tracking_information;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class policeForm extends AppCompatActivity {

    private Button policeBtn;
    private EditText mPoliceLocation, mBadgeNum;
    private Button getCurrentLocation;
    String cityName = "";

    private DatabaseReference mRef,mUserRef;
    private FirebaseAuth mAuth;
    private FusedLocationProviderClient fus;

    private ProgressDialog mPoliceProgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_form);

        mPoliceLocation = findViewById(R.id.policeLocation);
        mBadgeNum = findViewById(R.id.badgeNum);
        policeBtn = findViewById(R.id.policeBtnXml);

        getCurrentLocation = (Button) findViewById(R.id.currentLoc);

        mRef = FirebaseDatabase.getInstance().getReference().child("PoliceForm");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        mPoliceProgDialog = new ProgressDialog(this);

        fus = LocationServices.getFusedLocationProviderClient(this);

        getCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(policeForm.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getUserLocation();
                } else {

                    ActivityCompat.requestPermissions(policeForm.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        policeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent policeIntent = new Intent(policeForm.this, login.class);
//                startActivity(policeIntent);
                formProcess();
            }
        });
    }

    //    // Get current user location
    public void getUserLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fus.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();

                if(location !=  null){

                    List<Address> addressList;

                    try {
                        Geocoder ge = new Geocoder(policeForm.this, Locale.getDefault());

                        addressList = ge.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        String cityName = addressList.get(0).getAddressLine(0);


                        if(cityName.contains("County")){
                            String cityArr [] = cityName.split(" ");
                            mPoliceLocation.setText(cityArr[1]);
                        }
                        else{
                            mPoliceLocation.setText(cityName);
                        }
                        Log.i("CityName",addressList.toString());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    private void formProcess() {
        mPoliceProgDialog.setTitle("Process Form");
        mPoliceProgDialog.setMessage("Please wait for a moment");
        mPoliceProgDialog.setCanceledOnTouchOutside(false);
        mPoliceProgDialog.show();

        String policeLocation = mPoliceLocation.getText().toString();
        String policeBadgeNum = mBadgeNum.getText().toString();

        if(policeLocation.isEmpty())
        {
            mPoliceProgDialog.hide();
            mPoliceLocation.setError("Please enter this field");
        }

        if (policeBadgeNum.length() <= 5){
            mPoliceProgDialog.hide();
            mBadgeNum.setError("badge number must be 5 or more");
        }

        //to prevent letters in a badge number
        if (policeBadgeNum.matches("[a-zA-Z ]+")) {
            mBadgeNum.requestFocus();
            mBadgeNum.setError("Enter only numbers");
        }

        else if (policeBadgeNum.isEmpty()){
            mPoliceProgDialog.hide();
            mBadgeNum.setError("Please enter this field");
        }

        else{
            // get the id of current police;
            FirebaseUser mCurrentUser = mAuth.getCurrentUser();
            final String police_id = mCurrentUser.getUid();

            // used the values in hashMap

            HashMap<String,String> policeUser = new HashMap<>();

            policeUser.put("location", policeLocation);
            policeUser.put("Badgenumber", policeBadgeNum);

            mRef.child(police_id).setValue(policeUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        mUserRef.child(police_id).child("fillForm").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    mPoliceProgDialog.dismiss();
                                    Intent policeMainIntent= new Intent(policeForm.this, MainActivity.class);
                                    startActivity(policeMainIntent);
                                    finish();
                                }
                            }
                        });
                    }

                    else {
                        mPoliceProgDialog.hide();
                        Toast.makeText(policeForm.this, "There was an errors please try again", Toast.LENGTH_SHORT).show();
                    }
                }

            });

        }


    }


}