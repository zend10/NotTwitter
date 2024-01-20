package com.zen.nottwitter.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtensionsTest {

    @Test
    fun `isValidEmail will return true if String is a well formed email`() {
        assertEquals(true, "test@mail.com".isValidEmail())
        assertEquals(false, "!!@mail.com".isValidEmail())
        assertEquals(false, "test@!!.com".isValidEmail())
        assertEquals(false, "test@mail.!!".isValidEmail())
        assertEquals(false, "test@mail".isValidEmail())
        assertEquals(false, "testmail".isValidEmail())
        assertEquals(false, "testmail.com".isValidEmail())
        assertEquals(false, "@mail.com".isValidEmail())
        assertEquals(false, "@.".isValidEmail())
        assertEquals(false, "".isValidEmail())
    }
}