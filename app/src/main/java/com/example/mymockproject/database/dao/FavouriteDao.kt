package com.example.mymockproject.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mymockproject.model.response.DetailMovieData

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovieFavourite(movie: DetailMovieData)

    @Delete
    suspend fun deleteMovieFavourite(movie: DetailMovieData)

    @Query("SELECT * FROM movie")
    suspend fun getAllFavourite() : List<DetailMovieData>
}