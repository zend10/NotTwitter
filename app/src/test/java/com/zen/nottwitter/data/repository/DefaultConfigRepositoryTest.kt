package com.zen.nottwitter.data.repository

import com.zen.nottwitter.core.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DefaultConfigRepositoryTest : BaseTest() {

    private lateinit var repository: DefaultConfigRepository

    @Before
    override fun setUp() {
        super.setUp()
        repository = DefaultConfigRepository()
    }

    @Test
    fun `getUserConfig will return UserConfig`() {
        assertEquals(stubTestUserConfig, repository.getUserConfig())
    }

    @Test
    fun `getFallbackErrorMessage will return fallback error message String`() {
        assertEquals(stubTestFallbackErrorMessage, repository.getFallbackErrorMessage())
    }

    @Test
    fun `getPostConfig will return PostConfig`() {
        assertEquals(stubTestPostConfig, repository.getPostConfig())
    }

    @Test
    fun `getPaginationPerPageLimit will return pagination per page limit Int`() {
        assertEquals(25, repository.getPaginationPerPageLimit())
    }
}