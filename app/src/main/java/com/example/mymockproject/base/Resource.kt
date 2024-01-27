package com.example.mymockproject.base

class Resource <T>(
    val status: Status,
    val data: T?,
    val message: String?
) {
    enum class Status{
        LOADING, SUCCESS, FAILED
    }

    companion object{
        fun <T> loading(data:T? = null) = Resource(Status.LOADING, data, message = null)
        fun <T> success (data: T) = Resource(Status.SUCCESS, data, "")
        fun <T> error(data:T? = null, msg: String) = Resource(Status.FAILED, data, msg)
    }
}