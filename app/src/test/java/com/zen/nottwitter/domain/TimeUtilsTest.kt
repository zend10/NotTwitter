package com.zen.nottwitter.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.datetime.Clock
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
}