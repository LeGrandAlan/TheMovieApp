package fr.alanlg.themovieapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

import fr.alanlg.themovieapp.adapter.Member;
import fr.alanlg.themovieapp.adapter.MemberCardGalleryAdapter;
import fr.alanlg.themovieapp.dao.ApiCaller;
import fr.alanlg.themovieapp.listener.FabFavoriteClickListener;
import fr.alanlg.themovieapp.listener.FabNoFavoriteClickListener;
import fr.alanlg.themovieapp.model.CastMember;
import fr.alanlg.themovieapp.model.CrewMember;
import fr.alanlg.themovieapp.model.Movie;
import fr.alanlg.themovieapp.model.MovieImage;
import fr.alanlg.themovieapp.model.MovieVideo;

public class NoInternetActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);

        Button checkInternet = findViewById(R.id.checkInternet);

        checkInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.haveNetworkConnection(getApplicationContext())){
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Toujours pas !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    };

}