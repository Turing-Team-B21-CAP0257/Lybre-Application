package com.b21.finalproject.smartlibraryapp.vo

class Resource<T>(val status: Status, var data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T?): Resource<T> = Resource(Status.SUCCESS, data, null)

        fun <T> error(msg: String?, data: T?) = Resource(Status.ERROR, data, msg)

        fun <T> loading(data: T?): Resource<T> = Resource(Status.LOADING, data, null)
    }

}