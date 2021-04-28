package com.rukayat_oyefeso.police_tracking_information;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class viewFine extends AppCompatActivity {

    private ImageView mBackBTN;
    private TextView mBackBTN2;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter mAdaptor;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private TextView makePayment;
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fine);
        mBackBTN = findViewById(R.id.backBTN);
        mBackBTN2 = findViewById(R.id.backBTN2);

        makePayment = findViewById(R.id.payNow);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Fine").child(mAuth.getUid());
        mBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bc = new Intent(viewFine.this, VehicleOwnerMainActivity.class);
                startActivity(bc);
            }
        });

        mBackBTN2.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                Intent bc2 = new Intent(viewFine.this, VehicleOwnerMainActivity.class);
                startActivity(bc2);
            }
        });

        recyclerView = findViewById(R.id.recyclerViewTicketList);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        // And set it to RecyclerView
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<model>().
                setQuery(mRef, model.class).build();


        mAdaptor = new FirebaseRecyclerAdapter<model, ViewFineHolder>(options) {
            @NonNull
            @Override
            public ViewFineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_list, parent,false);
                return  new ViewFineHolder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull ViewFineHolder holder, int position, @NonNull model model) {

                String fineType = model.getFineType();
                holder.setFineType(fineType);

                String ticketId = getRef(position).getKey();
                holder.setTicketId(ticketId);

                String note = model.getFineNote();
                holder.setNote(note);

                String date = model.getDate();
                holder.setDate(date);

                String time = model.getTime();
                holder.setTime(time);

                String fine = model.getvFine();
                holder.setFine(fine);

                Log.i("typeFine", fine);
                if(fine.equals("5pt")){
                   holder.payView.setVisibility(View.INVISIBLE);
                }

                holder.payView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent payNow = new Intent(viewFine.this, payNow.class);
                        payNow.putExtra("amount", fine);
                        payNow.putExtra("id", ticketId);
                        startActivity(payNow);
                    }
                });

            }
        };
        mAdaptor.startListening();
        recyclerView.setAdapter(mAdaptor);

    }



    public class  ViewFineHolder extends RecyclerView.ViewHolder{

        private View mView;
        private   TextView payView;
        private TextView mFinePrice;
        public ViewFineHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            payView = itemView.findViewById(R.id.payNow);
//            if(mFinePrice.getText().toString().equals("5pt")){
//                payView.setVisibility(View.INVISIBLE);
//            }



        }


        public void setFineType(String fineType){
            TextView mfineType = mView.findViewById(R.id.fineType);
            mfineType.setText(fineType);

        }
        public void setTicketId(String fineType){
            TextView mTicketId = mView.findViewById(R.id.fineTicketID);
            mTicketId.setText(fineType);

        }
        public void setNote(String fineType){
            TextView mNote = mView.findViewById(R.id.fineNote);
            mNote.setText(fineType);

        }
        public void setDate(String fineType){
            TextView mDate = mView.findViewById(R.id.fineDate);
            mDate.setText(fineType);

        }
        public void setTime(String fineType){
            TextView mTime = mView.findViewById(R.id.fineTime);
            mTime.setText(fineType);

        }
        public void setFine(String fineType){
             mFinePrice = mView.findViewById(R.id.finePrice);
            mFinePrice.setText(fineType);

        }
    }
}