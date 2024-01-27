package com.example.mymockproject.constant

import com.example.mymockproject.R
import com.example.mymockproject.model.data.HotBannerData

object Constants {
    const val BASE_IMAGE = "https://image.tmdb.org/t/p/w500"
    const val API_KEY = "e7631ffcb8e766993e5ec0c1f4245f93"

    object Intent{
        const val MOVIE_ID = "MOVIE_ID"
        const val IS_FAVOURITE = "IS_FAVOURITE"
    }

    object HotBanner{
        fun initListBanner(): ArrayList<HotBannerData>{
            val list : ArrayList<HotBannerData> = arrayListOf()
            list.add(HotBannerData(R.drawable.hot_3))
            list.add(HotBannerData(R.drawable.kikyou))
            list.add(HotBannerData(R.drawable.hot_4))

            return list
        }
    }
}