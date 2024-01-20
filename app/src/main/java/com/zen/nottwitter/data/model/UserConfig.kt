package com.zen.nottwitter.data.model

data class UserConfig(
    val nicknameMinLength: Int = 0,
    val nicknameMaxLength: Int = 0,
    val passwordMinLength: Int = 0,
    val passwordMaxLength: Int = 0,
)