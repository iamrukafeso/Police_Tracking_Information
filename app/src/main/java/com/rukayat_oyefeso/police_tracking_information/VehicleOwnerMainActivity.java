package com.rukayat_oyefeso.police_tracking_information;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class VehicleOwnerMainActivity extends AppCompatActivity {

    //Initialize variable
    DrawerLayout drawerLayout;
    Switch switchNightMode;

    private TextView mFirstName, mLastName;
    private CircleImageView mVehicleImage;

//    private FirebaseUser user;
//    private DatabaseReference reference;
//    private String userID;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference reference;
    private String userID;
    private DatabaseReference mUserRef, mVehicleFormRef, mVehicleRef;

    RelativeLayout mPayFine, mPunishments, mCrimeMain, mProfile;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_vehicle_owner_main);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mFirstName = findViewById(R.id.displayVehicleName);
        mLastName = findViewById(R.id.displayVehicleLastName);
        mVehicleImage = findViewById(R.id.vehicle_MainProfile);

        //Relative Layouts in main Activity
        mPayFine = findViewById(R.id.payFines);
        mPayFine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fineIntent = new Intent(VehicleOwnerMainActivity.this,viewFine.class);
                startActivity(fineIntent);
            }
        });

        mPunishments = findViewById(R.id.punishment);
        mPunishments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent punIntent = new Intent(VehicleOwnerMainActivity.this,Punishments.class);
                startActivity(punIntent);
            }
        });

        mCrimeMain = findViewById(R.id.crimeMain);
        mCrimeMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent crimeIntent = new Intent(VehicleOwnerMainActivity.this,CrimeNews.class);
                startActivity(crimeIntent);
            }
        });

        mProfile = findViewById(R.id.profile);
        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vehicleIntent = new Intent(VehicleOwnerMainActivity.this,vehicleOwnerProfile.class);
                startActivity(vehicleIntent);
            }
        });

        //Assign variable for night or dark mode
        drawerLayout = findViewById(R.id.drawer_layout);
        switchNightMode = findViewById(R.id.switchNightMode);

        int currentMode = AppCompatDelegate.getDefaultNightMode();

        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES){
            switchNightMode.setChecked(true);
        } else {
            switchNightMode.setChecked(false);
        }

        switchNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    restartCurrentActivity();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restartCurrentActivity();
                }
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        final String userId = mUser.getUid();
        displayInfo(userId);

    }

    private void displayInfo(String userID) {

        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = snapshot.child("firstName").getValue().toString();
                String lName = snapshot.child("surname").getValue().toString();
                String img = snapshot.child("image").getValue().toString();

                if (!mVehicleImage.equals("default")) {
                    Picasso.get().load(img).placeholder(R.drawable.ic_user_photo).into(mVehicleImage);
                }
                mFirstName.setText(name + " ");
                mLastName.setText(lName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    public void ClickVehicleHome(View view){
        //Recreate activity
        recreate();
    }

    public void ClickVehicleUserProfile(View view){
        //Redirect activity to userprofile
        redirectActivity(this, vehicleOwnerProfile.class);
    }

//    public void ClickDetectText(View view){
//        //Redirect activity to detect text
//        redirectActivity(this, TextRecognizer.class);
//    }
    //ClickMaps

    public void ClickCrimeNews(View view){
        //Redirect activity to crime news
        redirectActivity(this, CrimeNews.class);
    }

//    public void ClickMaps(View view){
//        //Redirect activity to crime news
//        redirectActivity(this, MapActivity.class);
//    }

    public void ClickVehicleSettings(View view){
        //Redirect activity to settings activity
        redirectActivity(this, Settings.class);
    }

    public void ClickVehicleAbout(View view){
        //Redirect activity to about
        redirectActivity(this, About.class);
    }

    public void ClickVehicleHelp(View view){
        //Redirect activity to help
        redirectActivity(this, Help.class);
    }

    public void ClickVehicleLogOut(View view){
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
}
