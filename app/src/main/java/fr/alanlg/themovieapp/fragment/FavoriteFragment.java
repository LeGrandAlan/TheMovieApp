package fr.alanlg.themovieapp.fragment;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

import java.util.LinkedList;
import java.util.Objects;

import fr.alanlg.themovieapp.R;
import fr.alanlg.themovieapp.model.Movie;

public class FavoriteFragment extends MovieGridRecyclerFragment {

    LinkedList<Integer> favoriteMovieIdList;
    int index = 0;
    Task<QuerySnapshot> completed;
    boolean firstLauch = true;

    public FavoriteFragment() {
        super(R.id.favoriteRecyclerView, R.layout.fragment_favorite, 3);

        getFromDatabase();

    }

    /**
     * Récupération de la liste des ids des films préférés de l'utilisateur
     */
    private void getFromDatabase() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null; //ce fragment n'est proposé que si l'utilisateur est connecté

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference users = firestore.collection("users");
        final CollectionReference favoriteMovies = users.document(user.getUid()).collection("favorite-movies");

        favoriteMovieIdList = new LinkedList<>();

        completed = favoriteMovies.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        int movieId = Integer.parseInt(document.getId());
                        favoriteMovieIdList.add(movieId);
                    }
                } else {
                    Log.w("DEBUG", "Error getting documents.", task.getException());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!this.firstLauch){
            this.movieImageAdapter.deleteData();
            index = 0;

            //récupération des nouvelles données
            this.getFromDatabase();
            this.movieImageAdapter.notifyDataSetChanged();
            recyclerViewAddData();
        } else {
            this.firstLauch = false;
        }
    }

    @Override
    public void recyclerViewAddData() {
        if (!completed.isComplete()) {
            completed.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    recyclerViewAddData();
                }
            });
        } else {
            //TODO: si tout est affiché, alors bloquer

            int initialIndex = index;
            for (int i = initialIndex; i < initialIndex + 6; i++) {
                if (i >= this.favoriteMovieIdList.size())
                    return;
                apiCaller.movieDetails(this.favoriteMovieIdList.get(i)).setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e == null) {
                            Movie movie = new Gson().fromJson(result, Movie.class);

                            int previousIndex = movieImageAdapter.getItemCount();
                            movieImageAdapter.addData(movie);
                            movieImageAdapter.notifyItemRangeInserted(previousIndex, movieImageAdapter.getItemCount());
                            loading = false;
                        }
                    }
                });
                ++index;
            }
        }

    }
}