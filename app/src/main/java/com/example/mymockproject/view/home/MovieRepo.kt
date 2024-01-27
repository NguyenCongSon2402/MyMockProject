package com.example.mymockproject.view.home

import com.example.mymockproject.base.Resource
import com.example.mymockproject.model.response.CastAndCrewData
import com.example.mymockproject.model.response.MovieData
import com.example.mymockproject.network.ApiDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MovieRepo @Inject constructor(private val apiDataSource: ApiDataSource) {

    suspend fun getMoviePopular(page: Int) : Flow<Resource<MovieData>>{
        return flow {
            emit(apiDataSource.getPopularMovie(page))
        }.flowOn(
            Dispatchers.IO
        )
    }

    suspend fun search(key: String): Flow<Resource<MovieData>>{
        return flow {
            emit(apiDataSource.search(key))
        }.flowOn(
            Dispatchers.IO
        )
    }
}