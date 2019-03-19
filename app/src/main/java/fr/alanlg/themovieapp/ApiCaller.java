package fr.alanlg.themovieapp;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.koushikdutta.ion.future.ResponseFuture;

public class ApiCaller {

    private final String API_KEY = "0162ba21af88b681e0a203ead28348ec";
    private final String LANGUAGE = "fr-FR";
    private final String ADULT = "false";
    private final String BASE_URL = "https://api.themoviedb.org/";
    private final String API_VERSION = "3";

    private final Context context;

    public ApiCaller(Context context) {
        this.context = context;
    }

    public ResponseFuture<JsonObject> searchMovie(String query, int pageNumber, int year) {

        String yearString = (year == 0) ? "" : String.valueOf(year);

        String url = BASE_URL + API_VERSION + "/search/movie";

        return this.getBaseRequest("GET", url)
                .setBodyParameter("query", query)
                .setBodyParameter("include_adult", ADULT)
                .setBodyParameter("page", String.valueOf(pageNumber))
                .setBodyParameter("year", yearString)
                .asJsonObject();

    }

    public ResponseFuture<JsonObject> movieDetails(int movieId) {

        String url = BASE_URL + API_VERSION + "/movie/" + movieId;

        return this.getBaseRequest("GET", url)
                .asJsonObject();

    }

    public ResponseFuture<JsonObject> topRated(int page) {

        String url = BASE_URL + API_VERSION + "/movie/top_rated";

        return this.getBaseRequest("GET", url)
                .setBodyParameter("page", String.valueOf(page))
                .asJsonObject();

    }

    public ResponseFuture<JsonObject> nowPlaying(int page) {

        String url = BASE_URL + API_VERSION + "/movie/now_playing";

        return this.getBaseRequest("GET", url)
                .setBodyParameter("page", String.valueOf(page))
                .asJsonObject();

    }

    private Builders.Any.U getBaseRequest(String method, String url) {

        return Ion.with(this.context)
                .load(method, url)
                .setBodyParameter("api_key", API_KEY)
                .setBodyParameter("language", LANGUAGE);

    }

}
