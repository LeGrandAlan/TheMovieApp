package fr.alanlg.themovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import fr.alanlg.themovieapp.adapter.MovieImageAdapter;
import fr.alanlg.themovieapp.model.Movie;

public class TopRatedFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_rated, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final GridView topRatedGridView = view.findViewById(R.id.topRatedGridView);

        ApiCaller apiCaller = new ApiCaller(getContext());

        apiCaller.topRated(1)
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Type movieListType = new TypeToken<LinkedList<Movie>>(){}.getType();
                        LinkedList<Movie> topRatedMovies = new Gson().fromJson(result.get("results"), movieListType);

                        topRatedGridView.setAdapter(new MovieImageAdapter(getContext(), topRatedMovies));
                    }
                });

        topRatedGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie clickedMovie = (Movie) parent.getItemAtPosition((int) id);
                Intent intent = new Intent(getContext(),MovieInfoActivity.class);
                intent.putExtra("movie", clickedMovie);
                startActivity(intent);
            }
        });

    }
}
