package com.example.mymockproject.model.response

data class MovieData(
    val page: Int,
    val total_pages: Int,
    val total_results: Int,
    val results : ArrayList<DetailMovieData>
)