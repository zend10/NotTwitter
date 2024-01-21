package com.zen.nottwitter.data.model.entity

import io.realm.kotlin.types.RealmObject

class UserPostEntity : RealmObject {
    var uid = ""
    var nickname: String = ""
    var message: String = ""
    var imageUrl: String = ""
    var createdOn: Long = 0
}