package com.example.mymockproject.model.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity("movie")
data class DetailMovieData(
    @PrimaryKey
    val id: Int,
    val adult: Boolean,
    val title: String,
    val vote_average: Double,
    val vote_count : Int,
    val release_date : String,
    val overview: String,
    val poster_path: String)
{
    @Ignore
    @ColumnInfo(name = "selected")
    private var selected = false

    fun getSelected() = selected

    fun setSelected(selected: Boolean) {
        this.selected = selected
    }
}