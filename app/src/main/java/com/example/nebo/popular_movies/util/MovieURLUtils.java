package com.example.nebo.popular_movies.util;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @TODO Will want to figure out if at time of building the URL or URI if the parameters need to be
 * encrypted.  This is beyond the scope of this project however.
 */
public class MovieURLUtils {
    public static final int DEFAULT_PAGE_NUM = 1;
    private static final int MIN_PAGE_NUM = 1;
    private static final int MAX_PAGE_NUM = 1000;

    // QUERY PATHS
    private static final String THE_MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3";
    private static final String POPULAR_MOVIE_ENDPOINT = "movie/popular";
    private static final String TOP_RATED_ENDPOINT = "movie/top_rated";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p";
    private static final String VIDEO_IMAGE_BASE_URL = "https://img.youtube.com/vi"; // /6ZfuNTqbHE8/hqdefault.jpg"
    private static final String VIDEO_IMAGE_DEFAULT_PATH = "hqdefault.jpg";
    private static final String REVIEWS_PATH = "reviews";
    private static final String VIDEOS_PATH = "videos";
    private static final String MOVIE_PATH = "movie";
    private static final String SIZE_W500 = "w500";

    // @TODO determine if image size is needed for backdrop later.

    // QUERY PARAMETERS
    private static final String KEY_PARAM = "api_key";
    private static final String PAGE_PARAM = "page";

    // QUERY VALUES
    private static final String API_KEY =
            // !!! Place key below.
            "XXX";

    /**
     * @brief Build the url to obtain reviews from moviedb.
     * @param page Integer that represents the page of information to query form the site.
     * @param movieId Integer that represents the unique ID of the movie that has been assigned by
     *                moviedb.
     * @return Returns a populated URL if no errors otherwise a null.
     */
    public static URL buildReviewsURL(int page, int movieId) {
        if (page < MovieURLUtils.MIN_PAGE_NUM || page > MovieURLUtils.MAX_PAGE_NUM) {
            page = 1;
        }

        Uri uri = Uri.parse(MovieURLUtils.THE_MOVIE_DB_BASE_URL).buildUpon().
                appendEncodedPath(MovieURLUtils.MOVIE_PATH).
                appendEncodedPath(Integer.toString(movieId)).
                appendEncodedPath(MovieURLUtils.REVIEWS_PATH).
                appendQueryParameter(MovieURLUtils.KEY_PARAM, MovieURLUtils.API_KEY).
                appendQueryParameter(MovieURLUtils.PAGE_PARAM, Integer.toString((page))).build();
        URL url = null;

        try {
            url = new URL(uri.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            url = null;
        }

        return url;
    }

    /**
     * @brief Constructs the URL to obtain the videos associated with a movie (trailers).
     * @param moiveId Integer that represents the unique ID of the movie that has been assigned by
     *                moviedb.
     * @return Returns a populated URL if no errors otherwise null.
     */
    public static URL buildVideosURL(int moiveId) {
        Uri uri = Uri.parse(MovieURLUtils.THE_MOVIE_DB_BASE_URL).buildUpon().
                appendEncodedPath(MovieURLUtils.MOVIE_PATH).
                appendEncodedPath(Integer.toString(moiveId)).
                appendEncodedPath(MovieURLUtils.VIDEOS_PATH).
                appendQueryParameter(MovieURLUtils.KEY_PARAM, MovieURLUtils.API_KEY).build();
        URL url = null;

        try {
            url = new URL(uri.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            url = null;
        }

        return url;
    }

    /**
     * @brief Construct a valid URL with the popular movie endpoint specifying a desired page.
     * @param page int that indicates the desired page of results, an invalid page number will
     *             result in the usage of the default page.
     * @return URL with the popular movie as the endpoint.
     */
    public static URL buildPopularURL(int page) {
        if (page > MovieURLUtils.MAX_PAGE_NUM || page < MovieURLUtils.MIN_PAGE_NUM) {
            // Log.d("Popular URL", "Page value out of range " + page + " using default.");
            page = MovieURLUtils.DEFAULT_PAGE_NUM;
        }

        return MovieURLUtils.buildUrl(MovieURLUtils.POPULAR_MOVIE_ENDPOINT, page);
    }

    /**
     * @brief Construct a valid URL with the top rated movie endpoint specifying a desired page.
     * @param page int that indicates the desired page of results, an invalid page number will
     *             result in the usage of the default page.
     * @return URL with the top rated movie as the endpoint.
     */
    public static URL buildTopRatedURL(int page) {
        if (page > MovieURLUtils.MAX_PAGE_NUM || page < MovieURLUtils.MIN_PAGE_NUM) {
            // Log.d("Top Rated URL", "Page value out of range " + page + " using default.");
            page = MovieURLUtils.DEFAULT_PAGE_NUM;
        }

        return MovieURLUtils.buildUrl(MovieURLUtils.TOP_RATED_ENDPOINT, page);
    }

    public static URL buildVideoImageURL(@NonNull final String videoKey) {
        Uri uri;
        URL url = null;

        uri = Uri.parse(MovieURLUtils.VIDEO_IMAGE_BASE_URL).buildUpon().
                appendEncodedPath(videoKey).
                appendEncodedPath(MovieURLUtils.VIDEO_IMAGE_DEFAULT_PATH).build();
        try {
            url = new URL(uri.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            url = null;
        }

        return url;
    }

    /**
     * @brief Construct the URL that is responsible for the path to the movie poster image.
     * @param path String that indicates the unique endpoint path for the image.
     * @return URL upon success, otherwise null.
     */
    public static URL buildImageUrl(@NonNull final String path) {
        Uri uri;
        URL url = null;

        if (path == null) {
            return url;
        }

        uri = Uri.parse(MovieURLUtils.IMAGE_BASE_URL).buildUpon().
                appendEncodedPath(MovieURLUtils.SIZE_W500).
                appendEncodedPath(path).build();

        try {
            url = new URL(uri.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            url = null;
        }

        return url;
    }

    /**
     * @brief Build the URL that hosts the 'themoviedb' endpoint API.
     * @param path String that contains a detailed path that is to be appended to the base bath.
     * @param page integer that indicates the page of results desired to be listed in the response.
     * @return Non-null URL upon success, otherwise null.
     */
    private static URL buildUrl(@NonNull final String path, int page) {
        Uri uri;
        URL url = null;

        // Defensive check if options are not enforcing non-null checks.
        if (path == null) {
            return url;
        }

        // Build the URI resource.
        uri = Uri.parse(MovieURLUtils.THE_MOVIE_DB_BASE_URL).buildUpon().
                appendEncodedPath(path).
                appendQueryParameter(MovieURLUtils.KEY_PARAM,  MovieURLUtils.API_KEY).
                appendQueryParameter(MovieURLUtils.PAGE_PARAM, Integer.toString(page)).build();

        try {
            // Attempt to construct a valid URL.
            url = new URL(uri.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            url = null;
        }

        return url;
    }
}
