package com.example.kyeon.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TravelActivity extends AppCompatActivity {

    int id;
    String place;
    int people_count;
    int days;
    int smm, syy, sdd;
    int emm, eyy, edd;

    Toolbar toolbar;
    ViewPager viewPager;
    Travel travel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        // tmp data sets
        travel = (Travel) getIntent().getSerializableExtra("travelData");
        days = travel.days;


        viewPager = findViewById(R.id.trip_pager);

        TripPagerAdapter adapter = new TripPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public static class DiaryholderFragment extends android.support.v4.app.Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        DiaryAdapter.ViewHolder vh;
        RecyclerView recyclerView;
        DiaryAdapter diaryAdapter;
        final int GALLERY_CODE = 1;

        public DiaryholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DiaryholderFragment newInstance(int sectionNumber) {
            DiaryholderFragment fragment = new DiaryholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_travel, container, false);
            recyclerView = rootView.findViewById(R.id.diary_recview);

            final int index = getArguments().getInt(ARG_SECTION_NUMBER);
            TravelActivity travelActivity = (TravelActivity) getActivity();
            final Travel travel = travelActivity.travel;

            ImageView snapshot = rootView.findViewById(R.id.snapshot_image);

            String filePath = getContext().getFilesDir().getPath().toString() + "/"
                    + travel.title + "1.png";
            Log.d("DEBUG-TEST", filePath + " in bindview");
            File file = new File(filePath);
            if(file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                Drawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
                snapshot.setBackground(drawable);
            }


            List<String> place_names = new ArrayList<>();

            for (int i = 0; i < travel.dailyDiary[index-1].review.size(); i++) {
                place_names.add(travel.dailyDiary[index-1].review.get(i).place_name);
            }


            diaryAdapter = new DiaryAdapter(this, place_names, R.layout.fragment_travel, travel, index-1);
            recyclerView.setAdapter(diaryAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case 1: {
                        DiaryAdapter.ViewHolder vh;
                        vh = (DiaryAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
                        sendPicture(vh.add_image, data.getData());
                        break;
                    }
                }
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            /*
            final Travel travel = ((TravelActivity)getActivity()).travel;
            final int index = getArguments().getInt(ARG_SECTION_NUMBER);
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {

                    for (int i = 0; i < travel.dailyDiary[index-1].review.size(); i++) {
                        final int idx = i;

                        vh = (DiaryAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                        Log.d("diaryTest", index+"and"+i);

                        if(travel.dailyDiary[index-1].review.get(i).reviewed == true)
                        {
                            Travel.DailyDiary.PlaceReview rev;
                            rev = travel.dailyDiary[index-1].review.get(i);
                            Log.d("diaryload", "place = " +rev.place_name);
                            Log.d("diaryload", "review = " +rev.reviewText);
                            Log.d("diaryload", "score = " + rev.score+"");
                            vh.review.setText(rev.reviewText);
                            vh.add_image.setImageBitmap(rev.image.bitmap);
                            vh.rating.setNumStars(rev.score);
                            vh.review.setEnabled(false);
                            vh.comp_button.setText("수정");
                        }
                        else
                        {
                            vh.comp_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d("diaryTest", "onClick: imhere" + index+" and "+ idx);
                                    travel.dailyDiary[index-1].setPlaceReview(idx,vh.rating.getNumStars(), vh.review.getText().toString(),
                                            new SerialBitmap(((BitmapDrawable)vh.add_image.getDrawable()).getBitmap()));
                                    Log.d("diarysave", "place = "+ vh.place.toString());
                                    Log.d("diarysave", "review = "+vh.review.getText().toString());
                                    Log.d("diarysave", "score = "+vh.rating.getNumStars()+"");
                                    try
                                    {
                                        travel.save(getContext());
                                    }catch (IOException e)
                                    {
                                        Toast.makeText(getContext(), "sibal", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                }
            },100);
            */
        }

        private void sendPicture(ImageButton imageButton, Uri imguri) {
            Bitmap img;
            String imagePath = getRealPathFromURI(imguri);
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = exifOrientationToDegrees(exifOrientation);
            BitmapFactory.Options options;
            try {
                img = BitmapFactory.decodeFile(imagePath);
            } catch (OutOfMemoryError e) {
                options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                img = BitmapFactory.decodeFile(imagePath, options);
            }
            imageButton.setImageBitmap(img);
        }

        private int exifOrientationToDegrees(int exifOrientation) {
            if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
                return 90;
            } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
                return 180;
            } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
                return 270;
            }
            return 0;
        }

        private String getRealPathFromURI(Uri contentUri) {
            int column_index = 0;
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            }
            return cursor.getString(column_index);
        }

    }




    public void shareOnInternet(View v) {
        // tmp dataset

    }


    class TripPagerAdapter extends FragmentPagerAdapter {

        public TripPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return DiaryholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return days;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return "day" + (position + 1);
        }

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
