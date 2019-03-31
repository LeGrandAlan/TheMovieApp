package fr.alanlg.themovieapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;

import java.lang.reflect.Type;
import java.util.LinkedList;

import fr.alanlg.themovieapp.ApiCaller;
import fr.alanlg.themovieapp.R;
import fr.alanlg.themovieapp.adapter.Member;
import fr.alanlg.themovieapp.adapter.MemberCardGalleryAdapter;
import fr.alanlg.themovieapp.adapter.MovieCardGalleryAdapter;
import fr.alanlg.themovieapp.model.CastMember;
import fr.alanlg.themovieapp.model.CrewMember;
import fr.alanlg.themovieapp.model.Movie;
import fr.alanlg.themovieapp.model.MovieVideo;

public class HomeFragment extends Fragment {

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
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Type movieListType = new TypeToken<LinkedList<Movie>>() {}.getType();
                LinkedList<Movie> movies = new Gson().fromJson(result.get("results"), movieListType);

                ViewPager nowPlayingViewPager = view.findViewById(R.id.nowPlayingViewPager);

                final MovieCardGalleryAdapter memberCardGalleryAdapter = new MovieCardGalleryAdapter(movies, getContext());
                nowPlayingViewPager.setAdapter(memberCardGalleryAdapter);
            }
        });

    }
}
