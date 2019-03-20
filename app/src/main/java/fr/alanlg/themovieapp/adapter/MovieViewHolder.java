package fr.alanlg.themovieapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fr.alanlg.themovieapp.R;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    public final ImageView image;
    public final TextView title;
    public final TextView description;

    public MovieViewHolder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.movieImage);
        title = itemView.findViewById(R.id.movieTitle);
        description = itemView.findViewById(R.id.movieDescription);
    }
}