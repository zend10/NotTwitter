package com.zen.nottwitter.core

import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.PostConfig
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.model.UserConfig
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseTest {

    protected val testDispatchers: TestDispatchers = TestDispatchers()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(testDispatchers.testDispatcher)

    protected val stubTestUserConfig = UserConfig(
        nicknameMinLength = 3,
        nicknameMaxLength = 32,
        passwordMinLength = 6,
        passwordMaxLength = 32
    )

    protected val stubTestFallbackErrorMessage = "Something went wrong."

    protected val stubTestPostConfig = PostConfig(280)

    protected val stubTestNickname = "Bob"
    protected val stubTestEmail = "test@gmail.com"
    protected val stubTestPassword = "test-password"
    protected val stubTestUser = User("abc", stubTestNickname, stubTestEmail)

    protected val stubTestPosts = listOf(Post("123"), Post("456"), Post("789"))
    protected val stubTestPost = Post("wow")

    protected val stubTestImageUrl = "https://coolimage.jpg"

    @Before
    open fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @After
    open fun tearDown() {

    }
}