package com.example.mymockproject.view.home.detail_movie

import com.example.mymockproject.base.Resource
import com.example.mymockproject.model.response.CastAndCrewData
import com.example.mymockproject.model.response.DetailMovieData
import com.example.mymockproject.network.ApiDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DetailMovieRepo @Inject constructor(private val apiDataSource: ApiDataSource) {

    suspend fun getCastAndCrew(movieId: Int): Flow<Resource<CastAndCrewData>>{
        return flow {
            emit(apiDataSource.getCastAndCrew(movieId))
        }.flowOn(
            Dispatchers.IO
        )
    }

    suspend fun getDetailMovie(movieId: Int): Flow<Resource<DetailMovieData>>{
        return flow {
            emit(apiDataSource.getDetailMovie(movieId))
        }.flowOn(
            Dispatchers.IO
        )
    }
}