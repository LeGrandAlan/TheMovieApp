package fr.alanlg.themovieapp.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
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

    public MovieAdapter(Context context,List<Movie> movies) {
        this.context = context;
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
        final Movie movie = movies.get(position);
        Picasso.get().load(movie.getPosterLink()).placeholder(R.drawable.image_loading).into(itemViewHolder.image);
        itemViewHolder.title.setText(movie.getTitle());
        itemViewHolder.description.setText(movie.getDescription());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            itemViewHolder.description.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
