package com.zen.nottwitter.data.model.entity

import io.realm.kotlin.types.RealmObject

class PostEntity : RealmObject {
    var uid = ""
    var userUid: String = ""
    var nickname: String = ""
    var message: String = ""
    var imageUrl: String = ""
    var createdOn: Long = 0
}