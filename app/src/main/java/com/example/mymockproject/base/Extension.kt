package com.example.mymockproject.base

import android.view.View
import com.google.gson.Gson

fun View?.show(){
    this?.visibility = View.VISIBLE
}

fun View?.hide(){
    this?.visibility = View.GONE
}

fun Any?.toJson(): String{
    if(this == null)
        return ""
    else
        return Gson().toJson(this)
}