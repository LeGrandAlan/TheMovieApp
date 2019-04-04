package fr.alanlg.themovieapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MovieImage implements Serializable {

    @SerializedName("file_path")
    private String imagePath;
    @SerializedName("vote_average")
    private float voteAverage;
    @SerializedName("vote_count")
    private float voteCount;
    @SerializedName("aspect_ratio")
    private float aspectRatio;

    public MovieImage(String imagePath, float voteAverage, float voteCount, float aspectRatio) {
        this.imagePath = imagePath;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.aspectRatio = aspectRatio;
    }

    public String getImagePath() {
        if (this.imagePath == null) {
            return null;
        }
        return "https://image.tmdb.org/t/p/w780" + imagePath;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public float getVoteCount() {
        return voteCount;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    @Override
    public String toString() {
        return "MovieImage{" +
                "imagePath='" + imagePath + '\'' +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                ", aspectRatio=" + aspectRatio +
                '}';
    }

}
