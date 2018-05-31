package com.example.nebo.popular_movies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @brief Class provides the general contract definition detailing the SQLite schema for usage with
 * respect to the popular movies application.
 */
public class MovieContract {

    public final static String CONTENT_AUTHORITY = "com.example.nebo.popular_movies";
    public final static Uri BASE_CONTENT_URI = Uri.parse("content://" + MovieContract.CONTENT_AUTHORITY);
    public final static String PATH_FAVORITE_MOVIES = MovieEntry.TABLE_NAME;

    /**
     * @brief class that explicitly defines the `MovieEntry` table for the `MovieContract` class.
     * @note `BaseColumns` provides the `_id` column already for entry enumeration.
     */
    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = MovieContract.BASE_CONTENT_URI.buildUpon().
                appendPath(MovieContract.PATH_FAVORITE_MOVIES).build();

        // Table name
        public static final String TABLE_NAME = "Favorite_Movies";
        public static final String COLUMN_MOVIE_TITLE = "Movie_Title";
        public static final String COLUMN_MOVIE_ID = "Movie_ID";
        public static final String COLUMN_MOVIE_POSTER = "Movie_Poster";
        public static final String COLUMN_MOVIE_BACKGROUND = "Movie_Background";
        public static final String COLUMN_RELEASE_DATE = "Movie_Release_Date";
        public static final String COLUMN_RATING = "Movie_Rating";
        public static final String COLUMN_DESCRIPTION = "Movie_Description";
    }

}
