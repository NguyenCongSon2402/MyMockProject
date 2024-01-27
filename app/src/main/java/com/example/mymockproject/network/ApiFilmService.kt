package com.example.mymockproject.network

import com.example.mymockproject.constant.Endpoints
import com.example.mymockproject.model.response.CastAndCrewData
import com.example.mymockproject.model.response.DetailMovieData
import com.example.mymockproject.model.response.MovieData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiFilmService {
    @GET(Endpoints.MOVIE_POPULAR)
    suspend fun getPopularFilm(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<MovieData>

    @GET(Endpoints.SEARCH)
    suspend fun search(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Response<MovieData>

    @GET(Endpoints.GET_CAST_AND_CREW)
    suspend fun getCastAndCrew(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<CastAndCrewData>

    @GET(Endpoints.DETAIL_MOVIE)
    suspend fun getDetail(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<DetailMovieData>
}