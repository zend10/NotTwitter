package com.zen.nottwitter.data.repository

import com.zen.nottwitter.data.model.UserConfig

class DefaultConfigRepository : ConfigRepository {

    override fun getUserConfig(): UserConfig {
        return UserConfig(
            nicknameMinLength = 3,
            nicknameMaxLength = 32,
            passwordMinLength = 6,
            passwordMaxLength = 32
        )
    }

    override fun getFallbackErrorMessage(): String {
        return "Something went wrong."
    }
}