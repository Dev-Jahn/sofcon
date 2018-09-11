package com.example.kyeon.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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

public class RecommendItemAdapter extends RecyclerView.Adapter<RecommendItemAdapter.ViewHolder> {
    Context context;
    List<Rec_Item> items;
    int item_layout;
    private Intent intent;

    public RecommendItemAdapter(Context context, List<Rec_Item> items, int item_layout, Intent intent) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
        this.intent = intent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recommend, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecommendItemAdapter.ViewHolder holder, int position) {
        final Rec_Item rec_item = items.get(position);
        Drawable drawable = ContextCompat.getDrawable(context, rec_item.getImage());
        holder.image.setImageDrawable(drawable);
        holder.image.setColorFilter(rec_item.getColor());
        holder.title.setText(rec_item.getTitle());
        holder.content.setText(rec_item.getContent());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.title.getText().equals(context.getResources().getString(R.string.customized_tour_restaurant))) {
                    Intent i = new Intent(context, ChoosePlacesActivity.class);
                    IntentData intentData = new IntentData(intent);
                    intentData.transferDataToIntent(i);
                    Log.d("DEBUG-TEST!!!", i.getStringExtra(MapUtility.CURRENT_DAY_TAG));

                    i.putExtra("travelData", (intent.getSerializableExtra("travelData")));
                    context.startActivity(i);
                }
                Toast.makeText(context, rec_item.getTitle(), Toast.LENGTH_SHORT).show();
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
        TextView content;
        CardView cardview;

        public ViewHolder(View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.recommend_image);
            title = itemView.findViewById(R.id.recommend_title);
            content = itemView.findViewById(R.id.recommend_content);
            cardview = itemView.findViewById(R.id.recommend_cardview);
        }
    }
}
