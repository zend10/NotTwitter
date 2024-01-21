package com.zen.nottwitter.data.repository

import com.zen.nottwitter.data.model.PostConfig
import com.zen.nottwitter.data.model.UserConfig

interface ConfigRepository {
    fun getUserConfig(): UserConfig
    fun getFallbackErrorMessage(): String
    fun getPostConfig(): PostConfig
    fun getPaginationPerPageLimit(): Int
}