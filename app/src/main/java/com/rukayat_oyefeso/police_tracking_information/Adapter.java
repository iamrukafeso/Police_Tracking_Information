package com.rukayat_oyefeso.police_tracking_information;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rukayat_oyefeso.police_tracking_information.parameter.Articles;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        Context context;
        List<Articles> articles;
    public Adapter(Context context, List<Articles> articles) {
            this.context = context;
            this.articles = articles;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Articles art = articles.get(position);
            String url = art.getUrl();
            holder.tvTitle.setText(art.getTitle());
            //get source get name
            holder.tvSource.setText(art.getSource().getName());
            holder.tvDate.setText(art.getPublishedAt());

            String imageUrl=art.getUrlToImage();
            //picasso ka syntax new 33.07 part1
            Picasso.get().load(imageUrl).into(holder.imageView);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,DetailedActivity.class);
                    intent.putExtra("title",art.getTitle());
                    intent.putExtra("source",art.getSource().getName());
                    intent.putExtra("time",art.getPublishedAt());
                    intent.putExtra("imageUrl",art.getUrlToImage());
                    intent.putExtra("url",art.getUrl());
                    intent.putExtra("decs",art.getDescription());
                    context.startActivity(intent);
                }
            });
        }


        @Override
        public int getItemCount() {
            return articles.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle,tvSource,tvDate;
            ImageView imageView;
            CardView cardView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle=itemView.findViewById(R.id.tvId);
                tvDate=itemView.findViewById(R.id.tvDate);
                tvSource=itemView.findViewById(R.id.tvSource);
                imageView=itemView.findViewById(R.id.image);
                cardView=itemView.findViewById(R.id.cardView);
            }
        }
        public String getCountry()
        {
            Locale locale=Locale.getDefault();
            String country=locale.getCountry();
            return country.toLowerCase();
        }
    }
