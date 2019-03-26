package fr.alanlg.themovieapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class FavoriteFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;//ce fragment n'est proposé que si l'utilisateur est connecté

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference users = firestore.collection("users");
        final CollectionReference favoriteMovies = users.document(user.getUid()).collection("favorite-movies");

        favoriteMovies.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        int movieId = Integer.parseInt(document.getId());
                        //ajouter le film favoris à la vue
                    }
                } else {
                    Log.w("DEBUG", "Error getting documents.", task.getException());
                }
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
