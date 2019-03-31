package fr.alanlg.themovieapp.fragment;

import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;

import java.lang.reflect.Type;
import java.util.LinkedList;

import fr.alanlg.themovieapp.R;
import fr.alanlg.themovieapp.model.Movie;

public class PopularFragment extends MovieGridRecyclerFragment {

    int nextPage = 1;

    public PopularFragment() {
        super(R.id.popularRecyclerView, R.layout.fragment_popular, 2);
    }

    @Override
    void recyclerViewAddData() {
        apiCaller.popular(nextPage)
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
