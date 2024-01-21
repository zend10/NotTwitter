package com.zen.nottwitter.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.until


object TimeUtils {

    fun getCurrentMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    fun getDiff(seconds: Long): String {
        val timeZone = TimeZone.currentSystemDefault()
        val then = Instant.fromEpochSeconds(seconds)
        val now = Clock.System.now()
        val diff = then.until(now, DateTimeUnit.MINUTE, timeZone)
        return if (diff < 60)
            "${diff}m"
        else if (diff < 60 * 24) {
            "${diff / 60}h"
        } else {
            "${diff / 60 / 24}d"
        }
    }
}