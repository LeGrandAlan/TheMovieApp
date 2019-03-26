package fr.alanlg.themovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

    RecyclerView topRatedRecyclerView;
    ApiCaller apiCaller;
    int nextPage = 1;
    MovieImageAdapter movieImageAdapter;
    boolean loading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_rated, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        topRatedRecyclerView = view.findViewById(R.id.topRatedRecyclerView);

        apiCaller = new ApiCaller(getContext());

        movieImageAdapter = new MovieImageAdapter(getContext(), new LinkedList<Movie>());
        movieImageAdapter.setHasStableIds(true);

        topRatedRecyclerView.setAdapter(movieImageAdapter);
        topRatedRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        topRatedRecyclerView.setHasFixedSize(true);
        topRatedRecyclerView.setItemViewCacheSize(30);
        topRatedRecyclerView.setDrawingCacheEnabled(true);
        topRatedRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        topRatedRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
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
                    Intent intent = new Intent(getContext(), MovieInfoActivity.class);
                    intent.putExtra("movie", movieImageAdapter.getItem(position));
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

        topRatedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) topRatedRecyclerView
                    .getLayoutManager();
            int visibleThreshold = 4;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = gridLayoutManager.getItemCount();
                int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    loading = true;
                    recyclerViewAddData();
                }
            }
        });


        recyclerViewAddData();
    }


    public void recyclerViewAddData() {
        apiCaller.topRated(nextPage)
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            Type movieListType = new TypeToken<LinkedList<Movie>>() {
                            }.getType();
                            LinkedList<Movie> movies = new Gson().fromJson(result.get("results"), movieListType);
                            int previousIndex = movieImageAdapter.getItemCount();
                            movieImageAdapter.addData(movies);
                            movieImageAdapter.notifyItemRangeInserted(previousIndex, movieImageAdapter.getItemCount());
                            loading = false;
                        }
                    }
                });
        ++nextPage;
    }


}
