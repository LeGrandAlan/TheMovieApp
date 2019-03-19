package fr.alanlg.themovieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fr.alanlg.themovieapp.model.Movie;

public class ResultActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    ApiCaller apiCaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle bundle = getIntent().getExtras();

        String keyword = bundle.getString("keyword");
        int releaseYear = bundle.getInt("releaseYear");
        String genre = bundle.getString("genre");
        int resultNumber = bundle.getInt("resultNumber");

        recyclerView = findViewById(R.id.recyclerView);

        apiCaller = new ApiCaller(getApplicationContext());
        Log.d("", "onCreate: " + keyword + " " + releaseYear + " " + resultNumber);
        apiCaller.searchMovie(keyword, 1, releaseYear)
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Type movieListType = new TypeToken<LinkedList<Movie>>() {
                        }.getType();
                        LinkedList<Movie> movies = new Gson().fromJson(result.get("results"), movieListType);
                        Log.d("movies", "onCompleted: " + result.toString());

                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        movieAdapter = new MovieAdapter(movies);
                        recyclerView.setAdapter(movieAdapter);
                    }
                });
    }
}
