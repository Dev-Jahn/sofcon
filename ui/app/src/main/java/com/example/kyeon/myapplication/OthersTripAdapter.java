package com.example.kyeon.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class OthersTripAdapter extends RecyclerView.Adapter<OthersTripAdapter.ViewHolder>{
    Context context;
    List<TripOthersItem> items;
    int item_layout;

    public OthersTripAdapter(Context context, List<TripOthersItem> items, int item_layout)
    {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_trip_others, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TripOthersItem item = items.get(position);
        if(item == null)
            return;
        Drawable drawable = ContextCompat.getDrawable(context, item.getImage());
        Drawable userDrawable = ContextCompat.getDrawable(context, item.getUserImage());
        holder.userImage.setImageDrawable(userDrawable);
        holder.userId.setText(item.getUserId());
        holder.image.setImageDrawable(drawable);
        holder.views.setText(""+item.getViews());
        holder.day.setText(item.getDay());
        holder.title.setText(item.getTitle());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TravelActivity.class);
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
        ImageView userImage;
        TextView userId;
        TextView views;
        TextView title;
        TextView day;
        CardView cardview;

        public ViewHolder(View itemView)
        {
            super(itemView);
            image =  itemView.findViewById(R.id.placeImg_others);
            userImage = itemView.findViewById(R.id.user_profile_image_others);
            userId = itemView.findViewById(R.id.user_name_others);
            title = itemView.findViewById(R.id.trip_title_others);
            day =  itemView.findViewById(R.id.trip_date_others);
            views = itemView.findViewById(R.id.trip_views_others);
            cardview =  itemView.findViewById(R.id.cardview_others);
        }
    }
}
