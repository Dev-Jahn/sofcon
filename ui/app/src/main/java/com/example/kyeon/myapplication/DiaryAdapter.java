package com.example.kyeon.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> implements View.OnClickListener{
    Fragment fragment;
    List<String> items;
    int item_layout;
    int dayPosition;
    Travel travel;
    final int MY_PERMISSIONS_REQUEST_GALLERY = 108;
    final int GALLERY_CODE = 1;

    public DiaryAdapter(Fragment fragment, List<String> items, int item_layout, Travel travel, int dayPosition)
    {
        this.fragment = fragment;
        this.items = items;
        this.item_layout = item_layout;
        this.travel = travel;
        this.dayPosition = dayPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_review, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DiaryAdapter.ViewHolder holder, final int position) {
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
        final Travel.DailyDiary.PlaceReview curReview = travel.dailyDiary[dayPosition].review.get(position);

        if(curReview.reviewed == true)
        {
            holder.review.setText(curReview.reviewText);
            holder.rating.setRating(curReview.score);
            holder.add_image.setImageBitmap(curReview.image.bitmap);
            holder.comp_button.setText("수정");
        }
        else
        {
            holder.add_image.setOnClickListener(this);

            holder.comp_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    travel.dailyDiary[dayPosition].setPlaceReview(position, holder.rating.getRating(), holder.review.getText().toString(),
                            new SerialBitmap(((BitmapDrawable)holder.add_image.getDrawable()).getBitmap()));
                    int flag;
                    if(holder.rating.getRating() >=2.5)
                        flag = 0;
                    else
                        flag = 1;

                    String UID = getUID();
                    Log.d("diary", "onClick: " + UID);
                    Score score = new Score(flag, UID, curReview.place_id, curReview.place_class);
                    Log.d("diary", "dataset -> \n flag = " + flag +"\n placeId = " + curReview.place_id + " \n placeClass = " + curReview.place_class);
                    score.execute();
                    String test = "";
                    try {
                        test = score.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    Log.d("diaryTest", test);

                    try
                    {
                        travel.save(fragment.getContext());
                        holder.comp_button.setText("수정");
                    }catch (IOException e)
                    {
                        Toast.makeText(fragment.getContext(), "sibal", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

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
        Button comp_button;

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
            comp_button = itemView.findViewById(R.id.rating_comp_button);
        }
    }

    @Override
    public void onClick(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_GALLERY);
            //if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION))
        }
        else
        {
            if(permissionCheck == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                fragment.startActivityForResult(intent,GALLERY_CODE);
            }
            else
                Toast.makeText(fragment.getActivity(), "저장소 권한이 없어 이미지를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    public String getUID() {
        String saved_info[] = {""};
        String dirPath = fragment.getContext().getFilesDir().getAbsolutePath();
        File file = new File(dirPath);
        String content = "", temp = "";

        if (file.listFiles().length > 0) {
            for (File f : file.listFiles()) {
                String f_name = f.getName();
                String loadPath = dirPath + File.separator + f_name;
                if (f_name.equals("account_setup.txt")) {
                    try {
                        FileInputStream fis = new FileInputStream(loadPath);
                        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));

                        while ((temp = bufferReader.readLine()) != null) content += temp;
                        break;
                    } catch (Exception e) {
                    }
                }
            }
            saved_info = content.split(" ");

        }
        return saved_info[0];
    }
}
