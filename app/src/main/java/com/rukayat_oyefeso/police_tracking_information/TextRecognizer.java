package com.rukayat_oyefeso.police_tracking_information;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TextRecognizer extends AppCompatActivity {

    //Initialize variable
    private ImageButton snapBtn;
    private Button mDetectBtn,ser;
    private ImageView mimageView;
    private TextView txtView;
    private Bitmap imageBitmap;
    DrawerLayout drawerLayout;
    Switch switchNightMode;
    private ImageButton mSearchBtn2;
    private String userID;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase,mQuery;
    private String tex = "";
    private TextView mUserReg,mUserInsurance,mUserInsDate;
    private CircleImageView mImage;
    private ConstraintLayout constraintLayout, mGroupedList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");





//        Button mt;
//        mt = findViewById(R.id.testButton003);
//        mt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // Log.i("helloworld", "hello worl");
//            }
//        });
//        mSearchBtn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(TextRecognizer.this, "Hello world", Toast.LENGTH_SHORT).show();
//            }
//        });


        mProgressDialog = new ProgressDialog(this);

        setContentView(R.layout.activity_text_recognizer);
        snapBtn = findViewById(R.id.snapBtn);
        mDetectBtn = findViewById(R.id.detectBtn);
        mimageView = findViewById(R.id.imageScan);
        txtView = findViewById(R.id.displayText);
        mSearchBtn2 = findViewById(R.id.search_btn003);

        snapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        mDetectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectTxt();
//                String searchText = txtView.getText().toString();
                if(!tex.isEmpty())
                   firebaseUserScan(tex);
            }
        });

//              when the police clicks the search button
        mSearchBtn2.setOnClickListener(v -> {

          //  Toast.makeText(TextRecognizer.this, "Search started", Toast.LENGTH_SHORT).show();
            String searchText = txtView.getText().toString();
            Log.i("DetecetdTet", searchText);
            firebaseUserScan(searchText);
        });

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

    //Function that invokes an intent to capture a photo
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //Retrieves the image and displays it in an ImageView
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            mimageView.setImageBitmap(imageBitmap);
        }
    }

    private void detectTxt() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                processTxt(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void processTxt(FirebaseVisionText text) {
        List<FirebaseVisionText.Block> blocks = text.getBlocks();
        if (blocks.size() == 0) {
            Toast.makeText(TextRecognizer.this, "No Text Found", Toast.LENGTH_LONG).show();
            return;
        }
        for (FirebaseVisionText.Block block : text.getBlocks()) {
            String txt = block.getText();
            Log.i("User Text", txt);
//            tex = txt;
            txtView.setTextSize(24);
            txtView.setText(txt);

        }
    }

    private void firebaseUserScan(String searchText) {

        mProgressDialog.setTitle("Search Process ");
        mProgressDialog.setMessage("Searching for user, please wait for a moment");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();


        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("UserForm");
        Query query = mDatabaseRef.orderByChild("vehicleRegNumber").equalTo(searchText);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Log.i("User data", data.getKey());
                    userID = data.getKey();
                    constraintLayout = findViewById(R.id.groupedList2);
                    constraintLayout.setVisibility(View.VISIBLE);

                    constraintLayout.setOnClickListener(v -> {
                        Intent clickUser = new Intent(TextRecognizer.this, DisplayUserInfo.class);
                        clickUser.putExtra("userID", userID);
                        startActivity(clickUser);
                        finish();
                    });

                    mUserReg  = findViewById(R.id.user_reg2);
                    mUserInsDate = findViewById(R.id.user_insuranceDate2);
                    mUserInsurance = findViewById(R.id.user_insurance2);
                    mImage = findViewById(R.id.user_imageSearch2);


                    Users models = data.getValue(Users.class);
                    String regNum = models.getVehicleRegNumber();
                    String insExpDate = models.getInsuranceExpireDate();
                    String insurance = models.getInsuranceName();


                    mUserDatabase.child(userID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String UserImage = snapshot.child("image").getValue().toString();
                            Log.i("User data333", UserImage);
                            if (!mImage.equals("ic_user_photo")) {
                                Picasso.get().load(UserImage).placeholder(R.drawable.ic_user_photo).into(mImage);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    mUserInsDate.setText("Insurance Expiry Date: " + insExpDate);
                    mUserInsurance.setText("Insurance Name: " + insurance);
                    mUserReg.setText("Car Registration: " + regNum);
                    mProgressDialog.hide();

//                    Log.i("DataTest", latitude);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
        //Redirect activity to userprofile
        redirectActivity(this, policeProfile.class);
    }

    public void ClickDetectText(View view){
        //Recreate activity
        recreate();
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
}