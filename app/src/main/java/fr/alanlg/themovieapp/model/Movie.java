package fr.alanlg.themovieapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Movie implements Serializable {

    private int id;
    private String title;
    @SerializedName("overview")
    private String description;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("vote_average")
    private float voteAverage;
    @SerializedName("poster_path")
    private String posterPath;

    public Movie(int id, String title, String description, String releaseDate, int voteCount, float voteAverage, String posterPath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public String getPosterLink() {
        if (this.posterPath == null) {
            return null;
        }
        return "https://image.tmdb.org/t/p/original" + posterPath;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", voteCount=" + voteCount +
                ", voteAverage=" + voteAverage +
                ", posterPath='" + this.getPosterLink() + '\'' +
                '}';
    }

}
