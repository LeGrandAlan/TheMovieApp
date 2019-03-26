package fr.alanlg.themovieapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

import fr.alanlg.themovieapp.listener.FabFavoriteClickListener;
import fr.alanlg.themovieapp.listener.FabNoFavoriteClickListener;
import fr.alanlg.themovieapp.model.Movie;

public class MovieInfoActivity extends AppCompatActivity {

    ImageView imageView;
    TextView title;
    TextView date;
    TextView description;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        // ajout retour en arrière
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        imageView = findViewById(R.id.imageViewMoviePoster);
        title = findViewById(R.id.textViewTitleInfo);
        date = findViewById(R.id.textViewDateInfo);
        description = findViewById(R.id.textViewDescriptionInfo);

        Bundle bundle = getIntent().getExtras();

        final Movie movie = (Movie) bundle.getSerializable("movie");

        Picasso.get().load(movie.getPosterLink()).placeholder(R.drawable.image_loading).into(imageView);
        title.setText(movie.getTitle());
        date.setText(movie.getReleaseDate());
        description.setText(movie.getDescription());



        final FloatingActionButton fabFav = findViewById(R.id.fabFavMovie);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) {
            fabFav.setVisibility(View.INVISIBLE);
        } else { //l'utilisateur est connecté
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            CollectionReference users = firestore.collection("users");
            final CollectionReference favoriteMovies = users.document(user.getUid()).collection("favorite-movies");

            final LinkedList<Integer> movieIdList = new LinkedList<>();

            //Récupération de tous les ids des films favoris de l'utilisateur
            favoriteMovies.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            movieIdList.push(Integer.valueOf(document.getId()));
                        }
                    } else {
                        Log.w("DEBUG", "Error getting documents.", task.getException());
                    }
                }
            }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    //si le film est déjà dans les favoris de l'utilisateur
                    if(movieIdList.contains(movie.getId())) {
                        fabFav.setImageResource(R.drawable.ic_menu_favorite);
                        fabFav.setOnClickListener(new FabFavoriteClickListener(getApplication(), movie, favoriteMovies));
                    } else { //si le film n'est pas dans les favoris
                        fabFav.setOnClickListener(new FabNoFavoriteClickListener(getApplication(), movie, favoriteMovies));
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}