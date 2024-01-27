package com.example.mymockproject.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mymockproject.database.dao.FavouriteDao
import com.example.mymockproject.model.response.DetailMovieData

@Database(entities = [DetailMovieData::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun movieDao(): FavouriteDao

    companion object{
        const val APP_DB_NAME = "APP_MOVIE_DB_NAME"
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            val instance = INSTANCE
            if(instance != null)
                return instance
            synchronized(this){
                val temp = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    APP_DB_NAME
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = temp
                return temp
            }
        }
    }
}