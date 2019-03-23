package fr.alanlg.themovieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.alanlg.themovieapp.model.Movie;

public class MovieInfoActivity extends AppCompatActivity {

    ImageView imageView;
    TextView title;
    TextView date;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        imageView = findViewById(R.id.imageViewMoviePoster);
        title = findViewById(R.id.textViewTitleInfo);
        date = findViewById(R.id.textViewDateInfo);
        description = findViewById(R.id.textViewDescriptionInfo);

        Bundle bundle = getIntent().getExtras();

        Movie movie = (Movie) bundle.getSerializable("movie");

        Picasso.get().load(movie.getPosterLink()).placeholder(R.drawable.image_loading).into(imageView);
        title.setText(movie.getTitle());
        date.setText(movie.getReleaseDate());
        description.setText(movie.getDescription());

    }


}
