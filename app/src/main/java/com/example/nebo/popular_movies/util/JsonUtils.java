package com.example.nebo.popular_movies.util;

import android.support.annotation.NonNull;

import java.util.List;

public interface JsonUtils<T> {
    /**
     * @brief Parse the JSON response for a list of supported POJOs.
     * @param response String representing the JSON response.
     * @return List of defined T based on the implementing class.
     */
    public List<T> parseJsonResponse(@NonNull String response);
}
