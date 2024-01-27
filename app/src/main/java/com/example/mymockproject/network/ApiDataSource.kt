package com.example.mymockproject.network

import com.example.mymockproject.base.BaseDataSource
import com.example.mymockproject.constant.Constants
import javax.inject.Inject

class ApiDataSource @Inject constructor(private val apiService: ApiFilmService) : BaseDataSource() {

    suspend fun getPopularMovie(page: Int)  =
        safeApiCall { apiService.getPopularFilm(Constants.API_KEY,page) }

    suspend fun search(key: String) =
        safeApiCall { apiService.search(Constants.API_KEY, key) }

    suspend fun getCastAndCrew(movieId:Int) =
        safeApiCall { apiService.getCastAndCrew(movieId, Constants.API_KEY) }

    suspend fun getDetailMovie(movieId: Int) =
        safeApiCall { apiService.getDetail(movieId, Constants.API_KEY) }
}