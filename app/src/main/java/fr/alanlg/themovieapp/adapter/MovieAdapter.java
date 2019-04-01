package fr.alanlg.themovieapp.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import fr.alanlg.themovieapp.R;
import fr.alanlg.themovieapp.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private Context context;
    private List<Movie> movies;
    private boolean list;

    public MovieAdapter(Context context, List<Movie> movies, boolean list) {
        this.context = context;
        this.movies = movies;
        this.list = list;
    }

    public List<Movie> getList() {
        return this.movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(list ? R.layout.movie_list_item : R.layout.movie_grid_item_director, viewGroup, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder itemViewHolder, int position) {
        final Movie movie = movies.get(position);

        if (list) {
            Picasso.get().load(movie.getPosterLink()).placeholder(R.drawable.image_loading).fit().into(itemViewHolder.image);
            itemViewHolder.title.setText(movie.getTitle() == null || movie.getTitle().equals("") ? "Pas de titre" : movie.getTitle());
            itemViewHolder.description.setText(movie.getDescription() == null || movie.getDescription().equals("") ? "Pas de description" : movie.getDescription());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                itemViewHolder.description.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            }
        } else {
            Picasso.get().load(movie.getPosterLink()).placeholder(R.drawable.image_loading).into(itemViewHolder.image);
            itemViewHolder.title.setText(movie.getTitle() == null || movie.getTitle().equals("") ? "Pas de titre" : movie.getTitle());
            itemViewHolder.director.setText("Le réalisateur");
        }
    }

    @Override
    public void onViewRecycled(@NonNull MovieViewHolder holder) {
        super.onViewRecycled(holder);
        Picasso.get().cancelRequest(holder.image);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public long getItemId(int position) {
        return movies.get(position).hashCode();
    }

    public void addData(List<Movie> movies) {
        this.movies.addAll(movies);
    }

    public void changeViewType(boolean list) {
        this.list = list;
    }
}
