package com.zen.nottwitter.data.model

data class Post(
    val uid: String = "",
    val userUid: String = "",
    val nickname: String = "",
    val message: String = "",
    val imageUrl: String = "",
    val createdOn: Long = 0,
)