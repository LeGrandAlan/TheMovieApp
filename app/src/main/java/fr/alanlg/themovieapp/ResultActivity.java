package fr.alanlg.themovieapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;

import fr.alanlg.themovieapp.adapter.MovieAdapter;
import fr.alanlg.themovieapp.dao.ApiCaller;
import fr.alanlg.themovieapp.fragment.FilterDialogFragment;
import fr.alanlg.themovieapp.model.Movie;

public class ResultActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    ApiCaller apiCaller;
    String keyword;
    String studio;
    int releaseYear;
    String genre;
    int resultNumberMax;
    int currentResultNumber = 0;
    int nextPage = 1;
    boolean loading = false;
    boolean viewList = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();

        keyword = bundle.getString("keyword");
        studio = bundle.getString("Studio");
        releaseYear = bundle.getInt("releaseYear");
        genre = bundle.getString("genre");
        resultNumberMax = bundle.getInt("resultNumberMax") == 0 ? 100000 : bundle.getInt("resultNumberMax");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(30);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        movieAdapter = new MovieAdapter(getApplicationContext(), new LinkedList<Movie>(), viewList);
        movieAdapter.setHasStableIds(true);
        recyclerView.setAdapter(movieAdapter);

        apiCaller = new ApiCaller(getApplicationContext());

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
                    intent.putExtra("movie", movieAdapter.getList().get(position));
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            int visibleThreshold = 4;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold) && resultNumberMax != currentResultNumber) {
                    loading = true;
                    recyclerViewAddData();
                }
            }
        });

        recyclerViewAddData();
    }


    public void recyclerViewAddData() {
        apiCaller.searchMovie(keyword, nextPage, releaseYear)
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            Type movieListType = new TypeToken<LinkedList<Movie>>() {
                            }.getType();
                            LinkedList<Movie> movies = new Gson().fromJson(result.get("results"), movieListType);

                            if (currentResultNumber + movies.size() > resultNumberMax) {
                                movies.subList(resultNumberMax - currentResultNumber, movies.size()).clear();
                                currentResultNumber = resultNumberMax;
                            } else
                                currentResultNumber += movies.size();
                            Log.d("sdfgh", "onCompleted: " + movies.size());
                            int previousIndex = movieAdapter.getItemCount();
                            movieAdapter.addData(movies);
                            movieAdapter.notifyItemRangeInserted(previousIndex, movieAdapter.getItemCount());
                            loading = false;
                        }
                    }
                });
        ++nextPage;
    }

    public void viewFilter() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance(R.string.title_filter);
        filterDialogFragment.show(fm, "fragment_edit_name");
    }

    public void changeView() {
        viewList = !viewList;
        supportInvalidateOptionsMenu();
        recyclerView.setLayoutManager(viewList ? new LinearLayoutManager(this) : new GridLayoutManager(getApplicationContext(), 2));
        movieAdapter.changeViewType(viewList);
        recyclerView.setAdapter(movieAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home)
            this.finish();
        else if (id == R.id.action_filter)
            viewFilter();
        else if (id == R.id.action_changeView)
            changeView();

        return super.onOptionsItemSelected(item);
    }
}
