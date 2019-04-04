package fr.alanlg.themovieapp.listener;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;

import java.util.HashMap;
import java.util.Map;

import fr.alanlg.themovieapp.R;
import fr.alanlg.themovieapp.model.Movie;

public class FabNoFavoriteClickListener implements View.OnClickListener {

    private Context context;
    private Movie movie;
    private CollectionReference favoriteMovies;

    public FabNoFavoriteClickListener(Context context, Movie movie, CollectionReference favoriteMovies) {
        this.context = context;
        this.movie = movie;
        this.favoriteMovies = favoriteMovies;
    }

    @Override
    public void onClick(final View view) {
        FloatingActionButton fabFav = (FloatingActionButton) view;
        Map<String, Object> favoriteMovie = new HashMap<>();
        favoriteMovie.put("movieId", movie.getId());

        favoriteMovies
                .document(String.valueOf(movie.getId()))
                .set(favoriteMovie)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(view, "Le film à été ajouté aux favoris !", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Erreur : le film n'a pas été ajouté aux favoris", Toast.LENGTH_SHORT).show();
                    }
                });
        fabFav.setImageResource(R.drawable.ic_menu_favorite);
        fabFav.setOnClickListener(new FabFavoriteClickListener(context, movie, favoriteMovies));
    }

}
