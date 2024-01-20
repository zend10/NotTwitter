package com.zen.nottwitter.data.repository

import com.zen.nottwitter.core.BaseTest
import com.zen.nottwitter.data.localstorage.LocalStorageProvider
import com.zen.nottwitter.data.provider.FirebaseProvider
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class DefaultUserRepositoryTest : BaseTest() {

    private lateinit var repository: DefaultUserRepository

    @MockK
    private lateinit var firebaseProvider: FirebaseProvider

    @MockK
    private lateinit var localStorageProvider: LocalStorageProvider

    @Before
    override fun setUp() {
        super.setUp()
        repository = DefaultUserRepository(firebaseProvider, localStorageProvider)
    }

    @Test
    fun `authenticate positive flow will save User to local storage and return User`() = runTest {
        coEvery { firebaseProvider.authenticate() } returns stubTestUser
        coEvery { localStorageProvider.saveUser(stubTestUser) } returns stubTestUser
        assertEquals(stubTestUser, repository.authenticate())
        coVerify {
            firebaseProvider.authenticate()
            localStorageProvider.saveUser(stubTestUser)
        }
    }

    @Test
    fun `authenticate negative flow will return null`() = runTest {
        coEvery { firebaseProvider.authenticate() } returns null
        assertEquals(null, repository.authenticate())
        coVerify {
            firebaseProvider.authenticate()
            localStorageProvider.saveUser(any()) wasNot called
        }
    }

    @Test
    fun `authenticate will throw an exception if get an exception`() {
        coEvery { firebaseProvider.authenticate() } throws Exception()
        assertThrows(Exception::class.java) {
            runTest { repository.authenticate() }
        }
        coVerify {
            firebaseProvider.authenticate()
            localStorageProvider.saveUser(any()) wasNot called
        }
    }

    @Test
    fun `register positive flow will save User to local storage and return User`() = runTest {
        coEvery {
            firebaseProvider.register(
                stubTestNickname,
                stubTestEmail,
                stubTestPassword
            )
        } returns stubTestUser
        coEvery { localStorageProvider.saveUser(stubTestUser) } returns stubTestUser
        assertEquals(
            stubTestUser,
            repository.register(stubTestNickname, stubTestEmail, stubTestPassword)
        )
        coVerify {
            firebaseProvider.register(stubTestNickname, stubTestEmail, stubTestPassword)
            localStorageProvider.saveUser(stubTestUser)
        }
    }

    @Test
    fun `register will throw an exception if get an exception`() {
        coEvery {
            firebaseProvider.register(
                stubTestNickname,
                stubTestEmail,
                stubTestPassword
            )
        } throws Exception()
        assertThrows(Exception::class.java) {
            runTest { repository.register(stubTestNickname, stubTestEmail, stubTestPassword) }
        }
        coVerify {
            firebaseProvider.register(stubTestNickname, stubTestEmail, stubTestPassword)
            localStorageProvider.saveUser(any()) wasNot called
        }
    }

    @Test
    fun `login positive flow will save User to local storage and return User`() = runTest {
        coEvery {
            firebaseProvider.login(
                stubTestEmail,
                stubTestPassword
            )
        } returns stubTestUser
        coEvery { localStorageProvider.saveUser(stubTestUser) } returns stubTestUser
        assertEquals(
            stubTestUser,
            repository.login(stubTestEmail, stubTestPassword)
        )
        coVerify {
            firebaseProvider.login(stubTestEmail, stubTestPassword)
            localStorageProvider.saveUser(stubTestUser)
        }
    }

    @Test
    fun `login will throw an exception if get an exception`() {
        coEvery {
            firebaseProvider.login(
                stubTestEmail,
                stubTestPassword
            )
        } throws Exception()
        assertThrows(Exception::class.java) {
            runTest { repository.login(stubTestEmail, stubTestPassword) }
        }
        coVerify {
            firebaseProvider.login(stubTestEmail, stubTestPassword)
            localStorageProvider.saveUser(any()) wasNot called
        }
    }

    @Test
    fun `logout will delete user from local storage and logout from firebase`() = runTest {
        repository.logout()
        coVerify {
            localStorageProvider.deleteUser()
            firebaseProvider.logout()
        }
    }

    @Test
    fun `getUser will return User`() = runTest {
        coEvery { localStorageProvider.getUser() } returns stubTestUser
        assertEquals(stubTestUser, repository.getUser())
        coVerify { localStorageProvider.getUser() }
    }

    @Test
    fun `getUser will throw an exception if get an exception`() {
        coEvery { localStorageProvider.getUser() } throws Exception()
        assertThrows(Exception::class.java) {
            runTest { repository.getUser() }
        }
        coVerify { localStorageProvider.getUser() }
    }
}