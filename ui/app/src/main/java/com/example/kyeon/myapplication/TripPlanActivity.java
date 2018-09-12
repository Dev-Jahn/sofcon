package com.example.kyeon.myapplication;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TripPlanActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    public SectionsPagerAdapter mSectionsPagerAdapter;
    String d_yy, d_mm, d_dd;
    String a_yy, a_mm, a_dd;
    String etitle, person_count, ePlace;
    private String eFirstPlace;
    private String eCurrentDay;
    private String ePlaceLat;
    private String ePlaceLng;
    private String ePlaceType;
    private String ePlaceBitmapFilePath;

    private static String[] arrayPlaceLat;
    private static String[] arrayPlaceLng;
    private static String[] arrayPlaceType;
    private static String[] arrayPlaceBitmapFilePath;

    long diff_days;

    protected static long totalTravelDays;

    int day_count;

    Travel travel;//to save travel datas to local
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("여행 계획");

        get_datas_for_travel();
        calcTotalTravelDays();

        arrayPlaceLat = new String[(int) totalTravelDays + 1];
        arrayPlaceLng = new String[(int) totalTravelDays + 1];
        arrayPlaceType = new String[(int) totalTravelDays + 1];
        arrayPlaceBitmapFilePath = new String[(int) totalTravelDays + 1];

        arrayPlaceLat[1] = ePlaceLat;
        arrayPlaceLng[1] = ePlaceLng;
        arrayPlaceType[1] = ePlaceType;
        arrayPlaceBitmapFilePath[1] = ePlaceBitmapFilePath;

        //ImageButton comp = new ImageButton(getApplicationContext());
        //comp.setImageDrawable(getDrawable(R.drawable.outline_done_black_24dp));

        /*
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        */

        /*
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));

        */

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_plan, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_complete) {
            //s a v e to local


            try {
                travel.save(getApplicationContext());
                Log.d("travelTest", "hisnamesis: " + travel.dailyDiary[0].review.size());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "sibal", Toast.LENGTH_SHORT).show();
            }


            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public long calcTotalTravelDays() {
        get_datas();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date beginDate;
        Date endDate;
        try {
            beginDate = formatter.parse(d_yy + d_mm + d_dd);
            endDate = formatter.parse(a_yy + a_mm + a_dd);
            long diff = endDate.getTime() - beginDate.getTime();
            totalTravelDays = diff / (24 * 60 * 60 * 1000);
            ++totalTravelDays;
        } catch (ParseException e) {
            e.printStackTrace();
            totalTravelDays = -1;
        }
        return totalTravelDays;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends android.support.v4.app.Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_LAST = "last_number";
        private static final String ARG_SECTION_TITLE = "title_text";
        private static final String ARG_SECTION_CURRENT_DAY = MapUtility.CURRENT_DAY_TAG;
        private static final String ARG_SECTION_CURRENT_DAY_TEMP = "currentDay";
        private static final String ARG_SECTION_FIRST_PLACE = MapUtility.PLACE_NAME_TAG;
        private static final String ARG_SECTION_PLACE_LAT = MapUtility.PLACE_LAT_TAG;
        private static final String ARG_SECTION_PLACE_LNG = MapUtility.PLACE_LNG_TAG;
        private static final String ARG_SECTION_PLACE_TYPE = MapUtility.PLACE_TYPE_TAG;
        private static final String ARG_SECTION_PLACE_BITMAP = MapUtility.PLACE_BITMAP_FILE_PATH_TAG;
        private ImageView ivTravelMap;

        public PlaceholderFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String title, String currentDay, String firstPlace,
                                                      String placeLat, String placeLng, String placeType, String placeBitmapFilePath) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_SECTION_TITLE, title);
            args.putString(ARG_SECTION_CURRENT_DAY, currentDay);
            args.putString(ARG_SECTION_FIRST_PLACE, firstPlace);
            args.putString(ARG_SECTION_PLACE_LAT, placeLat);
            args.putString(ARG_SECTION_PLACE_LNG, placeLng);
            args.putString(ARG_SECTION_PLACE_TYPE, placeType);
            args.putString(ARG_SECTION_PLACE_BITMAP, placeBitmapFilePath);
            fragment.setArguments(args);
            return fragment;
        }

        public static PlaceholderFragment newInstance(int sectionNumber, int count, String title, String currentDay, String firstPlace,
                                                      String placeLat, String placeLng, String placeType, String placeBitmapFilePath) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(ARG_SECTION_LAST, count);
            args.putString(ARG_SECTION_TITLE, title);
            args.putString(ARG_SECTION_CURRENT_DAY, currentDay);
            args.putString(ARG_SECTION_FIRST_PLACE, firstPlace);
            args.putString(ARG_SECTION_PLACE_LAT, placeLat);
            args.putString(ARG_SECTION_PLACE_LNG, placeLng);
            args.putString(ARG_SECTION_PLACE_TYPE, placeType);
            args.putString(ARG_SECTION_PLACE_BITMAP, placeBitmapFilePath);
            fragment.setArguments(args);

            return fragment;
        }

        private Intent getDataIntentForReloadMap() {
            Intent intent = new Intent(getActivity(), ChoosePlacesActivity.class);

            intent.putExtra(MapUtility.D_YY_TAG, getArguments().getString(MapUtility.D_YY_TAG));
            intent.putExtra(MapUtility.D_MM_TAG, getArguments().getString(MapUtility.D_MM_TAG));
            intent.putExtra(MapUtility.D_DD_TAG, getArguments().getString(MapUtility.D_DD_TAG));
            intent.putExtra(MapUtility.A_YY_TAG, getArguments().getString(MapUtility.A_YY_TAG));
            intent.putExtra(MapUtility.A_MM_TAG, getArguments().getString(MapUtility.A_MM_TAG));
            intent.putExtra(MapUtility.A_DD_TAG, getArguments().getString(MapUtility.A_DD_TAG));
            intent.putExtra(MapUtility.TRAVEL_TITLE_TAG, getArguments().getString(MapUtility.TRAVEL_TITLE_TAG));
            intent.putExtra(MapUtility.TRAVEL_PERSON_COUNT_TAG, getArguments().getString(MapUtility.TRAVEL_PERSON_COUNT_TAG));
            intent.putExtra(MapUtility.PLACE_NAME_TAG, getArguments().getString(MapUtility.PLACE_NAME_TAG));
            intent.putExtra(MapUtility.PLACE_TYPE_TAG, getArguments().getString(MapUtility.PLACE_TYPE_TAG));
            intent.putExtra(MapUtility.PLACE_LAT_TAG, getArguments().getString(MapUtility.PLACE_LAT_TAG));
            intent.putExtra(MapUtility.PLACE_LNG_TAG, getArguments().getString(MapUtility.PLACE_LNG_TAG));
            intent.putExtra(MapUtility.PLACE_BITMAP_FILE_PATH_TAG, getArguments().getString(MapUtility.PLACE_BITMAP_FILE_PATH_TAG));
            intent.putExtra(MapUtility.PLACE_LOAD_TAG, true);
            intent.putExtra(MapUtility.CURRENT_DAY_TAG, getArguments().getString(MapUtility.CURRENT_DAY_TAG));

            return intent;
        }


        @Override
        public View onCreateView(LayoutInflater choose_places, ViewGroup container,
                                 Bundle savedInstanceState) {

            final View rootView = choose_places.inflate(R.layout.fragment_plan, container, false);
            ivTravelMap = (ImageView) rootView.findViewById(R.id.ivTravelMap);

            String filePath = getArguments().getString(ARG_SECTION_PLACE_BITMAP);
            Log.d("DEBUG-TEST", filePath + "in TripPlanActivity");

            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            ivTravelMap.setImageBitmap(bitmap);
            ivTravelMap.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = getDataIntentForReloadMap();
                    startActivity(intent);
                }
            });
            TextView textView = (TextView) rootView.findViewById(R.id.dt);
            ImageView left = (ImageView) rootView.findViewById(R.id.left);
            ImageView right = (ImageView) rootView.findViewById(R.id.right);
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1)//get arrow distinguished
                left.setVisibility(View.INVISIBLE);
            else
                left.setVisibility(View.VISIBLE);
            if (getArguments().getInt(ARG_SECTION_NUMBER) == getArguments().getInt(ARG_SECTION_LAST))//get arrow distinguished
            {
                right.setVisibility(View.INVISIBLE);
                LinearLayout linearLayout = rootView.findViewById(R.id.linear_layout_fragment);
                /*
                comp_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    //save to local
                    }
                });
                */

            } else
                right.setVisibility(View.VISIBLE);
            Button plan_self = (Button) rootView.findViewById(R.id.plan_self);
            plan_self.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("test", "onCreateView: " + getArguments().getInt(ARG_SECTION_NUMBER));
                    Intent choose_places = new Intent(getActivity(), ChoosePlacesActivity.class);
                    choose_places.putExtra(ARG_SECTION_TITLE, getArguments().getString(ARG_SECTION_TITLE));
                    choose_places.putExtra(ARG_SECTION_FIRST_PLACE, getArguments().getString(ARG_SECTION_FIRST_PLACE));
                    choose_places.putExtra(ARG_SECTION_PLACE_LAT, getArguments().getString(ARG_SECTION_PLACE_LAT));
                    choose_places.putExtra(ARG_SECTION_PLACE_LNG, getArguments().getString(ARG_SECTION_PLACE_LNG));
                    choose_places.putExtra(ARG_SECTION_PLACE_BITMAP, getArguments().getString(ARG_SECTION_PLACE_BITMAP));
                    choose_places.putExtra(ARG_SECTION_CURRENT_DAY, getArguments().getString(ARG_SECTION_CURRENT_DAY));
                    //for saving
                    choose_places.putExtra(ARG_SECTION_CURRENT_DAY_TEMP, getArguments().getInt(ARG_SECTION_NUMBER));
                    TripPlanActivity activity = (TripPlanActivity) getActivity();
                    Travel travel = activity.travel;
                    choose_places.putExtra("travelData", travel);
                    //end here
                    startActivityForResult(choose_places, getArguments().getInt(ARG_SECTION_NUMBER));
                    getActivity().overridePendingTransition(R.anim.sliding_up, R.anim.stay);
                }

            });

            Button plan_auto = (Button) rootView.findViewById(R.id.plan_auto);
            plan_auto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putInt(ARG_SECTION_NUMBER, getArguments().getInt(ARG_SECTION_NUMBER));
                    args.putString(ARG_SECTION_TITLE, getArguments().getString(ARG_SECTION_TITLE));
                    args.putString(ARG_SECTION_CURRENT_DAY, getArguments().getString(ARG_SECTION_CURRENT_DAY));
                    Log.d("DEBUG-TEST", getArguments().getString(ARG_SECTION_CURRENT_DAY));
                    Log.d("DEBUG-TEST", getArguments().getString(ARG_SECTION_NUMBER) + "");
                    args.putString(ARG_SECTION_PLACE_LAT, getArguments().getString(ARG_SECTION_PLACE_LAT));
                    args.putString(ARG_SECTION_PLACE_LNG, getArguments().getString(ARG_SECTION_PLACE_LNG));
                    args.putString(ARG_SECTION_PLACE_BITMAP, getArguments().getString(ARG_SECTION_PLACE_BITMAP));
                    args.putString(ARG_SECTION_CURRENT_DAY_TEMP, getArguments().getString(ARG_SECTION_CURRENT_DAY_TEMP));
                    TripPlanActivity activity = (TripPlanActivity) getActivity();
                    Travel travel = activity.travel;
                    args.putSerializable("travelData", travel);
                    BottomSheetDialog bottomSheetDialog = BottomSheetDialog.getInstance();
                    bottomSheetDialog.setArguments(args);
                    bottomSheetDialog.show(((AppCompatActivity) getActivity()).getSupportFragmentManager(), "bottomsheet");
                }
            });

            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode != RESULT_OK)
                return;

            int currentDay = getArguments().getInt(ARG_SECTION_NUMBER);

            int index = data.getIntExtra("curDay", 0);
            TripPlanActivity activity = (TripPlanActivity) getActivity();
            activity.travel = (Travel) data.getExtras().getSerializable("travelData");
            Log.d("placeId",currentDay + " is"+ activity.travel.dailyDiary[index-1].review.get(0).place_name);

            if (requestCode == currentDay) {
                String filePath = getContext().getFilesDir().getPath().toString() + "/"
                        + getArguments().getString(ARG_SECTION_TITLE) + currentDay + ".png";
                File file = new File(filePath);
                Log.d("DEBUG-TEST", "불러오는 파일의 path : " + file.getAbsolutePath());
                if (file.exists()) {
                    Log.d("DEBUG-TEST", "스냅샷을 이미지버튼에 불러옵니다. in TripPlanActivity");
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    ivTravelMap.setImageBitmap(bitmap);
                }


                Log.d("DEBUG-TEST", "curday : " + currentDay + ", total : " + totalTravelDays);

                if (currentDay != totalTravelDays) {
                    Intent choose_places = new Intent(getActivity(), ChoosePlacesActivity.class);
                    arrayPlaceLat[currentDay + 1] = data.getStringExtra(MapUtility.PLACE_LAT_TAG);
                    arrayPlaceLng[currentDay + 1] = data.getStringExtra(MapUtility.PLACE_LNG_TAG);
                    arrayPlaceType[currentDay + 1] = data.getStringExtra(MapUtility.PLACE_TYPE_TAG);
                    arrayPlaceBitmapFilePath[currentDay + 1] = data.getStringExtra(MapUtility.PLACE_BITMAP_FILE_PATH_TAG);

                    Log.d("DEBUG-TEST", "arrayPlaceBitmapFilePath : " + arrayPlaceBitmapFilePath[currentDay + 1]);

                    choose_places.putExtra(ARG_SECTION_TITLE, getArguments().getString(ARG_SECTION_TITLE));
                    choose_places.putExtra(ARG_SECTION_FIRST_PLACE, arrayPlaceType[currentDay + 1]);
                    choose_places.putExtra(ARG_SECTION_PLACE_LAT, arrayPlaceLat[currentDay + 1]);
                    choose_places.putExtra(ARG_SECTION_PLACE_LNG, arrayPlaceLng[currentDay + 1]);
                    choose_places.putExtra(ARG_SECTION_PLACE_BITMAP, arrayPlaceBitmapFilePath[currentDay + 1]);
                    choose_places.putExtra(ARG_SECTION_CURRENT_DAY, currentDay + 1);
                    choose_places.putExtra("travelData", activity.travel);
                    startActivityForResult(choose_places, currentDay+1);
                }
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == diff_days)
                return PlaceholderFragment.newInstance(position + 1, day_count, etitle, eCurrentDay,
                        eFirstPlace, arrayPlaceLat[position + 1], arrayPlaceLng[position + 1], arrayPlaceType[position + 1], arrayPlaceBitmapFilePath[position + 1]);
            else
                return PlaceholderFragment.newInstance(position + 1, etitle, eCurrentDay,
                        eFirstPlace, arrayPlaceLat[position + 1], arrayPlaceLng[position + 1], arrayPlaceType[position + 1], arrayPlaceBitmapFilePath[position + 1]);
        }

        /*
        @Override
        public android.support.v4.app.Fragment getItem(int position, boolean sibal) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            getSupportFragmentManager().getFragments()
        }
        */

        @Override
        public int getCount() {

            get_datas();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date beginDate;
            Date endDate;
            try {
                beginDate = formatter.parse(d_yy + d_mm + d_dd);
                endDate = formatter.parse(a_yy + a_mm + a_dd);
                long diff = endDate.getTime() - beginDate.getTime();
                diff_days = diff / (24 * 60 * 60 * 1000);
            } catch (ParseException e) {
                e.printStackTrace();
                diff_days = -1;
            }

            day_count = (int) diff_days + 1;
            // Show 3 total pages.
            return day_count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "day" + (position + 1);
        }
    }

    private String add_zero_to_string(String number) {
        try {
            if (Integer.parseInt(number) < 10)
                if (number.charAt(0) == '0')
                    return number;
                else
                    return "0" + number;
        } catch (NumberFormatException e) {
            Toast.makeText(this, number, Toast.LENGTH_SHORT).show();
            number = "0";
        }

        return number;
    }

    private void get_datas() {
        Intent intent = getIntent();
        d_yy = intent.getStringExtra("departing_year");
        d_mm = intent.getStringExtra("departing_month");
        d_mm = add_zero_to_string(d_mm);
        d_dd = intent.getStringExtra("departing_day");
        d_dd = add_zero_to_string(d_dd);
        a_yy = intent.getStringExtra("arriving_year");
        a_mm = intent.getStringExtra("arriving_month");
        a_mm = add_zero_to_string(a_mm);
        a_dd = intent.getStringExtra("arriving_day");
        a_dd = add_zero_to_string(a_dd);
        etitle = intent.getStringExtra("title_text");
        //eplace = intent.getStringExtra("place_name");
        person_count = intent.getStringExtra("person_count");
        eFirstPlace = intent.getStringExtra(MapUtility.PLACE_NAME_TAG);
        ePlaceLat = intent.getStringExtra(MapUtility.PLACE_LAT_TAG);
        ePlaceLng = intent.getStringExtra(MapUtility.PLACE_LNG_TAG);
        //for getting english city name

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
        try {
            double lat = Double.parseDouble(ePlaceLat);
            double lng = Double.parseDouble(ePlaceLng);
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 5);
            ePlace = addresses.get(0).getLocality();
            if(ePlace == null)
                ePlace = addresses.get(0).getCountryName();
        } catch (IOException e) {
            ePlace = "City not Found";
        }
        //getting english city name ended

        ePlaceType = intent.getStringExtra(MapUtility.PLACE_TYPE_TAG);
        ePlaceBitmapFilePath = intent.getStringExtra(MapUtility.PLACE_BITMAP_FILE_PATH_TAG);
        if (ePlaceBitmapFilePath == null)
            Log.d("DEBUG-TEST", getResources().getString(R.string.intent_bitmap_error) + "in TripPlanActivity");
        eCurrentDay = intent.getStringExtra(MapUtility.CURRENT_DAY_TAG);
    }

    private void get_datas_for_travel()// test for Travel class
    {
        Intent intent = getIntent();
        d_yy = intent.getStringExtra("departing_year");
        int s_yy = Integer.parseInt(d_yy);
        d_mm = intent.getStringExtra("departing_month");
        d_mm = add_zero_to_string(d_mm);
        int s_mm = Integer.parseInt(d_mm);
        d_dd = intent.getStringExtra("departing_day");
        d_dd = add_zero_to_string(d_dd);
        int s_dd = Integer.parseInt(d_dd);
        a_yy = intent.getStringExtra("arriving_year");
        int e_yy = Integer.parseInt(a_yy);
        a_mm = intent.getStringExtra("arriving_month");
        a_mm = add_zero_to_string(a_mm);
        int e_mm = Integer.parseInt(a_mm);
        a_dd = intent.getStringExtra("arriving_day");
        a_dd = add_zero_to_string(a_dd);
        int e_dd = Integer.parseInt(a_dd);
        etitle = intent.getStringExtra(MapUtility.TRAVEL_TITLE_TAG);
        person_count = intent.getStringExtra("person_count");
        Date beginDate;
        Date endDate;
        long diff;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        try {
            beginDate = formatter.parse(d_yy + d_mm + d_dd);
            endDate = formatter.parse(a_yy + a_mm + a_dd);
            diff = endDate.getTime() - beginDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            diff = 0;
        }
        diff_days = diff / (24 * 60 * 60 * 1000);

        travel = new Travel(getApplicationContext(), "psm", etitle, ePlace, Integer.parseInt(person_count), (int) diff_days + 1, s_mm, s_yy, s_mm, e_yy, e_mm, e_dd);
    }
}
