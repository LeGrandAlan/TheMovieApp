package fr.alanlg.themovieapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import fr.alanlg.themovieapp.dao.ApiCaller;
import fr.alanlg.themovieapp.MovieInfoActivity;
import fr.alanlg.themovieapp.adapter.MovieImageAdapter;
import fr.alanlg.themovieapp.model.Movie;

public abstract class MovieGridRecyclerFragment extends Fragment {

    @IdRes
    int recyclerViewRes;
    @LayoutRes
    int fragmentLayoutRes;
    RecyclerView recyclerView;
    int spanInGridLayout;

    ApiCaller apiCaller;
    MovieImageAdapter movieImageAdapter;
    boolean loading = false;

    public MovieGridRecyclerFragment(@IdRes int recyclerViewRes, @LayoutRes int fragmentLayoutRes, int spanInGridLayout) {
        this.recyclerViewRes = recyclerViewRes;
        this.fragmentLayoutRes = fragmentLayoutRes;
        this.spanInGridLayout = spanInGridLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(this.fragmentLayoutRes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(recyclerViewRes);

        apiCaller = new ApiCaller(getContext());

        movieImageAdapter = new MovieImageAdapter(getContext(), new LinkedList<Movie>());
        movieImageAdapter.setHasStableIds(true);

        recyclerView.setAdapter(movieImageAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanInGridLayout));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(30);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView
                    .getLayoutManager();
            int visibleThreshold = 6;

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

    abstract void recyclerViewAddData();

}
