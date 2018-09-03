package com.example.kyeon.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int c1;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        AnimationSet animationSet = new AnimationSet(true);

        Animation alpha = new AlphaAnimation(0.2f, 1.0f);
        alpha.setDuration(1500);

        Animation slide_up = new TranslateAnimation(0,0,100,0);
        slide_up.setDuration(1500);

        animationSet.addAnimation(alpha);
        animationSet.addAnimation(slide_up);
        final ImageView imageView = rootView.findViewById(R.id.imageView);
        imageView.startAnimation(animationSet);

        final CircleImageView circleImageView = rootView.findViewById(R.id.main_image);
        final CircleImageView circleImageView_background = rootView.findViewById(R.id.main_image_background);
        final Drawable backgrounds[] = new Drawable[5];
        backgrounds[0] = getResources().getDrawable(R.drawable.city_busan);
        backgrounds[1] = getResources().getDrawable(R.drawable.city_seoul);
        backgrounds[2] = getResources().getDrawable(R.drawable.city_fukuoka);
        backgrounds[3] = getResources().getDrawable(R.drawable.city_beijing);
        backgrounds[4] = getResources().getDrawable(R.drawable.city_tai);

        final Animation fade_out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        final Animation fade_in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);

        circleImageView.setAnimation(fade_out);// 한번 부름
        circleImageView_background.setAnimation(fade_in);
        c1 = 1;

        fade_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("image", "onAnimationEnd: c : " + c1);

                if(c1 % 2 == 0)
                {
                    Random rand = new Random();
                    int index = rand.nextInt(backgrounds.length);
                    circleImageView_background.setImageDrawable(backgrounds[index]);
                    circleImageView_background.startAnimation(fade_in);
                    animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                    animation.setDuration(1);
                    circleImageView.startAnimation(animation);
                    circleImageView.post(new Runnable() {
                        @Override
                        public void run() {
                            circleImageView.startAnimation(fade_out);
                        }
                    });
                    c1++;
                }
                else
                {
                    animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                    animation.setDuration(1);
                    circleImageView_background.startAnimation(animation);
                    circleImageView_background.post(new Runnable() {
                        @Override
                        public void run() {
                            circleImageView_background.startAnimation(fade_out);
                        }
                    });
                    Random rand = new Random();
                    int index = rand.nextInt(backgrounds.length);
                    circleImageView.setImageDrawable(backgrounds[index]);
                    circleImageView.startAnimation(fade_in);
                    c1++;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
