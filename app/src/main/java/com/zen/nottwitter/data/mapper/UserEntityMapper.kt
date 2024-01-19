package com.zen.nottwitter.data.mapper

import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.model.entity.UserEntity

class UserEntityMapper : Mapper<UserEntity, User> {
    override fun mapTo(obj: UserEntity): User {
        return User(
            uid = obj.uid,
            nickname = obj.nickname,
            email = obj.email
        )
    }

    override fun mapFrom(obj: User): UserEntity {
        return UserEntity().apply {
            uid = obj.uid
            nickname = obj.nickname
            email = obj.email
        }
    }
}