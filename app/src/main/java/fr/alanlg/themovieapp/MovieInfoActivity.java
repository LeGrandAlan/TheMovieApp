package fr.alanlg.themovieapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Random;

import fr.alanlg.themovieapp.adapter.CardGalleryAdapter;
import fr.alanlg.themovieapp.adapter.Member;
import fr.alanlg.themovieapp.listener.FabFavoriteClickListener;
import fr.alanlg.themovieapp.listener.FabNoFavoriteClickListener;
import fr.alanlg.themovieapp.model.CastMember;
import fr.alanlg.themovieapp.model.CrewMember;
import fr.alanlg.themovieapp.model.Movie;
import fr.alanlg.themovieapp.model.MovieImage;
import fr.alanlg.themovieapp.model.MovieVideo;

public class MovieInfoActivity extends YouTubeBaseActivity {

    ImageView imageView;
    ImageView movieImage;
    TextView title;
    TextView date;
    TextView description;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    Dialog dialog;
    ViewPager actorsViewPager;
    ViewPager crewViewPager;

    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        imageView = findViewById(R.id.imageViewMoviePoster);
        title = findViewById(R.id.textViewTitleInfo);
        date = findViewById(R.id.textViewDateInfo);
        description = findViewById(R.id.textViewDescriptionInfo);
        youTubePlayerView = findViewById(R.id.youtubePlayer);
        movieImage = findViewById(R.id.movieImage);
        actorsViewPager = findViewById(R.id.actorsViewPager);
        crewViewPager = findViewById(R.id.crewViewPager);

        Bundle bundle = getIntent().getExtras();

        final ApiCaller apiCaller = new ApiCaller(getApplicationContext());
        final Movie movie = (Movie) bundle.getSerializable("movie");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Détails sur " + movie.getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setActionBar(toolbar);

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

        //Affichage d'une image du film aléatoirement
        apiCaller.movieImages(movie.getId()).setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Type movieImageListType = new TypeToken<LinkedList<MovieImage>>() {}.getType();
                LinkedList<MovieImage> movieImages = new Gson().fromJson(result.get("backdrops"), movieImageListType);

                Random r = new Random();
                int randomIndex = r.nextInt(movieImages.size() - 1);

                Picasso.get().load(movieImages.get(randomIndex).getImagePath()).noFade().placeholder(R.drawable.image_loading).into(movieImage);
            }
        });

        //Affichage des acteurs et des membres de la prod
        apiCaller.movieCredits(movie.getId()).setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Type castMemberListType = new TypeToken<LinkedList<CastMember>>() {}.getType();
                LinkedList<Member> castMembers = new Gson().fromJson(result.get("cast"), castMemberListType);

                final CardGalleryAdapter cardGalleryAdapter = new CardGalleryAdapter(castMembers, getApplicationContext());
                actorsViewPager.setAdapter(cardGalleryAdapter);
                actorsViewPager.setPadding(100, 0, 100, 0);

                Type crewMemberListType = new TypeToken<LinkedList<CrewMember>>() {}.getType();
                LinkedList<Member> crewMembers = new Gson().fromJson(result.get("crew"), crewMemberListType);

                final CardGalleryAdapter cardGalleryAdapter2 = new CardGalleryAdapter(crewMembers, getApplicationContext());
                crewViewPager.setAdapter(cardGalleryAdapter2);
                crewViewPager.setPadding(100, 0, 100, 0);

            }
        });

        //Affichage de la bande annonce
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.setShowFullscreenButton(false);

                apiCaller.movieVideos(movie.getId()).setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Type movieVideoListType = new TypeToken<LinkedList<MovieVideo>>() {}.getType();
                        LinkedList<MovieVideo> movieVideos = new Gson().fromJson(result.get("results"), movieVideoListType);

                        LinkedList<String> youtubeKeys = new LinkedList<>();

                        for (MovieVideo movieVideo : movieVideos) {
                            if (movieVideo.getSite().equals("YouTube"))
                                youtubeKeys.add(movieVideo.getKey());
                        }
                        youTubePlayer.loadVideos(youtubeKeys);
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("DEBUG", "Youtube failed to init");
            }
        };

        youTubePlayerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                youTubePlayerView.initialize(YoutubeConfig.getApiKey(), onInitializedListener);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}