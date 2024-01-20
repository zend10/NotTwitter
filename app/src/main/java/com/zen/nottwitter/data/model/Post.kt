package com.zen.nottwitter.data.model

data class Post(
    val uid: String = "",
    val userUid: String = "",
    val nickname: String = "",
    val message: String = "",
    val imageUrl: String = "",
    val createdOn: Long = 0,
    val likes: Int = 0,
    val isLiked: Boolean = false,
    val parentUid: String = "",
    val replies: List<Post> = listOf()
)