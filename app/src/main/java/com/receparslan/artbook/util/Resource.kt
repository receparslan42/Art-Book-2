package com.receparslan.artbook.util

class Resource<out T>(
    val status: Status,
    val message: String? = null,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T?): Resource<T> = Resource(Status.SUCCESS, data = data)

        fun <T> error(message: String): Resource<T> = Resource(Status.ERROR, message = message)

        fun <T> loading(): Resource<T> = Resource(Status.LOADING)

        fun <T> none(): Resource<T> = Resource(Status.NONE)
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    NONE
}