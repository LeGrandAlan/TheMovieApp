package fr.alanlg.themovieapp.listener;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.firebase.firestore.CollectionReference;

import fr.alanlg.themovieapp.R;
import fr.alanlg.themovieapp.model.Movie;

public class FabFavoriteClickListener implements View.OnClickListener {

    private Context context;
    private Movie movie;
    private CollectionReference favoriteMovies;

    public FabFavoriteClickListener(Context context, Movie movie, CollectionReference favoriteMovies) {
        this.context = context;
        this.movie = movie;
        this.favoriteMovies = favoriteMovies;
    }

    @Override
    public void onClick(final View view) {
        FloatingActionButton fabFav = (FloatingActionButton) view;
        favoriteMovies.document(String.valueOf(movie.getId())).delete();
        Snackbar.make(view, "Film enlevé des favoris !", Snackbar.LENGTH_LONG).show();
        fabFav.setImageResource(R.drawable.ic_no_favorite);
        fabFav.setOnClickListener(new FabNoFavoriteClickListener(context, movie, favoriteMovies));
    }

}
