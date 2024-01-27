package com.example.mymockproject.base

import retrofit2.Response
import java.lang.Exception

open class BaseDataSource {
    suspend fun <T> safeApiCall( apiCall: suspend () -> Response<T>) : Resource<T>{
        try {
            val response = apiCall()
            if(response.isSuccessful){
                if(response.body() != null)
                    return Resource.success(response.body()!!)
                else
                    return Resource.error(msg = "Data null")
            }
            else
                return Resource.error(msg = "Call api thất bại")
        }catch (e: Exception){
            return Resource.error(msg = "error: ${e.message}")
        }
    }
}