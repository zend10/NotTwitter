package com.zen.nottwitter.data.model.entity

import io.realm.kotlin.types.RealmObject

class UserEntity : RealmObject {
    var uid = ""
    var nickname = ""
    var email = ""
}