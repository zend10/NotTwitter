package com.zen.nottwitter.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TimeUtilsTest {

    @Before
    fun setUp() {
        mockkObject(Clock.System)
    }

    @After
    fun tearDown() {
        unmockkObject(Clock.System)
    }

    @Test
    fun `getCurrentMillis will return current time in milliseconds`() {
        val stubTestMillis = 123L
        every { Clock.System.now().toEpochMilliseconds() } returns stubTestMillis
        assertEquals(stubTestMillis, TimeUtils.getCurrentMillis())
    }

    @Test
    fun `getDiff will return time diff in String`() {
        every { Clock.System.now() } returns Instant.fromEpochSeconds(88000)
        assertEquals("0m", TimeUtils.getDiff(88000))
        assertEquals("50m", TimeUtils.getDiff(85000))
        assertEquals("1h", TimeUtils.getDiff(84000))
        assertEquals("2h", TimeUtils.getDiff(80000))
        assertEquals("1d", TimeUtils.getDiff(1000))
    }
}