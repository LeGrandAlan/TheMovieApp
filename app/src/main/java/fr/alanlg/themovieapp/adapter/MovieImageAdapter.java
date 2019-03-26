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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import fr.alanlg.themovieapp.R;
import fr.alanlg.themovieapp.model.Movie;

public class MovieImageAdapter extends RecyclerView.Adapter<MovieImageAdapter.ViewHolder> {

    private Context context;
    private LinkedList<Movie> movies;

    public MovieImageAdapter(Context context, LinkedList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    public Movie getItem(int position) {
        return this.movies.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_image_grid_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Movie movie = movies.get(position);
        Picasso.get().load(movie.getPosterLink()).placeholder(R.drawable.image_loading).fit().into(viewHolder.getImageView());

    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        Picasso.get().cancelRequest(holder.getImageView());

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


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        ViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.movieImage);
        }

        ImageView getImageView() {
            return this.imageView;
        }

    }

}

