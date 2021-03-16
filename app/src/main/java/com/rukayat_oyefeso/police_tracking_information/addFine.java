package com.rukayat_oyefeso.police_tracking_information;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class addFine extends AppCompatActivity {
    private TextView mFirstUserName, mUserSurname, mUserRegNumber, mfinePrice;
    private MultiAutoCompleteTextView mNote;
    private CircleImageView mProfileImage;
    private ImageView mcreateBtn, mcancelBtn;
    private Spinner spinner;

    private TextView mTime, mDate;
    private ProgressDialog mProgDialog;

    private DatabaseReference mUserRef, mRef, mQuery;
    private FirebaseAuth mAuth;
    private String mUserId, mAccountType, mFineType;
    private FirebaseUser mUser;
    private FirebaseStorage mStorageRef;
    private ProgressDialog mProgressDialog;
    private String userID;

    String date;
    String time;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fine);

        mcreateBtn = findViewById(R.id.createFine);
        mcancelBtn = findViewById(R.id.cancelFine);
        mFirstUserName = findViewById(R.id.displayVehicleNameFine);
        mUserSurname = findViewById(R.id.displayVehicleLastNameFine);
        mProfileImage = findViewById(R.id.vehicleFineMainProfileFine);
        mUserRegNumber = findViewById(R.id.carRegFine);

        mNote = findViewById(R.id.multiAutoCompleteTextNote);
        mfinePrice = findViewById(R.id.finePricePolice);

        mProgDialog = new ProgressDialog(this);

        spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFineType = parent.getItemAtPosition(position).toString();

//                Toast.makeText(addFine.this, "hello" + mFineType, Toast.LENGTH_SHORT).show();
                switch (mFineType) {
                    case "Speeding Offence":
                        mfinePrice.setText("3pt / €80");
                        break;
                    case "Seatbelt Offence":
                        mfinePrice.setText("€60");
                        break;
                    case "Motor Insurance Offence":
                        mfinePrice.setText("5pt");
                        break;
                    case "Careless Driving":
                        mfinePrice.setText("5pt / €5000");
                        break;
                    default:
                        mfinePrice.setText("0");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       // mFineType = spinner.getSelectedItem().toString();
       // Log.i("FineType", mFineType);

//        switch (mFineType) {
//            case "Speeding Offence":
//                mfinePrice.setText("3pt / €80");
//                break;
//            case "Seatbelt Offence":
//                mfinePrice.setText("€60");
//                break;
//            case "Motor Insurance Offence":
//                mfinePrice.setText("5pt");
//                break;
//            case "Careless Driving":
//                mfinePrice.setText("5pt / €5000");
//                break;
//            default:
//                mfinePrice.setText("0");
//                break;
//        }

//        userID = getIntent().getStringExtra("userID");
//
//        mRef = FirebaseDatabase.getInstance().getReference().child("PoliceForm");
//        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mRef = FirebaseDatabase.getInstance().getReference().child("Fine");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userID = getIntent().getStringExtra("userID");
        displayInfo();

        mTime = findViewById(R.id.txt_time);
        mDate = findViewById(R.id.txt_date);

        Calendar c = Calendar.getInstance();
        int minutes = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR);


        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH )+ 1;
        int year = c.get(Calendar.YEAR);
        //add +1 to months as java months starts from 0

        int mu= minutes;
        String m = "";
        if(month < 10) {
            m = "0" + month;
        }
        if(minutes < 10) {
            mu = 0 + minutes;
        }
        Log.i("testMin", String.valueOf(mu));
        date = day + "/" + m  + "/" + year;
        time = hour + ":" + mu ;
        mDate.setText(date);
        mTime.setText(time);


        mcreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFine();
            }
        });

        mcancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent cancelBtn = new Intent(addFine.this, DisplayUserInfo.class);
                //startActivity(cancelBtn);
            }
        });

//        Date currentTime = Calendar.getInstance().getTime();

    }



    private void addFine() {
        mProgDialog.setTitle("Registration Process ");
        mProgDialog.setMessage("Please wait for a moment");
        mProgDialog.setCanceledOnTouchOutside(false);
        mProgDialog.show();

        mFineType = spinner.getSelectedItem().toString();
        String  note = mNote.getText().toString();

        if (mFineType.equals("Select")) {
            mProgDialog.hide();
            Toast.makeText(this, "Please select account type", Toast.LENGTH_LONG).show();
        }

        else if (note.isEmpty()){
            mProgDialog.hide();
            mNote.setError("Please enter this field");
        }

        else{
            String vFine = mfinePrice.getText().toString();
            // used the values in hashMap
            HashMap<String,String> vehicleUser = new HashMap<>();

            vehicleUser.put("fineNote", note);
            vehicleUser.put("fineType", mFineType);
            vehicleUser.put("time", time);
            vehicleUser.put("date", date);
            vehicleUser.put("vFine", vFine);
//            vehicleUser.put("Badgenumber", policeBadgeNum);
            mRef.child(userID).push().setValue(vehicleUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        mProgDialog.dismiss();
                        Toast.makeText(addFine.this, "Fine created ", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), fineAddedSuccessful.class);

                        startActivity(intent);
                        finish();

                    }

                }
            });
//
        }
    }

    private void displayInfo() {

        DatabaseReference nameRef = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        nameRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child("firstName").getValue().toString();
                String userSName = snapshot.child("surname").getValue().toString();
                String UserImage = snapshot.child("image").getValue().toString();

                mFirstUserName.setText(userName + " ");
                mUserSurname.setText(userSName);

                Log.i("testImage" , UserImage);
                // Load the image using picasso
                Picasso.get().load(UserImage).placeholder(R.drawable.ic_user_photo).into(mProfileImage);

                DatabaseReference carRef = FirebaseDatabase.getInstance().getReference("UserForm").child(userID);

                carRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String userReg = snapshot.child("vehicleRegNumber").getValue().toString();

                        mUserRegNumber.setText(userReg);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}