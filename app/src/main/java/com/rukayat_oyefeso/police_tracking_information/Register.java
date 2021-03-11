package com.rukayat_oyefeso.police_tracking_information;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText name, user, email, pass, dateOfBirth;
    private Spinner spinner;
    private Button nextBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef,mUserRef;

    private ProgressDialog mProgDialog;

    private String mFirstName,mSurname,mDob,mAccountType;
    private Calendar calendar = Calendar.getInstance();
    private int year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.regEditText);
        user = findViewById(R.id.regEditText2);
        email = findViewById(R.id.regEditText3);
        pass = findViewById(R.id.regEditText4);
        dateOfBirth = findViewById(R.id.regEditText5);
        spinner = findViewById(R.id.accTypeSpinner);

        //get the year, month, day calender
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day =  calendar.get(Calendar.DAY_OF_MONTH);

        nextBtn = findViewById(R.id.regNextBtn);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgDialog = new ProgressDialog(this);

        //move to different activity when you select either Police or user
//        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.userTypes, R.layout.support_simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show the Calender when Date of Birth clicked
                showCalenderDialog();
            }
        });

    }

    private void registerUser() {
        mProgDialog.setTitle("Registration Process ");
        mProgDialog.setMessage("Please wait for a moment");
        mProgDialog.setCanceledOnTouchOutside(false);
        mProgDialog.show();

        mFirstName = name.getText().toString().trim();
        mSurname = user.getText().toString().trim();
        final String UserEmail = email.getText().toString().trim();
        final String password = pass.getText().toString().trim();
        mDob = dateOfBirth.getText().toString().trim();
        mAccountType = spinner.getSelectedItem().toString();

//        mAccountType = spinner.getSelectedItem().toString();


        if (mFirstName.isEmpty()) {
            mProgDialog.hide();
            name.setError("Full name is required");
            name.requestFocus();
        } else if (mSurname.isEmpty()) {
            mProgDialog.hide();
            user.setError("Please give a surname");
            user.requestFocus();
        } else if (UserEmail.isEmpty()) {
            mProgDialog.hide();
            email.setError("Email is required");
            email.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches()) {
            mProgDialog.hide();
            email.setError("Please provide a valid email");
            email.requestFocus();
        } else if (password.isEmpty()) {
            mProgDialog.hide();
            pass.setError("Password is required");
            pass.requestFocus();
        } else if (mDob.isEmpty()) {
            mProgDialog.hide();
            dateOfBirth.setError("Date of birth is required");
            dateOfBirth.requestFocus();
        } else if (password.length() < 6) {
            mProgDialog.hide();
            pass.setError("Min password length should be 6 characters");
            pass.requestFocus();
        } else if (mAccountType.equals("Select")) {
            mProgDialog.hide();
            Toast.makeText(this, "Please select account type", Toast.LENGTH_LONG).show();
        }

        //if it's not empty insert data into database
        else {
            mAuth.fetchSignInMethodsForEmail(UserEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    boolean check = !task.getResult().getSignInMethods().isEmpty();

                    if (!check) {
                        mAuth.createUserWithEmailAndPassword(UserEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                //get user ID and store as string
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                String userId = currentUser.getUid();

                                //use HashMaps for string key value pairs
                                HashMap<String, String> userHashMap = new HashMap<>();
                                userHashMap.put("firstName", mFirstName);
                                userHashMap.put("surname", mSurname);
                                userHashMap.put("dateOfBirth", mDob);
                                userHashMap.put("accountType", mAccountType);
                                userHashMap.put("fillForm", "false");
                                userHashMap.put("image", "default-image");

                                if (mAccountType.equals("Vehicle Owner")) {

                                    mUserRef = mRef.child(userId);
                                    mUserRef.setValue(userHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {


                                                final FirebaseUser user = mAuth.getCurrentUser();
                                                assert user != null;
                                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            mProgDialog.dismiss();
                                                            Toast.makeText(Register.this,
                                                                    "Verification email sent to " + user.getEmail(),
                                                                    Toast.LENGTH_SHORT).show();
                                                            //Toast.makeText(Registratrion.this, "Your registration was successful", Toast.LENGTH_LONG).show();

                                                            Intent intent = new Intent(Register.this, login.class);
                                                            startActivity(intent);
                                                        } else {
                                                            mProgDialog.dismiss();
                                                            // Log.e(TAG, "sendEmailVerification", task.getException());
                                                            Toast.makeText(Register.this,
                                                                    "Failed to send verification email.",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else if (mAccountType.equals("Police")) {
                                    mUserRef = mRef.child(userId);
                                    mUserRef.setValue(userHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                final FirebaseUser user = mAuth.getCurrentUser();
                                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            mProgDialog.dismiss();
                                                            Toast.makeText(Register.this,
                                                                    "Verification email sent to " + user.getEmail(),
                                                                    Toast.LENGTH_SHORT).show();
                                                            //Toast.makeText(Registratrion.this, "Your registration was successful", Toast.LENGTH_LONG).show();
                                                            Intent formIntent = new Intent(Register.this, login.class);
                                                            startActivity(formIntent);
                                                        } else {
                                                            mProgDialog.dismiss();
                                                            // Log.e(TAG, "sendEmailVerification", task.getException());
                                                            Toast.makeText(Register.this,
                                                                    "Failed to send verification email.",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            }

                                        }
                                    });
                                }
                            }
                        });
                                } else {
                                    mProgDialog.hide();
                                    Toast.makeText(Register.this, R.string.toast, Toast.LENGTH_SHORT).show();
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
                    dateOfBirth.setText(dayOfMonth + "/" + month + "/" + year);
                }
            };
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener, year, month, day);
            datePickerDialog.show();
        }

    public void onClickTo(View view)
    {
        Intent intent = new Intent(Register.this, login.class);
        startActivity(intent);

    }
}