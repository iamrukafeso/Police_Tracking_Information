package com.rukayat_oyefeso.police_tracking_information;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class VehicleOwnerForm extends AppCompatActivity {

    private Button compBtn, getCurrentLocation;
    private EditText mVehicleReg, mInsuranceName, mInsuranceExpiryDate, mNCTValidDate, mRoadTaxValid,
    mAddress;
    String cityName = "";

    private DatabaseReference mRef,mUserRef;
    private FirebaseAuth mAuth;
    private FusedLocationProviderClient fus;

    private ProgressDialog mUserProgDialog;

    private Calendar calendar = Calendar.getInstance();
    private int year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_owner_form);

        mVehicleReg = findViewById(R.id.editTextNumber);
        mInsuranceName = findViewById(R.id.editTextTextPersonName);
        mInsuranceExpiryDate = findViewById(R.id.editTextDate);
        mNCTValidDate = findViewById(R.id.editTextDate2);
        mRoadTaxValid = findViewById(R.id.editTextDate3);
        mAddress = findViewById(R.id.editTextTextPostalAddress);
        compBtn =findViewById(R.id.button2);

        //get the year, month, day calender
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day =  calendar.get(Calendar.DAY_OF_MONTH);

        getCurrentLocation = (Button) findViewById(R.id.currentLoc);

        mRef = FirebaseDatabase.getInstance().getReference().child("UserForm");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        mUserProgDialog = new ProgressDialog(this);

        fus = LocationServices.getFusedLocationProviderClient(this);

        compBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(VehicleOwnerForm.this,login.class);
//                startActivity(intent);
                formProcess();
            }
        });

        getCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(VehicleOwnerForm.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getUserLocation();
                } else {

                    ActivityCompat.requestPermissions(VehicleOwnerForm.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        mInsuranceExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show the Calender when Date of Birth clicked
                showCalenderDialog();
            }
        });
        mNCTValidDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show the Calender when Date of Birth clicked
                showCalenderDialogNct();
            }
        });
        mRoadTaxValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show the Calender when Date of Birth clicked
                showCalenderDialogRoad();
            }
        });

    }

    private void formProcess() {
        mUserProgDialog.setTitle("Process Form");
        mUserProgDialog.setMessage("Please wait for a moment");
        mUserProgDialog.setCanceledOnTouchOutside(false);
        mUserProgDialog.show();

        String userRegNum = mVehicleReg.getText().toString();
        String insuranceName = mInsuranceName.getText().toString();
        String insuranceExpireDate = mInsuranceExpiryDate.getText().toString();
        String NCTValidDate = mNCTValidDate.getText().toString();
        String RoadTaxValid = mRoadTaxValid.getText().toString();
        String userAddress = mAddress.getText().toString();

        if(userRegNum.isEmpty())
        {
            mUserProgDialog.hide();
            mVehicleReg.setError("Please enter this field");
        }
        if(insuranceName.isEmpty())
        {
            mUserProgDialog.hide();
            mInsuranceName.setError("Please enter this field");
        }
        if(insuranceExpireDate.isEmpty())
        {
            mUserProgDialog.hide();
            mInsuranceExpiryDate.setError("Please enter this field");
        }
        if(NCTValidDate.isEmpty())
        {
            mUserProgDialog.hide();
            mNCTValidDate.setError("Please enter this field");
        }
        if(RoadTaxValid.isEmpty())
        {
            mUserProgDialog.hide();
            mRoadTaxValid.setError("Please enter this field");
        }
        else if(userAddress.isEmpty())
        {
            mUserProgDialog.hide();
            mAddress.setError("Please enter this field");
        }
        else {
            // get the id of current police;
            FirebaseUser mCurrentUser = mAuth.getCurrentUser();
            final String user_id = mCurrentUser.getUid();

            // used the values in hashMap

            HashMap<String,String> userUser = new HashMap<>();

            //mVehicleReg, mInsuranceName, mInsuranceExpiryDate, mNCTValidDate, mRoadTaxValid,
            //    mAddress;
            userUser.put("Vehicle Reg Number", userRegNum);
            userUser.put("Insurance Name", insuranceName);
            userUser.put("Insurance Expire Date", insuranceExpireDate);
            userUser.put("NCT Valid Date", NCTValidDate);
            userUser.put("Road Tax Valid Date", RoadTaxValid);
            userUser.put("Vehicle Owner Address", userAddress);

            mRef.child(user_id).setValue(userUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        mUserRef.child(user_id).child("fillForm").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    mUserProgDialog.dismiss();
                                    Intent userMainIntent= new Intent(VehicleOwnerForm.this, MainActivity.class);
                                    startActivity(userMainIntent);
                                    finish();
                                }
                            }
                        });
                    }

                    else {
                        mUserProgDialog.hide();
                        Toast.makeText(VehicleOwnerForm.this, "There was an errors please try again", Toast.LENGTH_SHORT).show();
                    }
                }

            });

        }

    }

    //Create Calender dialog
    public void showCalenderDialog ()
    {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //set text edit date format
                mInsuranceExpiryDate.setText(dayOfMonth + "/" + month + "/" + year);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener, year, month, day);
        datePickerDialog.show();
    }

    //Create Calender dialog
    public void showCalenderDialogNct ()
    {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //set text edit date format
                mNCTValidDate.setText(dayOfMonth + "/" + month + "/" + year);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener, year, month, day);
        datePickerDialog.show();
    }

    //Create Calender dialog
    public void showCalenderDialogRoad ()
    {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //set text edit date format
                mRoadTaxValid.setText(dayOfMonth + "/" + month + "/" + year);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener, year, month, day);
        datePickerDialog.show();
    }

    //    // Get current user location
    public void getUserLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
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
                        Geocoder ge = new Geocoder(VehicleOwnerForm.this, Locale.getDefault());

                        addressList = ge.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        String cityName = addressList.get(0).getAddressLine(0);


                        if(cityName.contains("County")){
                            String cityArr [] = cityName.split(" ");
                            mAddress.setText(cityArr[1]);
                        }
                        else{
                            mAddress.setText(cityName);
                        }
                        Log.i("CityName",addressList.toString());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }
}