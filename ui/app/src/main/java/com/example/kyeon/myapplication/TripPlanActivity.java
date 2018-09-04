package com.example.kyeon.myapplication;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TripPlanActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    String d_yy, d_mm, d_dd;
    String a_yy, a_mm, a_dd;
    String etitle, person_count, eplace;
    private String eFirstPlace;
    private String ePlaceLat;
    private String ePlaceLng;
    private String ePlaceType;
    long diff_days;

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
        }
        else if (id == R.id.action_complete)
        {
            //s a v e to local


            try
            {
                travel.save();
            }catch (IOException e)
            {
                e.printStackTrace();
                Toast.makeText(this, "sibal", Toast.LENGTH_SHORT).show();
            }


            finish();
        }

        return super.onOptionsItemSelected(item);
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
        private static final String ARG_SECTION_FIRST_PLACE = ChooseFirstPlaceActivity.PLACE_NAME;
        private static final String ARG_SECTION_PLACE_LAT = ChooseFirstPlaceActivity.PLACE_LAT;
        private static final String ARG_SECTION_PLACE_LNG = ChooseFirstPlaceActivity.PLACE_LNG;
        private static final String ARG_SECTION_PLACE_TYPE = ChooseFirstPlaceActivity.PLACE_TYPE;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String title, String firstPlace,
                                                      String placeLat, String placeLng, String placeType) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_SECTION_TITLE, title);
            args.putString(ARG_SECTION_FIRST_PLACE, firstPlace);
            args.putString(ARG_SECTION_PLACE_LAT, placeLat);
            args.putString(ARG_SECTION_PLACE_LNG, placeLng);
            args.putString(ARG_SECTION_PLACE_TYPE, placeType);
            fragment.setArguments(args);
            return fragment;
        }


        public static PlaceholderFragment newInstance(int sectionNumber, int count, String title, String firstPlace,
                                                      String placeLat, String placeLng, String placeType) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(ARG_SECTION_LAST, count);
            args.putString(ARG_SECTION_TITLE, title);
            args.putString(ARG_SECTION_FIRST_PLACE, firstPlace);
            args.putString(ARG_SECTION_PLACE_LAT, placeLat);
            args.putString(ARG_SECTION_PLACE_LNG, placeLng);
            args.putString(ARG_SECTION_PLACE_TYPE, placeType);
            fragment.setArguments(args);




            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater choose_places, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = choose_places.inflate(R.layout.fragment_plan, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.dt);
            ImageView left = (ImageView) rootView.findViewById(R.id.left);
            ImageView right = (ImageView) rootView.findViewById(R.id.right);
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1)//get arrow distinguished
                left.setVisibility(View.INVISIBLE);
            else
                left.setVisibility(View.VISIBLE);
            if(getArguments().getInt(ARG_SECTION_NUMBER) == getArguments().getInt(ARG_SECTION_LAST))//get arrow distinguished
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

            }
            else
                right.setVisibility(View.VISIBLE);
            Button plan_self = (Button) rootView.findViewById(R.id.plan_self);
            plan_self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "onCreateView: "+ getArguments().getInt(ARG_SECTION_NUMBER));
                Intent choose_places = new Intent(getActivity(), ChoosePlacesActivity.class);
                choose_places.putExtra(ARG_SECTION_TITLE, getArguments().getString(ARG_SECTION_TITLE));
                choose_places.putExtra(ARG_SECTION_FIRST_PLACE, getArguments().getString(ARG_SECTION_FIRST_PLACE));
                choose_places.putExtra(ARG_SECTION_PLACE_LAT, getArguments().getString(ARG_SECTION_PLACE_LAT));
                choose_places.putExtra(ARG_SECTION_PLACE_LNG, getArguments().getString(ARG_SECTION_PLACE_LNG));
                startActivityForResult(choose_places, getArguments().getInt(ARG_SECTION_NUMBER));
                getActivity().overridePendingTransition(R.anim.sliding_up, R.anim.stay);
            }

            });

            Button plan_auto = (Button) rootView.findViewById(R.id.plan_auto);
            plan_auto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BottomSheetDialog bottomSheetDialog = BottomSheetDialog.getInstance();
                    bottomSheetDialog.show(((AppCompatActivity)getActivity()).getSupportFragmentManager(),"bottomsheet");
                }
            });

            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter{

        public SectionsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == diff_days)
                return PlaceholderFragment.newInstance(position+1, day_count, etitle, eFirstPlace, ePlaceLat, ePlaceLng, ePlaceType);
            else
                return PlaceholderFragment.newInstance(position+1, etitle, eFirstPlace, ePlaceLat, ePlaceLng, ePlaceType);
        }

        @Override
        public int getCount() {

            get_datas();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date beginDate;
            Date endDate;
            try
            {
                beginDate = formatter.parse(d_yy+d_mm+d_dd);
                endDate = formatter.parse(a_yy+a_mm+a_dd);
                long diff= endDate.getTime() - beginDate.getTime();
                diff_days = diff / (24 * 60 * 60 * 1000);
            }catch (ParseException e)
            {
                e.printStackTrace();
                diff_days = -1;
            }

            day_count = (int) diff_days + 1;
            // Show 3 total pages.
            return day_count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "day" + (position+1);
        }
    }

    private String add_zero_to_string(String number)
    {
        if(Integer.parseInt(number) < 10)
            if(number.charAt(0) == '0')
                return number;
            else
                return "0"+number;
        else
            return number;
    }

    private void get_datas()
    {
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
        eFirstPlace = intent.getStringExtra(ChooseFirstPlaceActivity.PLACE_NAME);
        ePlaceLat = intent.getStringExtra(ChooseFirstPlaceActivity.PLACE_LAT);
        ePlaceLng = intent.getStringExtra(ChooseFirstPlaceActivity.PLACE_LNG);
        ePlaceType = intent.getStringExtra(ChooseFirstPlaceActivity.PLACE_TYPE);

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
        etitle = intent.getStringExtra("title_text");
        person_count = intent.getStringExtra("person_count");
        Date beginDate;
        Date endDate;
        long diff;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        try
        {
            beginDate = formatter.parse(d_yy+d_mm+d_dd);
            endDate = formatter.parse(a_yy+a_mm+a_dd);
            diff= endDate.getTime() - beginDate.getTime();
        }catch (Exception e)
        {
            e.printStackTrace();
            diff = 0;
        }
        diff_days = diff / (24 * 60 * 60 * 1000);
        travel = new Travel(getApplicationContext(),"psm", Integer.parseInt(person_count), (int)diff_days + 1, s_mm, s_yy, s_mm, e_yy, e_mm, e_dd);

        //let's test diff days is 3
        travel.dailyDiary[0].addPlace(123123, "숭실대");
        travel.dailyDiary[0].addPlace(123123, "숭실대학교");
        travel.dailyDiary[0].addPlace(123123, "숭실대도서관");

        travel.dailyDiary[1].addPlace(123123, "asd");
        travel.dailyDiary[1].addPlace(123123, "aosd");
        travel.dailyDiary[1].addPlace(123123, "123123");
        travel.dailyDiary[1].addPlace(123123, "239493");
        travel.dailyDiary[1].addPlace(123123, "1234104");

        travel.dailyDiary[2].addPlace(123123, "qqoo");
        travel.dailyDiary[2].addPlace(123123, "aoakk");
        travel.dailyDiary[2].addPlace(123123, "11ww");
        travel.dailyDiary[2].addPlace(123123, "open");

    }
}
