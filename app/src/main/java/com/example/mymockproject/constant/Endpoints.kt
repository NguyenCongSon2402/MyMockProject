package com.example.mymockproject.constant

object Endpoints {
    //get list movie
    const val MOVIE_POPULAR = "movie/popular"
    const val MOVIE_TOP_RATED = "movie/top_rated"
    const val MOVIE_UP_COMING = "movie/upcoming"
    const val MOVIE_NOW_PLAYING = "movie/now_playing"

    // search
    const val SEARCH = "search/movie"

    // get list cast and crew
    const val GET_CAST_AND_CREW = "movie/{movieId}/credits"

    // get detail movie
    const val DETAIL_MOVIE = "movie/{movieId}"
}