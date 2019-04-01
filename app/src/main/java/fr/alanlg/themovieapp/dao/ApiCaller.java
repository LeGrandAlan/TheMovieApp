package fr.alanlg.themovieapp.dao;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.koushikdutta.ion.future.ResponseFuture;

public class ApiCaller {

    private final String API_KEY = "0162ba21af88b681e0a203ead28348ec";
    private final String LANGUAGE = "fr-FR";
    private final String ADULT = "true";
    private final String BASE_URL = "https://api.themoviedb.org/";
    private final String API_VERSION = "3";

    private final Context context;

    public ApiCaller(Context context) {
        this.context = context;
    }

    public ResponseFuture<JsonObject> searchMovie(String keyword, int pageNumber, int year, String compagnie, String genre) {

        String yearString = (year == 0) ? "" : String.valueOf(year);

        String url = BASE_URL + API_VERSION + "/discover/movie";

        return this.getBaseRequest(url, null)
                .setBodyParameter("api_key", API_KEY)
                .setBodyParameter("language", LANGUAGE)
                .setBodyParameter("include_adult", ADULT)
                .setBodyParameter("page", String.valueOf(pageNumber))
                .setBodyParameter("year", yearString)
                .setBodyParameter("with_companies", compagnie)
                .setBodyParameter("with_genres", genre)
                .asJsonObject();
    }

    public ResponseFuture<JsonObject> searchCompagnie(String keyword) {

        String url = BASE_URL + API_VERSION + "/search/company";

        return this.getBaseRequest(url, "GET")
                .setBodyParameter("api_key", API_KEY)
                .setBodyParameter("query", keyword)
                .asJsonObject();
    }


    public ResponseFuture<JsonObject> movieDetails(int movieId) {

        String url = BASE_URL + API_VERSION + "/movie/" + movieId;

        return this.getBaseRequest(url, "GET")
                .asJsonObject();

    }

    public ResponseFuture<JsonObject> similarMovie(int movieId) {

        String url = BASE_URL + API_VERSION + "/movie/" + movieId + "/similar";

        return this.getBaseRequest(url, "GET")
                .asJsonObject();

    }

    public ResponseFuture<JsonObject> topRated(int page) {

        String url = BASE_URL + API_VERSION + "/movie/top_rated";

        return this.getBaseRequest(url, null)
                .setBodyParameter("page", String.valueOf(page))
                .asJsonObject();

    }

    public ResponseFuture<JsonObject> upcoming(int page) {

        String url = BASE_URL + API_VERSION + "/movie/upcoming";

        return this.getBaseRequest(url, null)
                .setBodyParameter("page", String.valueOf(page))
                .asJsonObject();

    }

    public ResponseFuture<JsonObject> popular(int page) {

        String url = BASE_URL + API_VERSION + "/movie/popular";

        return this.getBaseRequest(url, null)
                .setBodyParameter("page", String.valueOf(page))
                .asJsonObject();

    }

    public ResponseFuture<JsonObject> nowPlaying(int page) {

        String url = BASE_URL + API_VERSION + "/movie/now_playing";

        return this.getBaseRequest(url, null)
                .setBodyParameter("page", String.valueOf(page))
                .asJsonObject();

    }

    public ResponseFuture<JsonObject> movieImages(int movieId) {
        String url = BASE_URL + API_VERSION + "/movie/" + movieId + "/images";

        return Ion.with(this.context)
                .load("GET", url)
                .setBodyParameter("api_key", API_KEY)
                .asJsonObject();
    }

    public ResponseFuture<JsonObject> movieCredits(int movieId) {
        String url = BASE_URL + API_VERSION + "/movie/" + movieId + "/credits";

        return Ion.with(this.context)
                .load("GET", url)
                .setBodyParameter("api_key", API_KEY)
                .asJsonObject();
    }

    public ResponseFuture<JsonObject> movieVideos(int movieId) {
        String url = BASE_URL + API_VERSION + "/movie/" + movieId + "/videos";

        return this.getBaseRequest(url, "GET")
                .asJsonObject();
    }

    public ResponseFuture<JsonObject> genreList() {
        String url = BASE_URL + API_VERSION + "/genre/movie/list";

        return this.getBaseRequest(url, "GET")
                .asJsonObject();
    }


    private Builders.Any.U getBaseRequest(String url, String method) {

        if (method == null) {
            return Ion.with(this.context)
                    .load(url)
                    .setLogging("azesfdg",Log.DEBUG)
                    .setBodyParameter("api_key", API_KEY)
                    .setBodyParameter("language", LANGUAGE);
        } else {
            return Ion.with(this.context)
                    .load(method, url)
                    .setBodyParameter("api_key", API_KEY)
                    .setBodyParameter("language", LANGUAGE);
        }
    }

}
