package fr.alanlg.themovieapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import fr.alanlg.themovieapp.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private List<Movie> movies;

    public MovieAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_content, viewGroup, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder itemViewHolder, int position) {
        Movie movie = movies.get(position);
        Picasso.get().load(movie.getPosterLink()).into(itemViewHolder.image);
        itemViewHolder.title.setText(movie.getTitle());
        itemViewHolder.description.setText(movie.getDescription());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
