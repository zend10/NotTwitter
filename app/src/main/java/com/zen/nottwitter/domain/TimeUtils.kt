package com.zen.nottwitter.domain

import kotlinx.datetime.Clock


object TimeUtils {

    fun getCurrentMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }
}