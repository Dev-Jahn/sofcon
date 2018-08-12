package com.example.kyeon.myapplication;


import android.app.ActionBar;
import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        /*

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("");

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
        return true;
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
            Toast.makeText(getApplicationContext(), "sibal", Toast.LENGTH_SHORT).show();
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

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_plan, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.dt);
            ImageView left = (ImageView) rootView.findViewById(R.id.left);
            ImageView right = (ImageView) rootView.findViewById(R.id.right);
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1)//get arrow distinguished
                left.setVisibility(View.INVISIBLE);
            else
                left.setVisibility(View.VISIBLE);
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 3)//get arrow distinguished
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
                startActivityForResult(choose_places,getArguments().getInt(ARG_SECTION_NUMBER));
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
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
