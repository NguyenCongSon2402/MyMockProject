package com.example.mymockproject.utility

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.mymockproject.constant.Constants
import com.example.mymockproject.view.home.detail_movie.DetailMovieActivity

object Utility {
    fun toast(context: Context, msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun goToDetail(context: Context, movieId: Int, favourite : Boolean){
        val intent = Intent(context, DetailMovieActivity::class.java)
        intent.putExtra(Constants.Intent.MOVIE_ID, movieId)
        intent.putExtra(Constants.Intent.IS_FAVOURITE, favourite)
        context.startActivity(intent)
    }
}