package com.example.mymockproject.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymockproject.base.Resource
import com.example.mymockproject.database.dao.FavouriteDao
import com.example.mymockproject.model.response.DetailMovieData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repo: MovieRepo,
    private val dao: FavouriteDao) : ViewModel()
{
    private var _stateMovie = MutableLiveData<MovieState>()
    val stateMovie = _stateMovie

    private var _stateDbMovie = MutableLiveData<List<DetailMovieData>>()
    val stateDbMovie = _stateDbMovie

    fun getMoviePopular(page: Int){
        viewModelScope.launch {
            _stateMovie.value = MovieState.loading()
            repo.getMoviePopular(page).collect(){
                if(it.status == Resource.Status.SUCCESS){
                    if(it.data != null){
                        _stateMovie.value = MovieState.successGetMovie(it.data)
                    } else
                        _stateMovie.value = MovieState.error(it.message?:"")
                } else
                    _stateMovie.value = MovieState.error(it.message?:"")
            }
        }
    }

    fun search(key:String){
        viewModelScope.launch {
            _stateMovie.value = MovieState.loadingSearch()
            repo.search(key).collect(){
                if(it.status == Resource.Status.SUCCESS){
                    if(it.data != null){
                        _stateMovie.value = MovieState.successSearch(it.data)
                    } else
                        _stateMovie.value = MovieState.error(it.message?:"")
                } else
                    _stateMovie.value = MovieState.error(it.message?:"")
            }
        }
    }

    /** Save data to db */
    fun saveMovie(movie:DetailMovieData){
        viewModelScope.launch {
            dao.addMovieFavourite(movie)
            _stateDbMovie.value = dao.getAllFavourite()
        }
    }

    fun deleteMovie(movie: DetailMovieData){
        viewModelScope.launch {
            dao.deleteMovieFavourite(movie)
            _stateDbMovie.value = dao.getAllFavourite()
        }
    }

    fun getAllMovie(){
        viewModelScope.launch {
            _stateDbMovie.value = dao.getAllFavourite()
        }
    }
}