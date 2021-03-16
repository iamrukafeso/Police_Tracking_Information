package com.rukayat_oyefeso.police_tracking_information;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayUserInfo extends AppCompatActivity {

    private TextView mFirstUserName, mUserSurname, mUserCarReg, mUserInsurance, mUserInsuranceDate, mUserNCT, mUserRoadTax, mUserAddress;
    private Button mAddFine;
    private CircleImageView mProfileImage;
    private DatabaseReference mUserRef, mQuery;
    private FirebaseAuth mAuth;
    private String mUserId;
    private FirebaseUser mUser;
    private FirebaseStorage mStorageRef;
    private ProgressDialog mProgressDialog;
    private String userID;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_info);

        mFirstUserName = findViewById(R.id.user_display_name);
        mUserSurname = findViewById(R.id.user_display_surname);
        mUserCarReg = findViewById(R.id.userCarReg);
        mUserInsurance = findViewById(R.id.userInsurance);
        mUserInsuranceDate = findViewById(R.id.userInsuranceDate);
        mUserNCT = findViewById(R.id.userNCT);
        mUserRoadTax = findViewById(R.id.userRoadTax);
        mUserAddress = findViewById(R.id.userAddress);
        mAddFine = findViewById(R.id.addFine2);
        mProfileImage = findViewById(R.id.user_image);

//        mProgressDialog = new ProgressDialog(this);

        userID = getIntent().getStringExtra("userID");
//        Log.i("User Id", userID);

        firebaseUserInfo();

        mAddFine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aF = new Intent(DisplayUserInfo.this, addFine.class);
                aF.putExtra("userID", userID);
                startActivity(aF);
            }
        });

    }

    private void firebaseUserInfo() {
//        mProgressDialog.setTitle("Search Process ");
//        mProgressDialog.setMessage("Searching for user, please wait for a moment");
//        mProgressDialog.setCanceledOnTouchOutside(false);
//        mProgressDialog.show();

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("UserForm").child(userID);


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String carReg = snapshot.child("vehicleRegNumber").getValue().toString();
                String insuranceName = snapshot.child("insuranceName").getValue().toString();
                String insuranceExpiryDT = snapshot.child("insuranceExpireDate").getValue().toString();
                String NctExpiryDt = snapshot.child("nctValidDate").getValue().toString();
                String RDExpiryDT = snapshot.child("roadTaxValidDate").getValue().toString();
                String UserAddres = snapshot.child("vehicleOwnerAddress").getValue().toString();
//                String userName = snapshot.child("firstName").getValue().toString();

                mUserCarReg.setText("Car Registration: " + carReg);
                mUserInsurance.setText("Insurance Name: " + insuranceName);
                mUserInsuranceDate.setText("Insurance Expiry Date: "+ insuranceExpiryDT);
                mUserNCT.setText("NCT Expiry Date: "+ NctExpiryDt);
                mUserRoadTax.setText("Road Tax Expiry Date: "+ RDExpiryDT);
                mUserAddress.setText(UserAddres);

                DatabaseReference nameRef = FirebaseDatabase.getInstance().getReference("Users").child(userID);

                nameRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String userName = snapshot.child("firstName").getValue().toString();
                        String userSName = snapshot.child("surname").getValue().toString();
                        String UserImage = snapshot.child("image").getValue().toString();

                        mFirstUserName.setText(userName);
                        mUserSurname.setText(userSName);

                        Log.i("testImage" , UserImage);
                        // Load the image using picasso
                        Picasso.get().load(UserImage).placeholder(R.drawable.ic_user_photo).into(mProfileImage);
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
//        mDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//
//                    Log.i("User display info", data.toString());
//                    Users models=data.getValue(Users.class);
//                   // displayUser models = data.getValue(displayUser.class);
//////                    String regNum = models.getVehicleRegNumber();
//////                    String insExpDate = models.getInsuranceExpireDate();
//                    String insurance = models.getInsuranceName();
////
//                     mUserInsurance.setText("Insurance Name: " + insurance);
////                    mProgressDialog.hide();
//
//                }
//
//            }

//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
}