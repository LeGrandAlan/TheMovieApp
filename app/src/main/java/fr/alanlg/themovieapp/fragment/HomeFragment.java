package fr.alanlg.themovieapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import fr.alanlg.themovieapp.ApiCaller;
import fr.alanlg.themovieapp.MovieInfoActivity;
import fr.alanlg.themovieapp.R;
import fr.alanlg.themovieapp.adapter.Member;
import fr.alanlg.themovieapp.adapter.MemberCardGalleryAdapter;
import fr.alanlg.themovieapp.adapter.MovieCardGalleryAdapter;
import fr.alanlg.themovieapp.model.CastMember;
import fr.alanlg.themovieapp.model.CrewMember;
import fr.alanlg.themovieapp.model.Movie;
import fr.alanlg.themovieapp.model.MovieVideo;

public class HomeFragment extends Fragment {

    Timer timer = new Timer();
    private Handler mHandler = new Handler();
    private int currentPage = -1;

    ViewPager nowPlayingViewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ApiCaller apiCaller = new ApiCaller(getContext());
        apiCaller.nowPlaying(1).setCallback(new FutureCallback<JsonObject>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Type movieListType = new TypeToken<LinkedList<Movie>>() {}.getType();
                LinkedList<Movie> movies = new Gson().fromJson(result.get("results"), movieListType);

                nowPlayingViewPager = view.findViewById(R.id.nowPlayingViewPager);

                final MovieCardGalleryAdapter memberCardGalleryAdapter = new MovieCardGalleryAdapter(movies, getContext());
                nowPlayingViewPager.setAdapter(memberCardGalleryAdapter);

                nowPlayingViewPager.setOnTouchListener(new View.OnTouchListener() {
                    private float pointX;
                    private float pointY;
                    private int tolerance = 50;
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction()){
                            case MotionEvent.ACTION_MOVE:
                                return false; //This is important, if you return TRUE the action of swipe will not take place.
                            case MotionEvent.ACTION_DOWN:
                                pointX = event.getX();
                                pointY = event.getY();
                                break;
                            case MotionEvent.ACTION_UP:
                                boolean sameX = pointX + tolerance > event.getX() && pointX - tolerance < event.getX();
                                boolean sameY = pointY + tolerance > event.getY() && pointY - tolerance < event.getY();
                                if(sameX && sameY){
                                    //The user "clicked" certain point in the screen or just returned to the same position an raised the finger
                                    int itemPosition = ((ViewPager)v).getCurrentItem();
                                    Movie movie = ((MovieCardGalleryAdapter)nowPlayingViewPager.getAdapter()).getItemAtPosition(itemPosition);

                                    Intent intent = new Intent(getContext(), MovieInfoActivity.class);
                                    intent.putExtra("movie", movie);
                                    startActivity(intent);
                                }
                        }
                        return false;
                    }
                });

                nowPlayingViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float v, int i1) {
                    }

                    @Override
                    public void onPageSelected(int i) {
                        //nouvelle page
                        currentPage = i;
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {
                    }
                });

                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            public void run() {
                                ++currentPage;
                                if (currentPage == nowPlayingViewPager.getAdapter().getCount()) {
                                    currentPage = 0;
                                }
                                nowPlayingViewPager.setCurrentItem(currentPage, true);
                            }
                        });
                    }
                }, 0, 5000);

            }
        });

    }

    @Override
    public void onDetach() {
        timer.cancel();
        super.onDetach();
    }
}
