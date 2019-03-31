package fr.alanlg.themovieapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import fr.alanlg.themovieapp.R;
import fr.alanlg.themovieapp.model.Movie;

public class MovieCardGalleryAdapter extends PagerAdapter {

    private List<Movie> movies;
    private LayoutInflater layoutInflater;
    private Context context;

    public MovieCardGalleryAdapter(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.movie_card_gallery_item, container, false);

        ImageView cardGalleryImage = view.findViewById(R.id.cardGalleryImage);
        TextView cardGalleryText = view.findViewById(R.id.cardGalleryText);
        TextView cardGalleryText2 = view.findViewById(R.id.cardGalleryText2);
        TextView cardGalleryText3 = view.findViewById(R.id.cardGalleryText3);

        Movie movie = movies.get(position);

        Picasso.get().load(movie.getPosterLink()).noFade().placeholder(R.drawable.image_loading).into(cardGalleryImage);
        cardGalleryText.setText(movie.getTitle());
        cardGalleryText2.setText("Sorti le " + movie.getReleaseDate());
        cardGalleryText3.setText(movie.getDescription());


        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    public Movie getItemAtPosition(int i) {
        return this.movies.get(i);
    }

    /*@Override
    public float getPageWidth(int position) {
        return(0.5f);
    }*/

}
