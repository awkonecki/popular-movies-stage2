package com.example.nebo.popular_movies.data;

import android.provider.BaseColumns;

/**
 * @brief Class provides the general contract definition detailing the SQLite schema for usage with
 * respect to the popular movies application.
 */
public class MovieContract {

    /**
     * @brief class that explicitly defines the `MovieEntry` table for the `MovieContract` class.
     * @note `BaseColumns` provides the `_id` column already for entry enumeration.
     */
    private static final class MovieEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "Favorite Movies";
        public static final String COLUMN_MOVIE_TITLE = "Movie Title";
        public static final String COLUMN_MOVIE_ID = "Movie ID";


    }

}
