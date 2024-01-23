package com.zen.nottwitter.data.repository

import app.cash.turbine.test
import com.zen.nottwitter.core.BaseTest
import com.zen.nottwitter.data.localstorage.LocalStorageProvider
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.network.FirebaseProvider
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
        repository.user.test {
            assertEquals(stubTestUser, awaitItem())
        }
        coVerify {
            firebaseProvider.authenticate()
            localStorageProvider.deleteUser()
            localStorageProvider.saveUser(stubTestUser)
        }
    }

    @Test
    fun `authenticate negative flow will return null`() = runTest {
        coEvery { firebaseProvider.authenticate() } returns null
        assertEquals(null, repository.authenticate())
        repository.user.test {
            assertEquals(User(), awaitItem())
        }
        coVerify {
            firebaseProvider.authenticate()
        }
        coVerify(exactly = 0) {
            localStorageProvider.deleteUser()
            localStorageProvider.saveUser(any())
        }
    }

    @Test
    fun `authenticate will throw an exception if get an exception`() = runTest {
        coEvery { firebaseProvider.authenticate() } throws Exception()
        assertThrows(Exception::class.java) {
            kotlinx.coroutines.test.runTest {
                repository.authenticate()
            }
        }
        repository.user.test {
            assertEquals(User(), awaitItem())
        }
        coVerify {
            firebaseProvider.authenticate()
        }
        coVerify(exactly = 0) {
            localStorageProvider.deleteUser()
            localStorageProvider.saveUser(any())
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
        repository.user.test {
            assertEquals(stubTestUser, awaitItem())
        }
        coVerify {
            firebaseProvider.register(stubTestNickname, stubTestEmail, stubTestPassword)
            localStorageProvider.deleteUser()
            localStorageProvider.saveUser(stubTestUser)
        }
    }

    @Test
    fun `register will throw an exception if get an exception`() = runTest {
        coEvery {
            firebaseProvider.register(
                stubTestNickname,
                stubTestEmail,
                stubTestPassword
            )
        } throws Exception()
        assertThrows(Exception::class.java) {
            kotlinx.coroutines.test.runTest {
                repository.register(
                    stubTestNickname,
                    stubTestEmail,
                    stubTestPassword
                )
            }
        }
        repository.user.test {
            assertEquals(User(), awaitItem())
        }
        coVerify {
            firebaseProvider.register(stubTestNickname, stubTestEmail, stubTestPassword)
        }
        coVerify(exactly = 0) {
            localStorageProvider.deleteUser()
            localStorageProvider.saveUser(any())
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
        repository.user.test {
            assertEquals(stubTestUser, awaitItem())
        }
        coVerify {
            firebaseProvider.login(stubTestEmail, stubTestPassword)
            localStorageProvider.deleteUser()
            localStorageProvider.saveUser(stubTestUser)
        }
    }

    @Test
    fun `login will throw an exception if get an exception`() = runTest {
        coEvery {
            firebaseProvider.login(
                stubTestEmail,
                stubTestPassword
            )
        } throws Exception()
        assertThrows(Exception::class.java) {
            kotlinx.coroutines.test.runTest { repository.login(stubTestEmail, stubTestPassword) }
        }
        repository.user.test {
            assertEquals(User(), awaitItem())
        }
        coVerify {
            firebaseProvider.login(stubTestEmail, stubTestPassword)
        }
        coVerify(exactly = 0) {
            localStorageProvider.deleteUser()
            localStorageProvider.saveUser(any())
        }
    }

    @Test
    fun `logout will delete user from local storage and logout from firebase`() = runTest {
        repository.logoutTrigger.test {
            repository.logout()
            assertEquals(Unit, awaitItem())
            coVerify {
                localStorageProvider.deleteUser()
                localStorageProvider.deleteUserPosts()
                firebaseProvider.logout()
            }
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