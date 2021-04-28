package com.rukayat_oyefeso.police_tracking_information;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrackActivity extends AppCompatActivity {

    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView mResultList;
    private String userID;
    private DatabaseReference mUserDatabase,mQuery;
    private ProgressDialog mProgressDialog;

    private TextView mUserReg,mUserInsurance,mUserInsDate;
    private CircleImageView mImage;
    private  ConstraintLayout constraintLayout, mGroupedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_data);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mUserReg  = findViewById(R.id.user_reg);
        mUserInsDate = findViewById(R.id.user_insuranceDate);
        mUserInsurance = findViewById(R.id.user_insurance);

        constraintLayout = findViewById(R.id.groupedList);

        mImage = findViewById(R.id.user_imageSearch);

        mProgressDialog = new ProgressDialog(this);

        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);
        //mResultList = (RecyclerView) findViewById(R.id.result_list);

        //mResultList.setHasFixedSize(true);
        //mResultList.setLayoutManager(new LinearLayoutManager(this));

//      when the police clicks the search button
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(searchData.this, "Search started", Toast.LENGTH_SHORT).show();
                String searchText = mSearchField.getText().toString();
                firebaseUserSearch(searchText);
            }
        });

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clickUser = new Intent(TrackActivity.this, MapsActivity.class);
                clickUser.putExtra("userID", userID);
                startActivity(clickUser);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

//        mQuery = mUserDatabase.child("UserForm");
//
//        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Users>()
//                .setQuery(mQuery,Users.class).build();
//
//        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users,UsersViewHolder>(options) {
//            @NonNull
//            @Override
//            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.list_layout, parent, false);
//                return new UsersViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {
//
//                final String userId = getRef(position).getKey();
//
//                Log.i("User Id",  userId);
//
//                holder.setRegNumber(model.getVehicleRegNumber());
//                holder.setInsuranceName(model.getInsuranceName());
//                holder.setInsuranceExpireDate(model.getInsuranceExpireDate());
//
//            }
//        };
//        adapter.startListening();
//        mResultList.setAdapter(adapter);

    }


    private void firebaseUserSearch(String searchText) {

        mProgressDialog.setTitle("Search Process ");
        mProgressDialog.setMessage("Searching for user, please wait for a moment");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();


        DatabaseReference mDatabaseRef =FirebaseDatabase.getInstance().getReference("UserForm");
        Query query=mDatabaseRef.orderByChild("vehicleRegNumber").equalTo(searchText);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data:dataSnapshot.getChildren()){
                    Log.i("User data", data.getKey());
                    userID = data.getKey();
                    constraintLayout.setVisibility(View.VISIBLE);
                    Users models=data.getValue(Users.class);
                    String regNum=models.getVehicleRegNumber();
                    String insExpDate = models.getInsuranceExpireDate();
                    String insurance = models.getInsuranceName();
//                    String UserImage = dataSnapshot.child("image").getValue().toString();


                    mUserDatabase.child(userID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String  UserImage = snapshot.child("image").getValue().toString();
                            Log.i("User data333", UserImage);
                            if(!mImage.equals("ic_user_photo")){
                                Picasso.get().load(UserImage).placeholder(R.drawable.ic_user_photo).into(mImage);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    mUserInsDate.setText("Insurance Expiry Date: "+ insExpDate);
                    mUserInsurance.setText("Insurance Name: "+insurance);
                    mUserReg.setText("Car Registration: "+regNum);


                    mProgressDialog.hide();

//                    Log.i("DataTest", latitude);


                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // }

//        Toast.makeText(searchData.this, "Started Search", Toast.LENGTH_LONG).show();
//
//        Query firebaseSearchQuery = mUserDatabase.orderByChild("UserForm").startAt(searchText).endAt(searchText + "\uf8ff");
//
//        FirebaseRecyclerOptions options =  new FirebaseRecyclerOptions.Builder<Users>()
//                .setQuery(
//                        firebaseSearchQuery,
//                        Users.class
//                ).build();
//
//        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users,UsersViewHolder>(options) {
//            @NonNull
//            @Override
//            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.list_layout, parent, false);
//                return new UsersViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {
//
//                final String userId = getRef(position).getKey();
//
//                Log.i("User Id",  userId);
//
//                holder.setRegNumber(model.getVehicleRegNumber());
//                holder.setInsuranceName(model.getInsuranceName());
//                holder.setInsuranceExpireDate(model.getInsuranceExpireDate());
//
//            }
//        };
//        adapter.startListening();
//        mResultList.setAdapter(adapter);
//        FirebaseRecyclerAdapter<Users,UsersViewHolder> adapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {
//
//              holder.setDetails(getApplicationContext(), model.getFirstName(), model.getImage(), model.getVehicleRegNumber(), model.getInsuranceName());
//                Toast.makeText(searchData.this, "Started Search 2", Toast.LENGTH_LONG).show();
//                Log.i("Test",model.getFirstName());
//
//            }
//
//            @NonNull
//            @Override
//            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.list_layout, parent, false);
//                return new UsersViewHolder(view);
//            }
//        };
//
////        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
////                Users.class,
////                R.layout.list_layout,
////                UsersViewHolder.class,
////                mUserDatabase
////
////        )
////       {
////            @NonNull
////            @Override
////            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////                return null;
////            }
////
////            @Override
////            protected void onBindViewHolder(UsersViewHolder holder, int position, Users model) {
////
////                holder.setDetails(getApplicationContext(), model.getFirstName(), model.getImage(), model.getVehicleRegNumber(), model.getInsuranceName());
////            }
////        };
//        adapter.startListening();
//        mResultList.setAdapter(adapter);
    }


    //View Holder Class
    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public UsersViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        @SuppressLint("SetTextI18n")
        public void setRegNumber(String regNumber){
            TextView user_reg = mView.findViewById(R.id.user_reg);
            user_reg.setText("Car Registration:  " + regNumber);
        }

        @SuppressLint("SetTextI18n")
        public void setInsuranceName(String insuranceName){
            TextView user_insurance = mView.findViewById(R.id.user_insurance);
            user_insurance.setText("Insurance:  " + insuranceName);
        }

        @SuppressLint("SetTextI18n")
        public void setInsuranceExpireDate(String insuranceExpireDate){
            TextView user_insuranceExpireDate = mView.findViewById(R.id.user_insuranceDate);
            user_insuranceExpireDate.setText("Insurance Expire Date:  " + insuranceExpireDate);
        }

        public void setDetails(Context ctx, String firstName, String image, String vehicleRegNumber, String insuranceName){

            TextView user_name = mView.findViewById(R.id.user_insuranceDate);
            CircleImageView user_image = mView.findViewById(R.id.user_image);
            TextView user_reg = mView.findViewById(R.id.user_reg);
            TextView user_insurance = mView.findViewById(R.id.user_insurance);

            user_name.setText(firstName);
            user_reg.setText(vehicleRegNumber);
            user_insurance.setText(insuranceName);
            Glide.with(ctx).load(image).into(user_image);

        }

    }
}