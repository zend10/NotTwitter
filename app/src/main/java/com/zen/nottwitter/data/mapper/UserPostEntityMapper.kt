package com.zen.nottwitter.data.mapper

import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.entity.UserPostEntity

class UserPostEntityMapper : Mapper<UserPostEntity, Post> {
    override fun mapTo(obj: UserPostEntity): Post {
        return Post(
            uid = obj.uid,
            nickname = obj.nickname,
            message = obj.message,
            imageUrl = obj.imageUrl,
            createdOn = obj.createdOn
        )
    }

    override fun mapFrom(obj: Post): UserPostEntity {
        return UserPostEntity().apply {
            uid = obj.uid
            nickname = obj.nickname
            message = obj.message
            imageUrl = obj.imageUrl
            createdOn = obj.createdOn
        }
    }
}