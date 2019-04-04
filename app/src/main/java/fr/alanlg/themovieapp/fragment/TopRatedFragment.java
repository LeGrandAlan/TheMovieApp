package fr.alanlg.themovieapp.fragment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;

import java.lang.reflect.Type;
import java.util.LinkedList;

import fr.alanlg.themovieapp.R;
import fr.alanlg.themovieapp.model.Movie;

public class TopRatedFragment extends MovieGridRecyclerFragment {

    int nextPage = 1;

    public TopRatedFragment() {
        super(R.id.topRatedRecyclerView, R.layout.fragment_top_rated, 2);
    }

    @Override
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
