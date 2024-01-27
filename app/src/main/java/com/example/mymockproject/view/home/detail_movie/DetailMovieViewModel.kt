package com.example.mymockproject.view.home.detail_movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymockproject.base.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(private val repo: DetailMovieRepo): ViewModel() {

    private var _detailState = MutableLiveData<DetailMovieState>()
    val detailState = _detailState

    fun getCastAndCrew(movieId: Int){
        viewModelScope.launch {
            _detailState.value = DetailMovieState.loading()
            repo.getCastAndCrew(movieId).collect(){
                if(it.status == Resource.Status.SUCCESS){
                    if(it.data != null){
                        _detailState.value = DetailMovieState.successCastAndCrew(it.data)
                    } else
                        _detailState.value = DetailMovieState.failed(it.message?:"")
                } else
                    _detailState.value = DetailMovieState.failed(it.message?:"")
            }
        }
    }

    fun getDetailMovie(movieId: Int){
        viewModelScope.launch {
            _detailState.value = DetailMovieState.loading()
            repo.getDetailMovie(movieId).collect(){
                if(it.status == Resource.Status.SUCCESS){
                    if(it.data != null){
                        _detailState.value = DetailMovieState.successDetailMovie(it.data)
                    } else
                        _detailState.value = DetailMovieState.failed(it.message?:"")
                } else
                    _detailState.value = DetailMovieState.failed(it.message?:"")
            }
        }
    }
}