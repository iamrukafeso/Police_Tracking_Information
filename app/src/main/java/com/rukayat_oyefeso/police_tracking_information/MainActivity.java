package com.rukayat_oyefeso.police_tracking_information;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //Initialize variable
    DrawerLayout drawerLayout;
    Switch switchNightMode;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    RelativeLayout mSearchUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //welcome
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
//        userID = user.getUid();

        final TextView greetingTextView = findViewById(R.id.displayName);

        Query query = reference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){

                    //get data
                    String name = ""+ ds.child("name").getValue();
                    //set data
                    greetingTextView.setText("Welcome, " + name + "!" );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User
//                Register policeUsersProfile = snapshot.getValue(Register.class);
//
//                if (policeUsersProfile != null){
//                    String name = policeUsersProfile.mFirstName;
//
//                    greetingTextView.setText("Welcome, " + name + "!" );
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MainActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
//            }
//        });

        //Relative Layouts in main Activity
        mSearchUsers = findViewById(R.id.searchUser);

        mSearchUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(MainActivity.this,searchData.class);
                startActivity(searchIntent);
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
        //Recreate activity
        recreate();
    }

    public void ClickUserProfile(View view){
        //Redirect activity to userprofile
        redirectActivity(this, policeProfile.class);
    }

    public void ClickDetectText(View view){
        //Redirect activity to detect text
        redirectActivity(this, TextRecognizer.class);
    }
    //ClickMaps

    public void ClickCrimeNews(View view){
        //Redirect activity to crime news
        redirectActivity(this, CrimeNews.class);
    }

    public void ClickMaps(View view){
        //Redirect activity to crime news
        redirectActivity(this, MapActivity.class);
    }

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

    private static void logout(final Activity activity){
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
                //Finish activity
                activity.finishAffinity();
                //Exit app
                System.exit(0);
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
