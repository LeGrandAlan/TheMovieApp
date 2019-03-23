package fr.alanlg.themovieapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;

import java.lang.reflect.Type;
import java.util.LinkedList;

import fr.alanlg.themovieapp.adapter.MovieAdapter;
import fr.alanlg.themovieapp.model.Movie;

public class ResultActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    ApiCaller apiCaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        String keyword = bundle.getString("keyword");
        int releaseYear = bundle.getInt("releaseYear");
        String genre = bundle.getString("genre");
        int resultNumber = bundle.getInt("resultNumber");

        recyclerView = findViewById(R.id.recyclerView);

        apiCaller = new ApiCaller(getApplicationContext());

        apiCaller.searchMovie(keyword, 1, releaseYear)
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Type movieListType = new TypeToken<LinkedList<Movie>>() {
                        }.getType();
                        final LinkedList<Movie> movies = new Gson().fromJson(result.get("results"), movieListType);

                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        movieAdapter = new MovieAdapter(getApplicationContext(), movies);
                        recyclerView.setAdapter(movieAdapter);
                        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                            GestureDetector mGestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                                @Override
                                public boolean onSingleTapUp(MotionEvent e) {
                                    return true;
                                }
                            });

                            @Override
                            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                                View childView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                                if (childView != null && mGestureDetector.onTouchEvent(motionEvent)) {
                                    int position = recyclerView.getChildAdapterPosition(childView);
                                    Intent intent = new Intent(getApplicationContext(), MovieInfoActivity.class);
                                    intent.putExtra("movie", movies.get(position));
                                    startActivity(intent);
                                }
                                return false;

                            }

                            @Override
                            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                            }

                            @Override
                            public void onRequestDisallowInterceptTouchEvent(boolean b) {
                            }
                        });

                    }
                });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
            this.finish();

        return super.onOptionsItemSelected(item);
    }
}
