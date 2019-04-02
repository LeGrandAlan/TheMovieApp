package fr.alanlg.themovieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import fr.alanlg.themovieapp.MovieInfoActivity;
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

    public static View.OnTouchListener getOnTouchListener(final Context context, final ViewPager viewPager) {
        return new View.OnTouchListener() {
            private float pointX;
            private float pointY;
            private int tolerance = 50;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        return false; //This is important, if you return TRUE the action of swipe will not take place.
                    case MotionEvent.ACTION_DOWN:
                        pointX = event.getX();
                        pointY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        boolean sameX = pointX + tolerance > event.getX() && pointX - tolerance < event.getX();
                        boolean sameY = pointY + tolerance > event.getY() && pointY - tolerance < event.getY();
                        if(sameX && sameY){
                            //The user "clicked" certain point in the screen or just returned to the same position an raised the finger
                            int itemPosition = ((ViewPager)v).getCurrentItem();
                            Movie movie = ((MovieCardGalleryAdapter)viewPager.getAdapter()).getItemAtPosition(itemPosition);

                            Intent intent = new Intent(context, MovieInfoActivity.class);
                            intent.putExtra("movie", movie);
                            context.startActivity(intent);
                        }
                }
                return false;
            }
        };
    }

}
