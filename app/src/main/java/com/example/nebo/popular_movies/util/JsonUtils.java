package com.example.nebo.popular_movies.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nebo.popular_movies.data.Movie;
import com.example.nebo.popular_movies.data.Review;
import com.example.nebo.popular_movies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JsonUtils {
    private static final String LIST_RESPONSE_KEY = "results";

    private static final String MOVIE_ID_KEY = "id";
    private static final String MOVIE_VOTE_KEY = "vote_average";
    private static final String MOVIE_TITLE_KEY = "title";
    private static final String MOVIE_POPULARITY_KEY = "popularity";
    private static final String MOVIE_POSTER_PATH_KEY = "poster_path";
    private static final String MOVIE_BACKDROP_PATH_KEY = "backdrop_path";
    private static final String MOVIE_OVERVIEW_KEY = "overview";
    private static final String MOVIE_DATE_KEY = "release_date";

    private static final String REVIEW_ID_KEY = "id";
    private static final String REVIEW_AUTHOR_KEY = "author";
    private static final String REVIEW_CONTENT_KEY = "content";
    private static final String REVIEW_URL_KEY = "url";

    private static final String TRAILER_ID_KEY = "id";
    private static final String TRAILER_KEY_KEY = "key";
    private static final String TRAILER_SITE_KEY = "site";
    private static final String TRAILER_TYPE_KEY = "type";
    private static final String TRAILER_SIZE_KEY = "size";

    public static List<Trailer> parseJsonResponseForTrailers(@NonNull String response) {
        List<Trailer> trailers = new ArrayList<Trailer>();
        JSONObject jsonResponse;
        JSONArray jsonArrayOfTrailers;

        if (response == null) {
            return trailers;
        }

        try {
            jsonResponse = new JSONObject(response);
            jsonArrayOfTrailers = jsonResponse.getJSONArray(JsonUtils.LIST_RESPONSE_KEY);

            for (int index = 0; index < jsonArrayOfTrailers.length(); index++) {
                Trailer trailer = null;
                JSONObject jsonTrailer = jsonArrayOfTrailers.getJSONObject(index);
                trailer = JsonUtils.parseJsonTrailer(jsonTrailer);

                if (trailer != null) {
                    trailers.add(trailer);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Trailer parseJsonTrailer(@NonNull JSONObject jsonTrailer) {
        String id, key, site, type;
        int size;
        Trailer trailer = null;


        if (jsonTrailer == null) {
            return null;
        }

        try {
            id = jsonTrailer.getString(JsonUtils.TRAILER_ID_KEY);
            key = jsonTrailer.getString(JsonUtils.TRAILER_KEY_KEY);
            site = jsonTrailer.getString(JsonUtils.TRAILER_SITE_KEY);
            type = jsonTrailer.getString(JsonUtils.TRAILER_TYPE_KEY);
            size = jsonTrailer.getInt(JsonUtils.TRAILER_SIZE_KEY);

            trailer = new Trailer(id,
                    key,
                    site,
                    size,
                    type,
                    MovieURLUtils.buildVideoImageURL(key).toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return  trailer;
    }

    public static List<Review> parseJsonResponseForReviews(@NonNull String response) {
        List<Review> reviews = new ArrayList<Review>();
        JSONObject jsonResponse = null;
        JSONArray jsonArrayOfReivews = null;

        // Prevent an actual run-time exception failure.
        if (response == null) {
            return reviews;
        }

        // Log.d("ParsingReviews", response);

        try {
            jsonResponse = new JSONObject(response);
            jsonArrayOfReivews = jsonResponse.getJSONArray(JsonUtils.LIST_RESPONSE_KEY);

            for (int index = 0; index < jsonArrayOfReivews.length(); index++) {
                Review review = JsonUtils.parseJsonReview(jsonArrayOfReivews.getJSONObject(index));

                if (review != null) {
                    reviews.add(review);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    private static Review parseJsonReview(@NonNull JSONObject jsonReview) {
        String author, content, id, url;

        // Log.d("Parsing review", jsonReview.toString());

        try {
            author = jsonReview.getString(JsonUtils.REVIEW_AUTHOR_KEY);
            content = jsonReview.getString(JsonUtils.REVIEW_CONTENT_KEY);
            id = jsonReview.getString(JsonUtils.REVIEW_ID_KEY);
            url = jsonReview.getString(JsonUtils.REVIEW_URL_KEY);

            return new Review(author, content, id, url);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @brief Parse the JSON response for a list of supported POJOs.
     * @param response String representing the JSON response.
     * @return List of defined T based on the implementing class.
     */
    public static List<Movie> parseJsonResponseForMovies(@NonNull String response) {
        List<Movie> movies = new ArrayList<Movie>();
        JSONObject jsonResponse = null;
        JSONArray jsonArrayOfMovies = null;

        if (response == null) {
            return movies;
        }

        // Log.d("parsingMovies", response);

        try {
            jsonResponse = new JSONObject(response);
            jsonArrayOfMovies = jsonResponse.getJSONArray(JsonUtils.LIST_RESPONSE_KEY);

            for (int index = 0; index < jsonArrayOfMovies.length(); index++) {
                Movie movie = JsonUtils.parseJsonMovie(jsonArrayOfMovies.getJSONObject(index));

                if (movie != null) {
                    movies.add(movie);
                }
            }
        }
        catch (org.json.JSONException e) {
            Log.e("Parsing JSON Response", "Error in parsing the response " + response);
            e.printStackTrace();
        }

        return movies;
    }

    /**
     * @brief Parse the JSON object for the individual movie attributes.
     * @param movieJsonObject json Object that must contain the movie keys.
     * @return A movie object if successful otherwise null.
     */
    private static Movie parseJsonMovie(@NonNull JSONObject movieJsonObject) {
        Movie movie = null;

        if (movieJsonObject == null) {
            return movie;
        }

        // All movie attributes from the JSON response are at the same level.
        int id;
        String title, overview, backdropPath, posterPath, date;
        double popularity, vote;

        // Log.d("parseJsonMovie", movieJsonObject.toString());

        try {
            id = movieJsonObject.getInt(JsonUtils.MOVIE_ID_KEY);
            title = movieJsonObject.getString(JsonUtils.MOVIE_TITLE_KEY);
            overview = movieJsonObject.getString(JsonUtils.MOVIE_OVERVIEW_KEY);
            backdropPath = MovieURLUtils.buildImageUrl(movieJsonObject.getString(JsonUtils.MOVIE_BACKDROP_PATH_KEY)).toString();
            posterPath = MovieURLUtils.buildImageUrl(movieJsonObject.getString(JsonUtils.MOVIE_POSTER_PATH_KEY)).toString();
            date = movieJsonObject.getString(JsonUtils.MOVIE_DATE_KEY);
            popularity = movieJsonObject.getDouble(JsonUtils.MOVIE_POPULARITY_KEY);
            vote = movieJsonObject.getDouble(JsonUtils.MOVIE_VOTE_KEY);
        }
        catch (org.json.JSONException e) {
            Log.e("Parse Movie Json", "Error in parsing a field for a movie.");
            e.printStackTrace();
            return movie;
        }

        return new Movie(title, id, vote, popularity, posterPath, backdropPath, overview, date);
    }
}
