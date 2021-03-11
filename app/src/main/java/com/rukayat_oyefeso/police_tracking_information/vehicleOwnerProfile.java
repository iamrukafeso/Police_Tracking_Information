package com.rukayat_oyefeso.police_tracking_information;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class vehicleOwnerProfile extends AppCompatActivity {

    private EditText mFirstName,mSurname, mUserCarReg, mInsurance, mUserInsuranceDate, mUserNCT, mUserRoadTax, mUserAddress;
    private TextView mEmailText;
    private Button mUpdateBtn,mChangeProfileImgBtn;
    private Toolbar mProfileBar;

    private DatabaseReference mUserRef, mVehicleFormRef, mVehicleRef;
    private FirebaseAuth mAuth;
    private String mVehicleId;
    private FirebaseUser mUser;
    private StorageReference mStorageRef;
    private ProgressDialog mProgDialog;

    private CircleImageView mProfileImage;
    private static final int IMAGE_PICK = 1;

    DrawerLayout drawerLayout;
    Switch switchNightMode;

    public vehicleOwnerProfile() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_owner_profile);

        //Assign variable for night or dark mode
        drawerLayout = findViewById(R.id.drawer_layout);
        switchNightMode = findViewById(R.id.switchNightMode);

        int currentMode = AppCompatDelegate.getDefaultNightMode();

        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            switchNightMode.setChecked(true);
        } else {
            switchNightMode.setChecked(false);
        }

        switchNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                restartCurrentActivity();
            }
        });

//        mFirstName = findViewById(R.id.police_FirstNameEdit);

        mUserCarReg = findViewById(R.id.userCarReg2);
        mUserAddress = findViewById(R.id.userAddress2);
        mInsurance = findViewById(R.id.userInsurance2);
        mUserNCT = findViewById(R.id.userNCT2);
        mUserRoadTax = findViewById(R.id.userRoadTax2);
        mUserAddress = findViewById(R.id.userAddress2);
        mUserInsuranceDate = findViewById(R.id.userInsuranceDate2);

//        mEmailText = findViewById(R.id.doct_Email);
        mUpdateBtn = findViewById(R.id.vehicleUpdateBtn);
        mProfileImage = findViewById(R.id.vehicle_profile_image);
        mChangeProfileImgBtn = findViewById(R.id.vehicle_ChangeImage);
        mProgDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        final String userId = mUser.getUid();
        mVehicleId = getIntent().getStringExtra("user_id");

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mVehicleFormRef = FirebaseDatabase.getInstance().getReference().child("UserForm");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mVehicleRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        displayVehicleInfo(userId);

    }

    private void displayVehicleInfo(String userID){

        mVehicleFormRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String insName = snapshot.child("insuranceName").getValue().toString();
                String carreg = snapshot.child("vehicleRegNumber").getValue().toString();
                String insurDate = snapshot.child("insuranceExpireDate").getValue().toString();
                String nctDate = snapshot.child("nctValidDate").getValue().toString();
                String rdDate = snapshot.child("roadTaxValidDate").getValue().toString();
                String adde = snapshot.child("vehicleOwnerAddress").getValue().toString();


                mUserCarReg.setEnabled(false);
                mUserInsuranceDate.setEnabled(false);
                mInsurance.setEnabled(false);
                mUserNCT.setEnabled(false);
                mUserRoadTax.setEnabled(false);
                mUserAddress.setEnabled(true);
                mChangeProfileImgBtn.setVisibility(View.VISIBLE);

                mUserCarReg.setText(carreg);
                mUserInsuranceDate.setText(insurDate);
                mInsurance.setText(insName);
                mUserNCT.setText(nctDate);
                mUserRoadTax.setText(rdDate);
                mUserAddress.setText(adde);

                mUserRef.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String img = snapshot.child("image").getValue().toString();

                        if(!mProfileImage.equals("default")){
                            Picasso.get().load(img).placeholder(R.drawable.ic_user_photo).into(mProfileImage);
                        }
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

        // add click listen to open gallery

        mChangeProfileImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"), IMAGE_PICK);
            }
        });


        mUserAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                mUpdateBtn.setEnabled(true);

            }
        });


        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgDialog.setTitle("Updating details");
                mProgDialog.setMessage("Please wait");
                mProgDialog.show();

//                String userAddre = mUserAddress.getText().toString();
//                String surname = mSurname.getText().toString();
//                final String badgeNum = mBadgeNo.getText().toString();
                final String vehicleOwnerAddress = mUserAddress.getText().toString();

                if(vehicleOwnerAddress.isEmpty())
                {
                    mUserAddress.setError("Please enter first name");
                }
//                else if(surname.isEmpty())
//                {
//                    mSurname.setError("Please enter surname");
//                }
//
                else
                {
                    HashMap<String,Object> userMap = new HashMap<>();

//                    userMap.put("surname",surname);
                    userMap.put("vehicleOwnerAddress",vehicleOwnerAddress);

                    mVehicleFormRef.child(userID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                HashMap<String,Object> doctMap = new HashMap<>();

//                                doctMap.put("Email",email);
//                                doctMap.put("Badgenumber",badgeNum);
                                doctMap.put("vehicleOwnerAddress",vehicleOwnerAddress);

                                mVehicleFormRef.child(userID).updateChildren(doctMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()) {
                                            mProgDialog.dismiss();
                                            mUpdateBtn.setEnabled(false);
                                            // ProfileFragment rSum = new ProfileFragment();
                                            //getFragmentManager().beginTransaction().remove(rSum).commit();
                                        }

                                    }
                                });


                            }
                        }
                    });

                }

            }
        });



    }


    //night mode
    private void restartCurrentActivity(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void ClickMenu(View view){
        //open drawer
        openDrawer(drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        //Close drawer
        closeDrawer(drawerLayout);
    }

    private static void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer layout
        //check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            //When drawer is open
            //close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        //Redirect activity to detect text
        redirectActivity(this, MainActivity.class);
    }

    public void ClickUserProfile(View view){
        //Recreate activity
        recreate();
    }

    public void ClickDetectText(View view){
        //Redirect activity to Scanner activity
        redirectActivity(this, TextRecognizer.class);
    }

//    public void ClickCrimeNews(View view){
//        //Redirect activity to crime news
//        redirectActivity(this, CrimeNews.class);
//    }

    public void ClickSettings(View view){
        //Redirect activity to settings activity
        redirectActivity(this, Settings.class);
    }

    public void ClickAbout(View view){
        //Redirect activity to about
        redirectActivity(this, About.class);
    }

    public void ClickHelp(View view){
        //Redirect activity to help
        redirectActivity(this, Help.class);
    }

    public void ClickLogOut(View view){
        //Redirect activity to log out
        logout(this);
    }

    private void logout(final Activity activity){
        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Set title
        builder.setTitle("Logout");
        //Set message
        builder.setMessage("Are you sure you want to logout ?");
        //positive yes button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                //Finish activity
//                activity.finishAffinity();
//                //Exit app
//                System.exit(0);

                mAuth.signOut();
                Intent homeIntent = new Intent(getApplicationContext(),login.class);
                startActivity(homeIntent);
                finish();
            }
        });
        //Negative no button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss dialog
                dialog.dismiss();
            }
        });
        //show dialog
        builder.show();
    }

    private static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity, aClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //start activity
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMAGE_PICK && resultCode == RESULT_OK) {
            Uri imageURL = data.getData();
            // to select the image as square
            CropImage.activity(imageURL).setAspectRatio(1, 1).start( this);
            // Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();

        }

        // check if the requestcode is passed through crop activity

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            //store in result variable
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                // set the progress dialog while uploading the image
                mProgDialog.setTitle("Uploading image ...");
                mProgDialog.setMessage("Please wait");
                mProgDialog.setCanceledOnTouchOutside(false);
                mProgDialog.show();

                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

                final String currentUserId = mAuth.getUid();

                // store the image in firebase storage
                final StorageReference filePath = mStorageRef.child("profile-images").child(currentUserId + ".jpg");


                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {


                            mStorageRef.child("profile-images").child(currentUserId + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override
                                public void onSuccess(final Uri uri) {

                                    final String downloadUrl = uri.toString();


                                    assert currentUserId != null;
                                    mUserRef.child(currentUserId).child("image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mProgDialog.dismiss();

                                            } else {
                                                Toast.makeText(vehicleOwnerProfile.this, "Failed", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });

                                }
                            });

                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                mProgDialog.hide();
                Exception error = result.getError();
            }

        }
    }
}