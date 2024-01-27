package com.example.mymockproject.view.home.detail_movie

import com.example.mymockproject.model.response.CastAndCrewData
import com.example.mymockproject.model.response.DetailMovieData

class DetailMovieState(
    val status: Status,
    val data: Any?,
    val message: String?
) {
    enum class Status{
        LOADING,
        SUCCESS_DETAIL_MOVIE,
        SUCCESS_CAST_AND_CREW,
        FAILED
    }

    companion object{
        fun loading() = DetailMovieState(Status.LOADING, data = null, message = null)
        fun successDetailMovie(data: DetailMovieData) =
            DetailMovieState(Status.SUCCESS_DETAIL_MOVIE, data, "")
        fun successCastAndCrew(data: CastAndCrewData) =
            DetailMovieState(Status.SUCCESS_CAST_AND_CREW, data, "")
        fun failed(msg: String) = DetailMovieState(Status.FAILED, data = null, msg)
    }
}