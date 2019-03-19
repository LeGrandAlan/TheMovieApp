package fr.alanlg.themovieapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.LinkedList;

import fr.alanlg.themovieapp.model.Movie;

public class MovieImageAdapter extends BaseAdapter {

    private Context context;
    private LinkedList<Movie> movies;

    public MovieImageAdapter(Context context, LinkedList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return this.movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;

        if(view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.movie_image_grid_item, null, true);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Picasso.get().load(this.movies.get(position).getPosterLink()).placeholder(R.drawable.image_loading).into(holder.getImageView());

        return view;
    }

    class ViewHolder {
        private ImageView imageView;

        ViewHolder(View view) {
            this.imageView = view.findViewById(R.id.movieImage);
        }

        ImageView getImageView() {
            return this.imageView;
        }

    }

}

