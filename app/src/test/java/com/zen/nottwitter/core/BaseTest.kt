package com.zen.nottwitter.core

import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.data.model.UserConfig
import io.mockk.MockKAnnotations
import org.junit.After
import org.junit.Before

abstract class BaseTest {

    protected val stubTestUserConfig = UserConfig(
        nicknameMinLength = 3,
        nicknameMaxLength = 32,
        passwordMinLength = 6,
        passwordMaxLength = 32
    )

    protected val stubTestFallbackErrorMessage = "Something went wrong."

    protected val stubTestNickname = "Bob"
    protected val stubTestEmail = "test@gmail.com"
    protected val stubTestPassword = "test-password"
    protected val stubTestUser = User("abc", stubTestNickname, stubTestEmail)

    @Before
    open fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @After
    open fun tearDown() {

    }
}