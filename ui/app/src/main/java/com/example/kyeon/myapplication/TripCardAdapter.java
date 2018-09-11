package com.example.kyeon.myapplication;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TripCardAdapter extends RecyclerView.Adapter<TripCardAdapter.ViewHolder>{
    Context context;
    List<Item> items;
    int item_layout;

    public TripCardAdapter(Context context, List<Item> items, int item_layout)
    {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_trip, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Item item = items.get(position);
        if(item == null)
            return;
        final Travel travel = item.getTravel();
        Drawable drawable = ContextCompat.getDrawable(context, item.getImage());
        holder.image.setBackground(drawable);
        holder.place.setText(item.getPlace());
        holder.day.setText(item.getDay());
        holder.title.setText(item.getTitle());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TravelActivity.class);
                i.putExtra("travelData", travel);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        TextView place;
        TextView day;
        CardView cardview;

        public ViewHolder(View itemView)
        {
            super(itemView);
            image =  itemView.findViewById(R.id.placeImg);
            title = itemView.findViewById(R.id.trip_title);
            place =  itemView.findViewById(R.id.placeText);
            day =  itemView.findViewById(R.id.dayText);
            cardview =  itemView.findViewById(R.id.cardview);
        }
    }
}
