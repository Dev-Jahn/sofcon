package com.example.kyeon.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder>{
    Context context;
    List<String> items;
    int item_layout;

    public DiaryAdapter(Context context, List<String> items, int item_layout)
    {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Toast.makeText(context, "sibal", Toast.LENGTH_SHORT).show();
        Log.d("sibal", "onCreateViewHolder: sibal");
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_review, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryAdapter.ViewHolder holder, int position) {
        final String place_name = items.get(position);
        final LinearLayout linearLayout = holder.linearLayout;
        final ImageView imageView = holder.expand_image;


        holder.place.setText(place_name);
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearLayout.getVisibility() == View.GONE)
                {
                    imageView.setImageResource(R.drawable.baseline_arrow_drop_up_black_36dp);
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    imageView.setImageResource(R.drawable.baseline_arrow_drop_down_black_36dp);
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d("sibal", "getItemCount: " + this.items.size());
        return this.items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView place;
        EditText review;
        RatingBar rating;
        ImageView expand_image;
        ImageButton add_image;
        CardView cardview;
        LinearLayout linearLayout;

        public ViewHolder(View itemView)
        {
            super(itemView);
            place = itemView.findViewById(R.id.diary_place_name);
            rating = itemView.findViewById(R.id.ratingBar);
            review = itemView.findViewById(R.id.reviewtext);
            expand_image = itemView.findViewById(R.id.expand_button);
            add_image = itemView.findViewById(R.id.add_image);
            cardview = itemView.findViewById(R.id.review_cardview);
            linearLayout = itemView.findViewById(R.id.layout_hide);
        }
    }
}
