package com.zen.nottwitter.data.mapper

import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.model.entity.PostEntity
import com.zen.nottwitter.data.model.entity.UserEntity

class PostEntityMapper : Mapper<PostEntity, Post> {
    override fun mapTo(obj: PostEntity): Post {
        return Post(
            uid = obj.uid,
            userUid = obj.userUid,
            nickname = obj.nickname,
            message = obj.message,
            imageUrl = obj.imageUrl,
            createdOn = obj.createdOn
        )
    }

    override fun mapFrom(obj: Post): PostEntity {
        return PostEntity().apply {
            uid = obj.uid
            userUid = obj.userUid
            nickname = obj.nickname
            message = obj.message
            imageUrl = obj.imageUrl
            createdOn = obj.createdOn
        }
    }
}