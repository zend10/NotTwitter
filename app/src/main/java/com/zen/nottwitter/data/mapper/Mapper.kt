package com.zen.nottwitter.data.mapper

interface Mapper<T, U> {
    fun mapTo(obj: T): U
    fun mapFrom(obj: U): T
}