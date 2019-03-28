package fr.alanlg.themovieapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import fr.alanlg.themovieapp.model.CastMember;
import fr.alanlg.themovieapp.model.CrewMember;
import fr.alanlg.themovieapp.model.MovieVideo;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int movieId = 32617;
        ApiCaller apiCaller = new ApiCaller(getContext());
        apiCaller.movieCredits(movieId).setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                Type crewMemberListType = new TypeToken<LinkedList<CrewMember>>() {}.getType();
                LinkedList<CrewMember> crewMembers = new Gson().fromJson(result.get("crew"), crewMemberListType);

                CrewMember director = null;
                for (CrewMember crewMember : crewMembers) {
                    if (crewMember.getJob().equals("Director")){
                        director = crewMember;
                    }
                }

            }
        });

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
